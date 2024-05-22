package com.catbert.mga.api;

import net.mehvahdjukaar.sawmill.FilterableRecipe;
import net.minecraft.world.inventory.DataSlot;

import java.util.List;

public interface SawmillMenuAccessor {

    DataSlot getSelectedRecipeIndex$mga();

    List<FilterableRecipe> getRecipes$mga();

    FilterableRecipe getLastSelectedRecipe$mga();

    void setRecipes$mga(List<FilterableRecipe> _recipes);

    void setLastSelectedRecipe$mga(FilterableRecipe _recipe);

    void setSelectedRecipeIndex$mga(int _index);
}
