package fr.ringularity.infiniteg.blocks.entities.assembler;

import fr.ringularity.infiniteg.blocks.entities.ModBlockEntities;
import fr.ringularity.infiniteg.capabilities.InfiniteGEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AdvancedAssemblerControllerBlockEntity extends AbstractAssemblerControllerBlockEntity {

    public AdvancedAssemblerControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_ASSEMBLER_CONTROLLER_BE.get(), pos, state);
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
    public Component getDisplayName() {
        return null;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }
}
