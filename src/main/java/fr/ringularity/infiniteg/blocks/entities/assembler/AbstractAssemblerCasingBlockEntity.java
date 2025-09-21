package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerCasingBlock;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class AbstractAssemblerCasingBlockEntity extends BlockEntity {
    @Nullable
    private BlockPos cachedController;

    public AbstractAssemblerCasingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private MachineTier getTier() {
        BlockState s = getBlockState();
        if (s.getBlock() instanceof AbstractAssemblerCasingBlock c) return c.getTier();

        return MachineTier.BASIC;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        notifyChangeNearestController();
    }

    @Override
    public void setRemoved() {
        notifyChangeNearestController();
        cachedController = null;
        super.setRemoved();
    }

    public void notifyChangeNearestController() {
        if (!(level instanceof ServerLevel server)) return;
        MachineTier tier = getTier();

        if (cachedController != null) {
            BlockState cs = server.getBlockState(cachedController);
            if (cs.getBlock() instanceof AbstractAssemblerControllerBlock ctrl && ctrl.getTier() == tier) {
                var be = server.getBlockEntity(cachedController);
                if (be instanceof AbstractAssemblerControllerBlockEntity controllerBE) {
                    controllerBE.requestValidation();
                    return;
                }
            } else {
                cachedController = null;
            }
        }

        BlockPos best = null;
        long bestDist2 = Long.MAX_VALUE;
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int size = tier.size();
            int half = (size - 1) / 2;
            if (size == 1) {
                BlockPos cpos = worldPosition.relative(facing, 1);
                if (isMatchingController(server, cpos, tier)) {
                    long d2 = (long) worldPosition.distSqr(cpos) + 1;
                    if (d2 < bestDist2) { bestDist2 = d2; best = cpos; }
                }
                continue;
            }
            Direction left = facing.getCounterClockWise();
            for (int dz = 1; dz <= size; dz++) {
                for (int dy = 0; dy <= size - 1; dy++) {
                    for (int dx = -half; dx <= half; dx++) {
                        BlockPos cpos = worldPosition
                                .relative(facing, dz)
                                .relative(left, dx)
                                .below(dy);
                        if (!server.hasChunkAt(cpos)) continue;
                        if (isMatchingController(server, cpos, tier)) {
                            long d2 = (long) worldPosition.distSqr(cpos) + 1;
                            if (d2 < bestDist2) { bestDist2 = d2; best = cpos; }
                        }
                    }
                }
            }
        }

        if (best != null) {
            cachedController = best.immutable();
            var be = server.getBlockEntity(best);
            if (be instanceof AbstractAssemblerControllerBlockEntity controllerBE) {
                controllerBE.requestValidation();
            }
        }
    }

    private static boolean isMatchingController(ServerLevel level, BlockPos pos, MachineTier tier) {
        BlockState s = level.getBlockState(pos);
        if (!(s.getBlock() instanceof AbstractAssemblerControllerBlock ctrl)) return false;
        return ctrl.getTier() == tier;
    }

    public void clearCache() {
        cachedController = null;
    }
}
