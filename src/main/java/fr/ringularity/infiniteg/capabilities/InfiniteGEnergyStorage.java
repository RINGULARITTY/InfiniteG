package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.data.codec.BigIntegerCodecs;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.math.BigInteger;

public class InfiniteGEnergyStorage implements IEnergyStorage {

    public BigInteger storedEnergy;
    public BigInteger capacity;
    public BigInteger maxInputRate;
    public BigInteger maxOutputRate;

    public InfiniteGEnergyStorage(BigInteger storedEnergy, BigInteger capacity, BigInteger maxInputRate, BigInteger maxOutputRate) {
        this.storedEnergy = storedEnergy;
        this.capacity = capacity;
        this.maxInputRate = maxInputRate;
        this.maxOutputRate = maxOutputRate;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (maxReceive <= 0 || maxInputRate.equals(BigInteger.ZERO)) return 0;

        BigInteger want = BigInteger.valueOf(maxReceive);
        BigInteger space = capacity.subtract(storedEnergy).max(BigInteger.ZERO);

        int accepted = 0;
        if (maxInputRate.compareTo(BigInteger.ZERO) < 0)
            accepted = space.min(want).intValue();
        else
            accepted = space.min(want).min(maxInputRate).intValue();

        if (!simulate && accepted > 0) storedEnergy = storedEnergy.add(BigInteger.valueOf(accepted));

        return accepted;
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        if (maxExtract <= 0 || maxOutputRate.equals(BigInteger.ZERO)) return 0;

        BigInteger want = BigInteger.valueOf(maxExtract);

        int taken = 0;
        if (maxInputRate.compareTo(BigInteger.ZERO) < 0)
            taken = storedEnergy.min(want).intValue();
        else
            taken = storedEnergy.min(want).min(maxInputRate).intValue();

        if (!simulate && taken > 0) storedEnergy = storedEnergy.subtract(BigInteger.valueOf(taken));
        return taken;
    }

    public int getEnergyStored() {
        return storedEnergy.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public int getMaxEnergyStored() {
        return capacity.min(BigInteger.valueOf(Integer.MAX_VALUE)).intValue();
    }

    public boolean canExtract() { return !maxOutputRate.equals(BigInteger.ZERO); }
    public boolean canReceive() { return !maxInputRate.equals(BigInteger.ZERO); }

    public void loadAdditional(ValueInput input) {
        capacity = input.read("infinite_g_energy_adapter.capacity", BigIntegerCodecs.CODEC).orElse(BigInteger.ZERO);
        storedEnergy = input.read("infinite_g_energy_adapter.energy_stored", BigIntegerCodecs.CODEC).orElse(BigInteger.ZERO);
        maxInputRate = input.read("infinite_g_energy_adapter.max_input_rate", BigIntegerCodecs.CODEC).orElse(BigInteger.ZERO);
        maxOutputRate = input.read("infinite_g_energy_adapter.max_output_rate", BigIntegerCodecs.CODEC).orElse(BigInteger.ZERO);
    }

    public void saveAdditional(ValueOutput output) {
        output.store("infinite_g_energy_adapter.capacity", BigIntegerCodecs.CODEC, capacity);
        output.store("infinite_g_energy_adapter.energy_stored", BigIntegerCodecs.CODEC, storedEnergy);
        output.store("infinite_g_energy_adapter.max_input_rate", BigIntegerCodecs.CODEC, maxInputRate);
        output.store("infinite_g_energy_adapter.max_output_rate", BigIntegerCodecs.CODEC, maxOutputRate);
    }
}