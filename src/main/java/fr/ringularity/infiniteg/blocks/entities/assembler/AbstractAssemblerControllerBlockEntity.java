package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.abstracts.MachineTier;
import fr.ringularity.infiniteg.abstracts.StructureUpgrade;
import fr.ringularity.infiniteg.blocks.assembler.AbstractAssemblerControllerBlock;
import fr.ringularity.infiniteg.blocks.entities.AbstractIGBEContainer;
import fr.ringularity.infiniteg.capabilities.IInfiniteGEnergy;
import fr.ringularity.infiniteg.capabilities.InfiniteGEnergyStorage;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractAssemblerControllerBlockEntity extends AbstractIGBEContainer implements MenuProvider, IInfiniteGEnergy {
    private final ObjectOpenHashSet<BlockPos> linkedCasings = new ObjectOpenHashSet<>();
    public final HashSet<StructureUpgrade> structureUpgrades = new HashSet<>();
    public InfiniteGEnergyStorage energySystem;

    protected AbstractAssemblerControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.energySystem = new InfiniteGEnergyStorage(
                BigInteger.ZERO,
                BigInteger.valueOf(1000000 * ( 1 + getTier().ordinal() * getTier().ordinal())),
                BigInteger.valueOf(10000 * (1 + getTier().ordinal())),
                BigInteger.valueOf(10000 * (1 + getTier().ordinal()))
        );
    }

    private MachineTier getTier() {
        Block b = getBlockState().getBlock();
        return (b instanceof AbstractAssemblerControllerBlock ctrl) ? ctrl.getTier() : MachineTier.BASIC;
    }

    @Override
    public @Nullable IEnergyStorage getEnergy(@Nullable Direction side) {
        if (structureUpgrades.contains(StructureUpgrade.ENERGY)) {
            return energySystem;
        }
        return null;
    }

    @Override
    public @Nullable InfiniteGEnergyStorage getInfiniteGEnergy(@Nullable Direction side) {
        if (structureUpgrades.contains(StructureUpgrade.ENERGY)) {
            return energySystem;
        }
        return null;
    }

    public void initializeLinksAndValidate() {
        if (!(level instanceof ServerLevel server)) return;
        linkedCasings.clear();
        structureUpgrades.clear();
        MachineTier tier = getTier();
        Direction facing = getBlockState().getValue(AbstractAssemblerControllerBlock.FACING);
        for (BlockPos p : expectedPositions(server, getBlockPos(), facing, tier)) {
            BlockEntity be = server.getBlockEntity(p);
            if (be instanceof AbstractAssemblerCasingBlockEntity casing) {
                if (casingLinkTo(casing, getBlockPos())) {
                    linkedCasings.add(p.immutable());
                    structureUpgrades.add(casing.getRecipeType());
                }
            }
        }
        updateValidFlag();
    }

    public boolean tryAcceptCasing(BlockPos casingPos) {
        if (!(level instanceof ServerLevel server)) return false;
        if (!isExpectedPosition(server, casingPos)) return false;

        BlockEntity be = server.getBlockEntity(casingPos);
        if (!(be instanceof AbstractAssemblerCasingBlockEntity casing)) return false;
        if (!casingLinkTo(casing, getBlockPos())) return false;

        linkedCasings.add(casingPos.immutable());
        structureUpgrades.add(casing.getRecipeType());
        updateValidFlag();
        return true;
    }

    public void onCasingUnlinked(BlockPos casingPos) {
        if (!(level instanceof ServerLevel)) return;
        if (linkedCasings.remove(casingPos)) {
            structureUpgrades.clear();
            for (BlockPos bp : linkedCasings) {
                if (level.getBlockEntity(bp) instanceof AbstractAssemblerCasingBlockEntity cbe)
                    structureUpgrades.add(cbe.getRecipeType());
            }
            updateValidFlag();
        }
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (!(level instanceof ServerLevel server)) return;
        for (BlockPos p : new ObjectArrayList<>(linkedCasings)) {
            BlockEntity be = server.getBlockEntity(p);
            if (be instanceof AbstractAssemblerCasingBlockEntity casing) {
                casingClearIfLinkedTo(casing, getBlockPos());
            }
        }
        linkedCasings.clear();
        structureUpgrades.clear();
    }

    private void updateValidFlag() {
        if (!(level instanceof ServerLevel server)) return;
        int size = getTier().size();
        int required = (size == 1) ? 1 : size * size * size;
        boolean valid = linkedCasings.size() == required && allExpectedPresent(server);
        BlockState state = getBlockState();
        if (state.getValue(AbstractAssemblerControllerBlock.STRUCTURE_VALID) != valid) {
            server.setBlock(getBlockPos(), state.setValue(AbstractAssemblerControllerBlock.STRUCTURE_VALID, valid)
                    .setValue(AbstractAssemblerControllerBlock.WORKING, false), Block.UPDATE_CLIENTS);
        }
    }

    private boolean allExpectedPresent(ServerLevel server) {
        MachineTier tier = getTier();
        Direction facing = getBlockState().getValue(AbstractAssemblerControllerBlock.FACING);
        for (BlockPos p : expectedPositions(server, getBlockPos(), facing, tier)) if (!linkedCasings.contains(p)) return false;
        return true;
    }

    private boolean isExpectedPosition(ServerLevel server, BlockPos casingPos) {
        MachineTier tier = getTier();
        Direction facing = getBlockState().getValue(AbstractAssemblerControllerBlock.FACING);
        for (BlockPos p : expectedPositions(server, getBlockPos(), facing, tier)) if (p.equals(casingPos)) return true;
        return false;
    }

    private List<BlockPos> expectedPositions(ServerLevel server, BlockPos cpos, Direction facing, MachineTier tier) {
        List<BlockPos> list = new ObjectArrayList<>();
        int size = tier.size(); Direction back = facing.getOpposite(); Direction right = facing.getClockWise();
        if (size == 1) {
            BlockPos p = cpos.relative(back, 1);
            if (server.hasChunkAt(p)) list.add(p.immutable());
            return list;
        }
        int half = (size - 1) / 2;
        for (int dz = 1; dz <= size; dz++) for (int dy = 0; dy <= size - 1; dy++) for (int dx = -half; dx <= half; dx++) {
            BlockPos p = cpos.relative(back, dz).relative(right, dx).above(dy);
            if (server.hasChunkAt(p)) list.add(p.immutable());
        }
        return list;
    }

    private static boolean casingLinkTo(AbstractAssemblerCasingBlockEntity casing, BlockPos controllerPos) {
        try {
            var f = AbstractAssemblerCasingBlockEntity.class.getDeclaredField("linkedController");
            f.setAccessible(true);
            BlockPos prev = (BlockPos) f.get(casing);
            if (prev != null && !prev.equals(controllerPos)) {
                Level lvl = casing.getLevel();
                if (lvl instanceof ServerLevel sl) {
                    BlockEntity bePrev = sl.getBlockEntity(prev);
                    if (bePrev instanceof AbstractAssemblerControllerBlockEntity oldCtrl) oldCtrl.onCasingUnlinked(casing.getBlockPos());
                }
            }
            f.set(casing, controllerPos.immutable());
            return true;
        } catch (Exception ignore) { return false; }
    }

    private static void casingClearIfLinkedTo(AbstractAssemblerCasingBlockEntity casing, BlockPos controllerPos) {
        try {
            var f = AbstractAssemblerCasingBlockEntity.class.getDeclaredField("linkedController");
            f.setAccessible(true);
            BlockPos prev = (BlockPos) f.get(casing);
            if (controllerPos.equals(prev)) f.set(casing, null);
        } catch (Exception ignore) {}
    }

    public static <T extends AbstractAssemblerControllerBlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {}
}
