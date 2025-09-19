package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.blocks.AssemblerBaseBlock;
import net.minecraft.core.BlockPos;
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
            if (abb.assemblerBlockEntity != null) {
                abb.assemblerBlockEntity.removeStructureBlock(pos);
            }
        }

        super.preRemoveSideEffects(pos, state);
    }
}
