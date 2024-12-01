package com.wallev.common.mixin.obst;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.renderer.TooltipRenderer;
import com.wallev.common.handler.obst.ObTooltipRender;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TooltipRenderer.class)
public class MixinTooltipRenderer {

    @Inject(at = @At("HEAD"), method = "render", remap = false, cancellable = true)
    private static void render$cb(TooltipContext renderer, ItemStack stack, Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfoReturnable<Boolean> cir) {
        ObTooltipRender.render(renderer, stack, font, components, x, y, positioner, cir);
    }


}
