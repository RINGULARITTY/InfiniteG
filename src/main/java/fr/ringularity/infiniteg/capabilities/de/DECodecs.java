package fr.ringularity.infiniteg.capabilities.de;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public final class DECodecs {
    public static final Codec<ResourceKey<Level>> LEVEL_KEY_CODEC =
            ResourceKey.codec(Registries.DIMENSION);
}