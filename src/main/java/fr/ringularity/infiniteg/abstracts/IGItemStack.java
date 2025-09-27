package fr.ringularity.infiniteg.abstracts;

import net.minecraft.world.item.ItemStack;

import java.math.BigInteger;

public class IGItemStack {
    public static final BigInteger INFINITE_SIZE = BigInteger.valueOf(-1);

    private final ItemQuantity iq;

    public IGItemStack() {
        iq = new ItemQuantity(ItemStack.EMPTY, BigInteger.ZERO);
    }

    public IGItemStack(ItemStack stack) {
        iq = new ItemQuantity(stack.copy(), BigInteger.ONE);
    }

    public IGItemStack(ItemQuantity iq) {
        this.iq = new ItemQuantity(iq.stack, iq.quantity);
    }

    public ItemQuantity getItemQuantity() {
        return iq;
    }

    public BigInteger getMaxRatioQuantity() {
        return INFINITE_SIZE;
    }

    public BigInteger getMaxQuantity(ItemStack stack) {
        BigInteger ratio = getMaxRatioQuantity();
        if (ratio.compareTo(BigInteger.ZERO) < 0 || iq.stack.isEmpty())
            return INFINITE_SIZE;

        return ratio.multiply(BigInteger.valueOf(stack.getMaxStackSize()));
    }

    public boolean isMergeable(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;
        return ItemStack.isSameItemSameComponents(this.iq.stack, stack);
    }

    public void setStack(ItemStack stack) {
        iq.stack = stack;
    }

    public BigInteger addQuantity(BigInteger quantity) {
        if (quantity == null || quantity.signum() <= 0) return BigInteger.ZERO;
        final BigInteger sum = iq.quantity.add(quantity);
        final BigInteger maxSize = getMaxQuantity(iq.stack);

        if (maxSize.compareTo(BigInteger.ZERO) < 0 || sum.compareTo(maxSize) <= 0) {
            iq.quantity = sum;
            return BigInteger.ZERO;
        }

        iq.quantity = maxSize;

        return sum.subtract(maxSize);
    }

    public int addQuantity(int quantity) {
        return addQuantity(BigInteger.valueOf(quantity)).intValue();
    }

    public BigInteger addItemQuantity(ItemQuantity iq) {
        if (!isMergeable(iq.stack))
            return iq.quantity;

        return addQuantity(iq.quantity);
    }

    public int addStack(ItemStack stack) {
        final int stackQuantity = stack.getCount();

        if (!isMergeable(stack))
            return stackQuantity;

        return addQuantity(stackQuantity);
    }

    public BigInteger removeQuantity(BigInteger amount) {
        if (amount == null || amount.signum() <= 0) return BigInteger.ZERO;

        if (this.iq.quantity.signum() <= 0) {
            return amount;
        }

        int cmp = this.iq.quantity.compareTo(amount);
        if (cmp >= 0) {
            this.iq.quantity = this.iq.quantity.subtract(amount);
            if (this.iq.quantity.signum() == 0) {
                this.iq.stack = ItemStack.EMPTY;
            }
            return BigInteger.ZERO;
        } else {
            BigInteger leftover = amount.subtract(this.iq.quantity);
            this.iq.quantity = BigInteger.ZERO;
            this.iq.stack = ItemStack.EMPTY;
            return leftover;
        }
    }

    public int removeQuantity(int amount) {
        return removeQuantity(BigInteger.valueOf(amount)).intValue();
    }

    public BigInteger removeItemQuantity(ItemQuantity other) {
        if (other == null || other.quantity == null || other.quantity.signum() <= 0) {
            return BigInteger.ZERO;
        }
        if (!isMergeable(other.stack)) {
            return other.quantity;
        }
        return removeQuantity(other.quantity);
    }

    public int removeStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return 0;
        final int req = stack.getCount();
        if (req <= 0) return 0;

        if (!isMergeable(stack)) {
            return req;
        }
        return removeQuantity(req);
    }

    public boolean isEmpty() {
        return iq.stack.isEmpty() || iq.quantity.equals(BigInteger.ZERO);
    }
}
