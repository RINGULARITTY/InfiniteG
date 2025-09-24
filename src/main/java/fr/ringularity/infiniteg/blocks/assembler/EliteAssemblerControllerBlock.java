package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;

public class EliteAssemblerControllerBlock extends AbstractAssemblerControllerBlock {

    public EliteAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.ELITE; }
}
