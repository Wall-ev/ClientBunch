package com.wallev.clientbunch.compat.cloth;

import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.config.subconfig.RenderConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class MenuIntegration {
    private static final Component MENU_TITLE = Component.translatable("config.client_bunch.title");
    private static final Component MENU_TITLE_TIP = Component.translatable("config.client_bunch.title.tip").withStyle(ChatFormatting.YELLOW);
    private static final String MOD_TIP = "[Client Bunch]";
    public static ConfigBuilder getConfigBuilder() {
        ConfigBuilder root = ConfigBuilder.create().setTitle(MENU_TITLE);
        root.setGlobalized(true);
        root.setGlobalizedExpanded(false);
        return getConfigBuilder(root, false);
    }

    public static ConfigBuilder getConfigBuilder(ConfigBuilder root, boolean tlmEntry) {
        addConfig(root, root.entryBuilder(), tlmEntry);
        return root;
    }

    public static void addConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        renderConfig(root, entryBuilder, tlmEntry);
    }
    
    private static void renderConfig(ConfigBuilder root, ConfigEntryBuilder entryBuilder, boolean tlmEntry) {
        MutableComponent entryTitle = Component.translatable("config.client_bunch.render");
        MutableComponent addition = Component.literal("");
        if (tlmEntry) {
            entryTitle.append(MENU_TITLE_TIP);
            addition.append(Component.literal("\n" + MOD_TIP).withStyle(ChatFormatting.BLUE))
                    .append(Component.literal("\nModId: " + ClientBunch.MOD_ID).withStyle(ChatFormatting.DARK_GRAY));
        }
        ConfigCategory render = root.getOrCreateCategory(entryTitle);

        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.client_bunch.render.food_effect"), RenderConfig.FOOD_EFFECT.get())
                .setDefaultValue(RenderConfig.FOOD_EFFECT.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.food_effect.tooltip"), addition)
                .setSaveConsumer(RenderConfig.FOOD_EFFECT::set).build());
        render.addEntry(entryBuilder.startStrList(Component.translatable("config.client_bunch.render.food_effect_icon_black_regex"), RenderConfig.FOOD_EFFECT_ICON_BLACK_REGEX.get())
                .setDefaultValue(RenderConfig.FOOD_EFFECT_ICON_BLACK_REGEX.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.food_effect_icon_black_regex.tooltip"), addition)
                .setSaveConsumer(RenderConfig.FOOD_EFFECT_ICON_BLACK_REGEX::set).build());
        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.client_bunch.render.food_value"), RenderConfig.FOOD_VALUE.get())
                .setDefaultValue(RenderConfig.FOOD_VALUE.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.food_value.tooltip"), addition)
                .setSaveConsumer(RenderConfig.FOOD_VALUE::set).build());
        render.addEntry(entryBuilder.startEnumSelector(Component.translatable("config.client_bunch.render.item_icon_type"), RenderConfig.ItemIconType.class, RenderConfig.ITEM_ICON_TYPE.get())
                .setDefaultValue(RenderConfig.ITEM_ICON_TYPE.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.item_icon_type.tooltip"), addition)
                .setSaveConsumer(RenderConfig.ITEM_ICON_TYPE::set).build());
        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.client_bunch.render.armor"), RenderConfig.ARMOR.get())
                .setDefaultValue(RenderConfig.ARMOR.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.armor.tooltip"), addition)
                .setSaveConsumer(RenderConfig.ARMOR::set).build());
        render.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.client_bunch.render.auto_item_icon_type"), RenderConfig.AUTO_ITEM_ICON_TYPE.get())
                .setDefaultValue(RenderConfig.AUTO_ITEM_ICON_TYPE.getDefault()).setTooltip(Component.translatable("config.client_bunch.render.auto_item_icon_type.tooltip"), addition)
                .setSaveConsumer(RenderConfig.AUTO_ITEM_ICON_TYPE::set).build());

    }

    public static void registerModsPage() {
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
                new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> getConfigBuilder().setParentScreen(parent).build()));
    }
}
