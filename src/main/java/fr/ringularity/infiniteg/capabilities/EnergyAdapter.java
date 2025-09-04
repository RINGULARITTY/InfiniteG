package fr.ringularity.infiniteg.capabilities;

import net.neoforged.neoforge.energy.IEnergyStorage;

import java.math.BigInteger;

public final class EnergyAdapter implements IEnergyStorage {
    private BigInteger energy = new BigInteger("1000000");
    private final BigInteger capacity = new BigInteger("1000000000000000000000");;          // interne illimité

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0) return 0;
        BigInteger want = BigInteger.valueOf(maxReceive);
        BigInteger space = capacity.subtract(energy).max(BigInteger.ZERO);
        int accepted = space.min(want).intValue();
        if (!simulate && accepted > 0) energy = energy.add(BigInteger.valueOf(accepted));
        return accepted;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract <= 0) return 0;
        int req = maxExtract;
        BigInteger want = BigInteger.valueOf(req);
        int taken = energy.min(want).intValue();
        if (!simulate && taken > 0) energy = energy.subtract(BigInteger.valueOf(taken));
        return taken;
    }

    public int getEnergyStored() {
        return energy.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public int getMaxEnergyStored() {
        return capacity.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public boolean canExtract() { return true; }
    public boolean canReceive() { return true; }
}