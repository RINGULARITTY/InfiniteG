package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import fr.ringularity.infiniteg.capabilities.IInfiniteGEnergy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractAssemblerControllerBlockEntity extends BlockEntity implements MenuProvider, IInfiniteGEnergy {

    protected AbstractAssemblerControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected MachineTier getTier() {
        BlockState state = getBlockState();
        Block b = state.getBlock();
        if (b instanceof AbstractAssemblerControllerBlock ctrl) {
            return ctrl.getTier();
        }
        return MachineTier.BASIC;
    }

    public void requestValidation() {
        if (level != null && !level.isClientSide) {
            validateAndUpdate();
        }
    }

    protected void validateAndUpdate() {
        if (level == null || level.isClientSide) return;

        BlockState state = getBlockState();
        Direction facing = state.getValue(AbstractAssemblerControllerBlock.FACING);
        MachineTier tier = getTier();

        boolean valid = validateStructure(level, getBlockPos(), facing, tier);

        if (state.getValue(AbstractAssemblerControllerBlock.STRUCTURE_VALID) != valid) {
            BlockState updated = state.setValue(AbstractAssemblerControllerBlock.STRUCTURE_VALID, valid);
            updated = updated.setValue(AbstractAssemblerControllerBlock.WORKING, false);
            level.setBlock(getBlockPos(), updated, Block.UPDATE_CLIENTS);
        }
    }

    protected boolean validateStructure(Level level, BlockPos controllerPos, Direction facing, MachineTier tier) {
        Block expectedCasing = switch (tier) {
            case BASIC -> ModBlocks.BASIC_ASSEMBLER_CASING.get();
            case IMPROVED -> ModBlocks.IMPROVED_ASSEMBLER_CASING.get();
            case ADVANCED -> ModBlocks.ADVANCED_ASSEMBLER_CASING.get();
        };

        int size = tier.size();
        Direction back = facing.getOpposite();

        if (size == 1) {
            BlockPos backPos = controllerPos.relative(back, 1);
            return level.getBlockState(backPos).is(expectedCasing);
        }

        Direction right = facing.getClockWise();
        Direction up = Direction.UP;

        int half = (size - 1) / 2;

        for (int dz = 1; dz <= size; dz++) {
            for (int dy = 0; dy <= size - 1; dy++) {
                for (int dx = -half; dx <= half; dx++) {
                    BlockPos p = controllerPos
                            .relative(back, dz)
                            .relative(right, dx)
                            .relative(up, dy);
                    BlockState bs = level.getBlockState(p);
                    if (!bs.is(expectedCasing)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static <T extends AbstractAssemblerControllerBlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {}
}
