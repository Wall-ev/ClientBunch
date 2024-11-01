package com.wallev.clientbunch.handler;

import com.google.common.collect.Multimap;
import com.wallev.clientbunch.api.client.GetterItemTypeNameEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.extensions.IForgeMenuType;
import vectorwing.farmersdelight.common.tag.ForgeTags;

public enum ItemType {
    TOOLS_AND_UTILITIES(CreativeModeTabs.TOOLS_AND_UTILITIES),
    FUNCTIONAL_BLOCKS(CreativeModeTabs.FUNCTIONAL_BLOCKS),
    FOOD_AND_DRINKS(CreativeModeTabs.FOOD_AND_DRINKS),
    BUILDING_BLOCKS(CreativeModeTabs.BUILDING_BLOCKS),
    REDSTONE_BLOCKS(CreativeModeTabs.REDSTONE_BLOCKS),
    NATURAL_BLOCKS(CreativeModeTabs.NATURAL_BLOCKS),
    INGREDIENTS(CreativeModeTabs.INGREDIENTS),
    COMBAT(CreativeModeTabs.COMBAT);
    private static boolean rebuildTabs = false;
    private final Component groupName;

    {
//        public static final ResourceKey<CreativeModeTab> BUILDING_BLOCKS = createKey("building_blocks");
//        public static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = createKey("colored_blocks");
//        public static final ResourceKey<CreativeModeTab> NATURAL_BLOCKS = createKey("natural_blocks");
//        public static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = createKey("functional_blocks");
//        public static final ResourceKey<CreativeModeTab> REDSTONE_BLOCKS = createKey("redstone_blocks");
//        public static final ResourceKey<CreativeModeTab> HOTBAR = createKey("hotbar");
//        public static final ResourceKey<CreativeModeTab> SEARCH = createKey("search");
//        public static final ResourceKey<CreativeModeTab> TOOLS_AND_UTILITIES = createKey("tools_and_utilities");
//        public static final ResourceKey<CreativeModeTab> COMBAT = createKey("combat");
//        public static final ResourceKey<CreativeModeTab> FOOD_AND_DRINKS = createKey("food_and_drinks");
//        public static final ResourceKey<CreativeModeTab> INGREDIENTS = createKey("ingredients");
//        public static final ResourceKey<CreativeModeTab> SPAWN_EGGS = createKey("spawn_eggs");
//        public static final ResourceKey<CreativeModeTab> OP_BLOCKS = createKey("op_blocks");
//        public static final ResourceKey<CreativeModeTab> INVENTORY = createKey("inventory");
    }

    ItemType(ResourceKey<CreativeModeTab> tabResourceKey) {
        this(getCreativeModeTab(tabResourceKey).getDisplayName());
    }

    ItemType(Component groupName) {
        this.groupName = groupName;
    }

