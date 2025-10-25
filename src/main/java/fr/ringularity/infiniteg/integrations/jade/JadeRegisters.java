package fr.ringularity.infiniteg.integrations.jade;

import fr.ringularity.infiniteg.blocks.AbstractDEMachineBlock;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import fr.ringularity.infiniteg.blocks.entities.DEMachineBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import fr.ringularity.infiniteg.integrations.jade.component_providers.TooltipCompositeComponentProvider;
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
        registration.registerBlockDataProvider(JadeDEDataProvider.INSTANCE, AbstractDEMachineBlock.class);
        registration.registerBlockDataProvider(JadeEnergyDataProvider.INSTANCE, AbstractDEMachineBlock.class);

        registration.registerBlockDataProvider(JadeEnergyDataProvider.INSTANCE, AbstractAssemblerControllerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(TooltipCompositeComponentProvider.INSTANCE, AbstractDEMachineBlock.class);

        registration.registerBlockComponent(TooltipCompositeComponentProvider.INSTANCE, AbstractAssemblerControllerBlock.class);
    }
}
