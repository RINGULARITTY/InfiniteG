package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;

public class AdvancedAssemblerControllerBlock extends AbstractAssemblerControllerBlock {

    public AdvancedAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.ADVANCED; }
}
