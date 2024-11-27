package com.wallev.clientbunch.compat.l2screentracker;

import com.wallev.clientbunch.ClientBunch;
import dev.xkmc.l2screentracker.click.ReadOnlyStackClickHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class CustomReadOnlyStackClickHandler extends ReadOnlyStackClickHandler {
    private final BiConsumer<ServerPlayer, ItemStack> handler;
    private final Predicate<ItemStack> isAllowed;
    protected CustomReadOnlyStackClickHandler(BiConsumer<ServerPlayer, ItemStack> handler, Predicate<ItemStack> isAllowed) {
        super(new ResourceLocation(ClientBunch.MOD_ID, "custom_handler"));
        this.handler = handler;
        this.isAllowed = isAllowed;
    }

    @Override
    protected void handle(ServerPlayer serverPlayer, ItemStack itemStack) {
        this.handler.accept(serverPlayer, itemStack);
    }

    @Override
    public boolean isAllowed(ItemStack itemStack) {
        return this.isAllowed.test(itemStack);
    }

    public static class Builder {
        private BiConsumer<ServerPlayer, ItemStack> handler;
        private Predicate<ItemStack> isAllowed;

        public Builder setHandler(BiConsumer<ServerPlayer, ItemStack> handler) {
            this.handler = handler;
            return this;
        }

        public Builder setIsAllowed(Predicate<ItemStack> isAllowed) {
            this.isAllowed = isAllowed;
            return this;
        }

        public CustomReadOnlyStackClickHandler build() {
            return new CustomReadOnlyStackClickHandler(handler, isAllowed);
        }
    }
}
