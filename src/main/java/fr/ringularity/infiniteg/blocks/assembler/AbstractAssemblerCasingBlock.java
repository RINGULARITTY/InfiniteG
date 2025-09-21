package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerCasingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractAssemblerCasingBlock extends Block implements EntityBlock {
    private final MachineTier tier;

    protected AbstractAssemblerCasingBlock(MachineTier tier, Properties props) {
        super(props);
        this.tier = tier;
    }

    public MachineTier getTier() { return tier; }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        BlockEntityType<? extends AbstractAssemblerCasingBlockEntity> bet = switch (tier) {
            case BASIC    -> ModBlockEntities.BASIC_ASSEMBLER_CASING_BE.get();
            case IMPROVED -> ModBlockEntities.IMPROVED_ASSEMBLER_CASING_BE.get();
            case ADVANCED -> ModBlockEntities.ADVANCED_ASSEMBLER_CASING_BE.get();
        };
        return bet.create(pos, state);
    }

    @Override
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }
}
