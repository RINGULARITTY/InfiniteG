package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class ScrollElement {
    private final List<TextElement> textElements = new ArrayList<>();
    private final List<IconElement> iconElements = new ArrayList<>();
    private final List<TextureElement> textureElements = new ArrayList<>();
    private final List<CustomButton> buttons = new ArrayList<>();
    private final int height;
    private final int width;

    public ScrollElement(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ScrollElement addText(Component text, int x, int y, int color) {
        textElements.add(new TextElement(text, x, y, color));
        return this;
    }

    public ScrollElement addText(Component text, int x, int y, int color, float scale) {
        textElements.add(new TextElement(text, x, y, color, scale));
        return this;
    }

    public ScrollElement addIcon(ItemStack itemStack, int x, int y) {
        iconElements.add(new IconElement(itemStack, x, y));
        return this;
    }

    public ScrollElement addTexture(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        textureElements.add(new TextureElement(texture, x, y, width, height, textureWidth, textureHeight));
        return this;
    }

    public ScrollElement addButton(int x, int y, int width, int height,
                                   Component text, Runnable action) {
        buttons.add(new CustomButton(x, y, width, height, text, action));
        return this;
    }

    public void render(GuiGraphics guiGraphics, Font font, int elementX, int elementY, int mouseX, int mouseY) {
        // Render textures first (background)
        for (TextureElement texture : textureElements) {
            guiGraphics.blit(
                    RenderType.GUI_TEXTURED,
                    texture.texture,
                    elementX + texture.x,
                    elementY + texture.y,
                    0, 0,
                    texture.width,
                    texture.height,
                    texture.textureWidth,
                    texture.textureHeight
            );
        }

        // Render icons
        for (IconElement icon : iconElements) {
            guiGraphics.renderItem(icon.itemStack, elementX + icon.x, elementY + icon.y);
        }

        // Render text on top
        for (TextElement text : textElements) {
            if (text.scale != 1.0f) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().scale(text.scale, text.scale, 1.0f);
                guiGraphics.drawString(
                        font,
                        text.text,
                        (int)((elementX + text.x) / text.scale),
                        (int)((elementY + text.y) / text.scale),
                        text.color,
                        false
                );
                guiGraphics.pose().popPose();
            } else {
                guiGraphics.drawString(font, text.text, elementX + text.x, elementY + text.y, text.color, false);
            }
        }

        for(CustomButton button : buttons) {
            button.render(guiGraphics, font, elementX, elementY, mouseX, mouseY);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<IconElement> getIcons() {
        return iconElements;
    }

    public List<CustomButton> getButtons() {
        return buttons;
    }

    // Inner classes pour encapsuler les différents types d'éléments
    public static class TextElement {
        final Component text;
        final int x, y, color;
        final float scale;

        TextElement(Component text, int x, int y, int color) {
            this(text, x, y, color, 1.0f);
        }

        TextElement(Component text, int x, int y, int color, float scale) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.color = color;
            this.scale = scale;
        }
    }

    public static class IconElement {
        final ItemStack itemStack;
        final int x, y;

        IconElement(ItemStack itemStack, int x, int y) {
            this.itemStack = itemStack;
            this.x = x;
            this.y = y;
        }
    }

    public static class TextureElement {
        final ResourceLocation texture;
        final int x, y, width, height, textureWidth, textureHeight;

        TextureElement(ResourceLocation texture, int x, int y, int width, int height, int textureWidth, int textureHeight) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
    }
}
