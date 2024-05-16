package com.catbert.mga.mixin.other.drinkbeer;

import com.catbert.mga.api.ICookingAccessor;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeerBarrelBlockEntity.class)
public abstract class BeerBarrelBlockEntityMixin implements ICookingAccessor {

    @Accessor(value = "remainingBrewTime", remap = false)
    abstract int remainingBrewTime$mga();

    @Accessor(value = "statusCode", remap = false)
    abstract int statusCode$mga();

    @Final
    @Accessor(value = "handler", remap = false)
    abstract LazyOptional<IItemHandler> handler$mga();

    @Final
    @Accessor(value = "brewingInventory", remap = false)
    abstract BeerBarrelBlockEntity.BrewingInventory brewingInventory$mga();

    @Invoker(value = "clearResult", remap = false)
    abstract void clearResult$mga();

    @Override
    public boolean canCook$mga() {
        return statusCode$mga() != 1 && remainingBrewTime$mga() == 0;
    }
}
