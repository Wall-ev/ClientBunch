package com.catbert.mga.mixin.letsdo.vinery;

import com.catbert.mga.api.ICookingAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import satisfyu.vinery.block.entity.FermentationBarrelBlockEntity;

@Mixin(FermentationBarrelBlockEntity.class)
public abstract class FermentationBarrelBlockEntityMixin implements ICookingAccessor {

    @Accessor(value = "fermentationTime", remap = false)
    abstract int fermentationTimeMGA();
    @Override
    public boolean canCook$mga() {
        return this.fermentationTimeMGA() == 0;
    }
}
