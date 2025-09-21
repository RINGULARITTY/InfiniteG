package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class AdvancedAssemblerCasingBlock extends AbstractAssemblerCasingBlock {
    public AdvancedAssemblerCasingBlock(BlockBehaviour.Properties props) {
        super(MachineTier.ADVANCED, props);
    }
}