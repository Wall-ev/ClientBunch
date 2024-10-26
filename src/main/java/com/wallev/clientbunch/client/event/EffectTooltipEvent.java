package com.wallev.clientbunch.client.event;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.IconType;
import com.wallev.clientbunch.inventory.tooltip.FoodEffectTooltipComponent;
import com.wallev.clientbunch.inventory.tooltip.IconTooltipComponent;
import com.wallev.clientbunch.inventory.tooltip.MobEffectTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, value = Dist.CLIENT)
public final class EffectTooltipEvent {
    private EffectTooltipEvent() {
    }

    @SubscribeEvent
    public static void addFoodEffectTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack itemStack = event.getItemStack();

        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();

        if (!itemStack.isEmpty()) {
//            Optional<FormattedText> left = tooltipElements.get(0).left();
//            if (left.isPresent() && left.get() instanceof MutableComponent mutableComponent) {
//                ResourceLocation resourceLocation = getResourceLocation(itemStack);
//                if (resourceLocation != null) {
//                    tooltipElements.set(0, Either.right(new IconTooltipComponent(resourceLocation, mutableComponent)));
//                }
//            }
        }

        HashMap<Integer, MutableComponent> integerFormattedTextHashMap = new HashMap<>();
        int i = 0;
        for (Either<FormattedText, TooltipComponent> tooltipElement : tooltipElements) {
            i++;
            if (tooltipElement.left().isPresent() && tooltipElement.left().get() instanceof MutableComponent mutableComponent) {
                String effectKey = getEffectKey(mutableComponent.toString());
                if (effectKey.isEmpty()) {
                    continue;
                }
                MobEffect effectMobEffect = getEffectMobEffect(effectKey);
                if (effectMobEffect == null) {
                    continue;
                }
                if (!ForgeRegistries.MOB_EFFECTS.containsValue(effectMobEffect)) {
                    continue;
                }
                integerFormattedTextHashMap.put(i - 1, mutableComponent);
            }
        }

        if (integerFormattedTextHashMap.isEmpty()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player == null) return;
            FoodProperties properties = itemStack.getFoodProperties(minecraft.player);
            if (properties == null) return;
            List<Pair<MobEffectInstance, Float>> effects = properties.getEffects();
            if (effects.isEmpty()) {
                return;
            }
            tooltipElements.add(Either.right(new FoodEffectTooltipComponent(effects)));
        } else {
            for (Map.Entry<Integer, MutableComponent> entry : integerFormattedTextHashMap.entrySet()) {
                MutableComponent value = entry.getValue();
                tooltipElements.set(entry.getKey(), Either.right(new MobEffectTooltipComponent(getEffectMobEffect(getEffectKey(value.toString())), value)));
            }
        }
    }

    @Nullable
    private static ResourceLocation getResourceLocation(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem armorItem && armorItem.getMaterial() instanceof ArmorMaterials armorMaterials) {
            String name = armorMaterials.getName();
            return IconType.getIconTypeRes(name);
        }
        return null;
    }

    @Nullable
    private static MobEffect getEffectMobEffect(String effect) {
        for (MobEffect mobEffect : ForgeRegistries.MOB_EFFECTS) {
            if (mobEffect.getDescriptionId().equals(effect)) {
                return mobEffect;
            }
        }
        return null;
    }

    private static String getEffectKey(String input) {
//        List<MatchResult> results = Pattern.compile("effect\\.(\\w+)\\.(\\w+)").matcher(input).results().toList();
        List<MatchResult> results = Pattern.compile("effect\\.(?!(\\w+)\\.(\\w+\\.desc))(\\w+)\\.(\\w+)").matcher(input).results().toList();
        return results.isEmpty() ? StringUtils.EMPTY : results.get(0).group();
    }
}
