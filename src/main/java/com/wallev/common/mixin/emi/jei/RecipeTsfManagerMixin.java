package com.wallev.common.mixin.emi.jei;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.wallev.common.handler.emi.EmiHandler;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.library.recipes.RecipeTransferManager;
import net.minecraft.world.inventory.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeTransferManager.class)
public class RecipeTsfManagerMixin {
    @Inject(at = @At("RETURN"), method = "<init>", remap = false)
    public void RecipeTsfManagerMixin(ImmutableTable<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> recipeTransferHandlers, CallbackInfo ci) {
        vanillaRecipeMapInit();
        pluginRecipeMapInit(recipeTransferHandlers);
    }
    @Unique
    private static void pluginRecipeMapInit(ImmutableTable<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> recipeTransferHandlers) {
        for (Table.Cell<Class<? extends AbstractContainerMenu>, RecipeType<?>, IRecipeTransferHandler<?, ?>> classRecipeTypeIRecipeTransferHandlerCell : recipeTransferHandlers.cellSet()) {
            Class<? extends AbstractContainerMenu> rowKey = classRecipeTypeIRecipeTransferHandlerCell.getRowKey();
            String name = rowKey.getName();
            RecipeType<?> columnKey = classRecipeTypeIRecipeTransferHandlerCell.getColumnKey();
            String string = columnKey.getUid().toString();
            EmiHandler.recipeTypeMap.put(name, string);
        }
    }
    @Unique
    private static void vanillaRecipeMapInit() {
        EmiHandler.recipeTypeMap.put(CraftingMenu.class.getName(), VanillaEmiRecipeCategories.CRAFTING.getId().toString());
        EmiHandler.recipeTypeMap.put(FurnaceMenu.class.getName(), VanillaEmiRecipeCategories.SMELTING.getId().toString());
        EmiHandler.recipeTypeMap.put(BlastFurnaceMenu.class.getName(), VanillaEmiRecipeCategories.BLASTING.getId().toString());
        EmiHandler.recipeTypeMap.put(SmokerMenu.class.getName(), VanillaEmiRecipeCategories.SMOKING.getId().toString());
        EmiHandler.recipeTypeMap.put(StonecutterMenu.class.getName(), VanillaEmiRecipeCategories.STONECUTTING.getId().toString());
        EmiHandler.recipeTypeMap.put(BrewingStandMenu.class.getName(), VanillaEmiRecipeCategories.BREWING.getId().toString());
//        EmiHandler.recipeTypeMap.put(SmithingMenu.class.getName(), VanillaEmiRecipeCategories.SMITHING.getId().toString());
//        EmiHandler.recipeTypeMap.put(AnvilMenu.class.getName(), VanillaEmiRecipeCategories.ANVIL_REPAIRING.getId().toString());
    }
}