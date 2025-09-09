package fr.ringularity.infiniteg.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class DEMachineBlockEntity extends BlockEntity {
    public DEMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public @Nullable UUID getDENetworkId() { return null; }
    public void setDENetworkId(@Nullable UUID id) {}
}
