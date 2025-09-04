package fr.ringularity.infiniteg.integrations.jade;

import fr.ringularity.infiniteg.blocks.CompactorBlock;
import fr.ringularity.infiniteg.blocks.entities.CompactorBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadeRegisters implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(JadeEnergyDataProvider.INSTANCE, CompactorBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(JadeEnergyComponentProvider.INSTANCE, CompactorBlock.class);
    }
}
