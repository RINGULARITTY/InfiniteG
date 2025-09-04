package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScrollableElement {

    private final List<Primitives.TextElement> textElements = new ArrayList<>();
    private final List<Primitives.ItemIconElement> itemIconElements = new ArrayList<>();
    private final List<BackgroundTextureElement> backgroundTextureElements = new ArrayList<>();
    private final List<InteractiveButton> interactiveButtons = new ArrayList<>();

    private final int width;
    private final int height;

    public ScrollableElement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ScrollableElement addText(Component text, int x, int y, int color) {
        return addText(text, x, y, color, 1.0f);
    }

    public ScrollableElement addText(Component text, int x, int y, int color, float scale) {
        textElements.add(new Primitives.TextElement(text, x, y, color, scale));
        return this;
    }

    public ScrollableElement addItemIcon(ItemStack itemStack, int x, int y) {
        itemIconElements.add(new Primitives.ItemIconElement(itemStack, x, y));
        return this;
    }

    public ScrollableElement addBackgroundTexture(ResourceLocation texture, int x, int y, int width, int height,
                                                  int textureWidth, int textureHeight) {
        backgroundTextureElements.add(new BackgroundTextureElement(texture, x, y, width, height, textureWidth, textureHeight));
        return this;
    }

    public ScrollableElement addInteractiveButton(int x, int y, int width, int height,
                                                  Component text, ItemStack stack, Runnable action) {
        interactiveButtons.add(new InteractiveButton(x, y, width, height, text, stack, action));
        return this;
    }

    public ItemStack render(GuiGraphics guiGraphics, Font font,
                            int elementAbsoluteX, int elementAbsoluteY,
                            int mouseRelativeToElementX, int mouseRelativeToElementY) {

        ItemStack tooltipItem = null;

        for (BackgroundTextureElement texture : backgroundTextureElements) {
            renderBackgroundTexture(guiGraphics, texture, elementAbsoluteX, elementAbsoluteY);
        }

        for (Primitives.ItemIconElement icon : itemIconElements) {
            renderItemIcon(guiGraphics, icon, elementAbsoluteX, elementAbsoluteY);

            if (isMouseOverItemIcon(icon, mouseRelativeToElementX, mouseRelativeToElementY)) {
                tooltipItem = icon.itemStack;
            }
        }

        for (Primitives.TextElement text : textElements) {
            renderText(guiGraphics, font, text, elementAbsoluteX, elementAbsoluteY);
        }

        for (InteractiveButton button : interactiveButtons) {
            ItemStack buttonTooltipItem = button.render(
                    guiGraphics, font,
                    elementAbsoluteX, elementAbsoluteY,
                    mouseRelativeToElementX, mouseRelativeToElementY
            );

            if (buttonTooltipItem != null) {
                tooltipItem = buttonTooltipItem;
            }
        }

        return tooltipItem;
    }

    private void renderBackgroundTexture(GuiGraphics guiGraphics, BackgroundTextureElement texture,
                                         int elementAbsoluteX, int elementAbsoluteY) {
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture.resourceLocation,
                elementAbsoluteX + texture.x,
                elementAbsoluteY + texture.y,
                0, 0,
                texture.width, texture.height,
                texture.textureWidth, texture.textureHeight
        );
    }

    private void renderItemIcon(GuiGraphics guiGraphics, Primitives.ItemIconElement icon,
                                int elementAbsoluteX, int elementAbsoluteY) {
        guiGraphics.renderItem(
                icon.itemStack,
                elementAbsoluteX + icon.x,
                elementAbsoluteY + icon.y
        );
    }

    private void renderText(GuiGraphics guiGraphics, Font font, Primitives.TextElement text,
                            int elementAbsoluteX, int elementAbsoluteY) {
        if (text.scale != 1.0f) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().scale(text.scale, text.scale);
            guiGraphics.drawString(
                    font,
                    text.textComponent,
                    (int)((elementAbsoluteX + text.x) / text.scale),
                    (int)((elementAbsoluteY + text.y) / text.scale),
                    text.color,
                    false
            );
            guiGraphics.pose().popMatrix();
        } else {
            guiGraphics.drawString(
                    font,
                    text.textComponent,
                    elementAbsoluteX + text.x,
                    elementAbsoluteY + text.y,
                    text.color,
                    false
            );
        }
    }

    private boolean isMouseOverItemIcon(Primitives.ItemIconElement icon, int mouseRelativeX, int mouseRelativeY) {
        return mouseRelativeX >= icon.x &&
                mouseRelativeY >= icon.y &&
                mouseRelativeX <= icon.x + 16 &&
                mouseRelativeY <= icon.y + 16;
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Primitives.ItemIconElement> getItemIcons() {
        return itemIconElements;
    }

    public List<InteractiveButton> getInteractiveButtons() {
        return interactiveButtons;
    }


    public static class BackgroundTextureElement {
        final ResourceLocation resourceLocation;
        final int x, y, width, height, textureWidth, textureHeight;

        BackgroundTextureElement(ResourceLocation resourceLocation, int x, int y, int width, int height,
                                 int textureWidth, int textureHeight) {
            this.resourceLocation = resourceLocation;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
    }
}
