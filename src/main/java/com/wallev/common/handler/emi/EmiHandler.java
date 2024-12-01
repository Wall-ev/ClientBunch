package com.wallev.common.handler.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.registry.EmiRecipeFiller;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiFavorite;
import dev.emi.emi.runtime.EmiSidebars;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.RecipeBookMenu;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmiHandler {

    public static Map<String, String> recipeTypeMap = new HashMap<>();
    public static Consumer<Screen> updateTypeRecipesCon;
    public static Consumer<Void> updateCraftablesCon;
    public static Function<Screen, String> getContainerClassNameFunc;

    public static List<EmiIngredient> typeRecipes = Lists.newArrayList();

    public static List<EmiIngredient> getCraftablesFromOther(EmiPlayerInventory lastPlayerInventory) {
        List<EmiIngredient> ingredients = typeRecipes.stream().filter(emiIngredient -> {
            EmiRecipe recipe = ((EmiFavorite.Craftable) emiIngredient).getRecipe();
            return lastPlayerInventory.canCraft(recipe);
        }).toList();

        return ingredients;
//        if (updateCraftablesCon != null) {
//            updateCraftablesCon.accept(null);
//        }
//        else if (EmiSidebars.craftables.isEmpty() &&
//                !typeRecipes.isEmpty() &&
//                EmiRecipeFiller.getAllHandlers(EmiApi.getHandledScreen()).isEmpty()) {
////            EmiSidebars.craftables =
//            List<EmiIngredient> ingredients = typeRecipes.stream().filter(emiIngredient -> {
//                EmiRecipe recipe = ((EmiFavorite.Craftable) emiIngredient).getRecipe();
//                return lastPlayerInventory.canCraft(recipe);
//            }).toList();
//            return ingredients;
//        }
//
//        if (!EmiSidebars.craftables.isEmpty()) return EmiSidebars.craftables;
//
//        return Collections.emptyList();
    }

    public static void updateCraftables(EmiPlayerInventory lastPlayerInventory) {
//        if (updateCraftablesCon != null) {
//            updateCraftablesCon.accept(null);
//        }
//        else if (EmiSidebars.craftables.isEmpty() &&
//                !typeRecipes.isEmpty() &&
//                EmiRecipeFiller.getAllHandlers(EmiApi.getHandledScreen()).isEmpty()) {
////            EmiSidebars.craftables =
//            List<EmiIngredient> ingredients = typeRecipes.stream().filter(emiIngredient -> {
//                EmiRecipe recipe = ((EmiFavorite.Craftable) emiIngredient).getRecipe();
//                return lastPlayerInventory.canCraft(recipe);
//            }).toList();
//            EmiSidebars.craftables = ingredients;
//        }
    }

    public static void updateTypeRecipes(Screen screen) {
        typeRecipes.clear();

        if (updateTypeRecipesCon != null) {
            updateTypeRecipesCon.accept(screen);
        } else {
            String name = "";
            if (screen instanceof AbstractContainerScreen<?> hs && hs.getMenu() instanceof RecipeBookMenu<?>) {
                name = hs.getMenu().getClass().getName();
            }else if (getContainerClassNameFunc != null){
                name = getContainerClassNameFunc.apply(screen);
            }

            String s = recipeTypeMap.get(name);

            if (s == null) return;

            EmiRecipeManager manager = EmiRecipes.manager;
            List<EmiRecipeCategory> categories = manager.getCategories();
            for (EmiRecipeCategory category : categories) {
                if (Objects.equals(category.getId().toString(), s)) {
                    typeRecipes = manager.getRecipes(category)
                            .stream()
                            .map(EmiFavorite.Craftable::new)
                            .sorted((a, b) -> {
                                int i = Integer.compare(
                                        EmiStackList.getIndex(a.getStack()),
                                        EmiStackList.getIndex(b.getStack()));
                                if (i != 0) {
                                    return i;
                                }
                                return Long.compare(a.getAmount(), b.getAmount());
                            }).collect(Collectors.toList());
                    return;
                }
            }
        }
    }

}
