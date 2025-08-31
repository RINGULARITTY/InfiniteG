package fr.ringularity.infiniteg.screens.network;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import fr.ringularity.infiniteg.screens.WorkstationScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CustomListItemQuantityPayload(ItemStack stack, long quantity) implements CustomPacketPayload {
    public static final Type<CustomListItemQuantityPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "custom_item_quantity_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CustomListItemQuantityPayload> CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC,
            CustomListItemQuantityPayload::stack,
            ByteBufCodecs.LONG,
            CustomListItemQuantityPayload::quantity,
            CustomListItemQuantityPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(CustomListItemQuantityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof WorkstationMenu menu) {
                menu.be.addItem(payload.stack(), payload.quantity());

                PacketDistributor.sendToPlayersTrackingChunk(
                        (ServerLevel) menu.be.getLevel(),
                        new ChunkPos(menu.be.getBlockPos()),
                        payload
                );
            }
        });
    }

    public static void handleClient(CustomListItemQuantityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof WorkstationScreen screen) {
                screen.getMenu().be.addItemClient(payload.stack(), payload.quantity());
            }
        });
    }
}
