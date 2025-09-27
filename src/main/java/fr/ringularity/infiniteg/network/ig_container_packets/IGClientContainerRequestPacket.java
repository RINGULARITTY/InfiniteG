package fr.ringularity.infiniteg.network.ig_container_packets;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.menus.AbstractIGMenu;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record IGClientContainerRequestPacket(int slotId, ContainerAction action, boolean fromBlockContainer) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<IGClientContainerRequestPacket> TYPE =
            new CustomPacketPayload.Type<IGClientContainerRequestPacket>(ResourceLocation.fromNamespaceAndPath(
                    InfiniteG.MOD_ID, "packets.ig_client_container_request"
            ));


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleServer(IGClientContainerRequestPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player().containerMenu instanceof AbstractIGMenu<?> menu) || menu.beRef.isEmpty()) return;
        });
    }
}
