package com.wallev.clientbunch.client;

import com.wallev.clientbunch.ClientBunch;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Locale;

public enum IconType {
    ARMOR_CHAINMAIL(Type.ARMOR, "chainmail"),
    ARMOR_DIAMOND(Type.ARMOR, "diamond"),
    ARMOR_GOLD(Type.ARMOR, "gold"),
    ARMOR_GREEN(Type.ARMOR, "green"),
    ARMOR_IRON(Type.ARMOR, "iron"),
    ARMOR_LEATHER(Type.ARMOR, "iron"),
    ARMOR_NETHERITE(Type.ARMOR, "netherite"),

    DRINK_DRINK(Type.DRINK, "drink"),

    FOOD_EPIC(Type.FOOD, "epic"),
    FOOD_FOOD(Type.FOOD, "food"),
    FOOD_RARE(Type.FOOD, "rare"),
    ;

    public final ResourceLocation resourceLocation;
    public final String s;

    IconType(Type type, String s) {
        this.s = s;
        this.resourceLocation = new ResourceLocation(ClientBunch.MOD_ID, String.format("textures/badges/%s/%s.png", type.toString().toLowerCase(Locale.ROOT), s));
    }

    @Nullable
    public static IconType getIconType(String s) {
        for (IconType value : values()) {
            if (value.s.equals(s)) {
                return value;
            }
        }
        return null;
    }

    @Nullable
    public static ResourceLocation getIconTypeRes(String s) {
        IconType iconType = getIconType(s);
        if (iconType != null) {
            return iconType.resourceLocation;
        }
        return null;
    }

    enum Type {
        ARMOR,
        DRINK,
        FOOD,
        MATERIAL,
        SHERD,
        TRIM,
        UTILITY,
        WEAPON,
        ;
    }
}
