package fr.ringularity.infiniteg.network;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.entities.WorkstationBlockEntity;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Collections;
import java.util.List;

public record IntPayloadToServer(int value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<IntPayloadToServer> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "int_payload"));

    public static final StreamCodec<RegistryFriendlyByteBuf, IntPayloadToServer> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            IntPayloadToServer::value,
            IntPayloadToServer::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(IntPayloadToServer payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player().containerMenu instanceof WorkstationMenu menu) || menu.be.getLevel() == null) return;
            int cmd = payload.value();
            WorkstationBlockEntity be = menu.be;
            List<UpdateItemQuantitiesToClient.RecipeItemQuantityPayload> items =
                    be.updateRecipeItems(context.player(), cmd);
            if (items == null) items = Collections.emptyList();
            UpdateItemQuantitiesToClient response = new UpdateItemQuantitiesToClient(items);

            // Always respond to the sender; optionally also broadcast to trackers if multiple viewers are supported
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                    (net.minecraft.server.level.ServerPlayer) context.player(), response
            );
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayersTrackingChunk(
                    (net.minecraft.server.level.ServerLevel) be.getLevel(),
                    new net.minecraft.world.level.ChunkPos(be.getBlockPos()),
                    response
            );
        });
    }
}
