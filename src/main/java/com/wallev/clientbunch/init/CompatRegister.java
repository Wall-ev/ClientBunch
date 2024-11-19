package com.wallev.clientbunch.init;

import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.event.FoodTooltipEvent;
import com.wallev.clientbunch.client.event.PatchouliScreenEvent;
import com.wallev.clientbunch.compat.cloth.ClothCompat;
import com.wallev.clientbunch.config.subconfig.RenderConfig;
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
            if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("cloth_config")) {
                ClothCompat.init();
            }
        });
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("appleskin")) {
                MinecraftForge.EVENT_BUS.register(new FoodTooltipEvent());
            }
        });
        event.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT && ModList.get().isLoaded("patchouli")) {
                MinecraftForge.EVENT_BUS.register(new PatchouliScreenEvent());
            }
        });
        event.enqueueWork(CompatRegister::autoBindItemIconType);
    }

    private static void autoBindItemIconType() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;
        if (!RenderConfig.AUTO_ITEM_ICON_TYPE.get()) return;

        if (ModList.get().isLoaded("obscure_tooltips")) {
            RenderConfig.ITEM_ICON_TYPE.set(RenderConfig.ItemIconType.OBSCURE);
        } else if (ModList.get().isLoaded("legendarytooltips")) {
            RenderConfig.ITEM_ICON_TYPE.set(RenderConfig.ItemIconType.LEGENDARY);
        } else if (ModList.get().isLoaded("modernui")) {
            RenderConfig.ITEM_ICON_TYPE.set(RenderConfig.ItemIconType.MODERN_UI);
        } else {
            RenderConfig.ITEM_ICON_TYPE.set(RenderConfig.ItemIconType.NORMAL);
        }
    }
}
