package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.RecipeType;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerCasingBlock;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractAssemblerCasingBlockEntity extends BlockEntity {
    @Nullable private BlockPos linkedController;

    public AbstractAssemblerCasingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }

    public RecipeType getRecipeType() {
        BlockState bs = getBlockState();
        return bs.getBlock() instanceof AbstractAssemblerCasingBlock c ? c.getRecipeType() : RecipeType.NONE;
    }

    private MachineTier getTier() {
        BlockState bs = getBlockState();
        return bs.getBlock() instanceof AbstractAssemblerCasingBlock c ? c.getTier() : MachineTier.BASIC;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level instanceof ServerLevel server) tryLinkNearestController(server);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        if (linkedController != null) {
            BlockEntity be = server.getBlockEntity(linkedController);
            if (be instanceof AbstractAssemblerControllerBlockEntity ctrl) {
                ctrl.onCasingUnlinked(worldPosition);
            }
            linkedController = null;
        }
    }

    private void tryLinkNearestController(ServerLevel server) {
        MachineTier tier = getTier();
        BlockPos best = null; double bestD2 = Double.MAX_VALUE;
        int size = tier.size(), half = (size - 1) / 2;

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            if (size == 1) {
                BlockPos cpos = worldPosition.relative(facing, 1);
                if (isMatchingController(server, cpos, tier)) { double d2 = worldPosition.distSqr(cpos); if (d2 < bestD2) { bestD2 = d2; best = cpos; } }
                continue;
            }
            Direction left = facing.getCounterClockWise();
            for (int dz = 1; dz <= size; dz++) for (int dy = 0; dy <= size - 1; dy++) for (int dx = -half; dx <= half; dx++) {
                BlockPos cpos = worldPosition.relative(facing, dz).relative(left, dx).below(dy);
                if (!server.hasChunkAt(cpos)) continue;
                if (isMatchingController(server, cpos, tier)) { double d2 = worldPosition.distSqr(cpos); if (d2 < bestD2) { bestD2 = d2; best = cpos; } }
            }
        }
        if (best == null) return;

        BlockEntity be = server.getBlockEntity(best);
        if (be instanceof AbstractAssemblerControllerBlockEntity ctrl) {
            if (ctrl.tryAcceptCasing(worldPosition)) linkedController = best.immutable();
        }
    }

    private static boolean isMatchingController(ServerLevel level, BlockPos pos, MachineTier tier) {
        BlockState s = level.getBlockState(pos);
        if (!(s.getBlock() instanceof AbstractAssemblerControllerBlock ctrl)) return false;
        return ctrl.getTier() == tier;
    }
}
