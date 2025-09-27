package fr.ringularity.infiniteg.network.dto;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record DTOIndexItemQuantity(int key, DTOItemQuantity value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, DTOIndexItemQuantity> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, DTOIndexItemQuantity::key,
                    DTOItemQuantity.STREAM_CODEC, DTOIndexItemQuantity::value,
                    DTOIndexItemQuantity::new
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, List<DTOIndexItemQuantity>> LIST_CODEC =
            STREAM_CODEC.apply(ByteBufCodecs.list());
}