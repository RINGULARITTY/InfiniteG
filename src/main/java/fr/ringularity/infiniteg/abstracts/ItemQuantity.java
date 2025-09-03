package fr.ringularity.infiniteg.abstracts;

import net.minecraft.world.item.ItemStack;

import java.math.BigInteger;

public class ItemQuantity {
    public ItemStack stack;
    public BigInteger quantity;

    public ItemQuantity(ItemStack item, BigInteger quantity) {
        this.stack = item;
        this.quantity = quantity;
    }
}
