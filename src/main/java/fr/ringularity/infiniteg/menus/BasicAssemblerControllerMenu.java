package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.entities.assembler.BasicAssemblerControllerBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public final class BasicAssemblerControllerMenu
        extends AbstractAssemblerControllerMenu<BasicAssemblerControllerBlockEntity> {

    public static BasicAssemblerControllerMenu clientCtor(int id, Inventory pinv, FriendlyByteBuf buf) {
        return new BasicAssemblerControllerMenu(id, pinv, buf);
    }

    // Ctor client réel
    private BasicAssemblerControllerMenu(int id, Inventory pinv, FriendlyByteBuf buf) {
        super(ModMenuTypes.BASIC_ASSEMBLER_MENU.get(), id, pinv, buf);
        // initialiser l’état client (snapshots/deltas) si nécessaire
    }

    // Ctor serveur
    public BasicAssemblerControllerMenu(int id, Inventory pinv, BasicAssemblerControllerBlockEntity be) {
        super(ModMenuTypes.BASIC_ASSEMBLER_MENU.get(), id, pinv, be);
        // accès serveur au BE via beRef.get() si présent, sinon Optional.empty() côté client
    }

    @Override
    protected Block expectedBlock() {
        return ModBlocks.BASIC_ASSEMBLER_CONTROLLER.get(); // bloc commun de la famille
    }

    @Override
    public void onClientIntent(ServerPlayer player, int action, long amount) {
        // Ne jamais faire confiance au client : valider bornes, droits, distance; appliquer via BE/capabilities
        beRef.ifPresent(be -> {
            // mutations serveur; envoyer snapshot après application
        });
    }

    @Override
    public void pushSnapshot(ServerPlayer viewer) {
        // encoder et envoyer l’état au client (BigInteger -> bytes), DataSlot pour petits entiers seulement
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Si vous avez ajouté des Slots vanilla (ex. inventaire joueur), gérer ici le shift‑click
        return ItemStack.EMPTY;
    }
}
