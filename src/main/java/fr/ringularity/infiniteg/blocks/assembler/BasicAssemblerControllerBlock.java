package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.assembler.BasicAssemblerControllerBlockEntity;
import fr.ringularity.infiniteg.menus.BasicAssemblerControllerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;

public class BasicAssemblerControllerBlock extends AbstractAssemblerControllerBlock {

    public BasicAssemblerControllerBlock(Properties props) { super(props); }

    @Override public MachineTier getTier() { return MachineTier.BASIC; }

    @Override
    protected void openMenu(AbstractAssemblerControllerBlockEntity cbe, Player player, BlockPos pos) {
        if (cbe instanceof BasicAssemblerControllerBlockEntity basicACBE) {
            player.openMenu(
                    new SimpleMenuProvider(
                            (cid, inv, p) -> new BasicAssemblerControllerMenu(cid, inv, basicACBE),
                            Component.translatable(InfiniteG.MOD_ID + ".menu.basic_assembler_controller")
                    ),
                    buf -> buf.writeBlockPos(basicACBE.getBlockPos())
            );
        }
    }
}
