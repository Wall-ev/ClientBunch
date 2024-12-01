package com.wallev.clientbunch.client.tooltip;

import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public record ItemTooltipComponent(ItemStack stack, Component name, Component secondName, float scale, int height, Point point) implements TooltipComponent {
}
