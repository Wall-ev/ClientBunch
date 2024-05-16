package com.catbert.mga.mixin.fd.farmersdelight;

import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(CookingPotBlockEntity.class)
public abstract class CookingPotBlockEntityMixin{
    @Invoker(value = "canCook", remap = false)
    public abstract boolean canCook$mga(CookingPotRecipe cookingPotRecipe);

    @Invoker(value = "getMatchingRecipe", remap = false)
    public abstract Optional<CookingPotRecipe> getMatchingRecipe$mga(RecipeWrapper inventoryWrapper);
}
