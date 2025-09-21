package fr.ringularity.infiniteg.world;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerCasingBlock;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid=InfiniteG.MOD_ID)
public class StructureChecker {

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent e) {
        Level level = (Level) e.getLevel();
        if (level.isClientSide()) return;

        BlockState placed = e.getPlacedBlock();
        BlockPos pos = e.getPos();

        if (placed.getBlock() instanceof AbstractAssemblerCasingBlock casing) {
            revalidateControllersForCasing(level, pos, casing.getTier());
        }

        if (placed.getBlock() instanceof AbstractAssemblerControllerBlock) {
            if (level.getBlockEntity(pos) instanceof AbstractAssemblerControllerBlockEntity be) {
                be.requestValidation();
            }
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        Level level = e.getPlayer().level();
        if (level.isClientSide()) return;

        BlockPos pos = e.getPos();
        BlockState oldState = e.getState();

        if (oldState.getBlock() instanceof AbstractAssemblerCasingBlock casing) {
            revalidateControllersForCasing(level, pos, casing.getTier());
        }
    }

    private static void revalidateControllersForCasing(Level level, BlockPos casingPos, MachineTier tier) {
        int size = tier.size();
        int half = (size - 1) / 2;

        Set<BlockPos> candidates = new HashSet<>();

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            Direction left = facing.getCounterClockWise();

            if (size == 1) {
                BlockPos cpos = casingPos.relative(facing, 1);
                candidates.add(cpos);
            } else {
                for (int dz = 1; dz <= size; dz++) {
                    for (int dy = 0; dy <= size - 1; dy++) {
                        for (int dx = -half; dx <= half; dx++) {
                            BlockPos cpos = casingPos
                                    .relative(facing, dz)
                                    .relative(left, dx)
                                    .below(dy);
                            candidates.add(cpos);
                        }
                    }
                }
            }
        }

        for (BlockPos cpos : candidates) {
            BlockState cs = level.getBlockState(cpos);
            if (!(cs.getBlock() instanceof AbstractAssemblerControllerBlock ctrl)) continue;
            if (ctrl.getTier() != tier) continue;

            if (level.getBlockEntity(cpos) instanceof AbstractAssemblerControllerBlockEntity be) {
                be.requestValidation();
            }
        }
    }
}
