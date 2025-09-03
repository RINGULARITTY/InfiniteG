package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.screens.widgets.Primitives;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class ScreenTools {
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
