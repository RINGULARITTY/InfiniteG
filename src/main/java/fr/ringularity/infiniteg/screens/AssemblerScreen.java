package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.menus.AbstractAssemblerControllerMenu;
import fr.ringularity.infiniteg.screens.widgets.Primitives;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AssemblerScreen extends AbstractContainerScreen<AbstractAssemblerControllerMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID,"textures/gui/growth_chamber/growth_chamber_gui.png");

    public AssemblerScreen(AbstractAssemblerControllerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 512, 256);

        ScreenTools.renderScaledText(
            guiGraphics, Minecraft.getInstance().font, new Primitives.TextElement(Component.literal("Test"), x + 10, y + 10, 0, 1)
        );
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
