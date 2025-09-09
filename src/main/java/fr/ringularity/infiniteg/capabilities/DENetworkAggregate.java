package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.ringularity.infiniteg.data.codec.BigDecimalCodecs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public record DENetworkAggregate(BigDecimal quantity,
                                 Map<String, BigDecimal> properties // ex: "purity", "entropy", ...
) {
    public static final Codec<DENetworkAggregate> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BigDecimalCodecs.CODEC.fieldOf("q").forGetter(DENetworkAggregate::quantity),
            Codec.unboundedMap(Codec.STRING, BigDecimalCodecs.CODEC).fieldOf("props").forGetter(DENetworkAggregate::properties)
    ).apply(inst, DENetworkAggregate::new));

    public DENetworkAggregate mixIn(BigDecimal qIn, Map<String, BigDecimal> propsIn, boolean modify) {
        if (qIn.signum() == 0) return this;
        BigDecimal q0 = this.quantity;
        BigDecimal qN = q0.add(qIn);
        if (qN.signum() == 0) return new DENetworkAggregate(BigDecimal.ZERO, Map.of());

        Map<String, BigDecimal> out = new HashMap<>(properties);
        for (var key : propsIn.keySet()) {
            BigDecimal pIn = propsIn.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal p0  = properties.getOrDefault(key, BigDecimal.ZERO);

            // P' = (Qp*Pp + Qr*Pr) / (Qp + Qr)
            BigDecimal num = qIn.multiply(pIn).add(q0.multiply(p0));
            BigDecimal pN = qN.signum() == 0 ? BigDecimal.ZERO : num.divide(qN, MathContext.DECIMAL128);
            out.put(key, pN);
        }
        for (var key : properties.keySet()) {
            out.putIfAbsent(key, properties.get(key));
        }

        if (modify)
            return new DENetworkAggregate(q0, out);

        return new DENetworkAggregate(qN, out);
    }

    public DENetworkAggregate addLoss(BigDecimal dq) {
        return new DENetworkAggregate(this.quantity.add(dq), this.properties);
    }
}