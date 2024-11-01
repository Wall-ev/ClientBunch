package com.wallev.clientbunch.api.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class GetterItemTypeNameEvent extends Event {

    private final ItemStack itemStack;

    public GetterItemTypeNameEvent(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Cancelable
    public static class Pre extends GetterItemTypeNameEvent {

        private Component groupName;

        public Pre(ItemStack itemStack) {
            super(itemStack);
        }

        public Component getGroupName() {
            return groupName;
        }

        public void setGroupName(Component groupName) {
            this.setCanceled(true);
            this.groupName = groupName;
        }
    }

    @Cancelable
    public static class ItemType extends GetterItemTypeNameEvent {

        private com.wallev.clientbunch.handler.ItemType itemType;

        public ItemType(ItemStack itemStack) {
            super(itemStack);
        }

        public com.wallev.clientbunch.handler.ItemType getItemType() {
            return itemType;
        }

        public void setItemType(com.wallev.clientbunch.handler.ItemType itemType) {
            this.setCanceled(true);
            this.itemType = itemType;
        }
    }

    @Cancelable
    public static class ItemGroupName extends GetterItemTypeNameEvent {

        private Component groupName;

        public ItemGroupName(ItemStack itemStack) {
            super(itemStack);
        }

        public Component getGroupName() {
            return groupName;
        }

        public void setGroupName(Component groupName) {
            this.setCanceled(true);
            this.groupName = groupName;
        }
    }

}
