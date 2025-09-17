package fr.ringularity.infiniteg.integrations.jade.stackable_component;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.format.BigDecimalFormat;
import fr.ringularity.infiniteg.integrations.jade.data_providers.JadeDEDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.JadeUI;

import java.math.BigDecimal;

public class JadeDEComponentProvider implements IJadeStackableComponentProvider {
    private static final ResourceLocation PROGRESS_BAR_BASE = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/progress_bar_base");
    private static final ResourceLocation PROGRESS_BAR_DE_FILL = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/de_bar");

    public static final JadeDEComponentProvider DE_COMPONENT_PROVIDER = new JadeDEComponentProvider();

    public void renderStackableComponent(ITooltip tooltip, BlockAccessor blockAccessor, CompoundTag tag) {
        if (!tag.contains(JadeDEDataProvider.DE_CORRECT_NETWORK_TAG)) return;

        boolean correctDENetwork = tag.getBooleanOr(JadeDEDataProvider.DE_CORRECT_NETWORK_TAG, false);
        if (!correctDENetwork) {
            tooltip.add(Component.literal("Dark Energy Network Missing"));
            return;
        }

        BigDecimal deQuantity = new BigDecimal(tag.getStringOr(JadeDEDataProvider.DE_QUANTITY_NETWORK_TAG, "0"));

        tooltip.append(Component.literal("Dark Energy Network Information").withColor(0xFFFFFFFF).withStyle(ChatFormatting.BOLD));
        tooltip.add(JadeUI.progress(1, PROGRESS_BAR_BASE, PROGRESS_BAR_DE_FILL, 300, 13, Component.literal(BigDecimalFormat.format(deQuantity) + " Dark Energy").withColor(0xFFFFFFFF), JadeUI.progressStyle()));

        int propertiesAmount = tag.getIntOr(JadeDEDataProvider.DE_PROPERTIES_AMOUNT_TAG, 0);
        if (propertiesAmount == 0)
            return;

        for (int i = 0; i < propertiesAmount; ++i) {
            tooltip.add(JadeUI.spacer(0, 5));
            String propertyName = StringUtils.capitalize(tag.getStringOr(JadeDEDataProvider.DE_PROPERTIES_NAMES_TAG + i, "?"));
            BigDecimal propertyValue = new BigDecimal(tag.getStringOr(JadeDEDataProvider.DE_PROPERTIES_VALUES_TAG + i, "0"));

            tooltip.append(Component.literal(propertyName + ": " + BigDecimalFormat.format(propertyValue)));
        }
    }
}
