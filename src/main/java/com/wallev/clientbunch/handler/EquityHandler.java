package com.wallev.clientbunch.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class EquityHandler {

    public static void renderTooltip(AbstractContainerScreen<?> gui, GuiGraphics pGuiGraphics, int pX, int pY) {

//        if (!Minecraft.getInstance().options.keyShift.isDown()) return;
//        if (!(gui.getMenu().getCarried().isEmpty() && gui.hoveredSlot != null && this.hoveredSlot.hasItem())) return;
//
//        ItemStack hoverStack = this.hoveredSlot.getItem();
//        Item hoverItem = hoverStack.getItem();
//
//        // If this is a piece of equipment and we are already wearing the same type, display an additional tooltip as well.
//        EquipmentSlot slot = Mob.getEquipmentSlotForItem(hoverStack);
//
//        if (!(hoverItem instanceof DiggerItem)) return;
    }

}
