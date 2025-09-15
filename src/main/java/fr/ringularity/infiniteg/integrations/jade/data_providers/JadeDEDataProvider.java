package fr.ringularity.infiniteg.integrations.jade.data_providers;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.entities.DEMachineBlockEntity;
import fr.ringularity.infiniteg.capabilities.de.DENetwork;
import fr.ringularity.infiniteg.capabilities.de.DENetworkAggregate;
import fr.ringularity.infiniteg.capabilities.de.DENetworks;
import fr.ringularity.infiniteg.capabilities.de.DENetworksData;
import fr.ringularity.infiniteg.integrations.jade.JadeStackableComponentKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

import java.util.UUID;

public enum JadeDEDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_de_data_provider");
    public static final String DE_CORRECT_NETWORK_TAG = "de_network.correct";
    public static final String DE_QUANTITY_NETWORK_TAG = "de_network.quantity";
    public static final String DE_PROPERTIES_AMOUNT_TAG = "de_network.properties.amount";
    public static final String DE_PROPERTIES_NAMES_TAG = "de_network.properties.name_";
    public static final String DE_PROPERTIES_VALUES_TAG = "de_network.properties.value_";

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof DEMachineBlockEntity deMachine) {
            data.putInt(JadeStackableComponentKeys.FEATURES, data.getIntOr(JadeStackableComponentKeys.FEATURES, 0) | JadeStackableComponentKeys.DE_COMPONENT);

            UUID deNetworkId = deMachine.getDENetworkId();

            if (deNetworkId == null) {
                data.putBoolean(DE_CORRECT_NETWORK_TAG, false);
                return;
            }

            ServerLevel sl = (ServerLevel) blockAccessor.getLevel();
            DENetworksData networkData = DENetworks.get(sl);
            DENetwork network = networkData.get(deNetworkId);

            if (network == null) {
                data.putBoolean(DE_CORRECT_NETWORK_TAG, false);
                return;
            }

            data.putBoolean(DE_CORRECT_NETWORK_TAG, true);
            DENetworkAggregate networkAggregate = network.aggregate();
            data.putString(DE_QUANTITY_NETWORK_TAG, networkAggregate.quantity().toString());

            var properties = networkAggregate.properties();
            data.putInt(DE_PROPERTIES_AMOUNT_TAG, properties.size());

            int i = 0;
            for (var p : properties.keySet()) {
                data.putString(DE_PROPERTIES_NAMES_TAG + i, p);
                data.putString(DE_PROPERTIES_VALUES_TAG + i, properties.get(p).toString());
                i++;
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}

