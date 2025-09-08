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
import org.jetbrains.annotations.NotNull;

public class WorkstationMenu extends AbstractContainerMenu {
    public final WorkstationBlockEntity be;
    private final Level level;
    private final ContainerData data;

    public WorkstationMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public WorkstationMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.WORKSTATION_MENU.get(), containerId);
        this.be = ((WorkstationBlockEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addDataSlots(data);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, be.getBlockPos()), player, ModBlocks.WORKSTATION.get());
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

    public int getSelectedRecipeId() {
        return be.selectedRecipeId;
    }
}
