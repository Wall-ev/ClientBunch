package com.wallev.clientbunch.client.event;

import com.mojang.datafixers.util.Either;
import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.tooltip.FoodTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import squeek.appleskin.ModConfig;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.event.TooltipOverlayEvent;
import squeek.appleskin.api.food.FoodValues;
import squeek.appleskin.helpers.FoodHelper;
import squeek.appleskin.helpers.KeyHelper;

import java.util.List;

//https://github.com/squeek502/AppleSkin/blob/07dc6b6db4a98626617ceada5e87072653e6ae3f/java/squeek/appleskin/client/TooltipOverlayHandler.java#L288
public final class FoodTooltipEvent {
    @SubscribeEvent
    public void gatherTooltips(RenderTooltipEvent.GatherComponents event) {
        if (event.isCanceled())
            return;

        ItemStack hoveredStack = event.getItemStack();
        Minecraft mc = Minecraft.getInstance();
        if (!shouldShowTooltip(hoveredStack, mc.player))
            return;

        FoodValues defaultFood = FoodHelper.getDefaultFoodValues(hoveredStack, mc.player);
        FoodValues modifiedFood = FoodHelper.getModifiedFoodValues(hoveredStack, mc.player);

        FoodValuesEvent foodValuesEvent = new FoodValuesEvent(mc.player, hoveredStack, defaultFood, modifiedFood);
        MinecraftForge.EVENT_BUS.post(foodValuesEvent);
        defaultFood = foodValuesEvent.defaultFoodValues;
        modifiedFood = foodValuesEvent.modifiedFoodValues;

        // Notify everyone that we should render tooltip overlay
        TooltipOverlayEvent.Pre prerenderEvent = new TooltipOverlayEvent.Pre(hoveredStack, defaultFood, modifiedFood);
        MinecraftForge.EVENT_BUS.post(prerenderEvent);
        if (prerenderEvent.isCanceled())
            return;


        FoodTooltipComponent foodTooltip = new FoodTooltipComponent(prerenderEvent.itemStack, defaultFood, modifiedFood, mc.player);
        if (foodTooltip.shouldRenderHungerBars()) {
            List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();

            if (tooltipElements.size() > 1) {
                tooltipElements.add(1, Either.right(foodTooltip));
            } else {
                tooltipElements.add(Either.right(foodTooltip));
            }

        }

    }

    private static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
        if (hoveredStack.isEmpty())
            return false;

        if (!FoodHelper.isFood(hoveredStack, player))
            return false;

        return true;
    }
}
