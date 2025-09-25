package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ImprovedAssemblerControllerBlock extends AbstractAssemblerControllerBlock {
    public ImprovedAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.IMPROVED; }

    @Override
    protected void openMenu(AbstractAssemblerControllerBlockEntity cbe, Player player, BlockPos pos) {

    }
}
