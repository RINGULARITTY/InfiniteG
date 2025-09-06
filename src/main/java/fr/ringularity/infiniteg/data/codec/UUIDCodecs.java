package fr.ringularity.infiniteg.data.codec;

import com.mojang.serialization.Codec;

import java.util.UUID;

public class UUIDCodecs {
    public static final Codec<UUID> CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
}
