package fr.ringularity.infiniteg.capabilities;

import net.minecraft.core.Direction;

public class DarkEnergyStorage implements ITypedCapabilityStorage {
    @Override
    public int receiveCapability(InfiniteGCapabilities.CapabilityType type, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int extractCapability(InfiniteGCapabilities.CapabilityType type, int amount, boolean simulate) {
        return 0;
    }

    @Override
    public int getCapabilityStored(InfiniteGCapabilities.CapabilityType type) {
        return 0;
    }

    @Override
    public int getMaxCapabilityStored(InfiniteGCapabilities.CapabilityType type) {
        return 0;
    }

    @Override
    public boolean canReceive(InfiniteGCapabilities.CapabilityType type, Direction side) {
        return false;
    }

    @Override
    public boolean canExtract(InfiniteGCapabilities.CapabilityType type, Direction side) {
        return false;
    }
}
