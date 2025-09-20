package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.AssemblerBaseBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.AssemblerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssemblerBaseBlock extends BaseEntityBlock {
    public static final MapCodec<AssemblerBaseBlock> CODEC = simpleCodec(AssemblerBaseBlock::new);
    public static final BooleanProperty VALID_STRUCTURE = ModBlockStateProperties.STRUCTURE_VALID;

    public @Nullable AssemblerBlockEntity assemblerBlockEntity;

    public AssemblerBaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VALID_STRUCTURE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VALID_STRUCTURE);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AssemblerBaseBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                           @NotNull BlockState oldState, boolean movedByPiston) {
        if (movedByPiston) return;
        for (int x = -1; x < 2; ++x) {
            for (int z = -1; z < 2; ++z) {
                BlockEntity be = level.getBlockEntity(new BlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z));
                if (be instanceof AssemblerBlockEntity abe) {
                    abe.addStructureBlock(state, level, pos);
                    assemblerBlockEntity = abe;
                    return;
                }
            }
        }
        super.onPlace(state, level, pos, oldState, false);
    }
}
