package com.wallev.clientbunch.client.event.fix;

import com.wallev.clientbunch.ClientBunch;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FixBreakScreenMessageEvt {
    @SuppressWarnings("all")
    @SubscribeEvent
    public void fixClearButtonMessage(ScreenEvent.Init.Post event) {
        for (GuiEventListener guiEventListener : event.getListenersList()) {
            if (guiEventListener instanceof AbstractWidget abstractWidget && abstractWidget.getMessage() == null) {
                ClientBunch.LOGGER.warn(String.format("BreakWidget: %s message is null! screen: %s", abstractWidget.getClass().getName(), event.getScreen().getClass().getName()));
                abstractWidget.setMessage(Component.empty());
            }
        }
    }
}
