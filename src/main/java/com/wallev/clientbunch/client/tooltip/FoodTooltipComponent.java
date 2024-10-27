package com.wallev.clientbunch.client.tooltip;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import squeek.appleskin.api.food.FoodValues;

public class FoodTooltipComponent implements TooltipComponent {
    private FoodValues defaultFood;
    private FoodValues modifiedFood;

    private int biggestHunger;
    private float biggestSaturationIncrement;

    private int hungerBars;
    private String hungerBarsText;

    private int saturationBars;
    private String saturationBarsText;

    private ItemStack itemStack;

    public FoodTooltipComponent(ItemStack itemStack, FoodValues defaultFood, FoodValues modifiedFood, Player player) {
        this.itemStack = itemStack;
        this.defaultFood = defaultFood;
        this.modifiedFood = modifiedFood;

        biggestHunger = Math.max(defaultFood.hunger, modifiedFood.hunger);
        biggestSaturationIncrement = Math.max(defaultFood.getSaturationIncrement(), modifiedFood.getSaturationIncrement());

        hungerBars = (int) Math.ceil(Math.abs(biggestHunger) / 2f);
//        if (hungerBars > 10) {
            hungerBarsText = "x" + ((biggestHunger < 0 ? -1 : 1) * hungerBars);
            hungerBars = 1;
//        }

        saturationBars = (int) Math.ceil(Math.abs(biggestSaturationIncrement) / 2f);
//        if (saturationBars > 10 || saturationBars == 0) {
            saturationBarsText = "x" + ((biggestSaturationIncrement < 0 ? -1 : 1) * saturationBars);
            saturationBars = 1;
//        }
    }

    public boolean shouldRenderHungerBars() {
        return hungerBars > 0;
    }

    public FoodValues getDefaultFood() {
        return defaultFood;
    }

    public FoodValues getModifiedFood() {
        return modifiedFood;
    }

    public int getBiggestHunger() {
        return biggestHunger;
    }

    public float getBiggestSaturationIncrement() {
        return biggestSaturationIncrement;
    }

    public int getHungerBars() {
        return hungerBars;
    }

    public String getHungerBarsText() {
        return hungerBarsText;
    }

    public int getSaturationBars() {
        return saturationBars;
    }

    public String getSaturationBarsText() {
        return saturationBarsText;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}