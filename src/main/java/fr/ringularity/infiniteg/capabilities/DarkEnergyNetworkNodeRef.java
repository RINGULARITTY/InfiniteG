package fr.ringularity.infiniteg.capabilities;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record DarkEnergyNetworkNodeRef(ResourceKey<Level> level, BlockPos pos) {
    public static final Codec<DarkEnergyNetworkNodeRef> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            DarkEnergyCodecs.LEVEL_KEY_CODEC.fieldOf("level").forGetter(DarkEnergyNetworkNodeRef::level),
            BlockPos.CODEC.fieldOf("pos").forGetter(DarkEnergyNetworkNodeRef::pos)
    ).apply(inst, DarkEnergyNetworkNodeRef::new));
}
