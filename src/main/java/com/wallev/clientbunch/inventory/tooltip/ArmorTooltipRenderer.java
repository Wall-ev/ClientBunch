package com.wallev.clientbunch.inventory.tooltip;

import com.wallev.clientbunch.client.tooltip.ArmorTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.List;

public class ArmorTooltipRenderer implements ClientTooltipComponent {
    public static final int MARGIN = 6;
    public static final int DEFAULT_SIZE = 24;
    public static final int WIDTH = DEFAULT_SIZE;
    public static final int HEIGHT = DEFAULT_SIZE * 2;
    private final ArmorTooltipComponent component;
    private final ItemStack armorStack;
    private final int size;
    private final int compW;
    private final int compH;
    private final int offsetX;
    private final int offsetY;

    public ArmorTooltipRenderer(ArmorTooltipComponent component) {
        this.component = component;
        this.size = component.size();
        this.armorStack = component.stack();
        this.compW = component.compW();
        this.compH = component.compH();
        this.offsetX = component.offsetX();
        this.offsetY = component.offsetY();
    }

    private void renderEntityWithArmor(GuiGraphics graphics, int xPos, int yPos) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        EquipmentSlot slot = ((Equipable) armorStack.getItem()).getEquipmentSlot();
        List<ItemStack> armorInv = player.getInventory().armor;

        ItemStack oriArmorStack = player.getItemBySlot(slot);
        int slotId = slot.getIndex();

        double rot = ((System.currentTimeMillis() / 25.0) % 360);
        Quaternionf pose = (new Quaternionf()).rotateZ((float) Math.PI);
        Quaternionf rotation = (new Quaternionf()).rotateY((float) Math.toRadians(rot));
        pose.mul(rotation);

        armorInv.set(slotId, armorStack);
        InventoryScreen.renderEntityInInventory(graphics, xPos, yPos, size, pose, null, player);
        armorInv.set(slotId, oriArmorStack);
    }

    @Override
    public int getHeight() {
        return compH;
    }

    @Override
    public int getWidth(@NotNull Font pFont) {
        return compW;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        renderEntityWithArmor(pGuiGraphics, pX + offsetX, pY + offsetY);
    }
}
