package fr.ringularity.infiniteg.blocks.entities;

import fr.ringularity.infiniteg.blocks.ModBlocks;
import fr.ringularity.infiniteg.capabilities.IInfiniteGEnergy;
import fr.ringularity.infiniteg.capabilities.InfiniteGEnergyStorage;
import fr.ringularity.infiniteg.menus.AssemblerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class AssemblerBlockEntity extends BlockEntity implements MenuProvider, IInfiniteGEnergy {
    private final HashSet<BlockPos> structureBlocks = new HashSet<>();

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int i) {
            return 0;
        }

        @Override
        public void set(int i, int i1) {

        }

        @Override
        public int getCount() {
            return 0;
        }
    };

    public AssemblerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ASSEMBLER_BE.get(), pos, blockState);
    }

    @Override
    public @Nullable IEnergyStorage getEnergy(@Nullable Direction side) {
        return null;
    }

    @Override
    public @Nullable InfiniteGEnergyStorage getInfiniteGEnergy(@Nullable Direction side) {
        return null;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return ModBlocks.ASSEMBLER.asItem().getName();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return new AssemblerMenu(i, inventory, this, this.data);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {}

    public void addStructureBlock(BlockPos blockPos) {
        structureBlocks.add(blockPos);
        System.out.println("Add block " + blockPos.toShortString());
    }

    public void removeStructureBlock(BlockPos blockPos) {
        structureBlocks.remove(blockPos);
        System.out.println("Remove block " + blockPos.toShortString());
    }

    @Override
    public void preRemoveSideEffects(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.preRemoveSideEffects(pos, state);
    }
}
