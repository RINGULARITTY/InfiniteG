package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.entities.AbstractIGBEContainer;
import fr.ringularity.infiniteg.blocks.entities.assembler.BasicAssemblerControllerBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public final class BasicAssemblerControllerMenu
        extends AbstractAssemblerControllerMenu<BasicAssemblerControllerBlockEntity> {

    public static BasicAssemblerControllerMenu clientCtor(int id, Inventory pinv, RegistryFriendlyByteBuf buf) {
        return new BasicAssemblerControllerMenu(id, pinv, buf);
    }

    // Client
    private BasicAssemblerControllerMenu(int id, Inventory pinv, RegistryFriendlyByteBuf buf) {
        super(ModMenuTypes.BASIC_ASSEMBLER_MENU.get(), id, pinv, buf);
    }

    // Server
    public BasicAssemblerControllerMenu(int id, Inventory pinv, BasicAssemblerControllerBlockEntity be) {
        super(ModMenuTypes.BASIC_ASSEMBLER_MENU.get(), id, pinv, be);
    }

    @Override
    protected Map<Integer, IGSlot> createIGSlots(AbstractIGBEContainer igbe) {
        Map<Integer, IGSlot> slots = new HashMap<>();

        for (int slotIndex : igbe.storedItems.keySet())
            slots.put(slotIndex, new IGSlot(igbe.storedItems.get(slotIndex)));

        return slots;
    }

    @Override
    protected Block expectedBlock() {
        return ModBlocks.BASIC_ASSEMBLER_CONTROLLER.get();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
