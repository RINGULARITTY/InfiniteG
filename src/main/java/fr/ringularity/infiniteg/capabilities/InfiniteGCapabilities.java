package fr.ringularity.infiniteg.capabilities;

import fr.ringularity.infiniteg.InfiniteG;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class InfiniteGCapabilities {
    public enum CapabilityType {
        DARK_ENERGY
    }

    public static final BlockCapability<ITypedCapabilityStorage, Direction> TYPED_ENERGY_BLOCK =
            BlockCapability.createSided(
                    ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "dark_energy"),
                    ITypedCapabilityStorage.class
            );
}
