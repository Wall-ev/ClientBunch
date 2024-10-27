package com.wallev.clientbunch.client.tooltip;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record MobEffectTooltipComponent(MobEffect mobEffect, MutableComponent mutableComponent) implements TooltipComponent {

}
