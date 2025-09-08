package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.*;
import fr.ringularity.infiniteg.data.codec.UUIDCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DarkEnergyPurifierBlockEntity extends BlockEntity {
    private static final String TAG_NET = "network_id";
    private @Nullable UUID networkId;
    private int tickCounter = 0;
    private boolean componentAttachedOnce = false;
    private static final int TICK_INTERVAL = 10;
    private static final Map<String, BigDecimal> MAX_PURETY_PROPS = new HashMap<>(Map.of("purity", BigDecimal.valueOf(0.05)));
    private static final BigDecimal MODIFY_STRENGTH = BigDecimal.valueOf(0.05);

    public DarkEnergyPurifierBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DARK_ENERGY_PURIFIER_BE.get(), blockPos, blockState);
    }

    public @Nullable UUID getNetworkId() { return networkId; }
    public void setNetworkId(@Nullable UUID id) { this.networkId = id; setChanged(); }

    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (networkId == null) {
            UUID id = GraphUtils.findAttachedNetworkId(level, pos);
            if (id != null) {
                setNetworkId(id);
                DarkEnergyNetworksData data = DarkEnergyNetworks.get(level);
                data.addNodeTo(id, new DarkEnergyNetworkNodeRef(level.dimension(), pos));

                BlockPos neighborPipe = GraphUtils.neighbors6(worldPosition).stream()
                        .filter(p -> GraphUtils.isPipe(level.getBlockState(p))).findFirst().orElse(null);
                if (neighborPipe != null && !componentAttachedOnce) {
                    DarkEnergyNetworkOps.attachComponentToNetwork(level, networkId, neighborPipe);
                    componentAttachedOnce = true;
                }
            } else {
                return;
            }
        }
        if (++tickCounter % TICK_INTERVAL == 0) {
            DarkEnergyNetworksData data = DarkEnergyNetworks.get(level);
            DarkEnergyNetwork rec = data.networksView().get(networkId);
            if (rec == null) {
                networkId = null;
                return;
            }
            data.modifyDarkEnergy(networkId, MODIFY_STRENGTH, MAX_PURETY_PROPS);
        }
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        this.networkId = input.read(TAG_NET, UUIDCodecs.CODEC).orElse(null);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        if (networkId != null) output.store(TAG_NET, UUIDCodecs.CODEC, networkId);
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
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
