package com.wallev.clientbunch.api.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.eventbus.api.Event;

public class OverlayRenderEvent extends Event {

    private final ForgeGui gui;
    private final GuiGraphics guiGraphics;
    private final float partialTick;
    private final int screenWidth;
    private final int screenHeight;

    public OverlayRenderEvent(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        this.gui = gui;
        this.guiGraphics = guiGraphics;
        this.partialTick = partialTick;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public ForgeGui getGui() {
        return gui;
    }

    public GuiGraphics getGuiGraphics() {
        return guiGraphics;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
