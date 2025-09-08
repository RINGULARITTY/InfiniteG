package fr.ringularity.infiniteg.network;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import fr.ringularity.infiniteg.screens.WorkstationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

public record UpdateItemQuantitiesToClient(List<RecipeItemQuantityPayload> items) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateItemQuantitiesToClient> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "update_item_quantities_to_client"));

    public record RecipeItemQuantityPayload(ItemStack stack, BigInteger currentAmount, BigInteger requiredAmount) {
        public static final StreamCodec<RegistryFriendlyByteBuf, RecipeItemQuantityPayload> CODEC = StreamCodec.composite(
                ItemStack.STREAM_CODEC,
                RecipeItemQuantityPayload::stack,
                BigIntegerCodecs.STREAM_CODEC,
                RecipeItemQuantityPayload::currentAmount,
                BigIntegerCodecs.STREAM_CODEC,
                RecipeItemQuantityPayload::requiredAmount,
                RecipeItemQuantityPayload::new
        );
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateItemQuantitiesToClient> CODEC = StreamCodec.composite(
            RecipeItemQuantityPayload.CODEC.apply(ByteBufCodecs.list()),
            UpdateItemQuantitiesToClient::items,
            UpdateItemQuantitiesToClient::new
    );

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(UpdateItemQuantitiesToClient payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof WorkstationScreen screen) {
                screen.updateIngredientsDisplay(payload.items());
            }
        });
    }
}
