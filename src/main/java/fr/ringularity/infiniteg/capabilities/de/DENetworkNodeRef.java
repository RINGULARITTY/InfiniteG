package fr.ringularity.infiniteg.capabilities.de;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record DENetworkNodeRef(ResourceKey<Level> level, BlockPos pos) {
    public static final Codec<DENetworkNodeRef> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            DECodecs.LEVEL_KEY_CODEC.fieldOf("level").forGetter(DENetworkNodeRef::level),
            BlockPos.CODEC.fieldOf("pos").forGetter(DENetworkNodeRef::pos)
    ).apply(inst, DENetworkNodeRef::new));
}
