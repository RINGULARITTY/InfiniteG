package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.entities.WorkstationBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class WorkstationMenu extends AbstractContainerMenu {
    public final WorkstationBlockEntity be;
    private final Level level;
    private final ContainerData data;

    public WorkstationMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public WorkstationMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.WORKSTATION_MENU.get(), pContainerId);
        this.be = ((WorkstationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        for (int i = 0; i < this.be.OUTPUT_SLOT + 1; ++i) {
            this.addSlot(new SlotItemHandler(be.itemHandler, i, 20 + 25 * i, 34));
        }

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public float getProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);

        return (float) progress / (float) maxProgress;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()),
                pPlayer, ModBlocks.WORKSTATION.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 9; i < 18; ++i)
            this.addSlot(new Slot(playerInventory, i, 15 + i * 18, 160));
        for (int i = 0; i < 18; ++i)
            this.addSlot(new Slot(playerInventory, i + 18, 15 + i * 18, 178));
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 15 + i * 18, 160));
        }
    }
}
