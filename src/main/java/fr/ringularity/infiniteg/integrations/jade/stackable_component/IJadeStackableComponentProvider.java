package fr.ringularity.infiniteg.integrations.jade.stackable_component;

import net.minecraft.nbt.CompoundTag;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;

public interface IJadeStackableComponentProvider {
    void renderStackableComponent(ITooltip tooltip, BlockAccessor blockAccessor, CompoundTag tag);
}
