package fr.ringularity.infiniteg.integrations.jei;

import fr.ringularity.infiniteg.screens.ScreenTools;
import fr.ringularity.infiniteg.screens.widgets.Primitives;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TextOverlayDrawable implements IDrawable {
    private final Font font;
    private final Component text;
    private final int color;
    private final float scale;
    private final int width;
    private final int height;

    public TextOverlayDrawable(Font font, Component text, int color, float scale) {
        this.font = font;
        this.text = text;
        this.color = color;
        this.scale = scale;

        this.width = (int) Math.ceil(font.width(text) * scale);
        this.height = (int) Math.ceil(font.lineHeight * scale);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(GuiGraphics g, int x, int y) {
        ScreenTools.renderScaledTextBottomRight(g, font, new Primitives.TextElement(
                text, x, y, color, scale
        ));
    }
}
