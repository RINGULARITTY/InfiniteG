package fr.ringularity.infiniteg.blocks;

import fr.ringularity.infiniteg.InfiniteG;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(InfiniteG.MOD_ID);

    public static final DeferredBlock<Block> COMPACTOR =
            BLOCKS.registerBlock("compactor", CompactorBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> WORKSTATION =
            BLOCKS.registerBlock("workstation", WorkstationBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> DENSE_COBBLESTONE =
            BLOCKS.registerSimpleBlock("dense_cobblestone", BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> DENSE_WOOD =
            BLOCKS.registerSimpleBlock("dense_wood", BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> DENSE_PLANKS =
            BLOCKS.registerSimpleBlock("dense_planks", BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> DENSE_IRON =
            BLOCKS.registerSimpleBlock("dense_iron", BlockBehaviour.Properties.of());


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
