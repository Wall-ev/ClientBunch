package com.wallev.clientbunch.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public class RenderConfig {
    public static ForgeConfigSpec.BooleanValue FOOD_EFFECT;
    public static ForgeConfigSpec.BooleanValue FOOD_VALUE;
    public static ForgeConfigSpec.EnumValue<ItemIconType> ITEM_ICON_TYPE;
    public static ForgeConfigSpec.BooleanValue ARMOR;
    public static ForgeConfigSpec.BooleanValue AUTO_ITEM_ICON_TYPE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("Render");

        builder.comment("Food effect tooltip render.");
        FOOD_EFFECT = builder.define("FoodEffect", true);
        builder.comment("Food values tooltip render.");
        FOOD_VALUE = builder.define("FoodValue", true);
        builder.comment("Item icon type tooltip render.");
        ITEM_ICON_TYPE = builder.defineEnum("ItemIconType", ItemIconType.NORMAL);
        builder.comment("Armor tooltip render.");
        ARMOR = builder.define("Armor", true);
        builder.comment("Auto define item_icon_type tooltip.");
        AUTO_ITEM_ICON_TYPE = builder.define("AutoItemIconType", true);

        builder.pop();
    }

    public enum ItemIconType {
        LEGENDARY,
        MODERN_UI,
        OBSCURE,
        NORMAL
        ;
    }
}
