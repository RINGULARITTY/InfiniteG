package fr.ringularity.infiniteg.integrations.jade;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.format.BigIntegerFormat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.JadeUI;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public enum JadeEnergyComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_energy_component_provider");
    private static final ResourceLocation PROGRESS_BAR_BASE = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/progress_bar_base");
    private static final ResourceLocation PROGRESS_BAR_FILL = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/energy_bar");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag tag = accessor.getServerData();
        if (!tag.contains("EnergyBig") || !tag.contains("CapacityBig")) return;

        BigInteger e = new BigInteger(tag.getString("EnergyBig").orElse("0"));
        BigInteger c = new BigInteger(tag.getString("CapacityBig").orElse("0"));
        if (c.signum() <= 0) return;

        BigDecimal p = new BigDecimal(e).divide(new BigDecimal(c), MathContext.DECIMAL64);
        float ratio = (float) Math.max(0.0, Math.min(1.0, p.doubleValue()));

        String eStr = BigIntegerFormat.format_n(e, 6);
        String cStr = BigIntegerFormat.format_n(c, 6);
        Component label = Component.literal(eStr + " / " + cStr).withColor(0xFFFFFFFF);

        try {
            tooltip.add(JadeUI.progress(ratio, PROGRESS_BAR_BASE, PROGRESS_BAR_FILL, 300, 13, label, JadeUI.progressStyle()));
        } catch (Throwable t) {
            tooltip.append(label);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}
