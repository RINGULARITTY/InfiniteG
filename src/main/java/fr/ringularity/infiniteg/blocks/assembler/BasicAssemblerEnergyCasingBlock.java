package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.RecipeType;
import fr.ringularity.infiniteg.blocks.entities.assembler.BasicAssemblerEnergyCasingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BasicAssemblerEnergyCasingBlock extends AbstractAssemblerCasingBlock {
    public BasicAssemblerEnergyCasingBlock(Properties props) {
        super(MachineTier.BASIC, RecipeType.ENERGY, props);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BasicAssemblerEnergyCasingBlockEntity(pos, state);
    }
}
