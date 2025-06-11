package fr.ringularity.infiniteg.blocks;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(InfiniteG.MOD_ID);

    public static final DeferredBlock<Block> COMPACTOR = registerBlock("compactor", CompactorBlock::new);
    public static final DeferredBlock<Block> WORKSTATION = registerBlock("workstation", WorkstationBlock::new);

    public static final DeferredBlock<Block> DENSE_COBBLESTONE = registerBlock("dense_cobblestone", Block::new);

    public static final DeferredBlock<Block> DENSE_WOOD = registerBlock("dense_wood", Block::new);
    public static final DeferredBlock<Block> DENSE_PLANKS = registerBlock("dense_planks", Block::new);

    public static final DeferredBlock<Block> DENSE_IRON = registerBlock("dense_iron", Block::new);

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, (properties) -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
