package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyGeneratorBlockEntity;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyPurifierBlockEntity;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworkNodeRef;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworks;
import fr.ringularity.infiniteg.capabilities.GraphUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class DarkEnergyPurifierBlock extends BaseEntityBlock implements IDarkEnergyMachine {
    public static final MapCodec<DarkEnergyPurifierBlock> CODEC = simpleCodec(DarkEnergyPurifierBlock::new);

    public DarkEnergyPurifierBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DarkEnergyPurifierBlockEntity(blockPos, blockState);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;
        ServerLevel sl = (ServerLevel) level;

        int pipes = GraphUtils.countAdjacentPipes(sl, pos);
        if (pipes > 1) {
            level.destroyBlock(pos, true);
            return;
        }

        BlockEntity be = sl.getBlockEntity(pos);
        if (be instanceof DarkEnergyPurifierBlockEntity gen) {
            UUID id = GraphUtils.findAttachedNetworkId(sl, pos);
            if (id != null) {
                gen.setNetworkId(id);
                var data = DarkEnergyNetworks.get(sl);
                data.addNodeTo(id, new DarkEnergyNetworkNodeRef(sl.dimension(), pos));
            }
        }
    }

    @Override
    @javax.annotation.Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof DarkEnergyPurifierBlockEntity g) g.serverTick((ServerLevel) lvl, pos, st);
        };
    }
}
