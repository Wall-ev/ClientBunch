package com.catbert.mga.mixin.vanilla;

import com.catbert.mga.api.ICookingAccessor;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements ICookingAccessor {
    @Final
    @Accessor(value = "recipeType")
    public abstract RecipeType<? extends AbstractCookingRecipe> recipeType$mga();

    @Invoker(value = "isLit")
    public abstract boolean isLit$mga();
    @Override
    public boolean canCook$mga() {
        return false;
    }
}