    public static ItemType seriousStackType(ItemStack stack) {
        Item item = stack.getItem();
        if (stack.getFoodProperties(null) != null) {
            return FOOD_AND_DRINKS;
        }
        if (item instanceof PotionItem) {
            return FOOD_AND_DRINKS;
        }


        if (item instanceof Equipable) {
            return COMBAT;
        }
        if (item instanceof ArrowItem) {
            return COMBAT;
        }
        if (item instanceof ProjectileWeaponItem) {
            return COMBAT;
        }
        if (item instanceof Vanishable) {
            EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(stack);
            Multimap<Attribute, AttributeModifier> multimap = stack.getAttributeModifiers(equipmentSlot);
            if (multimap.containsKey(Attributes.ATTACK_DAMAGE)) {
                return COMBAT;
            }
        }


        if (item instanceof DiggerItem) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof BucketItem) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof BoatItem) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof BaseRailBlock) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof MinecartItem) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof RecordItem) {
            return TOOLS_AND_UTILITIES;
        }
        if (item instanceof TieredItem) {
            return TOOLS_AND_UTILITIES;
        }



        if(hasTag(stack, Tags.Items.INGOTS, Tags.Items.RAW_MATERIALS, Tags.Items.GEMS, Tags.Items.NUGGETS)) {
            return INGREDIENTS;
        }
        if(hasTag(stack, Tags.Items.INGOTS)) {
            return INGREDIENTS;
        }
        if (item instanceof EnchantedBookItem) {
            return INGREDIENTS;
        }
        if (item instanceof SmithingTemplateItem) {
            return INGREDIENTS;
        }
        if (hasTag(stack, ItemTags.DECORATED_POT_INGREDIENTS)) {
            return INGREDIENTS;
        }
        if (item instanceof BannerItem) {
            return INGREDIENTS;
        }


        if (hasTag(stack, Tags.Items.ORES, Tags.Items.STORAGE_BLOCKS)) {
            return NATURAL_BLOCKS;
        }
        if (hasTag(stack, ItemTags.LOGS, ItemTags.LEAVES)) {
            return NATURAL_BLOCKS;
        }
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof IPlantable) {
            return NATURAL_BLOCKS;
        }


        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof CraftingTableBlock || block instanceof StonecutterBlock || block instanceof CartographyTableBlock
                    || block instanceof FletchingTableBlock || block instanceof SmithingTableBlock || block instanceof GrindstoneBlock
                    || block instanceof LoomBlock || block instanceof AbstractFurnaceBlock || block instanceof AnvilBlock
                    || block instanceof JukeboxBlock || block instanceof EnchantmentTableBlock || block instanceof BrewingStandBlock) {
                return FUNCTIONAL_BLOCKS;
            }
            if (block instanceof BaseEntityBlock) {
                return FUNCTIONAL_BLOCKS;
            }
        }


        if (item instanceof BlockItem) {
//            return BUILDING_BLOCKS;
        }


        GetterItemTypeNameEvent.ItemType itemType = new GetterItemTypeNameEvent.ItemType(stack);
        if (MinecraftForge.EVENT_BUS.post(itemType)) {
            return itemType.getItemType();
        }

        return null;
    }

    public static boolean hasTag(ItemStack stack, TagKey<Item> tagKeyA) {
        return stack.getTags().anyMatch(tagKey -> tagKey == tagKeyA);
    }

    @SafeVarargs
    public static boolean hasTag(ItemStack stack, TagKey<Item>... tagKeyA) {
        for (TagKey<Item> itemTagKey : stack.getTags().toList()) {
            for (TagKey<Item> tagKey : tagKeyA) {
                if (itemTagKey == tagKey) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Component getItemGroupName(ItemStack stack) {
        GetterItemTypeNameEvent.Pre pre = new GetterItemTypeNameEvent.Pre(stack);
        if (MinecraftForge.EVENT_BUS.post(pre)) {
            return pre.getGroupName();
        }

        ItemType itemType = seriousStackType(stack);
        if (itemType != null) {
            return itemType.getGroupName();
        }

        GetterItemTypeNameEvent.ItemGroupName itemGroupName = new GetterItemTypeNameEvent.ItemGroupName(stack);
        if (MinecraftForge.EVENT_BUS.post(itemGroupName)) {
            return itemGroupName.getGroupName();
        }

        return getTabsTypeName(stack);
    }

    private static CreativeModeTab getCreativeModeTab(ResourceKey<CreativeModeTab> tabResourceKey) {
        return BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(tabResourceKey);
    }

    public static Component getTabsTypeName(ItemStack stack) {
        rebuildTabs();

        Component groupName = Component.empty();
        Item item = stack.getItem();
        for (CreativeModeTab creativemodetab : BuiltInRegistries.CREATIVE_MODE_TAB.stream().toList()) {
            if (!creativemodetab.hasSearchBar() && creativemodetab.getDisplayItems().stream().anyMatch(stack1 -> stack1.is(item))) {
                groupName = creativemodetab.getDisplayName().copy().withStyle(ChatFormatting.BLUE);
                break;
            }
        }

        return groupName;
    }

    private static void rebuildTabs() {
        if (!rebuildTabs) {
            rebuildTabs = rebuildTabContents();
        }
    }

    private static boolean rebuildTabContents() {
        Minecraft mc = Minecraft.getInstance();
        Boolean b = mc.options.operatorItemsTab().get();
        LocalPlayer player = mc.player;
        if (player == null) return false;
        boolean hasPermissions = player.canUseGameMasterBlocks() && b;
        return CreativeModeTabs.tryRebuildTabContents(player.connection.enabledFeatures(), hasPermissions, player.level().registryAccess());
    }

    public Component getGroupName() {
        return groupName;
    }

}
