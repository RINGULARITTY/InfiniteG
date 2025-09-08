package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyPipeBlockEntity;
import fr.ringularity.infiniteg.capabilities.DarkEnergyNetworkOps;
import fr.ringularity.infiniteg.capabilities.GraphUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class DarkEnergyPipeBlock extends BaseEntityBlock {
    public static final MapCodec<DarkEnergyPipeBlock> CODEC = simpleCodec(DarkEnergyPipeBlock::new);

    public DarkEnergyPipeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new DarkEnergyPipeBlockEntity(blockPos, blockState);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;
        ServerLevel sl = (ServerLevel) level;

        Set<UUID> touching = GraphUtils.findAllNetworksTouching(sl, pos);
        if (touching.size() > 1) {
            level.destroyBlock(pos, true);
            return;
        }

        UUID id = GraphUtils.findAttachedNetworkId(sl, pos);
        if (id != null) {
            DarkEnergyNetworkOps.attachComponentToNetwork(sl, id, pos);
        }
    }

}
