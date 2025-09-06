package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.ringularity.infiniteg.data.codec.BigDecimalCodecs;
import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public record DarkEnergyNetworkAggregate(BigInteger quantity,
                                         Map<String, BigDecimal> properties // ex: "purity", "entropy", ...
) {
    public static final Codec<DarkEnergyNetworkAggregate> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BigIntegerCodecs.CODEC.fieldOf("q").forGetter(DarkEnergyNetworkAggregate::quantity),
            Codec.unboundedMap(Codec.STRING, BigDecimalCodecs.CODEC).fieldOf("props").forGetter(DarkEnergyNetworkAggregate::properties)
    ).apply(inst, DarkEnergyNetworkAggregate::new));

    public DarkEnergyNetworkAggregate mixIn(BigInteger qIn, Map<String, BigDecimal> propsIn, boolean modify) {
        if (qIn.signum() == 0) return this;
        BigInteger q0 = this.quantity;
        BigInteger qN = q0.add(qIn);
        if (qN.signum() == 0) return new DarkEnergyNetworkAggregate(BigInteger.ZERO, Map.of());

        Map<String, BigDecimal> out = new HashMap<>(properties);
        for (var key : propsIn.keySet()) {
            BigDecimal pIn = propsIn.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal p0  = properties.getOrDefault(key, BigDecimal.ZERO);

            // P' = (Qp*Pp + Qr*Pr) / (Qp + Qr)
            BigDecimal num = new BigDecimal(qIn).multiply(pIn).add(new BigDecimal(q0).multiply(p0));
            BigDecimal den = new BigDecimal(qN);
            BigDecimal pN = den.signum() == 0 ? BigDecimal.ZERO : num.divide(den, MathContext.DECIMAL128);
            out.put(key, pN);
        }
        for (var key : properties.keySet()) {
            out.putIfAbsent(key, properties.get(key));
        }

        if (modify)
            return new DarkEnergyNetworkAggregate(q0, out);

        return new DarkEnergyNetworkAggregate(qN, out);
    }

    public DarkEnergyNetworkAggregate addLoss(BigInteger dq) {
        return new DarkEnergyNetworkAggregate(this.quantity.add(dq), this.properties);
    }
}