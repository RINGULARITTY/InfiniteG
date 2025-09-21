package fr.ringularity.infiniteg.capabilities.de;

import fr.ringularity.infiniteg.blocks.DENetworkControllerBlock;
import fr.ringularity.infiniteg.blocks.DEPipeBlock;
import fr.ringularity.infiniteg.blocks.AbstractDEMachineBlock;
import fr.ringularity.infiniteg.blocks.entities.DENetworkControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.*;

public final class GraphUtils {
    private GraphUtils() {}

    public static boolean isPipe(BlockState s) {
        return s.getBlock() instanceof DEPipeBlock;
    }

    public static boolean isNetworkBlock(BlockState s) {
        return s.getBlock() instanceof DENetworkControllerBlock;
    }

    public static boolean isDEMachine(BlockState s) {
        return s.getBlock() instanceof AbstractDEMachineBlock;
    }

    public static List<BlockPos> neighbors6(BlockPos p) {
        return List.of(p.above(), p.below(), p.north(), p.south(), p.east(), p.west());
    }

    public static @Nullable UUID findAttachedNetworkId(ServerLevel level, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> dq = new ArrayDeque<>();
        int explored = 0, limit = 8192;

        dq.add(start);
        visited.add(start);

        while (!dq.isEmpty() && explored < limit) {
            explored++;
            BlockPos cur = dq.poll();
            for (BlockPos n : neighbors6(cur)) {
                if (!visited.add(n)) continue;
                BlockState st = level.getBlockState(n);
                if (isNetworkBlock(st)) {
                    BlockEntity be = level.getBlockEntity(n);
                    if (be instanceof DENetworkControllerBlockEntity nb && nb.getDENetworkId() != null) {
                        return nb.getDENetworkId();
                    }
                }
                if (isPipe(st)) {
                    dq.add(n);
                }
            }
        }
        return null;
    }

    public static Set<UUID> findAllNetworksTouching(ServerLevel level, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> dq = new ArrayDeque<>();
        dq.add(start);
        visited.add(start);
        Set<UUID> found = new HashSet<>();
        int explored = 0, limit = 16384;
        while (!dq.isEmpty() && explored < limit) {
            explored++;
            BlockPos cur = dq.poll();
            for (BlockPos n : neighbors6(cur)) {
                if (!visited.add(n)) continue;
                BlockState st = level.getBlockState(n);
                if (isNetworkBlock(st)) {
                    BlockEntity be = level.getBlockEntity(n);
                    if (be instanceof DENetworkControllerBlockEntity nb && nb.getDENetworkId() != null) {
                        found.add(nb.getDENetworkId());
                    }
                } else if (isPipe(st)) {
                    dq.add(n);
                }
            }
        }
        return found;
    }

    public static int countAdjacentPipes(Level level, BlockPos pos) {
        int c = 0;
        for (BlockPos n : neighbors6(pos)) {
            if (isPipe(level.getBlockState(n))) c++;
        }
        return c;
    }

    public static Set<BlockPos> collectPipeComponent(ServerLevel level, BlockPos start) {
        Set<BlockPos> pipes = new HashSet<>();
        if (!isPipe(level.getBlockState(start))) return pipes;

        ArrayDeque<BlockPos> dq = new ArrayDeque<>();
        int explored = 0, limit = 16384;

        dq.add(start);
        pipes.add(start);

        while (!dq.isEmpty() && explored++ < limit) {
            BlockPos cur = dq.poll();
            for (BlockPos n : neighbors6(cur)) {
                if (pipes.contains(n)) continue;
                if (isPipe(level.getBlockState(n))) {
                    pipes.add(n);
                    dq.add(n);
                }
            }
        }
        return pipes;
    }

    public static Set<BlockPos> collectAdjacentMachines(ServerLevel level, Set<BlockPos> pipeComponent) {
        Set<BlockPos> machines = new HashSet<>();
        for (BlockPos p : pipeComponent) {
            for (BlockPos n : neighbors6(p)) {
                if (isDEMachine(level.getBlockState(n))) {
                    machines.add(n);
                }
            }
        }
        return machines;
    }
}
