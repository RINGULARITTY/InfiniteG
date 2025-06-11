package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class CustomButton implements GuiEventListener {
    public final int baseX, baseY, width, height;
    public int realX, realY;
    private final Component text;
    private final Runnable onAction;
    private boolean pressed = false;
    private boolean hovered = false;

    // Style personnalisable
    private int backgroundColor = 0xFF555555;
    private int hoverColor = 0xFF777777;
    private int pressedColor = 0xFF333333;
    private int textColor = 0xFFFFFFFF;
    private float textScale = 0.5f;

    public CustomButton(int x, int y, int width, int height, Component text, Runnable onAction) {
        this.baseX = x;
        this.baseY = y;
        this.realX = 0;
        this.realY = 0;
        this.width = width;
        this.height = height;
        this.text = text;
        this.onAction = onAction;
    }

    public void render(GuiGraphics guiGraphics, Font font, int dx, int dy, int mouseX, int mouseY) {
        hovered = isMouseOver(mouseX, mouseY);
        int bgColor = getBgColor();
        int outlineColor = getOutlineColor();

        this.realX = baseX + dx;
        this.realY = baseY + dy;

        guiGraphics.fill(realX - 1, realY - 1, realX + width + 1, realY + height + 1, outlineColor);
        guiGraphics.fill(realX, realY, realX + width, realY + height, bgColor);
        drawCenteredText(guiGraphics, font, text, realX + width/2, realY + height/2);
    }

    private void drawCenteredText(GuiGraphics guiGraphics, Font font, Component text, int x, int y) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(textScale, textScale, 1f);
        int scaledX = (int)(x / textScale);
        int scaledY = (int)(y / textScale);
        guiGraphics.drawString(font, text,
                scaledX - font.width(text)/2,
                scaledY - font.lineHeight/2,
                textColor, false);
        guiGraphics.pose().popPose();
    }

    private int getBgColor() {
        if(pressed) return pressedColor;
        return hovered ? hoverColor : backgroundColor;
    }

    private int getOutlineColor() {
        return pressed ? backgroundColor : pressedColor;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isMouseOver(mouseX, mouseY) && button == 0) {
            pressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(pressed && isMouseOver(mouseX, mouseY)) {
            onAction.run();
        }
        pressed = false;
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= realX && mouseX <= realX + width &&
                mouseY >= realY && mouseY <= realY + height;
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public CustomButton withColors(int bg, int hover, int pressed) {
        this.backgroundColor = bg;
        this.hoverColor = hover;
        this.pressedColor = pressed;
        return this;
    }

    public CustomButton withTextScale(float scale) {
        this.textScale = scale;
        return this;
    }
}

