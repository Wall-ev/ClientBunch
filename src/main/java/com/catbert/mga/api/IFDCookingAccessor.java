package com.catbert.mga.api;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public interface IFDCookingAccessor<T extends Recipe<RecipeWrapper>> {

    boolean canCook$mga(T t);

    Optional<T> getMatchingRecipe$mga(RecipeWrapper inventoryWrapper);

}
