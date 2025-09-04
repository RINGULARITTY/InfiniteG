package fr.ringularity.infiniteg.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Compact BigInteger codecs using ZigZag + base-128 VarInt to minimize network size for signed values
 * - ZigZag maps signed integers to unsigned so that small-magnitude negatives become small encodings too [6]
 * - Base-128 VarInt encodes the unsigned integer with 7 data bits per byte and a continuation MSB [7]
 * - This avoids two's-complement sign-extension bytes produced by BigInteger.toByteArray() [5]
 */
public final class BigIntegerCodecs {
    private BigIntegerCodecs() {}

    // Small reusable constants to avoid recreating BigInteger instances
    private static final BigInteger BI_ZERO = BigInteger.ZERO; // constant zero [5]
    private static final BigInteger BI_ONE  = BigInteger.ONE;  // constant one [5]
    private static final BigInteger MASK_7BITS = BigInteger.valueOf(0x7F); // 7-bit mask for VarInt chunks [7]

    // Defensive limit to avoid pathological inputs during decode (1 MiB of VarInt bytes ~ 7+ Mbits of payload)
    // Tune or remove as needed depending on protocol constraints [7]
    private static final int MAX_VAR_INT_BYTES = 1 << 20; // 1,048,576 bytes guard [7]

    /**
     * Public codec: writes BigInteger as ZigZag VarInt and reads it back symmetrically [6]
     */
    public static final StreamCodec<ByteBuf, BigInteger> BIG_INT_STREAM_CODEC = StreamCodec.of(
            // Encoder: ZigZag-encode signed BigInteger, then write base-128 VarInt [6][7]
            BigIntegerCodecs::writeZigZagVarBigInt, // compact write path [6][7]
            // Decoder: read base-128 VarInt as unsigned BigInteger, then ZigZag-decode to signed [6][7]
            BigIntegerCodecs::readZigZagVarBigInt // compact read path [6][7]
    );

    public static final Codec<BigInteger> BIG_INT_CODEC =
            Codec.BYTE_BUFFER.comapFlatMap(
                    buf -> {
                        ByteBuffer dup = buf.slice();
                        byte[] bytes = new byte[dup.remaining()];
                        dup.get(bytes);
                        try {
                            return DataResult.success(decodeZigZagVarBigInt(bytes));
                        } catch (RuntimeException ex) {
                            return DataResult.error(ex::getMessage);
                        }
                    },
                    bi -> ByteBuffer.wrap(encodeZigZagVarBigInt(bi))
            );

    /**
     * ZigZag encode a signed BigInteger to an unsigned BigInteger
     * Definition:
     *   zz(n) = (n >= 0) ? (n << 1) : ((-n << 1) - 1)  [6]
     * This makes small negative values encode into few bytes, just like small positives [6]
     */
    private static BigInteger zigZagEncode(BigInteger n) {
        if (n.signum() >= 0) {
            return n.shiftLeft(1); // positive path [6]
        } else {
            return n.negate().shiftLeft(1).subtract(BI_ONE); // negative path [6]
        }
    }

    /**
     * ZigZag decode an unsigned BigInteger back to a signed BigInteger
     * Inverse:
     *   if (zz is even)  n = zz >> 1
     *   if (zz is odd)   n = -((zz >> 1) + 1)  [6]
     */
    private static BigInteger zigZagDecode(BigInteger zz) {
        // Test least-significant bit to detect odd/even (sign) [6]
        boolean odd = zz.testBit(0); // LSB set => negative original [6]
        BigInteger shifted = zz.shiftRight(1); // divide by 2 [6]
        return odd ? shifted.add(BI_ONE).negate() : shifted; // reconstruct signed value [6]
    }

    /**
     * Write unsigned BigInteger as base-128 VarInt into ByteBuf
     * Emits at least one byte (0x00) for zero; otherwise 7-bit chunks with MSB=1 for continuation [7]
     */
    private static void writeUnsignedVarBigInt(ByteBuf out, BigInteger u) {
        if (u.signum() == 0) {
            out.writeByte(0); // canonical zero representation [7]
            return;
        }
        BigInteger x = u; // working copy [7]
        while (x.signum() != 0) {
            int low7 = x.and(MASK_7BITS).intValue(); // extract lowest 7 bits [7]
            x = x.shiftRight(7); // consume 7 bits [7]
            if (x.signum() != 0) {
                out.writeByte(low7 | 0x80); // set continuation bit [7]
            } else {
                out.writeByte(low7); // last chunk, MSB=0 [7]
            }
        }
    }

