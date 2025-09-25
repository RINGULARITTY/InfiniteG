package fr.ringularity.infiniteg.menus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractAssemblerControllerMenu<B extends BlockEntity>
        extends AbstractIGMenu<B> {

    protected final Inventory playerInv;

    protected AbstractAssemblerControllerMenu(MenuType<?> type, int id, Inventory pinv, B be) {
        super(type, id, pinv, be);
        this.playerInv = pinv;
        addPlayerInventorySlots(pinv); // si on veut afficher l’inventaire joueur
    }

    protected AbstractAssemblerControllerMenu(MenuType<?> type, int id, Inventory pinv, FriendlyByteBuf buf) {
        super(type, id, pinv, buf);
        this.playerInv = pinv;
        addPlayerInventorySlots(pinv); // mirroir côté client
    }

    // Fournit le bloc attendu pour stillValid (chaque famille de contrôleurs a un bloc commun)
    @Override
    protected abstract Block expectedBlock();

    // Slots joueur (facultatif si UI 100% custom)
    protected void addPlayerInventorySlots(Inventory pinv) {
        // 3x9 + 1x9, aux coordonnées adéquates si on rend l’inventaire joueur
    }

    // Squelette shift‑click commun; déléguer à des méthodes de tier si besoin
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Si aucun Slot vanilla local, ne gérer que les transferts vers/depuis inventaire joueur, sinon déléguer
        return ItemStack.EMPTY;
    }
}

