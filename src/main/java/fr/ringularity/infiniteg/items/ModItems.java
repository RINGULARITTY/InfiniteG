package fr.ringularity.infiniteg.items;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(InfiniteG.MOD_ID);

    public static final DeferredItem<BlockItem> COMPACTOR =
            ITEMS.registerSimpleBlockItem("compactor", ModBlocks.COMPACTOR, new Item.Properties());
    public static final DeferredItem<BlockItem> WORKSTATION =
            ITEMS.registerSimpleBlockItem("workstation", ModBlocks.WORKSTATION, new Item.Properties());

    public static final DeferredItem<BlockItem> DE_NETWORK_CONTROLLER =
            ITEMS.registerSimpleBlockItem("de_network_controller", ModBlocks.DE_NETWORK_CONTROLLER, new Item.Properties());
    public static final DeferredItem<BlockItem> DE_PIPE =
            ITEMS.registerSimpleBlockItem("de_pipe", ModBlocks.DE_PIPE, new Item.Properties());
    public static final DeferredItem<BlockItem> DE_GENERATOR =
            ITEMS.registerSimpleBlockItem("de_generator", ModBlocks.DE_GENERATOR, new Item.Properties());
    public static final DeferredItem<BlockItem> DE_PURIFIER =
            ITEMS.registerSimpleBlockItem("de_purifier", ModBlocks.DE_PURIFIER, new Item.Properties());

    public static final DeferredItem<BlockItem> ASSEMBLER =
            ITEMS.registerSimpleBlockItem("assembler", ModBlocks.ASSEMBLER, new Item.Properties());
    public static final DeferredItem<BlockItem> ASSEMBLER_BASE =
            ITEMS.registerSimpleBlockItem("assembler_base", ModBlocks.ASSEMBLER_BASE, new Item.Properties());

    public static final DeferredItem<BlockItem> DENSE_COBBLESTONE_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DENSE_COBBLESTONE);
    public static final DeferredItem<BlockItem> DENSE_WOOD_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DENSE_WOOD);
    public static final DeferredItem<BlockItem> DENSE_PLANKS_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DENSE_PLANKS);
    public static final DeferredItem<BlockItem> DENSE_IRON_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DENSE_IRON);

    public static final DeferredItem<Item> TEST =
            ITEMS.registerItem("test", props -> new TestItem(props, "test"), new Item.Properties());
    public static final DeferredItem<Item> DIAMOND_COMPACTOR =
            ITEMS.registerItem("diamond_compactor", props -> new CompactItem(props, Items.DIAMOND), new Item.Properties());
    public static final DeferredItem<Item> LARGE_PLANK =
            ITEMS.registerItem("dense_wood_plank", Item::new, new Item.Properties());
    public static final DeferredItem<Item> IRON_PLATE =
            ITEMS.registerItem("iron_plate", Item::new, new Item.Properties());
    public static final DeferredItem<Item> DENSE_IRON_PLATE =
            ITEMS.registerItem("dense_iron_plate", Item::new, new Item.Properties());
    public static final DeferredItem<Item> HAMMER =
            ITEMS.registerItem("hammer", Item::new, new Item.Properties());

    public static void register(IEventBus bus) { ITEMS.register(bus); }
}
