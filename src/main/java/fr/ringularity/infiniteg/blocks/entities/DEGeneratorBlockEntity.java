package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.de.*;
import fr.ringularity.infiniteg.data.codec.UUIDCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public class DEGeneratorBlockEntity extends DEMachineBlockEntity {
    private static final String TAG_NET = "de_network_id";
    private static final int TICK_INTERVAL = 10;
    private @Nullable UUID networkId;
    private int tickCounter = 0;
    private boolean componentAttachedOnce = false;

    private final BigDecimal deProduction = BigDecimal.valueOf(0.01d);
    private final Map<String, BigDecimal> props = Map.of("purity", new BigDecimal("0"));

    public DEGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DE_GENERATOR_BE.get(), pos, state);
    }

    @Override
    public @Nullable UUID getDENetworkId() { return networkId; }
    @Override
    public void setDENetworkId(@Nullable UUID id) { this.networkId = id; setChanged(); }

    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (networkId == null) {
            UUID id = GraphUtils.findAttachedNetworkId(level, pos);
            if (id != null) {
                setDENetworkId(id);
                DENetworksData data = DENetworks.get(level);
                data.addNodeTo(id, new DENetworkNodeRef(level.dimension(), pos));

                BlockPos neighborPipe = GraphUtils.neighbors6(worldPosition).stream()
                        .filter(p -> GraphUtils.isPipe(level.getBlockState(p))).findFirst().orElse(null);
                if (neighborPipe != null && !componentAttachedOnce) {
                    DENetworkOps.attachComponentToNetwork(level, networkId, neighborPipe);
                    componentAttachedOnce = true;
                }
            } else {
                return;
            }
        }
        if (++tickCounter % TICK_INTERVAL == 0) {
            DENetworksData data = DENetworks.get(level);
            DENetwork rec = data.networksView().get(networkId);
            if (rec == null) {
                networkId = null;
                return;
            }
            data.injectDE(networkId, deProduction, props);
        }
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (level instanceof ServerLevel sl) {
            DENetworksData data = DENetworks.get(sl);
            DENetworkNodeRef self = new DENetworkNodeRef(sl.dimension(), pos);

            UUID netId = this.getDENetworkId();
            if (netId == null) netId = data.networkOf(self).orElse(null);

            data.removeNode(self);
            this.setDENetworkId(null);

            if (netId != null && data.networksView().get(netId) != null) {
                DENetworkOps.purgeUnreachableFromHub(sl, netId);
            }
        }
        super.preRemoveSideEffects(pos, state);
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
}
