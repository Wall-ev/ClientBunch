package com.wallev.clientbunch.compat.l2screentracker;

import dev.xkmc.l2screentracker.click.quickaccess.QuickAccessAction;
import dev.xkmc.l2screentracker.click.quickaccess.QuickAccessClickHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class CustomQuickAccessAction implements QuickAccessAction {
    private BiConsumer<ServerPlayer, ItemStack> perform;
    @Override
    public void perform(@NotNull ServerPlayer serverPlayer, @NotNull ItemStack itemStack) {
        this.perform.accept(serverPlayer, itemStack);
    }

    public void register(Item item) {
        QuickAccessClickHandler.register(item, this);
    }

    public static class Builder {
        private final CustomQuickAccessAction action = new CustomQuickAccessAction();
        public Builder setPerform(BiConsumer<ServerPlayer, ItemStack> perform) {
            this.action.perform = perform;
            return this;
        }
        public CustomQuickAccessAction build() {
            return this.action;
        }
    }


}
