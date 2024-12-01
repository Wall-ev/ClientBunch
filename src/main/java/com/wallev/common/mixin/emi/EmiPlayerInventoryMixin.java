package com.wallev.common.mixin.emi;

import com.wallev.common.handler.emi.EmiHandler;
import dev.emi.emi.api.recipe.EmiPlayerInventory;
import dev.emi.emi.api.stack.EmiIngredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = EmiPlayerInventory.class, remap = false)
public class EmiPlayerInventoryMixin {

    @Inject(at = @At("RETURN"), method = "getCraftables", cancellable = true)
    private void getCraftablesFromOther(CallbackInfoReturnable<List<EmiIngredient>> cir) {
        List<EmiIngredient> returnValue = cir.getReturnValue();
        if (returnValue.isEmpty()) {
            cir.setReturnValue(EmiHandler.getCraftablesFromOther((EmiPlayerInventory)(Object) this));
        }
    }

}
