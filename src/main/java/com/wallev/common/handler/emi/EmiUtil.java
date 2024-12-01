package com.wallev.common.handler.emi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class EmiUtil {
    public static boolean isScreenHandler(Screen screen){
        return screen instanceof AbstractContainerScreen<?> hs && hs.getMenu() instanceof RecipeBookMenu<?>;
    }

    public static AbstractContainerMenu getScreenHandler(Screen screen) {
        if (screen instanceof AbstractContainerScreen<?> hs) {
            return hs.getMenu();
        }
        return null;
    }

    public static <C extends AbstractContainerMenu & Container, T extends Recipe<C>> void recipeClick(EmiRecipe recipe, EmiCraftContext<C> context, RecipeType<T> type) {
        Minecraft client = getClient();
        Level world = client.level;
        SimpleContainer inv = new SimpleContainer(recipe.getInputs().get(0).getEmiStacks().get(0).getItemStack());
        List<T> recipes = null;
//        if (world != null) {
//
//            world.getRecipeManager().getRecipesFor()
//
//            recipes = world.getRecipeManager().getAllMatches(type, inv, world);
//        }
//        for (int i = 0; i < recipes.size(); i++) {
//            if (EmiPort.getId(recipes.get(i)) != null && EmiPort.getId(recipes.get(i)).equals(recipe.getId())) {
//                K sh = context.getScreenHandler();
//                client.interactionManager.clickButton(sh.syncId, i);
//                if (context.getDestination() == EmiCraftContext.Destination.CURSOR) {
//                    client.interactionManager.clickSlot(sh.syncId, 1, 0, ClickType.PICKUP, client.player);
//                } else if (context.getDestination() == EmiCraftContext.Destination.INVENTORY) {
//                    client.interactionManager.clickSlot(sh.syncId, 1, 0, ClickType.QUICK_MOVE, client.player);
//                }
//                break;
//            }
//        }
    }

    public static <K extends AbstractContainerMenu> void recipeClick(EmiCraftContext<K> context, int i, boolean clickSlot) {
        Minecraft client = getClient();
        if (client.gameMode == null || client.player == null) {
            return;
        }

        K sh = context.getScreenHandler();
        client.gameMode.handleInventoryButtonClick(sh.containerId, i);
        if (clickSlot) {
            if (context.getDestination() == EmiCraftContext.Destination.CURSOR) {
                client.gameMode.handleInventoryMouseClick(sh.containerId, 1, 0, ClickType.PICKUP, client.player);
            } else if (context.getDestination() == EmiCraftContext.Destination.INVENTORY) {
                client.gameMode.handleInventoryMouseClick(sh.containerId, 1, 0, ClickType.QUICK_MOVE, client.player);
            }
        }
    }

    public static MultiPlayerGameMode getInteractionManager() {
        return getClient().gameMode;
    }

    public static Minecraft getClient() {
        return Minecraft.getInstance();
    }

}
