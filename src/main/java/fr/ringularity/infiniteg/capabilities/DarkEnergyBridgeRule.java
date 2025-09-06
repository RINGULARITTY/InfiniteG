package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.ringularity.infiniteg.data.codec.BigDecimalCodecs;
import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import net.minecraft.core.UUIDUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

public record DarkEnergyBridgeRule(
        UUID fromNetwork,
        UUID toNetwork,
        Map<String, BigDecimal> minProps, // ex: {"entropy": 0.5} pour >= 50%
        Map<String, BigDecimal> maxProps, // ex: {"purity": 0.4} pour <= 40%
        BigInteger maxPerTick
) {
    public static final Codec<DarkEnergyBridgeRule> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.CODEC.fieldOf("from").forGetter(DarkEnergyBridgeRule::fromNetwork),
            UUIDUtil.CODEC.fieldOf("to").forGetter(DarkEnergyBridgeRule::toNetwork),
            Codec.unboundedMap(Codec.STRING, BigDecimalCodecs.CODEC).fieldOf("min").orElse(Map.of()).forGetter(DarkEnergyBridgeRule::minProps),
            Codec.unboundedMap(Codec.STRING, BigDecimalCodecs.CODEC).fieldOf("max").orElse(Map.of()).forGetter(DarkEnergyBridgeRule::maxProps),
            BigIntegerCodecs.CODEC.fieldOf("max_per_tick").orElse(BigInteger.valueOf(1_000_000L)).forGetter(DarkEnergyBridgeRule::maxPerTick)
    ).apply(inst, DarkEnergyBridgeRule::new));
}