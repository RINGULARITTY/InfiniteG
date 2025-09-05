package fr.ringularity.infiniteg.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;

public interface ITypedCapabilityStorage {
    int receiveCapability(InfiniteGCapabilities.CapabilityType type, int amount, boolean simulate);
    int extractCapability(InfiniteGCapabilities.CapabilityType type, int amount, boolean simulate);
    int getCapabilityStored(InfiniteGCapabilities.CapabilityType type);
    int getMaxCapabilityStored(InfiniteGCapabilities.CapabilityType type);
    boolean canReceive(InfiniteGCapabilities.CapabilityType type, Direction side);
    boolean canExtract(InfiniteGCapabilities.CapabilityType type, Direction side);
}