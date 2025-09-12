package fr.ringularity.infiniteg.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public interface IInfiniteGEnergy {
    @Nullable
    public IEnergyStorage getEnergy(@Nullable Direction side);

    @Nullable
    public InfiniteGEnergyStorage getInfiniteGEnergy(@Nullable Direction side);
}
