package fr.ringularity.infiniteg.integrations.jade;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.entities.CompactorBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import java.math.BigInteger;

public enum JadeEnergyDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_energy_data_provider");

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof CompactorBlockEntity be) {
            BigInteger energy = be.energy.energyStored;
            BigInteger capacity = be.energy.capacity;

            data.putString("EnergyBig", energy.toString());
            data.putString("CapacityBig", capacity.toString());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}

