package fr.ringularity.infiniteg.network.codec;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.math.BigInteger;

public final class BigIntegerCodecs {
    private BigIntegerCodecs() {}

    // Encode BigInteger as:
    // [sign: 0|1 byte][mode: VAR_INT (0=VAR_LONG,1=BYTES)]
    // if mode==0: [magnitude as VAR_LONG]
    // else: [len VAR_INT][len bytes magnitude]
    public static final StreamCodec<ByteBuf, BigInteger> BIG_INT = StreamCodec.of(
            (buf, value) -> {
                int sign = value.signum() < 0 ? 1 : 0;
                BigInteger abs = value.abs();
                buf.writeByte(sign);
                if (abs.bitLength() <= 63) {
                    ByteBufCodecs.VAR_INT.encode(buf, 0); // mode 0: fits in long
                    ByteBufCodecs.VAR_LONG.encode(buf, abs.longValue());
                } else {
                    ByteBufCodecs.VAR_INT.encode(buf, 1); // mode 1: big magnitude
                    byte[] mag = abs.toByteArray(); // two's complement for positive abs => no leading sign bit
                    ByteBufCodecs.VAR_INT.encode(buf, mag.length);
                    buf.writeBytes(mag);
                }
            },
            buf -> {
                int sign = buf.readUnsignedByte();
                int mode = ByteBufCodecs.VAR_INT.decode(buf);
                BigInteger abs;
                if (mode == 0) {
                    long l = ByteBufCodecs.VAR_LONG.decode(buf);
                    abs = BigInteger.valueOf(l);
                } else {
                    int len = ByteBufCodecs.VAR_INT.decode(buf);
                    byte[] mag = new byte[len];
                    buf.readBytes(mag);
                    abs = new BigInteger(1, mag);
                }
                return sign == 1 ? abs.negate() : abs;
            }
    );
}
