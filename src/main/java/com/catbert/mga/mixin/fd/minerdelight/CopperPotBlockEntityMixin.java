package com.catbert.mga.mixin.fd.minerdelight;

import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import java.util.Optional;

@Mixin(CopperPotBlockEntity.class)
public abstract class CopperPotBlockEntityMixin {
    @Invoker(value = "canCook", remap = false)
    abstract boolean canCook$mga(CookingPotRecipe cookingPotRecipe);

    @Invoker(value = "getMatchingRecipe", remap = false)
    abstract Optional<CookingPotRecipe> getMatchingRecipe$mga(RecipeWrapper inventoryWrapper);
}
