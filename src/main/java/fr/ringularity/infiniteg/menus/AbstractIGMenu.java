package fr.ringularity.infiniteg.menus;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

// Base: ne connaît aucun MenuType concret, ni le bloc exact.
// B est le type BE attaché côté serveur, absent côté client.
public abstract class AbstractIGMenu<B extends BlockEntity> extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    protected final BlockPos pos;
    protected final Optional<B> beRef; // présent côté serveur, vide côté client

    // Ctor serveur: reçoit le BE réel
    protected AbstractIGMenu(MenuType<?> type, int id, Inventory pinv, B be) {
        super(type, id);
        this.pos = be.getBlockPos();
        this.access = ContainerLevelAccess.create(be.getLevel(), this.pos);
        this.beRef = Optional.of(be);
    }

    // Ctor client: lit les infos minimales (ex. pos) depuis le buffer
    protected AbstractIGMenu(MenuType<?> type, int id, Inventory pinv, FriendlyByteBuf buf) {
        super(type, id);
        this.pos = buf.readBlockPos();
        this.access = ContainerLevelAccess.NULL;
        this.beRef = Optional.empty();
    }

    // Le bloc attendu est fourni par une sous-classe
    protected abstract Block expectedBlock();

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, expectedBlock());
    }

    // Intention venant du Screen (à traiter uniquement sur serveur)
    public abstract void onClientIntent(ServerPlayer player, int action, long amount);

    // Snapshots S2C pour synchroniser l'affichage client
    public abstract void pushSnapshot(ServerPlayer viewer);

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // Base: aucun Slot vanilla par défaut
    }
}
