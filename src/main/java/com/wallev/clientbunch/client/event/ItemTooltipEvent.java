package com.wallev.clientbunch.client.event;

import com.mojang.datafixers.util.Either;
import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.IconType;
import com.wallev.clientbunch.client.tooltip.ItemTooltipComponent;
import com.wallev.clientbunch.client.tooltip.LineTooltipComponent;
import com.wallev.clientbunch.handler.ItemType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, value = Dist.CLIENT)
public final class ItemTooltipEvent {
    private static boolean compare = false;
    private static ItemStack comparedStack = ItemStack.EMPTY;
    private static ItemStack lastRenderStack = ItemStack.EMPTY;
    private static long lastMillis = 0;

    private ItemTooltipEvent() {
    }

    private static void setLastRenderStack(ItemStack stack) {
        lastRenderStack = stack;
    }

    public static void setComparedStack(ItemStack stack) {
        comparedStack = stack;
    }

    @SubscribeEvent
    public static void addItemRendererTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack tooltipStack = event.getItemStack();

        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();

        if (tooltipStack.isEmpty()) return;

        Component stackName;
        if (tooltipElements.get(0).left().isPresent()) {
            stackName = (Component) tooltipElements.get(0).left().get();
        } else {
            stackName = tooltipStack.getHoverName();
        }

        float scale = 0;
        if (isCompare() && ItemStack.matches(comparedStack, tooltipStack)) {
            scale = 1.25f;
        } else {
            if (!ItemStack.matches(lastRenderStack, tooltipStack)) {
                setLastRenderStack(tooltipStack);
                lastMillis = System.currentTimeMillis();
            } else {
                float tooltipSeconds = (System.currentTimeMillis() - lastMillis) / 1000.0F;
                scale = tooltipSeconds < 0.25 ? (float) ((1.0F - Math.pow((1.0F - tooltipSeconds * 4.0F), 3.0)) * 1.5F) : (tooltipSeconds < 0.5 ? 1.5F - (1.0F - (float) Math.pow((1.0F - (tooltipSeconds - 0.25F) * 4.0F), 3.0)) * 0.25F : 1.25F);
            }
        }

        tooltipElements.set(0, Either.right(new ItemTooltipComponent(tooltipStack, stackName, getSecondName(tooltipStack), scale)));
    }

    /* LineTooltipComponent */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addLineComponentTooltip(RenderTooltipEvent.GatherComponents event) {
        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();
        if (tooltipElements.size() <= 1) return;
        Font font = Minecraft.getInstance().font;
        int width = 0;
        for (Either<FormattedText, TooltipComponent> tooltipElement : tooltipElements) {

            if (tooltipElement.left().isPresent()) {
                width = Math.max(width, font.width(tooltipElement.left().get()));
            }

            if (tooltipElement.right().isPresent()) {
                width = Math.max(width, ClientTooltipComponent.create(tooltipElement.right().get()).getWidth(font));
            }

        }

        tooltipElements.add(1, Either.right(new LineTooltipComponent(width)));
    }

    private static void parseComponent(MutableComponent original, MutableComponent... groupName) {
        for (MutableComponent mutableComponent : groupName) {
            if (!original.equals(Component.empty())) {
                original.append("、");
            }
            original.append(mutableComponent);
        }
    }

    private static Component getSecondName(ItemStack stack) {
        return ItemType.getItemGroupName(stack);
//        Minecraft mc = Minecraft.getInstance();
//
//        MutableComponent groupName = Component.empty();
//        Item item = stack.getItem();
//        if (item instanceof PotionItem) {
//            parseComponent(groupName, Component.literal("药水"));
//        }
//        if (item.getCraftingRemainingItem(stack).is(Items.GLASS_BOTTLE)) {
//            parseComponent(groupName, Component.literal("饮品"));
//        }
//        if (stack.getFoodProperties(mc.player) != null) {
//            parseComponent(groupName, Component.literal("食物"));
//        }
//
//        if (!groupName.equals(Component.empty())) {
//            return groupName.withStyle(ChatFormatting.BLUE);
//        } else {
//
//        }
//
//        return groupName;
    }

    public static boolean isCompare() {
        return compare;
    }

    public static void setCompare(boolean equipped) {
        ItemTooltipEvent.compare = equipped;
        if (!equipped && !comparedStack.isEmpty()) {
            comparedStack = ItemStack.EMPTY;
        }
    }
}

