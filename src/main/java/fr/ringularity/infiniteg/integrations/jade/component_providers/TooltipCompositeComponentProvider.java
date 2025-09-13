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

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig cfg) {
        CompoundTag nbt = blockAccessor.getServerData();
        int feats = nbt.getInt(JadeKeys.FEATURES).orElse(0);

        boolean showEnergy   = (feats & JadeKeys.FEAT_ENERGY) != 0;
        boolean showProgress = (feats & JadeKeys.FEAT_PROGRESS) != 0;

        boolean first = true;

        if (showEnergy) {
            if (!first) JadeUI.addTextRule(tooltip, 140, 2, 2, 0xFF777777);
            first = false;
            long energy = nbt.getLong("mymod.energy");
            tooltip.add(Component.translatable("mymod.jade.energy", energy));
        }

        if (showProgress) {
            if (!first) JadeUI.addTextRule(tooltip, 140, 2, 2, 0xFF777777);
            first = false;
            int p = nbt.getInt("mymod.progress");
            tooltip.add(Component.translatable("mymod.jade.progress", p + "%"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