    /**
     * Read unsigned BigInteger from base-128 VarInt in ByteBuf
     * Accumulates 7-bit groups until a byte with MSB=0 is seen; throws on truncated/oversized input [7]
     */
    private static BigInteger readUnsignedVarBigInt(ByteBuf in) {
        BigInteger result = BI_ZERO; // accumulator [7]
        int shift = 0; // bit offset in result [7]
        int count = 0; // byte counter for safety [7]

        while (true) {
            if (!in.isReadable()) {
                throw new IndexOutOfBoundsException("Truncated VarInt BigInteger"); // defensive check [7]
            }
            int b = in.readUnsignedByte(); // read next byte [7]
            int data = b & 0x7F; // 7-bit payload [7]
            if (data != 0) {
                // OR the chunk at current shift using BigInteger to avoid overflow [7]
                result = result.or(BigInteger.valueOf(data).shiftLeft(shift)); // accumulate [7]
            }
            shift += 7; // next 7-bit window [7]
            count++;
            if (count > MAX_VAR_INT_BYTES) {
                throw new IllegalArgumentException("VarInt BigInteger too long"); // sanity guard [7]
            }
            if ((b & 0x80) == 0) {
                break; // last byte [7]
            }
        }
        return result; // unsigned result [7]
    }

    /**
     * Write signed BigInteger using ZigZag + VarInt to minimize size for small magnitudes [6][7]
     */
    private static void writeZigZagVarBigInt(ByteBuf out, BigInteger value) {
        // ZigZag: map signed -> unsigned to keep small negatives small [6]
        BigInteger zz = zigZagEncode(value); // signed to unsigned mapping [6]
        // VarInt: write 7-bit groups with continuation MSB [7]
        writeUnsignedVarBigInt(out, zz); // final wire format [7]
    }

    /**
     * Read signed BigInteger encoded as ZigZag + VarInt [6][7]
     */
    private static BigInteger readZigZagVarBigInt(ByteBuf in) {
        // VarInt: read unsigned value from 7-bit chunks [7]
        BigInteger zz = readUnsignedVarBigInt(in); // wire to unsigned [7]
        // ZigZag: map unsigned -> signed BigInteger [6]
        return zigZagDecode(zz); // final signed value [6]
    }

    // Encode signed BigInteger as ZigZag then VarInt -> byte[]
    private static byte[] encodeZigZagVarBigInt(BigInteger value) {
        BigInteger zz = zigZagEncode(value);
        return writeUnsignedVarBigIntToArray(zz);
    }

    // Decode byte[] VarInt -> ZigZag -> signed BigInteger
    private static BigInteger decodeZigZagVarBigInt(byte[] bytes) {
        BigInteger zz = readUnsignedVarBigIntFromArray(bytes);
        return zigZagDecode(zz);
    }

    // Unsigned VarInt -> byte[]
    private static byte[] writeUnsignedVarBigIntToArray(BigInteger u) {
        if (u.signum() == 0) {
            return new byte[]{0};
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(16);
        BigInteger x = u;
        while (x.signum() != 0) {
            int low7 = x.and(MASK_7BITS).intValue();
            x = x.shiftRight(7);
            out.write(x.signum() != 0 ? (low7 | 0x80) : low7);
        }
        return out.toByteArray();
    }

    // Unsigned VarInt <- byte[]
    private static BigInteger readUnsignedVarBigIntFromArray(byte[] in) {
        BigInteger result = BI_ZERO;
        int shift = 0;

        if (in.length == 0) {
            throw new IllegalArgumentException("Empty VarInt BigInteger");
        }
        if (in.length > MAX_VAR_INT_BYTES) {
            throw new IllegalArgumentException("VarInt BigInteger too long");
        }

        boolean terminated = false;
        for (byte value : in) {
            int b = value & 0xFF;
            int data = b & 0x7F;
            if (data != 0) {
                result = result.or(BigInteger.valueOf(data).shiftLeft(shift));
            }
            shift += 7;
            if ((b & 0x80) == 0) {
                terminated = true;
                // Ignore any extra bytes that may appear after the terminator
                break;
            }
        }
        if (!terminated) {
            // No byte with MSB=0: truncated input
            throw new IllegalArgumentException("Truncated VarInt BigInteger (no terminator)");
        }
        return result;
    }


}
