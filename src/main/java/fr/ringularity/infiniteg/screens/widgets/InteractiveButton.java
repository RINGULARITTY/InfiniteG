package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * Interactive button with support for text and items
 * Uses a coordinate system relative to the parent
 */
public class InteractiveButton implements GuiEventListener {

    // ===================== POSITION PROPERTIES =====================
    private final int relativeX, relativeY;
    private final int width, height;

    // ===================== BUTTON CONTENT =====================
    private final Component displayText;
    private final ItemStack displayItem;
    private final Runnable clickAction;

    // ===================== BUTTON STATE =====================
    private boolean isPressed = false;
    private boolean isCurrentlyHovered = false;

    // ===================== STYLE =====================
    private int normalBackgroundColor = 0xFF555555;
    private int hoveredBackgroundColor = 0xFF777777;
    private int pressedBackgroundColor = 0xFF333333;
    private int textColor = 0xFFFFFFFF;
    private float textScale = 0.65f;

    /**
     * @param relativeX X position relative to the parent
     * @param relativeY Y position relative to the parent
     */
    public InteractiveButton(int relativeX, int relativeY, int width, int height,
                             Component displayText, ItemStack displayItem, Runnable clickAction) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
        this.displayText = displayText;
        this.displayItem = displayItem;
        this.clickAction = clickAction;
    }

    // ===================== RENDERING =====================

    /**
     * @param parentAbsoluteX Parent's absolute X position
     * @param parentAbsoluteY Parent's absolute Y position
     * @param mouseRelativeToParentX Mouse X position relative to the parent
     * @param mouseRelativeToParentY Mouse Y position relative to the parent
     * @return ItemStack for tooltip or null
     */
    public ItemStack render(GuiGraphics guiGraphics, Font font,
                            int parentAbsoluteX, int parentAbsoluteY,
                            int mouseRelativeToParentX, int mouseRelativeToParentY) {

        // Absolut position
        int buttonAbsoluteX = parentAbsoluteX + relativeX;
        int buttonAbsoluteY = parentAbsoluteY + relativeY;

        // Update hover
        this.isCurrentlyHovered = isMouseOver(mouseRelativeToParentX, mouseRelativeToParentY);

        renderButtonBackground(guiGraphics, buttonAbsoluteX, buttonAbsoluteY);
        renderButtonContent(guiGraphics, font, buttonAbsoluteX, buttonAbsoluteY);

        // Tooltip item
        return (displayItem != null && isCurrentlyHovered) ? displayItem : null;
    }

    private void renderButtonBackground(GuiGraphics guiGraphics, int buttonAbsoluteX, int buttonAbsoluteY) {
        int backgroundColor = determineCurrentBackgroundColor();
        int borderColor = determineCurrentBorderColor();

        // Bordure
        guiGraphics.fill(
                buttonAbsoluteX - 1, buttonAbsoluteY - 1,
                buttonAbsoluteX + width + 1, buttonAbsoluteY + height + 1,
                borderColor
        );

        // Fond
        guiGraphics.fill(
                buttonAbsoluteX, buttonAbsoluteY,
                buttonAbsoluteX + width, buttonAbsoluteY + height,
                backgroundColor
        );
    }

    /**
     * Render text and item
     */
    private void renderButtonContent(GuiGraphics guiGraphics, Font font, int buttonAbsoluteX, int buttonAbsoluteY) {
        if (displayItem == null) {
            renderCenteredScaledText(
                    guiGraphics, font, displayText,
                    buttonAbsoluteX + width / 2,
                    buttonAbsoluteY + height / 2
            );
        } else {
            guiGraphics.renderItem(displayItem, buttonAbsoluteX + 1, buttonAbsoluteY + 1);
            renderCenteredScaledText(
                    guiGraphics, font, displayText,
                    buttonAbsoluteX + width - (int)(textScale * (float)font.width(displayText)),
                    buttonAbsoluteY + height - 3
            );
        }
    }

    // ===================== EVENT HANDLING =====================

    public boolean handleMouseClick(int mouseRelativeToParentX, int mouseRelativeToParentY, int mouseButton) {
        if (isMouseOver(mouseRelativeToParentX, mouseRelativeToParentY) && mouseButton == 0) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean handleMouseRelease(int mouseRelativeToParentX, int mouseRelativeToParentY, int mouseButton) {
        if (isPressed && mouseButton == 0) {
            if (isMouseOver(mouseRelativeToParentX, mouseRelativeToParentY)) {
                if (clickAction != null) {
                    clickAction.run();
                }
            }

            isPressed = false;
            return true;
        }

        return false;
    }


    @Override
    public boolean isMouseOver(double mouseRelativeToParentX, double mouseRelativeToParentY) {
        return mouseRelativeToParentX >= relativeX &&
                mouseRelativeToParentX <= relativeX + width &&
                mouseRelativeToParentY >= relativeY &&
                mouseRelativeToParentY <= relativeY + height;
    }

    // ===================== UTILITY METHODS =====================

    private void renderCenteredScaledText(GuiGraphics guiGraphics, Font font, Component text, int centerX, int centerY) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(textScale, textScale, 1f);

        int scaledCenterX = (int)(centerX / textScale);
        int scaledCenterY = (int)(centerY / textScale);

        guiGraphics.drawString(
                font, text,
                scaledCenterX - font.width(text) / 2,
                scaledCenterY - font.lineHeight / 2,
                textColor, false
        );

        guiGraphics.pose().popPose();
    }

    private int determineCurrentBackgroundColor() {
        if (isPressed) return pressedBackgroundColor;
        return isCurrentlyHovered ? hoveredBackgroundColor : normalBackgroundColor;
    }

    private int determineCurrentBorderColor() {
        return isPressed ? normalBackgroundColor : pressedBackgroundColor;
    }

    // ===================== STYLE =====================

    public InteractiveButton withCustomColors(int normal, int hovered, int pressed) {
        this.normalBackgroundColor = normal;
        this.hoveredBackgroundColor = hovered;
        this.pressedBackgroundColor = pressed;
        return this;
    }

    public InteractiveButton withTextScale(float scale) {
        this.textScale = scale;
        return this;
    }

    // ===================== GuiEventListener =====================

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Override by handleMouseClick
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Override by handleMouseRelease
        return false;
    }
}
