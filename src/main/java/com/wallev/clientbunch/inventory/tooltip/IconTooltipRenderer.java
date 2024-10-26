package com.wallev.clientbunch.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class IconTooltipRenderer implements ClientTooltipComponent {
    private final IconTooltipComponent iconTooltipComponent;
    private final int imageOffsetX;
    private final int textOffsetX;

    public IconTooltipRenderer(IconTooltipComponent iconTooltipComponent) {
        this.iconTooltipComponent = iconTooltipComponent;
        int[] offsetX = getOffsetX();
        this.imageOffsetX = offsetX[0];
        this.textOffsetX = offsetX[1];
    }

    private int[] getOffsetX() {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent description = iconTooltipComponent.mutableComponent();

        String string = description.getString();
        int spaceCount = 0;
        String spaceString = CommonComponents.SPACE.getString();
        if (string.startsWith(spaceString)) {
            for (String s : string.split(spaceString)) {
                if (!s.isEmpty()) {
                    break;
                }
                spaceCount++;
            }

            int a1 = 0;
            if (description.getContents() instanceof LiteralContents literalContents) {
                a1 += mc.font.width(literalContents.text());
            }

            int offsetX = mc.font.width(CommonComponents.SPACE.getString().repeat(spaceCount));
            return new int[]{offsetX, offsetX + 40 - a1};
        } else {
            return new int[]{0, 40};
        }
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 2;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return font.width(iconTooltipComponent.mutableComponent()) + 40 + 2;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
        final int x1 = x + imageOffsetX, y1 = y + 1;
        final float scale = 0.75f;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        graphics.blit(iconTooltipComponent.resourceLocation(), (int) (x1 / scale), (int) (y1 / scale), 0, 0, 40, 9, 256, 256);
        graphics.pose().popPose();
    }

    @Override
    public void renderText(
            @NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource buffer) {
        MutableComponent description = iconTooltipComponent.mutableComponent();
        int color = description.getStyle().getColor() == null ? 0xAABBCC : description.getStyle().getColor().getValue();
        font.drawInBatch(description, x + textOffsetX, y + 1, color, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
