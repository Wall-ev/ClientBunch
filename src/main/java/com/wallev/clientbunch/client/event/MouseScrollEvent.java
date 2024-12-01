package com.wallev.clientbunch.client.event;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import com.wallev.clientbunch.ClientBunch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, value = Dist.CLIENT)
public class MouseScrollEvent {
    @SubscribeEvent
    public static void onScreenMouseScroll(ScreenEvent.MouseScrolled.Post event) {
        Screen screen = Minecraft.getInstance().screen;
        if (event.getScrollDelta() != 0 && screen != null) {
            boolean next = event.getScrollDelta() < 0;
            if (screen instanceof RecipeUpdateListener recipeUpdateListener) {
                RecipeBookComponent recipeBookComponent = recipeUpdateListener.getRecipeBookComponent();
            }
            if (ModList.get().isLoaded("solcarrot") && screen instanceof FoodBookScreen foodBookScreen) {
                foodBookScreen.switchToPage(foodBookScreen.getCurrentPageNumber() + (next ? 1 : -1));
                return;
            }
            if (ModList.get().isLoaded("solapplepie") && screen instanceof com.tarinoita.solsweetpotato.client.gui.FoodBookScreen foodBookScreen) {
                foodBookScreen.switchToPage(foodBookScreen.getCurrentPageNumber() + (next ? 1 : -1));
                return;
            }
        }
    }

}
