package fr.ringularity.infiniteg.integrations.jade.data_providers;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.capabilities.IInfiniteGEnergy;
import fr.ringularity.infiniteg.capabilities.InfiniteGEnergyStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import java.math.BigInteger;

public enum JadeEnergyDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_energy_data_provider");

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof IInfiniteGEnergy be) {
            InfiniteGEnergyStorage energy = be.getInfiniteGEnergy(null);
            if (energy == null)
                return;

            BigInteger storedEnergy = energy.storedEnergy;
            BigInteger capacity = energy.capacity;

            data.putString("stored_energy", storedEnergy.toString());
            data.putString("energy_capacity", capacity.toString());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}

