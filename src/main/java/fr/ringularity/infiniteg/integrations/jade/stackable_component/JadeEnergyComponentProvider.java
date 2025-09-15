package fr.ringularity.infiniteg.integrations.jade.stackable_component;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.format.BigIntegerFormat;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.JadeUI;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class JadeEnergyComponentProvider implements IJadeStackableComponentProvider {
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "infiniteg_energy_component_provider");
    private static final ResourceLocation PROGRESS_BAR_BASE = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/progress_bar_base");
    private static final ResourceLocation PROGRESS_BAR_ENERGY_FILL = ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID, "jade/energy_bar");

    public static final JadeEnergyComponentProvider ENERGY_COMPONENT_PROVIDER = new JadeEnergyComponentProvider();

    @Override
    public void renderStackableComponent(ITooltip tooltip, BlockAccessor blockAccessor, CompoundTag tag) {
        if (!tag.contains("stored_energy") || !tag.contains("energy_capacity")) return;

        BigInteger storedEnergy = new BigInteger(tag.getString("stored_energy").orElse("0"));
        BigInteger energyCapacity = new BigInteger(tag.getString("energy_capacity").orElse("0"));
        if (storedEnergy.signum() <= 0) return;

        BigDecimal p = new BigDecimal(storedEnergy).divide(new BigDecimal(energyCapacity), MathContext.DECIMAL64);
        float ratio = (float) Math.max(0.0, Math.min(1.0, p.doubleValue()));

        Component label = Component.literal(BigIntegerFormat.format_n(storedEnergy, 6) + " / "
                + BigIntegerFormat.format_n(energyCapacity, 6)).withColor(0xFFFFFFFF);

        tooltip.add(JadeUI.spacer(0, 5));
        tooltip.append(Component.literal("Energy Information").withColor(0xFFFFFFFF).withStyle(ChatFormatting.BOLD));
        tooltip.add(JadeUI.progress(ratio, PROGRESS_BAR_BASE, PROGRESS_BAR_ENERGY_FILL, 300, 13, label, JadeUI.progressStyle()));
    }
}
