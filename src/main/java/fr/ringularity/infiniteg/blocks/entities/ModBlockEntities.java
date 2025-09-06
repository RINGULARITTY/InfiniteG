package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, InfiniteG.MOD_ID);

    public static final Supplier<BlockEntityType<CompactorBlockEntity>> COMPACTOR_BE =
            BLOCK_ENTITIES.register("compactor_be", () -> new BlockEntityType<>(
                    CompactorBlockEntity::new, ModBlocks.COMPACTOR.get()));

    public static final Supplier<BlockEntityType<WorkstationBlockEntity>> WORKSTATION_BE =
            BLOCK_ENTITIES.register("workstation_be", () -> new BlockEntityType<>(
                    WorkstationBlockEntity::new, ModBlocks.WORKSTATION.get()));

    public static final Supplier<BlockEntityType<DarkEnergyNetworkControllerBlockEntity>> DARK_ENERGY_NETWORK_BE =
            BLOCK_ENTITIES.register("dark_energy_network_controller", () -> new BlockEntityType<>(
                    DarkEnergyNetworkControllerBlockEntity::new, ModBlocks.DARK_ENERGY_NETWORK_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<DarkEnergyPipeBlockEntity>> DARK_ENERGY_PIPE_BE =
            BLOCK_ENTITIES.register("dark_energy_pipe_be", () -> new BlockEntityType<>(
                    DarkEnergyPipeBlockEntity::new, ModBlocks.DARK_ENERGY_PIPE.get()));

    public static final Supplier<BlockEntityType<DarkEnergyGeneratorBlockEntity>> DARK_ENERGY_GENERATOR_BE =
            BLOCK_ENTITIES.register("dark_energy_generator_be", () -> new BlockEntityType<>(
                    DarkEnergyGeneratorBlockEntity::new, ModBlocks.DARK_ENERGY_GENERATOR.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
