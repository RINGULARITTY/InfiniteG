package fr.ringularity.infiniteg.integrations.jade.component_providers;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.integrations.jade.JadeStackableComponentKeys;
import fr.ringularity.infiniteg.integrations.jade.stackable_component.IJadeStackableComponentProvider;
import fr.ringularity.infiniteg.integrations.jade.stackable_component.JadeDEComponentProvider;
import fr.ringularity.infiniteg.integrations.jade.stackable_component.JadeEnergyComponentProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

import java.util.HashMap;
import java.util.Map;

public enum TooltipCompositeComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_tooltip_client_component_provider");
    private static final ResourceLocation LINE = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/line");

    private static final HashMap<Integer, IJadeStackableComponentProvider> KEYS_STACKABLE_COMPONENTS = new HashMap<>(Map.of(
            JadeStackableComponentKeys.ENERGY_COMPONENT, JadeEnergyComponentProvider.ENERGY_COMPONENT_PROVIDER,
            JadeStackableComponentKeys.DE_COMPONENT, JadeDEComponentProvider.DE_COMPONENT_PROVIDER
    ));

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig cfg) {
        CompoundTag tag = blockAccessor.getServerData();
        int feats = tag.getInt(JadeStackableComponentKeys.FEATURES).orElse(0);

        boolean firstComponent = true;

        tooltip.add(JadeUI.spacer(1, 3));

        for (int key : KEYS_STACKABLE_COMPONENTS.keySet()) {
            boolean showComponent = (feats & key) != 0;
            if (!showComponent)
                continue;

            if (!firstComponent) {
                tooltip.add(JadeUI.spacer(1, 1));
                tooltip.add(JadeUI.sprite(LINE, 300, 2));
                tooltip.add(JadeUI.spacer(1, 1));
            }

            firstComponent = false;
            KEYS_STACKABLE_COMPONENTS.get(key).renderStackableComponent(tooltip, blockAccessor, tag);
        }

        tooltip.add(JadeUI.spacer(1, 1));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
