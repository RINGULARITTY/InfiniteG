package fr.ringularity.infiniteg.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Compact BigDecimal codecs using:
 * - ZigZag + base-128 VarInt for the scale (sint32-like) to make small +/- scales tiny [15][13]
 * - ZigZag + base-128 VarInt for the unscaledValue (BigInteger), reusing the same idea as BigIntegerCodecs [15][13]
 *
 * BigDecimal value is defined as: unscaledValue * 10^{-scale}, with scale a 32-bit int and can be negative [2].
 * We serialize the exact scale and unscaledValue so that decode() reconstructs an equal BigDecimal (equals, not only compareTo) [2].
 */
public final class BigDecimalCodecs {
    private BigDecimalCodecs() {}

    // ---- constants ----
    private static final BigInteger BI_ZERO = BigInteger.ZERO;
    private static final BigInteger BI_ONE  = BigInteger.ONE;
    private static final BigInteger MASK_7BITS = BigInteger.valueOf(0x7F);

    // For safety: max bytes for a 32-bit varint is 5 (ceil(32/7)) [13]
    private static final int MAX_VAR_INT32_BYTES = 5;

    // Defensive guard for BigInteger varint (same order as BigIntegerCodecs comment)
    private static final int MAX_VAR_BIGINT_BYTES = 1 << 20; // 1,048,576

    // ---- public codecs ----

    /**
     * Stream codec: [scale as ZigZag VarInt32] then [unscaled BigInteger as ZigZag VarInt]
     * Uses existing BigIntegerCodecs stream codec for the BigInteger part to avoid duplicate logic.
     */
    public static final StreamCodec<ByteBuf, BigDecimal> STREAM_CODEC = StreamCodec.of(
            // encode
            (out, value) -> {
                // 1) write scale (sint32 via ZigZag -> unsigned varint base-128)
                writeZigZagVarInt32(out, value.scale());
                // 2) write unscaled BigInteger (signed via ZigZag -> unsigned varint)
                BigIntegerCodecs.STREAM_CODEC.encode(out, value.unscaledValue());
            },
            // decode
            (in) -> {
                // 1) read scale
                int scale = readZigZagVarInt32(in);
                // 2) read unscaled
                BigInteger unscaled = BigIntegerCodecs.STREAM_CODEC.decode(in);
                return new BigDecimal(unscaled, scale);
            }
    );

    /**
     * Binary Codec over ByteBuffer:
     * Layout = VarInt32(zigzag(scale)) || VarBigInt(zigzag(unscaled))
     * Both fields are self-delimited by MSB continuation bits.
     */
    public static final Codec<BigDecimal> CODEC =
            Codec.BYTE_BUFFER.comapFlatMap(
                    buf -> {
                        ByteBuffer dup = buf.slice();
                        byte[] bytes = new byte[dup.remaining()];
                        dup.get(bytes);
                        try {
                            return DataResult.success(decodeZigZagVarBigDecimal(bytes));
                        } catch (RuntimeException ex) {
                            return DataResult.error(ex::getMessage);
                        }
                    },
                    bd -> ByteBuffer.wrap(encodeZigZagVarBigDecimal(bd))
            );

    // ---- int (scale) ZigZag + VarInt32 ----

    // ZigZag encode 32-bit int to unsigned 32-bit in a long container
    private static long zigZagEncodeInt(int n) {
        // (n << 1) ^ (n >> 31) — classic ZigZag for sint32 [15]
        return ((((long) n << 1) ^ (n >> 31))) & 0xFFFFFFFFL;
    }

    private static int zigZagDecodeInt(long u) {
        // inverse: (u >>> 1) ^ -(u & 1) on 32-bit domain [15]
        return (int)((u >>> 1) ^ (-(u & 1L)));
    }

    private static void writeUnsignedVarInt32(ByteBuf out, long u) {
        // u is in [0, 2^32-1], emit up to 5 bytes [13]
        long x = u;
        while ((x & ~0x7FL) != 0L) {
            out.writeByte(((int)x & 0x7F) | 0x80);
            x >>>= 7;
        }
        out.writeByte((int)x);
    }

