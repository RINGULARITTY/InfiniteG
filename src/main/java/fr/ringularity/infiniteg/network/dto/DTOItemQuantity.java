package fr.ringularity.infiniteg.network.dto;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public record DTOItemQuantity(ItemStack stack, BigInteger quantity) {
    public static final StreamCodec<RegistryFriendlyByteBuf, BigInteger> BIGINT_CODEC =
            new StreamCodec<>() {
                @Override
                public @NotNull BigInteger decode(RegistryFriendlyByteBuf buf) {
                    int len = buf.readVarInt();
                    byte[] bytes = new byte[len];
                    buf.readBytes(bytes);
                    return new BigInteger(bytes);
                }
                @Override
                public void encode(RegistryFriendlyByteBuf buf, BigInteger value) {
                    byte[] bytes = value.toByteArray();
                    buf.writeVarInt(bytes.length);
                    buf.writeBytes(bytes);
                }
            };

    public static final StreamCodec<RegistryFriendlyByteBuf, DTOItemQuantity> STREAM_CODEC =
            StreamCodec.composite(
                    ItemStack.STREAM_CODEC, DTOItemQuantity::stack,
                    BIGINT_CODEC,          DTOItemQuantity::quantity,
                    DTOItemQuantity::new
            );
}

