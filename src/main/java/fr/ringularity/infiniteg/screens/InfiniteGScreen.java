package fr.ringularity.infiniteg.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class InfiniteGScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public InfiniteGScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public void customRenderTooltip(GuiGraphics guiGraphics, int x, int y) {
        this.renderTooltip(guiGraphics, x, y);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    }
}
