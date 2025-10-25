package fr.ringularity.infiniteg.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public interface IInfiniteGEnergy {
    @Nullable
    IEnergyStorage getEnergy(@Nullable Direction side);

    @Nullable
    InfiniteGEnergyStorage getInfiniteGEnergy(@Nullable Direction side);
}
