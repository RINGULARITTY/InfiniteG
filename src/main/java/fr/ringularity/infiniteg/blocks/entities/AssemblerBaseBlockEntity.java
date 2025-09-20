package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.blocks.AssemblerBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AssemblerBaseBlockEntity extends BlockEntity {
    public AssemblerBaseBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ASSEMBLER_BASE_BE.get(), pos, blockState);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof AssemblerBaseBlock abb) {
            Level level = getLevel();
            if (level != null && abb.assemblerBlockEntity != null) {
                abb.assemblerBlockEntity.removeStructureBlock(state, getLevel(), pos);
            }
        }

        super.preRemoveSideEffects(pos, state);
    }
}
