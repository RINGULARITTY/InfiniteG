package fr.ringularity.infiniteg.integrations.jade;

import fr.ringularity.infiniteg.blocks.CompactorBlock;
import fr.ringularity.infiniteg.blocks.DEMachineBlock;
import fr.ringularity.infiniteg.blocks.entities.CompactorBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.DEMachineBlockEntity;
import fr.ringularity.infiniteg.integrations.jade.component_providers.JadeDEComponentProvider;
import fr.ringularity.infiniteg.integrations.jade.component_providers.JadeEnergyComponentProvider;
import fr.ringularity.infiniteg.integrations.jade.data_providers.JadeDEDataProvider;
import fr.ringularity.infiniteg.integrations.jade.data_providers.JadeEnergyDataProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeRegisters implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeDEDataProvider.INSTANCE, DEMachineBlockEntity.class);
        registration.registerBlockDataProvider(JadeEnergyDataProvider.INSTANCE, DEMachineBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeDEComponentProvider.INSTANCE, DEMachineBlock.class);
        registration.registerBlockComponent(JadeEnergyComponentProvider.INSTANCE, DEMachineBlock.class);
    }
}
