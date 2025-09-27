package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.abstracts.ItemQuantity;
import fr.ringularity.infiniteg.blocks.entities.AbstractIGBE;
import fr.ringularity.infiniteg.network.dto.DTOIndexItemQuantity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractIGMenu<B extends BlockEntity> extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    protected final BlockPos pos;
    public final Optional<B> beRef;
    protected @Nullable Map<Integer, IGSlot> slots;

    protected AbstractIGMenu(MenuType<?> type, int id, Inventory pinv, B be) {
        super(type, id);
        this.pos = be.getBlockPos();
        this.access = ContainerLevelAccess.create(be.getLevel(), this.pos);
        this.beRef = Optional.of(be);

        if (beRef.orElse(null) instanceof AbstractIGBE igbe) {
            slots = createIGSlots(igbe);
        } else {
            slots = null;
        }
    }

    protected abstract Map<Integer, IGSlot> createIGSlots(AbstractIGBE igbe);

    protected AbstractIGMenu(MenuType<?> type, int id, Inventory pinv, RegistryFriendlyByteBuf buf) {
        super(type, id);
        this.pos = buf.readBlockPos();
        this.access = ContainerLevelAccess.NULL;
        this.beRef = Optional.empty();

        int n = buf.readVarInt();
        Map<Integer, IGSlot> slots = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            DTOIndexItemQuantity dto = DTOIndexItemQuantity.STREAM_CODEC.decode(buf);
            slots.put(dto.key(), new IGSlot(new ItemQuantity(dto.value().stack(), dto.value().quantity())));
        }

        this.slots = slots;
    }

    protected abstract Block expectedBlock();

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, expectedBlock());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    public void onSlotClick(Player p, int slotIndex) {
        if (beRef.isEmpty() || slots == null) return;

        B b = beRef.get();
        IGSlot slot = slots.get(slotIndex);
        if (slot == null || slot.isEmpty()) return;

        if (getCarried().isEmpty()) {
            ItemQuantity iq = slot.slotStack.getItemQuantity();
            ItemStack result = iq.stack.copy();

            int extractedQuantity = result.getMaxStackSize() - slot.slotStack.removeQuantity(result.getMaxStackSize());
            result.setCount(extractedQuantity);

            setCarried(result);
        } else {
            ItemStack result = getCarried();
            if (!slot.slotStack.isMergeable(result)) return;

            if (slot.isEmpty()) slot.slotStack.setStack(result);
            int rest = slot.slotStack.addQuantity(result.getCount());
            result.setCount(rest);

            setCarried(result);
        }
    }

    public void updateClientItemContent(Map<Integer, IGSlot> slots) {
        this.slots = slots;
    }

    public @Nullable Map<Integer, IGSlot> getSlots() {
        return slots;
    }
}
