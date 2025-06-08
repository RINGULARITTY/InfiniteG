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

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
