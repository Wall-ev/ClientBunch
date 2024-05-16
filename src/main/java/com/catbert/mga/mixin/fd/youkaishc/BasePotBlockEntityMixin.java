package com.catbert.mga.mixin.fd.youkaishc;

import dev.xkmc.youkaishomecoming.content.pot.base.BasePotBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(BasePotBlockEntity.class)
public abstract class BasePotBlockEntityMixin {
    @Invoker(value = "canCook", remap = false)
    abstract boolean canCook$mga(BasePotRecipe basePotRecipe);

    @Invoker(value = "getMatchingRecipe", remap = false)
    abstract Optional<BasePotRecipe> getMatchingRecipe$mga(RecipeWrapper inventoryWrapper);
}
