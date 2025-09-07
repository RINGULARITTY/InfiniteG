package fr.ringularity.infiniteg.blocks;

import com.mojang.serialization.MapCodec;
import fr.ringularity.infiniteg.blocks.entities.DarkEnergyPurifierBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DarkEnergyPurifierBlock extends BaseEntityBlock {
    public static final MapCodec<DarkEnergyPurifierBlock> CODEC = simpleCodec(DarkEnergyPurifierBlock::new);

    public DarkEnergyPurifierBlock(Properties properties) {
        super(properties);
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DarkEnergyPurifierBlockEntity(blockPos, blockState);
    }
}
