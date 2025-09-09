package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.blocks.DENetworkControllerBlock;
import fr.ringularity.infiniteg.blocks.entities.DEGeneratorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class DENetworkOps {
    private DENetworkOps() {}

    // Purge orphelins: garde uniquement ce qui est atteignable depuis le controller
    public static void purgeUnreachableFromHub(ServerLevel level, UUID networkId) {
        var data = DENetworks.get(level);
        var rec = data.networksView().get(networkId);
        if (rec == null) return;

        // 1) Trouver le hub (controller) parmi les noeuds
        DENetworkNodeRef hub = null;
        for (var n : rec.nodes()) {
            BlockState st = level.getBlockState(n.pos());
            if (st.getBlock() instanceof DENetworkControllerBlock) {
                hub = n;
                break;
            }
        }
        if (hub == null) return;

        Set<DENetworkNodeRef> reachable = new HashSet<>();
        ArrayDeque<BlockPos> dq = new ArrayDeque<>();
        dq.add(hub.pos());
        reachable.add(hub);

        while (!dq.isEmpty()) {
            BlockPos cur = dq.poll();
            for (BlockPos nb : GraphUtils.neighbors6(cur)) {
                BlockState st = level.getBlockState(nb);
                if (GraphUtils.isPipe(st)) {
                    DENetworkNodeRef nr = new DENetworkNodeRef(level.dimension(), nb);
                    if (reachable.add(nr)) dq.add(nb);
                    continue;
                }
                if (GraphUtils.isDEMachine(st)) {
                    DENetworkNodeRef nr = new DENetworkNodeRef(level.dimension(), nb);
                    reachable.add(nr);
                }
            }
        }

        Set<DENetworkNodeRef> toRemove = new HashSet<>(rec.nodes());
        toRemove.removeAll(reachable);

        if (!toRemove.isEmpty()) {
            int removed = 0;
            for (var n : toRemove) {
                data.removeNode(n);
                removed++;
                // Si machine, nettoyer sa BE
                var be = level.getBlockEntity(n.pos());
                if (be instanceof DEGeneratorBlockEntity gen && Objects.equals(gen.getDENetworkId(), networkId)) {
                    gen.setDENetworkId(null);
                }
            }
        }
    }

    public static void attachComponentToNetwork(ServerLevel level, UUID networkId, BlockPos startPipe) {
        var data = DENetworks.get(level);
        var comp = GraphUtils.collectPipeComponent(level, startPipe);
        if (comp.isEmpty()) return;

        int addedPipes = 0, addedMachines = 0;

        for (BlockPos p : comp) {
            DENetworkNodeRef n = new DENetworkNodeRef(level.dimension(), p);
            if (data.networkOf(n).orElse(null) == null) {
                data.addNodeTo(networkId, n);
                addedPipes++;
            }
        }

        var machines = GraphUtils.collectAdjacentMachines(level, comp);
        for (BlockPos mpos : machines) {
            DENetworkNodeRef n = new DENetworkNodeRef(level.dimension(), mpos);
            if (data.networkOf(n).orElse(null) == null) {
                data.addNodeTo(networkId, n);
                addedMachines++;
            }
            BlockEntity be = level.getBlockEntity(mpos);
            if (be instanceof DEGeneratorBlockEntity gen) {
                gen.setDENetworkId(networkId);
            }
        }

    }
}
