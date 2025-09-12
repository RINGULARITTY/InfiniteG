package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.capabilities.*;
import fr.ringularity.infiniteg.capabilities.de.*;
import fr.ringularity.infiniteg.data.codec.UUIDCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DEPurifierBlockEntity extends DEMachineBlockEntity implements IInfiniteGEnergy {
    private static final String TAG_NET = "de_network_id";
    private @Nullable UUID networkId;
    private int tickCounter = 0;
    private boolean componentAttachedOnce = false;
    private static final int TICK_INTERVAL = 10;
    private static final Map<String, BigDecimal> MAX_PURITY_PROPS = new HashMap<>(Map.of("purity", BigDecimal.valueOf(0.05)));
    private static final BigDecimal MODIFY_STRENGTH = BigDecimal.valueOf(0.05);

    public final InfiniteGEnergyStorage energy = new InfiniteGEnergyStorage(
            BigInteger.valueOf(0),
            BigInteger.valueOf(10_000),
            BigInteger.valueOf(100),
            BigInteger.valueOf(1)
    );

    public DEPurifierBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DE_PURIFIER_BE.get(), blockPos, blockState);
    }

    @Override
    public @Nullable UUID getDENetworkId() { return networkId; }
    @Override
    public void setDENetworkId(@Nullable UUID id) { this.networkId = id; setChanged(); }

    @Nullable
    public IEnergyStorage getEnergy(@Nullable Direction side) {
        if (side == null) {
            return null;
        }
        return energy;
    }

    @Override
    public @Nullable InfiniteGEnergyStorage getInfiniteGEnergy(@Nullable Direction side) {
        return energy;
    }

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
            data.injectModifyDE(networkId, MODIFY_STRENGTH, MAX_PURITY_PROPS);
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