    private static long readUnsignedVarInt32(ByteBuf in) {
        long result = 0L;
        int shift = 0;
        int count = 0;
        while (true) {
            if (!in.isReadable()) {
                throw new IndexOutOfBoundsException("Truncated VarInt32");
            }
            int b = in.readUnsignedByte();
            result |= (long)(b & 0x7F) << shift;
            shift += 7;
            count++;
            if (count > MAX_VAR_INT32_BYTES) {
                throw new IllegalArgumentException("VarInt32 too long");
            }
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return result;
    }

    private static void writeZigZagVarInt32(ByteBuf out, int value) {
        writeUnsignedVarInt32(out, zigZagEncodeInt(value));
    }

    private static int readZigZagVarInt32(ByteBuf in) {
        long u = readUnsignedVarInt32(in);
        return zigZagDecodeInt(u);
    }

    // ---- BigInteger (unscaled) ZigZag + VarInt for array path ----

    private static BigInteger zigZagEncodeBigInt(BigInteger n) {
        return n.signum() >= 0 ? n.shiftLeft(1) : n.negate().shiftLeft(1).subtract(BI_ONE);
    }

    private static BigInteger zigZagDecodeBigInt(BigInteger zz) {
        boolean odd = zz.testBit(0);
        BigInteger shifted = zz.shiftRight(1);
        return odd ? shifted.add(BI_ONE).negate() : shifted;
    }

    private static void writeUnsignedVarBigInt(ByteArrayOutputStream out, BigInteger u) {
        if (u.signum() == 0) {
            out.write(0);
            return;
        }
        BigInteger x = u;
        while (x.signum() != 0) {
            int low7 = x.and(MASK_7BITS).intValue();
            x = x.shiftRight(7);
            out.write(x.signum() != 0 ? (low7 | 0x80) : low7);
        }
    }

    // Returns pair via simple holder for decoding from a byte[] with offset progression
    private record BigIntDec(BigInteger value, int next) {}
    private record UInt32Dec(long value, int next) {}

    private static UInt32Dec readUnsignedVarInt32FromArray(byte[] in) {
        long result = 0L;
        int shift = 0;
        int count = 0;
        int i = 0;
        while (true) {
            if (i >= in.length) {
                throw new IllegalArgumentException("Truncated VarInt32 (no terminator)");
            }
            int b = in[i++] & 0xFF;
            result |= (long)(b & 0x7F) << shift;
            shift += 7;
            count++;
            if (count > MAX_VAR_INT32_BYTES) {
                throw new IllegalArgumentException("VarInt32 too long");
            }
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return new UInt32Dec(result, i);
    }

    private static BigIntDec readUnsignedVarBigIntFromArray(byte[] in, int off) {
        BigInteger result = BI_ZERO;
        int shift = 0;
        int count = 0;
        int i = off;
        while (true) {
            if (i >= in.length) {
                throw new IllegalArgumentException("Truncated VarInt BigInteger (no terminator)");
            }
            int b = in[i++] & 0xFF;
            int data = b & 0x7F;
            if (data != 0) {
                result = result.or(BigInteger.valueOf(data).shiftLeft(shift));
            }
            shift += 7;
            count++;
            if (count > MAX_VAR_BIGINT_BYTES) {
                throw new IllegalArgumentException("VarInt BigInteger too long");
            }
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return new BigIntDec(result, i);
    }

    // ---- BigDecimal encode/decode (array) ----

    private static byte[] encodeZigZagVarBigDecimal(BigDecimal value) {
        ByteArrayOutputStream out = new ByteArrayOutputStream(32);
        // 1) scale as ZigZag VarInt32
        long x = zigZagEncodeInt(value.scale());
        while ((x & ~0x7FL) != 0L) {
            out.write(((int)x & 0x7F) | 0x80);
            x >>>= 7;
        }
        out.write((int)x);
        // 2) unscaled as ZigZag VarBigInt
        BigInteger zz = zigZagEncodeBigInt(value.unscaledValue());
        writeUnsignedVarBigInt(out, zz);
        return out.toByteArray();
    }

    private static BigDecimal decodeZigZagVarBigDecimal(byte[] bytes) {
        // read scale (u32)
        UInt32Dec s = readUnsignedVarInt32FromArray(bytes);
        int scale = zigZagDecodeInt(s.value);
        // read unscaled (big)
        BigIntDec ud = readUnsignedVarBigIntFromArray(bytes, s.next);
        // strict: require full consumption
        if (ud.next != bytes.length) {
            throw new IllegalArgumentException("Trailing bytes after BigDecimal payload");
        }
        BigInteger unscaled = zigZagDecodeBigInt(ud.value);
        return new BigDecimal(unscaled, scale);
    }
}
