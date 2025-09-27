package fr.ringularity.infiniteg.network.ig_container_packets;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.menus.AbstractIGMenu;
import fr.ringularity.infiniteg.menus.IGSlot;
import fr.ringularity.infiniteg.network.dto.DTOIndexItemQuantity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record IGContainerSlotsS2C(int containerId, BlockPos pos, List<DTOIndexItemQuantity> entries)
        implements CustomPacketPayload {

    public static final Type<IGContainerSlotsS2C> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "ig_slots_s2c"));

    public static final StreamCodec<RegistryFriendlyByteBuf, IGContainerSlotsS2C> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,                IGContainerSlotsS2C::containerId,
                    BlockPos.STREAM_CODEC,                IGContainerSlotsS2C::pos,
                    DTOIndexItemQuantity.LIST_CODEC,         IGContainerSlotsS2C::entries,
                    IGContainerSlotsS2C::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleClient(IGContainerSlotsS2C payload, IPayloadContext context) {
        final Minecraft mc = Minecraft.getInstance();
        mc.execute(() -> {
            if (mc.player == null) return;

            final AbstractContainerMenu menu = mc.player.containerMenu;
            if (menu.containerId != payload.containerId()) return;
            if (!(menu instanceof AbstractIGMenu<?> ig)) return;

            Map<Integer, IGSlot> slots = new HashMap<>(payload.entries().size());
            for (DTOIndexItemQuantity e : payload.entries()) slots.put(e.key(), new IGSlot(new ItemQuantity(e.value().stack(), e.value().quantity())));

            ig.updateClientItemContent(slots);
        });
    }
}

