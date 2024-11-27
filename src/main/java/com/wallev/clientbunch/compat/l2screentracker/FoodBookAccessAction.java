package com.wallev.clientbunch.compat.l2screentracker;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import com.cazsius.solcarrot.item.SOLCarrotItems;
import com.wallev.clientbunch.ClientBunch;
import dev.xkmc.l2screentracker.click.ReadOnlyStackClickHandler;
import dev.xkmc.l2screentracker.init.L2STLangData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;

import java.util.Objects;

public class FoodBookAccessAction extends ReadOnlyStackClickHandler {
    public FoodBookAccessAction() {
        super(new ResourceLocation(ClientBunch.MOD_ID, "food_book"));
    }

    @Override
    protected void handle(ServerPlayer serverPlayer, ItemStack itemStack) {
    }

    @Override
    public boolean isAllowed(ItemStack itemStack) {

        if (ModList.get().isLoaded("solcarrot") && itemStack.is(SOLCarrotItems.FOOD_BOOK.get())) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isLocalPlayer()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                    return () -> {
                        FoodBookScreen.open(player);
                    };
                });
            }
            return true;
        } else if (ModList.get().isLoaded("solapplepie") && Objects.equals(itemStack.getItem().getCreatorModId(itemStack), "solapplepie:food_book")) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.isLocalPlayer()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
                    return () -> {
                        com.tarinoita.solsweetpotato.client.gui.FoodBookScreen.open(Minecraft.getInstance().player);
                    };
                });
            }

            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void tooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if ((ModList.get().isLoaded("solcarrot") && itemStack.is(SOLCarrotItems.FOOD_BOOK.get())) ||
            (ModList.get().isLoaded("solapplepie") && Objects.equals(itemStack.getItem().getCreatorModId(itemStack), "solapplepie:food_book"))) {
            event.getToolTip().add(L2STLangData.QUICK_ACCESS.get().withStyle(ChatFormatting.GRAY));
        }
    }
}
