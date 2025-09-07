package fr.ringularity.infiniteg.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DarkEnergyPurifierBlockEntity extends BlockEntity {
    public DarkEnergyPurifierBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.DARK_ENERGY_PURIFIER_BE.get(), blockPos, blockState);
    }
}
