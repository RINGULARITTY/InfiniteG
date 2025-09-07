package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworkOps;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class DarkEnergyPipeBlockEntity extends BlockEntity {

    public DarkEnergyPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DARK_ENERGY_PIPE_BE.get(), pos, blockState);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel sl) {
            var data = DarkEnergyNetworks.get(sl);
            DarkEnergyNetworkNodeRef self = new DarkEnergyNetworkNodeRef(sl.dimension(), pos);

            UUID netId = data.networkOf(self).orElse(null);
            data.removeNode(self);

            if (netId != null && data.networksView().get(netId) != null) {
                DarkEnergyNetworkOps.purgeUnreachableFromHub(sl, netId);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }


}
