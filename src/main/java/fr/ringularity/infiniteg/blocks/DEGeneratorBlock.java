package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DEGeneratorBlockEntity;
import fr.ringularity.infiniteg.capabilities.de.DENetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.de.DENetworks;
import fr.ringularity.infiniteg.capabilities.de.GraphUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class DEGeneratorBlock extends DEMachineBlock {
    public static final MapCodec<DEGeneratorBlock> CODEC = simpleCodec(DEGeneratorBlock::new);

    public DEGeneratorBlock(Properties props) { super(props); }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DEGeneratorBlockEntity(pos, state);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;
        ServerLevel sl = (ServerLevel) level;

        // Enforcer feuille: une seule adjacence pipe
        int pipes = GraphUtils.countAdjacentPipes(sl, pos);
        if (pipes > 1) {
            level.destroyBlock(pos, true);
            return;
        }

        BlockEntity be = sl.getBlockEntity(pos);
        if (be instanceof DEGeneratorBlockEntity gen) {
            UUID id = GraphUtils.findAttachedNetworkId(sl, pos);
            if (id != null) {
                gen.setDENetworkId(id);
                var data = DENetworks.get(sl);
                data.addNodeTo(id, new DENetworkNodeRef(sl.dimension(), pos));
            }
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof DEGeneratorBlockEntity g) g.serverTick((ServerLevel) lvl, pos, st);
        };
    }
}
