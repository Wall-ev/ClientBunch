package com.wallev.common.mixin.emi;

import com.wallev.common.handler.emi.EmiHandler;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.registry.EmiStackList;
import dev.emi.emi.runtime.EmiFavorite;
import dev.emi.emi.runtime.EmiSidebars;
import dev.emi.emi.screen.EmiScreenManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.checkerframework.checker.index.qual.IndexFor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.wallev.common.handler.emi.EmiHandler.*;
import static org.spongepowered.asm.mixin.injection.At.Shift.*;

@Mixin(value = EmiScreenManager.class, remap = false)
public class EmiScreenManagerMixin {

    @Shadow public static EmiPlayerInventory lastPlayerInventory;

    @Inject(at = @At(value = "INVOKE",
//    target = "Ldev/emi/emi/api/recipe/EmiPlayerInventory;getCraftables()Ljava/util/List;"), method = "updateCraftables")
//    target = "Ldev/emi/emi/api/recipe/EmiPlayerInventory;getCraftables()Ljava/util/List;", shift = At.Shift.AFTER), method = "updateCraftables")
    target = "Ldev/emi/emi/runtime/EmiFavorites;updateSynthetic(Ldev/emi/emi/api/recipe/EmiPlayerInventory;)V"), method = "updateCraftables")
    private static void updateCraftables$cb(CallbackInfo ci) {
        EmiHandler.updateCraftables(lastPlayerInventory);
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Ldev/emi/emi/screen/EmiScreenManager;forceRecalculate()V"), method = "addWidgets")
    private static void addWidgets$cb(Screen screen, CallbackInfo ci) {
        EmiHandler.updateTypeRecipes(screen);
    }


    private static void updateTypeRecipes(Screen screen) {

//        EmiSidebars.typeRecipes.clear();

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

//            EmiRecipeManager manager = EmiRecipes.manager;
//            List<EmiRecipeCategory> categories = manager.getCategories();
//            for (EmiRecipeCategory category : categories) {
//                if (Objects.equals(category.getId().toString(), s)) {
//                    EmiSidebars.typeRecipes = manager.getRecipes(category)
//                            .stream()
//                            .map(EmiFavorite.Craftable::new)
//                            .sorted((a, b) -> {
//                                int i = Integer.compare(
//                                        EmiStackList.getIndex(a.getStack()),
//                                        EmiStackList.getIndex(b.getStack()));
//                                if (i != 0) {
//                                    return i;
//                                }
//                                return Long.compare(a.getAmount(), b.getAmount());
//                            }).collect(Collectors.toList());
//                    return;
//                }
//            }
        }
    }

}
