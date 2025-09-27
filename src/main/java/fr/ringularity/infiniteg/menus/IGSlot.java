package fr.ringularity.infiniteg.menus;

import fr.ringularity.infiniteg.abstracts.IGItemStack;
import fr.ringularity.infiniteg.abstracts.ItemQuantity;

import java.math.BigInteger;

public class IGSlot {
    public IGItemStack slotStack;

    public IGSlot() {
        slotStack = new IGItemStack();
    }

    public IGSlot(int maxRatioSize) {
        slotStack = new IGItemStack() {
            @Override
            public BigInteger getMaxRatioQuantity() {
                return BigInteger.valueOf(maxRatioSize);
            }
        };
    }

    public IGSlot(ItemQuantity iq) {
        slotStack = new IGItemStack(iq);
    }

    public IGSlot(ItemQuantity iq, int maxRatioSize) {
        slotStack = new IGItemStack(iq) {
            @Override
            public BigInteger getMaxRatioQuantity() {
                return BigInteger.valueOf(maxRatioSize);
            }
        };
    }

    public boolean isEmpty() {
        return slotStack.isEmpty();
    }
}
