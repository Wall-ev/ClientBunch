package com.wallev.clientbunch.init;

import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.event.FoodTooltipEvent;
import com.wallev.clientbunch.client.event.ItemTooltipEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatRegister {
    @SubscribeEvent
    public static void onEnqueue(final InterModEnqueueEvent event) {
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("appleskin")) {
                MinecraftForge.EVENT_BUS.register(new FoodTooltipEvent());
            }
        });
    }
}
