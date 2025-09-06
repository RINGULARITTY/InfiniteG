package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.blocks.DarkEnergyGeneratorBlock;
import fr.ringularity.infiniteg.blocks.DarkEnergyNetworkControllerBlock;
import fr.ringularity.infiniteg.blocks.DarkEnergyPipeBlock;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyNetworkControllerBlockEntity;
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
        return s.getBlock() instanceof DarkEnergyPipeBlock;
    }

    public static boolean isNetworkBlock(BlockState s) {
        return s.getBlock() instanceof DarkEnergyNetworkControllerBlock;
    }

    public static boolean isDarkEnergyMachine(BlockState s) {
        return s.getBlock() instanceof DarkEnergyGeneratorBlock;
    }

    public static List<BlockPos> neighbors6(BlockPos p) {
        return List.of(p.above(), p.below(), p.north(), p.south(), p.east(), p.west());
    }

    public static @Nullable UUID findAttachedNetworkId(ServerLevel level, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> dq = new ArrayDeque<>();
        dq.add(start);
        visited.add(start);
        int explored = 0, limit = 8192; // garde-fou
        while (!dq.isEmpty() && explored < limit) {
            explored++;
            BlockPos cur = dq.poll();
            for (BlockPos n : neighbors6(cur)) {
                if (!visited.add(n)) continue;
                BlockState st = level.getBlockState(n);
                if (isNetworkBlock(st)) {
                    BlockEntity be = level.getBlockEntity(n);
                    if (be instanceof DarkEnergyNetworkControllerBlockEntity nb && nb.getNetworkId() != null) {
                        return nb.getNetworkId();
                    }
                }
                if (isPipe(st)) {
                    dq.add(n);
                }
                // ne traverse pas à travers les machines -> machines = feuilles
            }
        }
        return null;
    }

    // Trouve potentiellement plusieurs réseaux atteignables si le pipe relie deux hubs (cas interdit)
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
                    if (be instanceof DarkEnergyNetworkControllerBlockEntity nb && nb.getNetworkId() != null) {
                        found.add(nb.getNetworkId());
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
        dq.add(start);
        pipes.add(start);
        int explored = 0, limit = 16384;
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
                if (isDarkEnergyMachine(level.getBlockState(n))) {
                    machines.add(n);
                }
            }
        }
        return machines;
    }
}
