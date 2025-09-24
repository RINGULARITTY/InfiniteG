package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;

public class SophisticatedAssemblerControllerBlock extends AbstractAssemblerControllerBlock {

    public SophisticatedAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.SOPHISTICATED; }
}
