package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractAssemblerControllerMenu<B extends AbstractAssemblerControllerBlockEntity>
        extends AbstractIGMenu<B> {

    protected final Inventory playerInv;

    protected AbstractAssemblerControllerMenu(MenuType<?> type, int id, Inventory pinv, B be) {
        super(type, id, pinv, be);
        this.playerInv = pinv;
        addPlayerInventorySlots(pinv);
    }

    protected AbstractAssemblerControllerMenu(MenuType<?> type, int id, Inventory pinv, RegistryFriendlyByteBuf buf) {
        super(type, id, pinv, buf);
        this.playerInv = pinv;
        addPlayerInventorySlots(pinv);
    }

    @Override
    protected abstract Block expectedBlock();

    protected void addPlayerInventorySlots(Inventory pinv) {}

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}

