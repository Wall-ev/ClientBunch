package com.wallev.clientbunch.util;

import net.minecraft.client.Minecraft;

public final class MouseHandlerUtil {

    private MouseHandlerUtil() {
    }

    public static int getMouseX() {
        return getMouseX(Minecraft.getInstance());
    }

    public static int getMouseY() {
        return getMouseY(Minecraft.getInstance());
    }

    public static int getMouseX(Minecraft mc) {
        return (int)(mc.mouseHandler.xpos() * (double)mc.getWindow().getGuiScaledWidth() / (double)mc.getWindow().getScreenWidth());
    }

    public static int getMouseY(Minecraft mc) {
        return (int)(mc.mouseHandler.ypos() * (double)mc.getWindow().getGuiScaledHeight() / (double)mc.getWindow().getScreenHeight());
    }
}
