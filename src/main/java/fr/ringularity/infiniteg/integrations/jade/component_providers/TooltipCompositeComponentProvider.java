package fr.ringularity.infiniteg.integrations.jade.component_providers;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.integrations.jade.JadeKeys;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

public enum TooltipCompositeComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_tooltip_client_component_provider");
    private static final ResourceLocation LINE = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/line.png");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig cfg) {
        CompoundTag nbt = blockAccessor.getServerData();
        int feats = nbt.getInt(JadeKeys.FEATURES).orElse(0);

        boolean showEnergy = (feats & JadeKeys.ENERGY_COMPONENT) != 0;
        boolean showDE = (feats & JadeKeys.DE_COMPONENT) != 0;

        boolean first = true;

        if (showDE) {
            if (!first) tooltip.add(JadeUI.sprite(LINE, 300, 4));
            first = false;
            tooltip.add(Component.literal("de"));
        }

        if (showEnergy) {
            if (!first) tooltip.add(JadeUI.sprite(LINE, 300, 4));
            first = false;
            tooltip.add(Component.literal("energy"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
