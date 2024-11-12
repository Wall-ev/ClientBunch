package com.wallev.clientbunch.client.init;

import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.overlay.CustomOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.CROSSHAIR;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT, modid = ClientBunch.MOD_ID)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(CROSSHAIR.id(), String.format("%s.custom_overlay.%s", ClientBunch.MOD_ID, CROSSHAIR.id().getPath()), new CustomOverlay());
    }
}