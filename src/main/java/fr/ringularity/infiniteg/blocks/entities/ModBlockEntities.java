package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.entities.assembler.*;
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

    public static final Supplier<BlockEntityType<DENetworkControllerBlockEntity>> DE_NETWORK_BE =
            BLOCK_ENTITIES.register("de_network_controller", () -> new BlockEntityType<>(
                    DENetworkControllerBlockEntity::new, ModBlocks.DE_NETWORK_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<DEPipeBlockEntity>> DE_PIPE_BE =
            BLOCK_ENTITIES.register("de_pipe_be", () -> new BlockEntityType<>(
                    DEPipeBlockEntity::new, ModBlocks.DE_PIPE.get()));

    public static final Supplier<BlockEntityType<DEGeneratorBlockEntity>> DE_GENERATOR_BE =
            BLOCK_ENTITIES.register("de_generator_be", () -> new BlockEntityType<>(
                    DEGeneratorBlockEntity::new, ModBlocks.DE_GENERATOR.get()));

    public static final Supplier<BlockEntityType<DEPurifierBlockEntity>> DE_PURIFIER_BE =
            BLOCK_ENTITIES.register("de_purifier_be", () -> new BlockEntityType<>(
                    DEPurifierBlockEntity::new, ModBlocks.DE_PURIFIER.get()));

    public static final Supplier<BlockEntityType<BasicAssemblerControllerBlockEntity>> BASIC_ASSEMBLER_CONTROLLER_BE =
            BLOCK_ENTITIES.register("basic_assembler_controller_be", () -> new BlockEntityType<>(
                    BasicAssemblerControllerBlockEntity::new, ModBlocks.BASIC_ASSEMBLER_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<ImprovedAssemblerControllerBlockEntity>> IMPROVED_ASSEMBLER_CONTROLLER_BE =
            BLOCK_ENTITIES.register("improved_assembler_controller_be", () -> new BlockEntityType<>(
                    ImprovedAssemblerControllerBlockEntity::new, ModBlocks.IMPROVED_ASSEMBLER_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<AdvancedAssemblerControllerBlockEntity>> ADVANCED_ASSEMBLER_CONTROLLER_BE =
            BLOCK_ENTITIES.register("advanced_assembler_controller_be", () -> new BlockEntityType<>(
                    AdvancedAssemblerControllerBlockEntity::new, ModBlocks.ADVANCED_ASSEMBLER_CONTROLLER.get()));

    public static final Supplier<BlockEntityType<BasicAssemblerCasingBlockEntity>> BASIC_ASSEMBLER_CASING_BE =
            BLOCK_ENTITIES.register("basic_assembler_casing_be", () -> new BlockEntityType<>(
                    BasicAssemblerCasingBlockEntity::new, ModBlocks.BASIC_ASSEMBLER_CASING.get()));

    public static final Supplier<BlockEntityType<ImprovedAssemblerCasingBlockEntity>> IMPROVED_ASSEMBLER_CASING_BE =
            BLOCK_ENTITIES.register("improved_assembler_casing_be", () -> new BlockEntityType<>(
                    ImprovedAssemblerCasingBlockEntity::new, ModBlocks.IMPROVED_ASSEMBLER_CASING.get()));

    public static final Supplier<BlockEntityType<AdvancedAssemblerCasingBlockEntity>> ADVANCED_ASSEMBLER_CASING_BE =
            BLOCK_ENTITIES.register("advanced_assembler_casing_be", () -> new BlockEntityType<>(
                    AdvancedAssemblerCasingBlockEntity::new, ModBlocks.ADVANCED_ASSEMBLER_CASING.get()));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
