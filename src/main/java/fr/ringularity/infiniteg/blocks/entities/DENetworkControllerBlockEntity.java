package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.de.DENetworks;
import fr.ringularity.infiniteg.data.codec.UUIDCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class DENetworkControllerBlockEntity extends DEMachineBlockEntity {
    private static final String TAG_NETWORK_ID = "de_network_id";
    private @Nullable UUID networkId;

    public DENetworkControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DE_NETWORK_BE.get(), pos, state);
    }

    @Override
    public @Nullable UUID getDENetworkId() { return networkId; }
    @Override
    public void setDENetworkId(@Nullable UUID id) { this.networkId = id; setChanged(); }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        this.networkId = input.read(TAG_NETWORK_ID, UUIDCodecs.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        if (networkId != null) output.store(TAG_NETWORK_ID, UUIDCodecs.CODEC, networkId);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (level instanceof ServerLevel sl) {
            var data = DENetworks.get(sl);
            UUID id = this.getDENetworkId();
            if (id != null) {
                data.deleteNetwork(id);
                this.setDENetworkId(null);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }
}

