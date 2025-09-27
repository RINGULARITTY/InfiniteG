package fr.ringularity.infiniteg.screens.widgets;

import fr.ringularity.infiniteg.screens.ScreenTools;
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
    private final Primitives.TextElement textElement;
    private final ItemStack displayItem;
    private final Runnable clickAction;

    // ===================== BUTTON STATE =====================
    private boolean isPressed = false;
    private boolean isCurrentlyHovered = false;

    // ===================== STYLE =====================
    private int normalBackgroundColor = 0xFF555555;
    private int hoveredBackgroundColor = 0xFF777777;
    private int pressedBackgroundColor = 0xFF333333;

    private static int TEXT_COLOR = 0xFFFFFFFF;
    private static float TEXT_SCALE = 0.65f;
    private static float ITEM_TEXT_SCALE = 0.5f;

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

        if (displayItem == null)
            this.textElement = new Primitives.TextElement(displayText, 0, 0, TEXT_COLOR, TEXT_SCALE);
        else
            this.textElement = new Primitives.TextElement(displayText, 0, 0, TEXT_COLOR, ITEM_TEXT_SCALE);
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

        // Border
        guiGraphics.fill(
                buttonAbsoluteX - 1, buttonAbsoluteY - 1,
                buttonAbsoluteX + width + 1, buttonAbsoluteY + height + 1,
                borderColor
        );

        // Background
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
                    guiGraphics, font,
                    buttonAbsoluteX + width / 2,
                    buttonAbsoluteY + height / 2
            );
        } else {
            ScreenTools.renderScaledItem(guiGraphics, displayItem, buttonAbsoluteX + 1, buttonAbsoluteY + 1, 0.6667f);
            //guiGraphics.renderItem(displayItem, buttonAbsoluteX + 1, buttonAbsoluteY + 1);
            renderScaledTextBottomRight(
                    guiGraphics, font,
                    buttonAbsoluteX + width - 2,
                    buttonAbsoluteY + height
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

    private void renderCenteredScaledText(GuiGraphics guiGraphics, Font font, int centerX, int centerY) {
        textElement.x = centerX;
        textElement.y = centerY;

        ScreenTools.renderCenteredScaledText(guiGraphics, font, textElement);
    }


    private void renderScaledTextBottomRight(GuiGraphics guiGraphics, Font font, int x, int y) {
        textElement.x = x;
        textElement.y = y;

        ScreenTools.renderScaledTextBottomRight(guiGraphics, font, textElement);
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
        this.textElement.scale = scale;
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
