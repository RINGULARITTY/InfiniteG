package fr.ringularity.infiniteg.blocks.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.RecipeType;
import fr.ringularity.infiniteg.blocks.ModBlockStateProperties;
import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import fr.ringularity.infiniteg.blocks.entities.assembler.AbstractAssemblerControllerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAssemblerControllerBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty STRUCTURE_VALID = ModBlockStateProperties.STRUCTURE_VALID;
    public static final BooleanProperty WORKING = ModBlockStateProperties.WORKING;

    protected AbstractAssemblerControllerBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STRUCTURE_VALID, false)
                .setValue(WORKING, false));
    }

    public abstract MachineTier getTier();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STRUCTURE_VALID, WORKING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction horizontal = ctx.getHorizontalDirection().getOpposite();
        return this.defaultBlockState()
                .setValue(FACING, horizontal)
                .setValue(STRUCTURE_VALID, false)
                .setValue(WORKING, false);
    }

    @Override
    public @javax.annotation.Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        BlockEntityType<? extends AbstractAssemblerControllerBlockEntity> bet = switch (getTier()) {
            case BASIC    -> ModBlockEntities.BASIC_ASSEMBLER_CONTROLLER_BE.get();
            case IMPROVED -> ModBlockEntities.IMPROVED_ASSEMBLER_CONTROLLER_BE.get();
            case ADVANCED -> ModBlockEntities.ADVANCED_ASSEMBLER_CONTROLLER_BE.get();
        };
        return bet.create(pos, state);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide())
            return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof AbstractAssemblerControllerBlockEntity cbe) {
            for (RecipeType rt : cbe.recipeTypes) {
                System.out.print(rt.typeName());
                System.out.print(", ");
            }
            System.out.println("");

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return null;
    }

    @Override
    public void onPlace(@NotNull BlockState newState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(newState, level, pos, oldState, movedByPiston);
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AbstractAssemblerControllerBlockEntity ctrl) ctrl.initializeLinksAndValidate();
        }
    }

    @Override
    public PushReaction getPistonPushReaction(@NotNull BlockState state) {
        return PushReaction.BLOCK;
    }
}
