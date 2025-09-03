package fr.ringularity.infiniteg.screens.widgets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class Primitives {
    public static class TextElement {
        public Component textComponent;
        public int x, y, color;
        public float scale;

        public TextElement(Component textComponent, int x, int y, int color, float scale) {
            this.textComponent = textComponent;
            this.x = x;
            this.y = y;
            this.color = color;
            this.scale = scale;
        }
    }

    public static class ItemIconElement {
        public ItemStack itemStack;
        public int x, y;

        public ItemIconElement(ItemStack itemStack, int x, int y) {
            this.itemStack = itemStack;
            this.x = x;
            this.y = y;
        }
    }
}
