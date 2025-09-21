package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class AbstractAssemblerCasingBlock extends Block {
    private final MachineTier tier;

    protected AbstractAssemblerCasingBlock(MachineTier tier, BlockBehaviour.Properties props) {
        super(props);
        this.tier = tier;
    }

    public MachineTier getTier() {
        return tier;
    }
}
