package com.wallev.clientbunch.client.tooltip;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record IconTooltipComponent(ResourceLocation resourceLocation, MutableComponent mutableComponent) implements TooltipComponent {

}
