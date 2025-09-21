package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ImprovedAssemblerCasingBlock extends AbstractAssemblerCasingBlock {
    public ImprovedAssemblerCasingBlock(BlockBehaviour.Properties props) {
        super(MachineTier.IMPROVED, props);
    }
}