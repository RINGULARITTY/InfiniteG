package fr.ringularity.infiniteg.integrations.jade.data_providers;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.capabilities.IInfiniteGEnergy;
import fr.ringularity.infiniteg.capabilities.InfiniteGEnergyStorage;
import fr.ringularity.infiniteg.integrations.jade.JadeStackableComponentKeys;
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
            data.putInt(JadeStackableComponentKeys.FEATURES, data.getIntOr(JadeStackableComponentKeys.FEATURES, 0) | JadeStackableComponentKeys.ENERGY_COMPONENT);

            InfiniteGEnergyStorage energy = be.getInfiniteGEnergy(null);
            if (energy == null) {
                data.putString("stored_energy", "0");
                data.putString("energy_capacity", "0");
                return;
            }

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

