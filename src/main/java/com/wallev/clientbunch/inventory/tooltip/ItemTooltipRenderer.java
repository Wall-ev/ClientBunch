package com.wallev.clientbunch.inventory.tooltip;

import com.wallev.clientbunch.client.tooltip.ItemTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class ItemTooltipRenderer implements ClientTooltipComponent {
    private final ItemStack stack;
    private final Component name;
    private final Component secondName;
    private final float scale;

    public ItemTooltipRenderer(ItemTooltipComponent itemTooltipComponent) {
        this.stack = itemTooltipComponent.stack();
        this.name = itemTooltipComponent.name();
        this.secondName = itemTooltipComponent.secondName();
        this.scale = itemTooltipComponent.scale();
    }

    @Override
    public int getHeight() {
        return 20 + 2 + 2 + 2;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 22 + 2 + 2 + Math.max(font.width(name), secondName == null ? 0 : font.width(secondName));
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
        final int color = 553648127; /* 553648127 */
        graphics.fillGradient(x + 2, y + 2, x + 2 + 20, y + 2 + 20, color, color);
        graphics.fillGradient(x + 2, y + 1, x + 2 + 20, y + 1 + 1, color, color);
        graphics.fillGradient(x + 2 + 20, y + 2, x + 2 + 20 + 1, y + 2 + 20, color, color);
        graphics.fillGradient(x + 2, y + 2 + 20, x + 2 + 20, y + 2 + 20 + 1, color, color);
        graphics.fillGradient(x + 1, y + 2, x + 2, y + 2 + 20, color, color);

        graphics.pose().pushPose();
//        graphics.pose().translate(x, y, 500.0F);
//        graphics.pose().scale(scale, scale, scale);
//        graphics.renderItem(stack, 2, 2);

        graphics.pose().translate(x + 12, y + 12, 500.0F);
        graphics.pose().scale(scale, scale, scale);
        graphics.renderItem(stack, -8, -8);

        graphics.pose().popPose();
    }

    @Override
    public void renderText(
            @NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource buffer) {
        Component description = name;
        int color = description.getStyle().getColor() == null ? 0xAABBCC : description.getStyle().getColor().getValue();
        font.drawInBatch(name, x + 22 + 2 + 2, y + 2, color, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);

        if (secondName != null) {
            font.drawInBatch(secondName, x + 22 + 2 + 2, y + 2 + 2 + font.lineHeight, color, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}
