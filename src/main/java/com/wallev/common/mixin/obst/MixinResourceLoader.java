package com.wallev.common.mixin.obst;

import com.obscuria.tooltips.client.ResourceLoader;
import com.wallev.common.handler.ObsTooltipRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLoader.class)
public class MixinResourceLoader {

    @Inject(method = "onResourceManagerReload", at = @At("HEAD"))
    private void clearObsTooltipRender(ResourceManager manager, CallbackInfo ci) {
        ObsTooltipRenderer.clear();
    }

}
