package com.catbert.mga.mixin.other.sawmill;


import com.catbert.mga.api.SawmillMenuAccessor;
import net.mehvahdjukaar.sawmill.FilterableRecipe;
import net.mehvahdjukaar.sawmill.SawmillMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SawmillMenu.class)
public abstract class SawmillMenuMixin implements SawmillMenuAccessor {

    @Shadow(remap = false)
    private List<FilterableRecipe> recipes;

    @Shadow(remap = false)
    private FilterableRecipe lastSelectedRecipe;

    @Shadow(remap = false)
    @Final
    private DataSlot selectedRecipeIndex;

    @Override
    public DataSlot getSelectedRecipeIndex$mga() {
        return selectedRecipeIndex;
    }

    @Override
    public List<FilterableRecipe> getRecipes$mga() {
        return recipes;
    }

    @Override
    public FilterableRecipe getLastSelectedRecipe$mga() {
        return lastSelectedRecipe;
    }

    @Override
    public void setRecipes$mga(List<FilterableRecipe> _recipes) {
        recipes = _recipes;
    }

    @Override
    public void setLastSelectedRecipe$mga(FilterableRecipe _recipe) {
        lastSelectedRecipe = _recipe;
    }

    @Override
    public void setSelectedRecipeIndex$mga(int _index) {
        selectedRecipeIndex.set(_index);
    }
}
