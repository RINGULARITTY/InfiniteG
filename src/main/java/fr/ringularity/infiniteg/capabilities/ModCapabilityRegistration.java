package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.blocks.entities.CompactorBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.DEPurifierBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilityRegistration {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.COMPACTOR_BE.get(),
                CompactorBlockEntity::getEnergy
        );
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                ModBlockEntities.DE_PURIFIER_BE.get(),
                DEPurifierBlockEntity::getEnergy
        );
    }
}
