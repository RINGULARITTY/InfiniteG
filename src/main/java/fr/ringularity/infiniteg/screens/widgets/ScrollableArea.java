package fr.ringularity.infiniteg.screens.widgets;

import fr.ringularity.infiniteg.screens.InfiniteGScreen;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScrollableArea implements GuiEventListener {

    // ===================== POSITION PROPERTIES =====================
    private final int relativeX, relativeY, width, height;

    // ===================== SCROLL PROPERTIES =====================
    private static final int SCROLLBAR_WIDTH = 2;
    public final double scrollSpeed;
    private float currentScrollOffset = 0.0f;
    private int totalContentHeight = 0;

    // ===================== BUTTON CONTENT =====================
    private final List<ScrollableElement> containedElements = new ArrayList<>();

    // ===================== STATE CONTENT =====================
    private boolean isHandlingInteraction = false;
    private final List<InteractiveButton> pressedButtons = new ArrayList<>();
    private ItemStack hoveredItemForTooltip = null;

    // ===================== STYLE CONTENT =====================
    private static final int SCROLLBAR_BACKGROUND_COLOR = 0xFF303030;
    private static final int SCROLLBAR_HANDLE_COLOR = 0xFF8B8B8B;
    private static final int SCROLLBAR_HANDLE_HOVER_COLOR = 0xFFFFFFFF;

    public ScrollableArea(int relativeX, int relativeY, int width, int height, double scrollSpeed) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.width = width;
        this.height = height;
        this.scrollSpeed = scrollSpeed;
    }

    public int getRelativeX() { return relativeX; }
    public int getRelativeY() { return relativeY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // ===================== Handle elements =====================

    public ScrollableArea addElement(ScrollableElement element) {
        containedElements.add(element);
        recalculateTotalContentHeight();
        return this;
    }

    public ScrollableArea clearElements() {
        containedElements.clear();
        totalContentHeight = 0;
        currentScrollOffset = 0.0f;
        pressedButtons.clear();
        return this;
    }

    // ===================== RENDERING =====================

    public void render(GuiGraphics guiGraphics, Font font,
                       int absoluteX, int absoluteY,
                       int mouseAbsoluteX, int mouseAbsoluteY) {

        hoveredItemForTooltip = null;

        renderBackground(guiGraphics, absoluteX, absoluteY);

        guiGraphics.enableScissor(absoluteX, absoluteY, absoluteX + width, absoluteY + height);
        renderScrollableContent(guiGraphics, font, absoluteX, absoluteY, mouseAbsoluteX, mouseAbsoluteY);
        guiGraphics.disableScissor();

        if (requiresScrollbar()) {
            renderScrollbar(guiGraphics, absoluteX, absoluteY, mouseAbsoluteX, mouseAbsoluteY);
        }
    }

    private void renderBackground(GuiGraphics guiGraphics, int absoluteX, int absoluteY) {
        // Border
        guiGraphics.fill(absoluteX - 1, absoluteY - 1,
                absoluteX + width + 4, absoluteY + height + 1, 0xFF050505);
        // Background
        guiGraphics.fill(absoluteX, absoluteY,
                absoluteX + width + 3, absoluteY + height, 0xFF373737);
    }

    private void renderScrollableContent(GuiGraphics guiGraphics, Font font,
                                         int absoluteX, int absoluteY,
                                         int mouseAbsoluteX, int mouseAbsoluteY) {

        int currentElementAbsoluteY = absoluteY - (int) currentScrollOffset;

        for (int elementIndex = 0; elementIndex < containedElements.size(); elementIndex++) {
            ScrollableElement element = containedElements.get(elementIndex);

            if (isElementVisibleInScrollArea(currentElementAbsoluteY, element.getHeight(), absoluteY)) {
                int mouseRelativeToElementX = mouseAbsoluteX - absoluteX;
                int mouseRelativeToElementY = mouseAbsoluteY - currentElementAbsoluteY;

                ItemStack elementTooltipItem = element.render(
                        guiGraphics, font,
                        absoluteX, currentElementAbsoluteY,
                        mouseRelativeToElementX, mouseRelativeToElementY
                );

                if (elementTooltipItem != null) {
                    hoveredItemForTooltip = elementTooltipItem;
                }
            }

            // Separators
            if (elementIndex < containedElements.size() - 1) {
                int separatorY = currentElementAbsoluteY + element.getHeight();
                guiGraphics.fill(absoluteX, separatorY, absoluteX + width, separatorY + 1, 0xFF373737);
            }

            currentElementAbsoluteY += element.getHeight() + 1;
        }
    }

    private void renderScrollbar(GuiGraphics guiGraphics, int absoluteX, int absoluteY,
                                 int mouseAbsoluteX, int mouseAbsoluteY) {

        int scrollbarAbsoluteX = absoluteX + width + 1;
        int scrollbarAbsoluteY = absoluteY;
        int scrollbarHeight = height;

        // Scrollbar background
        guiGraphics.fill(scrollbarAbsoluteX, scrollbarAbsoluteY,
                scrollbarAbsoluteX + SCROLLBAR_WIDTH, scrollbarAbsoluteY + scrollbarHeight,
                SCROLLBAR_BACKGROUND_COLOR);

        // Handle
        float visibleContentRatio = (float) height / totalContentHeight;
        int handleHeight = Math.max(4, (int) (scrollbarHeight * visibleContentRatio));
        float scrollProgress = currentScrollOffset / getMaximumScrollOffset();
        int handleAbsoluteY = scrollbarAbsoluteY + (int) ((scrollbarHeight - handleHeight) * scrollProgress);

        // Handle hover
        boolean isHandleHovered = isPointInRectangle(mouseAbsoluteX, mouseAbsoluteY,
                scrollbarAbsoluteX, handleAbsoluteY,
                SCROLLBAR_WIDTH, handleHeight);

        int handleColor = isHandleHovered ? SCROLLBAR_HANDLE_HOVER_COLOR : SCROLLBAR_HANDLE_COLOR;
        guiGraphics.fill(scrollbarAbsoluteX, handleAbsoluteY,
                scrollbarAbsoluteX + SCROLLBAR_WIDTH, handleAbsoluteY + handleHeight,
                handleColor);
    }

    public void renderTooltip(InfiniteGScreen<?> screen, GuiGraphics guiGraphics, int mouseAbsoluteX, int mouseAbsoluteY) {
        if (hoveredItemForTooltip != null) {
            guiGraphics.setTooltipForNextFrame(screen.getMinecraft().font, hoveredItemForTooltip, mouseAbsoluteX, mouseAbsoluteY);
        }
    }

    private void recalculateTotalContentHeight() {
        totalContentHeight = containedElements.stream()
                .mapToInt(element -> element.getHeight() + 1)
                .sum() - 1;
    }

    public boolean handleMouseClick(double mouseAbsoluteX, double mouseAbsoluteY, int button,
                                    int absoluteX, int absoluteY) {
        if (isHandlingInteraction) return false;
        isHandlingInteraction = true;

        try {
            return processMouseClickOnElements(mouseAbsoluteX, mouseAbsoluteY, button, absoluteX, absoluteY);
        } finally {
            isHandlingInteraction = false;
        }
    }

    public boolean handleMouseRelease(double mouseAbsoluteX, double mouseAbsoluteY, int button,
                                      int absoluteX, int absoluteY) {
        return processMouseReleaseOnElements(mouseAbsoluteX, mouseAbsoluteY, button, absoluteX, absoluteY);
    }

    private boolean processMouseClickOnElements(double mouseAbsoluteX, double mouseAbsoluteY, int button,
                                                int absoluteX, int absoluteY) {

        int currentElementAbsoluteY = absoluteY - (int) currentScrollOffset;

        for (ScrollableElement element : containedElements) {
            if (isElementVisibleInScrollArea(currentElementAbsoluteY, element.getHeight(), absoluteY)) {

                for (InteractiveButton elementButton : element.getInteractiveButtons()) {
                    int mouseRelativeToElementX = (int) (mouseAbsoluteX - absoluteX);
                    int mouseRelativeToElementY = (int) (mouseAbsoluteY - currentElementAbsoluteY);

                    if (elementButton.isMouseOver(mouseRelativeToElementX, mouseRelativeToElementY)) {
                        boolean handled = elementButton.handleMouseClick(mouseRelativeToElementX, mouseRelativeToElementY, button);
                        if (handled && button == 0) {
                            if (!pressedButtons.contains(elementButton)) {
                                pressedButtons.add(elementButton);
                            }
                        }
                        return handled;
                    }
                }
            }
            currentElementAbsoluteY += element.getHeight() + 1;
        }

        return false;
    }

    private boolean processMouseReleaseOnElements(double mouseAbsoluteX, double mouseAbsoluteY, int button,
                                                  int absoluteX, int absoluteY) {

        boolean handledAnyRelease = false;

        for (InteractiveButton pressedButton : new ArrayList<>(pressedButtons)) {
            ButtonPosition buttonPos = findButtonPosition(pressedButton, absoluteX, absoluteY);

            if (buttonPos != null) {
                int mouseRelativeToElementX = (int) (mouseAbsoluteX - absoluteX);
                int mouseRelativeToElementY = (int) (mouseAbsoluteY - buttonPos.elementAbsoluteY);

                boolean handled = pressedButton.handleMouseRelease(mouseRelativeToElementX, mouseRelativeToElementY, button);
                if (handled) {
                    handledAnyRelease = true;
                }
            }

            pressedButtons.remove(pressedButton);
        }

        return handledAnyRelease;
    }

    private boolean isElementVisibleInScrollArea(int elementAbsoluteY, int elementHeight, int scrollAreaAbsoluteY) {
        return elementAbsoluteY + elementHeight >= scrollAreaAbsoluteY &&
                elementAbsoluteY <= scrollAreaAbsoluteY + height;
    }

    private ButtonPosition findButtonPosition(InteractiveButton targetButton, int absoluteX, int absoluteY) {
        int currentElementAbsoluteY = absoluteY - (int) currentScrollOffset;

        for (ScrollableElement element : containedElements) {
            for (InteractiveButton elementButton : element.getInteractiveButtons()) {
                if (elementButton == targetButton) {
                    return new ButtonPosition(currentElementAbsoluteY);
                }
            }
            currentElementAbsoluteY += element.getHeight() + 1;
        }

        return null;
    }

    private static class ButtonPosition {
        final int elementAbsoluteY;

        ButtonPosition(int elementAbsoluteY) {
            this.elementAbsoluteY = elementAbsoluteY;
        }
    }

    public boolean handleMouseScroll(double mouseAbsoluteX, double mouseAbsoluteY,
                                     double scrollX, double scrollY,
                                     int absoluteX, int absoluteY) {

        if (isPointInScrollArea(mouseAbsoluteX, mouseAbsoluteY, absoluteX, absoluteY) && requiresScrollbar()) {
            float scrollAmount = (float) (-scrollY * scrollSpeed);
            setScrollOffset(currentScrollOffset + scrollAmount);
            return true;
        }

        return false;
    }

    private boolean isPointInScrollArea(double pointX, double pointY, int absoluteX, int absoluteY) {
        return pointX >= absoluteX && pointX <= absoluteX + width + 3 &&
                pointY >= absoluteY && pointY <= absoluteY + height;
    }

    private boolean isPointInRectangle(double pointX, double pointY,
                                       int rectX, int rectY, int rectWidth, int rectHeight) {
        return pointX >= rectX && pointX <= rectX + rectWidth &&
                pointY >= rectY && pointY <= rectY + rectHeight;
    }

    private boolean requiresScrollbar() {
        return totalContentHeight > height;
    }

    private float getMaximumScrollOffset() {
        return Math.max(0, totalContentHeight - height);
    }

    private void setScrollOffset(float newOffset) {
        this.currentScrollOffset = Mth.clamp(newOffset, 0.0f, getMaximumScrollOffset());
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
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }
}
