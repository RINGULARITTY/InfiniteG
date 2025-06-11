package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ScrollArea implements GuiEventListener {
    private final List<ScrollElement> elements = new ArrayList<>();
    private final int x, y, width, height;
    private final int scrollBarWidth = 2;

    private float scrollOffset = 0.0f;
    private int totalContentHeight = 0;
    private boolean isMouseOver = false;

    private static final int SCROLLBAR_BACKGROUND = 0xFF303030;
    private static final int SCROLLBAR_HANDLE = 0xFF8B8B8B;
    private static final int SCROLLBAR_HANDLE_HOVER = 0xFFFFFFFF;

    private ItemStack hoveredItemStack = ItemStack.EMPTY;
    private int hoveredX, hoveredY;

    public ScrollArea(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ScrollArea addElement(ScrollElement element) {
        elements.add(element);
        calculateTotalHeight();
        return this;
    }

    public ScrollArea clearElements() {
        elements.clear();
        totalContentHeight = 0;
        scrollOffset = 0.0f;
        return this;
    }

    private void calculateTotalHeight() {
        totalContentHeight = elements.stream()
                .mapToInt(e -> e.getHeight() + 1)
                .sum() - 1;
    }

    public void render(GuiGraphics guiGraphics, Font font, int mouseX, int mouseY) {
        isMouseOver = isMouseOver(mouseX, mouseY);

        hoveredItemStack = ItemStack.EMPTY;

        guiGraphics.fill(x - 1, y - 1, x + width + 4, y + height + 1, 0xFF050505);
        guiGraphics.fill(x, y, x + width + 3, y + height, 0xFF373737);

        guiGraphics.enableScissor(x, y, x + width, y + height);

        renderContent(guiGraphics, font, mouseX, mouseY);

        guiGraphics.disableScissor();

        if (needsScrollbar()) {
            renderScrollbar(guiGraphics, mouseX, mouseY);
        }
    }

    private void renderContent(GuiGraphics guiGraphics, Font font, int mouseX, int mouseY) {
        int currentY = y - (int)scrollOffset;

        for (int i = 0; i < elements.size(); ++i) {
            final ScrollElement e = elements.get(i);
            if (currentY + e.getHeight() >= y && currentY <= y + height) {
                e.render(guiGraphics, font, x, currentY, mouseX, mouseY);

                for (ScrollElement.IconElement icon : e.getIcons()) {
                    int iconX = x + icon.x;
                    int iconY = currentY + icon.y;

                    if (mouseX >= iconX && mouseX < iconX + 16 &&
                            mouseY >= iconY && mouseY < iconY + 16) {
                        hoveredItemStack = icon.itemStack;
                        hoveredX = mouseX;
                        hoveredY = mouseY;
                    }
                }
            }
            if (i != elements.size() - 1)
                guiGraphics.fill(x, currentY + e.getHeight(), x + width, currentY + e.getHeight() + 1, 0xFF373737);

            currentY += e.getHeight() + 1;
        }
    }

    private void renderScrollbar(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int scrollbarX = x + width + 1;
        int scrollbarY = y;
        int scrollbarHeight = height;

        guiGraphics.fill(scrollbarX, scrollbarY, scrollbarX + scrollBarWidth, scrollbarY + scrollbarHeight, SCROLLBAR_BACKGROUND);

        float contentRatio = (float) height / totalContentHeight;
        int handleHeight = Math.max(10, (int)(scrollbarHeight * contentRatio));
        float scrollRatio = scrollOffset / getMaxScroll();
        int handleY = scrollbarY + (int)((scrollbarHeight - handleHeight) * scrollRatio);

        boolean mouseOverHandle = mouseX >= scrollbarX && mouseX <= scrollbarX + scrollBarWidth &&
                mouseY >= handleY && mouseY <= handleY + handleHeight;
        int handleColor = mouseOverHandle ? SCROLLBAR_HANDLE_HOVER : SCROLLBAR_HANDLE;

        guiGraphics.fill(scrollbarX, handleY, scrollbarX + scrollBarWidth, handleY + handleHeight, handleColor);
    }

    private void handleButtonEvents(double mouseX, double mouseY, int button, Consumer<CustomButton> action) {
        int currentY = y - (int)scrollOffset;
        for(ScrollElement element : elements) {
            if(currentY + element.getHeight() >= y && currentY <= y + height) {
                for(CustomButton btn : element.getButtons()) {
                    if(btn.isMouseOver(mouseX - x, mouseY - currentY)) {
                        action.accept(btn);
                    }
                }
            }
            currentY += element.getHeight() + 1;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isMouseOver && needsScrollbar()) {
            float scrollAmount = (float) (-scrollY * 20.0); // Ajuster la vitesse de scroll
            setScrollOffset(scrollOffset + scrollAmount);
            return true;
        }
        return false;
    }

    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width + 3 && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        handleButtonEvents(mouseX, mouseY, button, btn -> btn.mouseClicked(mouseX, mouseY, button));
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        handleButtonEvents(mouseX, mouseY, button, btn -> btn.mouseReleased(mouseX, mouseY, button));
        return true;
    }

    private boolean needsScrollbar() {
        return totalContentHeight > height;
    }

    private float getMaxScroll() {
        return Math.max(0, totalContentHeight - height);
    }

    private void setScrollOffset(float offset) {
        this.scrollOffset = Mth.clamp(offset, 0.0f, getMaxScroll());
    }

    public ItemStack getHoveredItemStack() {
        return hoveredItemStack;
    }

    @Override public void setFocused(boolean focused) {}
    @Override public boolean isFocused() { return false; }
}
