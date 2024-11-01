package com.wallev.clientbunch.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public record ArmorTooltipComponent(ItemStack stack, int size, int compW, int compH, int offsetX, int offsetY) implements TooltipComponent {
}
