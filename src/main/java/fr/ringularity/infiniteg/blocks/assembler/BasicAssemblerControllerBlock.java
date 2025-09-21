package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;

public class BasicAssemblerControllerBlock extends AbstractAssemblerControllerBlock {

    public BasicAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.BASIC; }
}
