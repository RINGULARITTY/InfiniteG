package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.screens.widgets.Primitives;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenTools {
    public static void renderScaledText(GuiGraphics g, Font font, Primitives.TextElement t) {
        g.pose().pushMatrix();
        g.pose().scale(t.scale, t.scale);
        int sx = Math.round(t.x / t.scale);
        int sy = Math.round(t.y / t.scale);
        g.drawString(font, t.textComponent, sx, sy, t.color, false);
        g.pose().popMatrix();
    }

    public static void renderScaledTextBottomRight(GuiGraphics g, Font font, Primitives.TextElement t) {
        g.pose().pushMatrix();
        g.pose().scale(t.scale, t.scale);

        int w = font.width(t.textComponent);
        int h = font.lineHeight;

        int sx = Math.round(t.x / t.scale - w);
        int sy = Math.round(t.y / t.scale - h);

        g.drawString(font, t.textComponent, sx, sy, t.color, true);
        g.pose().popMatrix();
    }

    static public void renderCenteredScaledText(GuiGraphics guiGraphics, Font font, Primitives.TextElement text) {
        guiGraphics.pose().pushMatrix();

        guiGraphics.pose().scale(text.scale, text.scale);

        int scaledCenterX = (int) ((float) text.x / text.scale);
        int scaledCenterY = (int) ((float) text.y / text.scale);

        guiGraphics.drawString(
                font,
                text.textComponent,
                scaledCenterX - font.width(text.textComponent) / 2,
                scaledCenterY - font.lineHeight / 2,
                text.color,
                false
        );

        guiGraphics.pose().popMatrix();
    }
}
