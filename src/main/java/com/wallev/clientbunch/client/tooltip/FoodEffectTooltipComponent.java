package com.wallev.clientbunch.client.tooltip;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public record FoodEffectTooltipComponent(List<Pair<MobEffectInstance, Float>> effects) implements TooltipComponent {

}
