package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.blocks.entities.CompactorBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.DEPurifierBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import fr.ringularity.infiniteg.blocks.entities.assembler.*;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

public class ModCapabilityRegistration {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.COMPACTOR_BE.get(), CompactorBlockEntity::getEnergy);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.DE_PURIFIER_BE.get(), DEPurifierBlockEntity::getEnergy);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.IMPROVED_ASSEMBLER_CONTROLLER_BE.get(), ImprovedAssemblerControllerBlockEntity::getEnergy);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.ADVANCED_ASSEMBLER_CONTROLLER_BE.get(), AdvancedAssemblerControllerBlockEntity::getEnergy);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.SOPHISTICATED_ASSEMBLER_CONTROLLER_BE.get(), SophisticatedAssemblerControllerBlockEntity::getEnergy);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModBlockEntities.ELITE_ASSEMBLER_CONTROLLER_BE.get(), EliteAssemblerControllerBlockEntity::getEnergy);
    }
}
