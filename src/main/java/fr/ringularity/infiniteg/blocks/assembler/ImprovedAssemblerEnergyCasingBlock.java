package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.StructureUpgrade;
import fr.ringularity.infiniteg.blocks.entities.assembler.ImprovedAssemblerEnergyCasingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ImprovedAssemblerEnergyCasingBlock extends AbstractAssemblerCasingBlock {
    public ImprovedAssemblerEnergyCasingBlock(Properties props) {
        super(MachineTier.IMPROVED, StructureUpgrade.ENERGY, props);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ImprovedAssemblerEnergyCasingBlockEntity(pos, state);
    }
}
