package fr.ringularity.infiniteg.blocks;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.blocks.assembler.*;
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

    public static final DeferredBlock<Block> DE_NETWORK_CONTROLLER = BLOCKS.registerBlock(
            "de_network_controller",
            DENetworkControllerBlock::new,
            BlockBehaviour.Properties.of()
    );

    public static final DeferredBlock<Block> DE_PIPE =
            BLOCKS.registerBlock("de_pipe", DEPipeBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> DE_GENERATOR =
            BLOCKS.registerBlock("de_generator", DEGeneratorBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> DE_PURIFIER =
            BLOCKS.registerBlock("de_purifier", DEPurifierBlock::new, BlockBehaviour.Properties.of());


    public static final DeferredBlock<Block> BASIC_ASSEMBLER_CONTROLLER =
            BLOCKS.registerBlock("basic_assembler_controller", BasicAssemblerControllerBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> IMPROVED_ASSEMBLER_CONTROLLER =
            BLOCKS.registerBlock("improved_assembler_controller", ImprovedAssemblerControllerBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> ADVANCED_ASSEMBLER_CONTROLLER =
            BLOCKS.registerBlock("advanced_assembler_controller", AdvancedAssemblerControllerBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> SOPHISTICATED_ASSEMBLER_CONTROLLER =
            BLOCKS.registerBlock("sophisticated_assembler_controller", SophisticatedAssemblerControllerBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> ELITE_ASSEMBLER_CONTROLLER =
            BLOCKS.registerBlock("elite_assembler_controller", EliteAssemblerControllerBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> BASIC_ASSEMBLER_CASING =
            BLOCKS.registerBlock("basic_assembler_casing", BasicAssemblerCasingBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> BASIC_ASSEMBLER_ENERGY_CASING =
            BLOCKS.registerBlock("basic_assembler_energy_casing", BasicAssemblerEnergyCasingBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> IMPROVED_ASSEMBLER_CASING =
            BLOCKS.registerBlock("improved_assembler_casing", ImprovedAssemblerCasingBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> IMPROVED_ASSEMBLER_ENERGY_CASING =
            BLOCKS.registerBlock("improved_assembler_energy_casing", ImprovedAssemblerEnergyCasingBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> ADVANCED_ASSEMBLER_CASING =
            BLOCKS.registerBlock("advanced_assembler_casing", AdvancedAssemblerCasingBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> ADVANCED_ASSEMBLER_ENERGY_CASING =
            BLOCKS.registerBlock("advanced_assembler_energy_casing", AdvancedAssemblerEnergyCasingBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> SOPHISTICATED_ASSEMBLER_CASING =
            BLOCKS.registerBlock("sophisticated_assembler_casing", SophisticatedAssemblerCasingBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> SOPHISTICATED_ASSEMBLER_ENERGY_CASING =
            BLOCKS.registerBlock("sophisticated_assembler_energy_casing", SophisticatedAssemblerEnergyCasingBlock::new, BlockBehaviour.Properties.of());

    public static final DeferredBlock<Block> ELITE_ASSEMBLER_CASING =
            BLOCKS.registerBlock("elite_assembler_casing", EliteAssemblerCasingBlock::new, BlockBehaviour.Properties.of());
    public static final DeferredBlock<Block> ELITE_ASSEMBLER_ENERGY_CASING =
            BLOCKS.registerBlock("elite_assembler_energy_casing", EliteAssemblerEnergyCasingBlock::new, BlockBehaviour.Properties.of());


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
