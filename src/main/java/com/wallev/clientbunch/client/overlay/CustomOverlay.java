package com.wallev.clientbunch.client.overlay;

import com.wallev.clientbunch.api.client.OverlayRenderEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

public class CustomOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        MinecraftForge.EVENT_BUS.post(new OverlayRenderEvent(gui, guiGraphics, partialTick, screenWidth, screenHeight));
    }

    public static class Builder {
        private Consumer<IGuiOverlay> render;

        public Builder render(Consumer<IGuiOverlay> render) {
            this.render = render;
            return this;
        }

        public CustomOverlay build() {
            return new CustomOverlay(){
                @Override
                public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
                    render.accept(this);
                }
            };
        }
    }
}
