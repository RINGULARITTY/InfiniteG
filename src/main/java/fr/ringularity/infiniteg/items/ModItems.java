package fr.ringularity.infiniteg.items;

import fr.ringularity.infiniteg.InfiniteG;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(InfiniteG.MOD_ID);

    public static final DeferredItem<Item> TEST = ITEMS.registerItem("test",
            (Item.Properties properties) -> new TestItem(properties, "test"), new Item.Properties());

    public static final DeferredItem<Item> DIAMOND_COMPACTOR = ITEMS.registerItem("diamond_compactor",
            (Item.Properties properties) -> new CompactItem(properties, Items.DIAMOND), new Item.Properties());

    public static final DeferredItem<Item> LARGE_PLANK = ITEMS.registerItem("dense_wood_plank", Item::new, new Item.Properties());
    public static final DeferredItem<Item> IRON_PLATE = ITEMS.registerItem("iron_plate", Item::new, new Item.Properties());
    public static final DeferredItem<Item> DENSE_IRON_PLATE = ITEMS.registerItem("dense_iron_plate", Item::new, new Item.Properties());

    public static final DeferredItem<Item> HAMMER = ITEMS.registerItem("hammer", Item::new, new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
