package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworks;
import fr.ringularity.infiniteg.data.codec.UUIDCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.UUID;

// DarkEnergyNetworkBlockBE.java
public class DarkEnergyNetworkControllerBlockEntity extends BlockEntity {
    private static final String TAG_NETWORK_ID = "NetworkId";
    private @Nullable UUID networkId;

    public DarkEnergyNetworkControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DARK_ENERGY_NETWORK_BE.get(), pos, state);
    }

    public @Nullable UUID getNetworkId() { return networkId; }
    public void setNetworkId(@Nullable UUID id) { this.networkId = id; setChanged(); }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.networkId = input.read(TAG_NETWORK_ID, UUIDCodecs.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        if (networkId != null) output.store(TAG_NETWORK_ID, UUIDCodecs.CODEC, networkId);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel sl) {
            var data = DarkEnergyNetworks.get(sl);
            UUID id = this.getNetworkId();
            if (id != null) {
                data.deleteNetwork(id);
                this.setNetworkId(null);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }
}

