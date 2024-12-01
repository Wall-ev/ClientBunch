package com.wallev.common.handler.obst;

import com.obscuria.tooltips.client.renderer.TooltipContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class ObTooltipRender {

    public static void render(TooltipContext renderer, ItemStack stack, Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
//        ObsTooltipRenderer.render(renderer, stack, font, components, x, y, positioner);
    }

}
