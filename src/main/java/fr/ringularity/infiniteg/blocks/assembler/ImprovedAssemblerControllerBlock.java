package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;

public class ImprovedAssemblerControllerBlock extends AbstractAssemblerControllerBlock {
    public ImprovedAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.IMPROVED; }
}
