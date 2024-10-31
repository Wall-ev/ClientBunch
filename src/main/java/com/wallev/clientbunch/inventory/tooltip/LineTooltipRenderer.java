package com.wallev.clientbunch.inventory.tooltip;

import com.wallev.clientbunch.client.tooltip.LineTooltipComponent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class LineTooltipRenderer implements ClientTooltipComponent {
    private final int width;
    public LineTooltipRenderer(LineTooltipComponent lineTooltipComponent) {
        this.width = lineTooltipComponent.width();
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return width;
    }

    @Override
    public void renderImage(@NotNull Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        pGuiGraphics.fill(pX, pY, pX + width, pY + 1, Color.WHITE.getRGB());
    }
}