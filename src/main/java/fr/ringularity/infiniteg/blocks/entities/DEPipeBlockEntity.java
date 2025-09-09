package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.DENetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.DENetworkOps;
import fr.ringularity.infiniteg.capabilities.DENetworks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DEPipeBlockEntity extends BlockEntity {

    public DEPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DE_PIPE_BE.get(), pos, blockState);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (level instanceof ServerLevel sl) {
            var data = DENetworks.get(sl);
            DENetworkNodeRef self = new DENetworkNodeRef(sl.dimension(), pos);

            UUID netId = data.networkOf(self).orElse(null);
            data.removeNode(self);

            if (netId != null && data.networksView().get(netId) != null) {
                DENetworkOps.purgeUnreachableFromHub(sl, netId);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }
}
