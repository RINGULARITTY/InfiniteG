package fr.ringularity.infiniteg.recipes;

import net.minecraft.world.item.ItemStack;

public class ItemQuantity {
    public final ItemStack stack;
    public final long quantity;

    public ItemQuantity(ItemStack stack, long quantity) {
        this.stack = stack;
        this.quantity = quantity;
    }
}
