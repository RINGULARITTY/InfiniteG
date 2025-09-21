package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BasicAssemblerCasingBlock extends AbstractAssemblerCasingBlock {
    public BasicAssemblerCasingBlock(BlockBehaviour.Properties props) {
        super(MachineTier.BASIC, props);
    }
}