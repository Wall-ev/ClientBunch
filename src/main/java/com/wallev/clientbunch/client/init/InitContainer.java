package com.wallev.clientbunch.client.init;

import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.inventory.tooltip.*;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitContainer {
    private InitContainer() {
    }

    @SubscribeEvent
    public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(FoodEffectTooltipComponent.class, FoodEffectTooltipRenderer::new);
        event.register(MobEffectTooltipComponent.class, MobEffectTooltipRenderer::new);
        event.register(IconTooltipComponent.class, IconTooltipRenderer::new);
    }
}
