package fr.ringularity.infiniteg.screens;

import fr.ringularity.infiniteg.InfiniteG;
import fr.ringularity.infiniteg.component.CompactDataComponent;
import fr.ringularity.infiniteg.component.ModDataComponents;
import fr.ringularity.infiniteg.items.ModItems;
import fr.ringularity.infiniteg.menus.WorkstationMenu;
import fr.ringularity.infiniteg.screens.widgets.CustomButton;
import fr.ringularity.infiniteg.screens.widgets.ScrollArea;
import fr.ringularity.infiniteg.screens.widgets.ScrollElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class WorkstationScreen extends AbstractContainerScreen<WorkstationMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID,"textures/gui/container/workstation.png");
    private static final ResourceLocation ELEMENT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(InfiniteG.MOD_ID,"textures/gui/container/element.png");

    private final List<CustomButton> externalButtons = new ArrayList<>();

    private ScrollArea scrollArea;
    int xOffset = 15;
    int yOffset = 50;
    int scrollWidth = 162;
    int scrollHeight = 80;

    public WorkstationScreen(WorkstationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 2*176;
        imageHeight = 200; //166

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        initializeScrollArea();

        externalButtons.add(new CustomButton(
                15, 140, 80, 10,
                Component.literal("Fill Items"),
                () -> {
                    System.out.println("Test");
                })
        );
    }

    private void initializeScrollArea() {
        int scrollX = (width - imageWidth) / 2 + xOffset;
        int scrollY = (height - imageHeight) / 2 + yOffset;

        scrollArea = new ScrollArea(scrollX, scrollY, scrollWidth, scrollHeight);

        populateScrollArea();
    }

    private class ItemRequired {
        public final ItemStack itemStack;
        public final long required;
        public long current;

        private ItemRequired(ItemStack itemStack, long required, long current) {
            this.itemStack = itemStack;
            this.required = required;
            this.current = current;
        }
    }

    private void populateScrollArea() {
        final ItemStack s = new ItemStack(ModItems.DIAMOND_COMPACTOR.get());
        s.set(ModDataComponents.COMPACT_COMPONENT, new CompactDataComponent(10, Items.DIAMOND));

        ArrayList<ItemRequired> items = new ArrayList<>(List.of(
                new ItemRequired(s, 1, 0),
                new ItemRequired(new ItemStack(Items.DIAMOND), 15, 3),
                new ItemRequired(new ItemStack(Items.REDSTONE), 125, 10),
                new ItemRequired(new ItemStack(Items.GLOWSTONE), 35, 7),
                new ItemRequired(new ItemStack(Items.SAND), 400, 400),
                new ItemRequired(new ItemStack(Items.PUMPKIN), 30, 12),
                new ItemRequired(new ItemStack(Items.GHAST_TEAR), 7, 2)
        ));

        for (final ItemRequired ir : items) {
            scrollArea.addElement((
                    new ScrollElement(162, 20)
                            .addTexture(ELEMENT_TEXTURE, 0, 0, 162, 22, 162, 20)
                            .addIcon(ir.itemStack, 3, 2)
                            .addText(Component.translatable(ir.itemStack.getItem().getDescriptionId()), 23, 3, 0x000000, 0.75f)
                            .addText(Component.literal(ir.current + "/" + ir.required), 23, 13, ir.current < ir.required ? 0xbf2004 : 0x008c05, 0.6f)
            ));
        }
    }

    @Override
    protected void init() {
        super.init();
        initializeScrollArea();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 512, 256);
        renderProgressArrow(guiGraphics, x, y, menu.getProgress());
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y, float progress) {
        if(menu.isCrafting()) {
            //guiGraphics.blit(RenderType.GUI_TEXTURED, ARROW_TEXTURE,x + 73, y + 35, 0, 0, (int) (24.0f * progress), 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int dx = (width - imageWidth) / 2;
        int dy = (height - imageHeight) / 2;

        if (scrollArea != null) {
            scrollArea.render(pGuiGraphics, this.font, pMouseX, pMouseY);

            final ItemStack hoveredStack = scrollArea.getHoveredItemStack();
            if (!hoveredStack.isEmpty()) {
                pGuiGraphics.renderTooltip(this.font, hoveredStack, pMouseX, pMouseY);
            }
        }

        externalButtons.forEach(btn -> btn.render(pGuiGraphics, font, dx, dy, pMouseX, pMouseY));

        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        float scaleFactor = 0.5f;
        renderTextWithScale(guiGraphics, scaleFactor, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        renderTextWithScale(guiGraphics, scaleFactor, this.playerInventoryTitle, this.titleLabelX, this.titleLabelY, 4210752, false);
    }

    private void renderTextWithScale(GuiGraphics guiGraphics, float scale, Component text, int x, int y, int color, Boolean dropShadow) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0f);
        guiGraphics.drawString(
                this.font,
                text,
                (int)((float) x / scale),
                (int)((float) y / scale),
                color,
                dropShadow
        );
        guiGraphics.pose().popPose();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollArea != null && scrollArea.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(CustomButton btn : externalButtons) {
            if(btn.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for(CustomButton btn : externalButtons) {
            if(btn.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
