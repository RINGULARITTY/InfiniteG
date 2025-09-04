package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.math.BigInteger;

public final class InfiniteGEnergyAdapter implements IEnergyStorage {
    public BigInteger energyStored = new BigInteger("100000");
    public BigInteger capacity = new BigInteger("100000000000");

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0) return 0;
        BigInteger want = BigInteger.valueOf(maxReceive);
        BigInteger space = capacity.subtract(energyStored).max(BigInteger.ZERO);
        int accepted = space.min(want).intValue();
        if (!simulate && accepted > 0) energyStored = energyStored.add(BigInteger.valueOf(accepted));
        return accepted;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract <= 0) return 0;
        int req = maxExtract;
        BigInteger want = BigInteger.valueOf(req);
        int taken = energyStored.min(want).intValue();
        if (!simulate && taken > 0) energyStored = energyStored.subtract(BigInteger.valueOf(taken));
        return taken;
    }

    public int getEnergyStored() {
        return energyStored.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public int getMaxEnergyStored() {
        return capacity.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public boolean canExtract() { return true; }
    public boolean canReceive() { return true; }

    public void loadAdditional(ValueInput input) {
        capacity = input.read("infinite_g_energy_adapter.capacity", BigIntegerCodecs.BIG_INT_CODEC).orElse(BigInteger.ZERO);
        energyStored = input.read("infinite_g_energy_adapter.energy_stored", BigIntegerCodecs.BIG_INT_CODEC).orElse(BigInteger.ZERO);
    }

    public void saveAdditional(ValueOutput output) {
        output.store("infinite_g_energy_adapter.capacity", BigIntegerCodecs.BIG_INT_CODEC, capacity);
        output.store("infinite_g_energy_adapter.energy_stored", BigIntegerCodecs.BIG_INT_CODEC, energyStored);
    }
}