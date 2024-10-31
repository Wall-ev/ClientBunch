package com.wallev.clientbunch.handler;

import com.wallev.clientbunch.client.event.ItemTooltipEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

public class EquityHandler {

    public static void renderTooltip(ContainerInfo containerInfo, GuiGraphics pGuiGraphics, int pX, int pY) {
        AbstractContainerScreen<?> gui = containerInfo.gui;
        Minecraft mc = containerInfo.mc;
        Font font = mc.font;
        ItemStack hoverStack = containerInfo.hoverSlot.getItem();
        ItemStack equitmentStack = mc.player.getItemBySlot(containerInfo.equippedSlot);

//        if (containerInfo.equippedSlot == EquipmentSlot.MAINHAND) return;

        int guiScaledWidth = mc.getWindow().getGuiScaledWidth();
        int guiScaledHeight = mc.getWindow().getGuiScaledHeight();

        List<Component> tooltipFromHoverItem = containerInfo.getTooltipFromContainerStack(hoverStack);
        Optional<TooltipComponent> hoverStackTooltipImage = hoverStack.getTooltipImage();
        int hoverStackWidth = 0, hoverStackHeight = 0;
        List<ClientTooltipComponent> hoverList = ForgeHooksClient.gatherTooltipComponents(hoverStack, tooltipFromHoverItem, hoverStackTooltipImage, pX, guiScaledWidth, guiScaledHeight, font);
        for (ClientTooltipComponent clientTooltipComponent : hoverList) {
            hoverStackWidth = Math.max(hoverStackWidth, clientTooltipComponent.getWidth(font));
            hoverStackHeight += clientTooltipComponent.getHeight();
        }

        List<Component> tooltipFromEquitmentItem = containerInfo.getTooltipFromContainerStack(equitmentStack);
        Optional<TooltipComponent> equitmentStackTooltipImage = equitmentStack.getTooltipImage();
        int equitmentStackWidth = 0, equitmentStackHeight = 0;
        List<ClientTooltipComponent> equipedList = ForgeHooksClient.gatherTooltipComponents(equitmentStack, tooltipFromEquitmentItem, equitmentStackTooltipImage, pX, guiScaledWidth, guiScaledHeight, font);
        for (ClientTooltipComponent clientTooltipComponent : equipedList) {
            equitmentStackWidth = Math.max(equitmentStackWidth, clientTooltipComponent.getWidth(font));
            equitmentStackHeight += clientTooltipComponent.getHeight();
        }
        int equipmentX = pX - equitmentStackWidth - 20;
        if (gui.width - hoverStackWidth - 20 <= pX) {
//            equipmentX -= hoverStackWidth;
        }

        pGuiGraphics.renderTooltip(font, tooltipFromEquitmentItem, equitmentStackTooltipImage, equitmentStack, equipmentX, pY);

        int textY = pY - 20;
        if (gui.height - equitmentStackHeight < pY) {
            textY = gui.height - equitmentStackHeight - 10;
        }
        Component text = Component.literal("Equipped").withStyle(ChatFormatting.YELLOW);
        if (font.width(text) < equitmentStackWidth) {
            int leftWidth = (equitmentStackWidth - font.width(text)) / 2;
            int amount = leftWidth / font.width(StringUtils.SPACE);
            text = Component.literal(StringUtils.SPACE.repeat(amount)).append(text).append(Component.literal(StringUtils.SPACE.repeat(amount)));
            pGuiGraphics.renderTooltip(font, text, pX - equitmentStackWidth - ((font.width(text) - equitmentStackWidth) / 2) - 20, textY);
        } else {
            pGuiGraphics.renderTooltip(font, text, pX - font.width(text) - ((equitmentStackWidth - font.width(text)) / 2) - 20, textY);
        }
    }

    public static boolean canCompareHandItem(ItemStack hoverStack, ItemStack comparedStack) {
        LocalPlayer player = Minecraft.getInstance().player;
        Item hoverItem = hoverStack.getItem();
        Item comparedItem = comparedStack.getItem();
        if (hoverItem.isDamageable(hoverStack) && comparedItem.isDamageable(comparedStack)) {
            return true;
        }
        if (hoverStack.getFoodProperties(player) != null && comparedStack.getFoodProperties(player) != null) {
            return true;
        }
        return false;
    }

    public static abstract class ContainerInfo {

        private final AbstractContainerScreen<?> gui;
        private final Minecraft mc;
        private final Slot hoverSlot;
        private final EquipmentSlot equippedSlot;

        protected ContainerInfo(AbstractContainerScreen<?> gui, Minecraft mc, Slot hoverSlot, EquipmentSlot equippedSlot) {
            this.gui = gui;
            this.mc = mc;
            this.hoverSlot = hoverSlot;
            this.equippedSlot = equippedSlot;
        }

        public AbstractContainerScreen<?> getGui() {
            return gui;
        }

        public Minecraft getMc() {
            return mc;
        }

        public Slot getHoverSlot() {
            return hoverSlot;
        }

        public EquipmentSlot getEquippedSlot() {
            return equippedSlot;
        }

        public abstract List<Component> getTooltipFromContainerStack(ItemStack stack);
    }

}
