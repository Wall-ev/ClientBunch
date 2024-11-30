package com.wallev.common.mixin.toms;

import dev.shwg.smoothswapping.SmoothSwapping;
import dev.shwg.smoothswapping.SwapStacks;
import dev.shwg.smoothswapping.SwapUtil;
import dev.shwg.smoothswapping.Vec2;
import dev.shwg.smoothswapping.config.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = StorageScreenBase.class)
public abstract class MixinStorageScreenBase extends AbstractContainerScreen {
    private Screen currentScreen = null;

    public MixinStorageScreenBase(AbstractContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Inject(method = {"render"}, at = {@At(value = "HEAD")})
    public void onRender(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        try {
            this.doRender(mouseX, mouseY);
        } catch (Exception e) {
            SwapUtil.reset();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doRender(double mouseX, double mouseY) {
        if (!ConfigManager.getConfig().getToggleMod()) {
            return;
        }
        AbstractContainerScreen handledScreen = this;
        if (handledScreen instanceof CreativeModeInventoryScreen) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.player.containerMenu == null) {
            return;
        }
        SmoothSwapping.currentStacks = client.player.containerMenu.getItems();
        try {
            SmoothSwapping.currentCursorStackLock.lock();
            ItemStack cursorStack = client.player.containerMenu.getCarried();
            ItemStack prevStack = SmoothSwapping.currentCursorStack.get();
            if (prevStack == null || prevStack.getCount() != cursorStack.getCount() || prevStack.getItem() != cursorStack.getItem()) {
                SmoothSwapping.currentCursorStack.set(cursorStack.copy());
            }
        } finally {
            SmoothSwapping.currentCursorStackLock.unlock();
        }
        Screen screen = client.screen;
        if (SmoothSwapping.clickSwap) {
            SmoothSwapping.clickSwap = false;
            SwapUtil.copyStacks(SmoothSwapping.currentStacks, SmoothSwapping.oldStacks);
            return;
        }
        if (this.currentScreen != screen) {
            SmoothSwapping.swaps.clear();
            SwapUtil.copyStacks(SmoothSwapping.currentStacks, SmoothSwapping.oldStacks);
            this.currentScreen = screen;
            return;
        }
        Map<Integer, ItemStack> changedStacks = this.getChangedStacks(SmoothSwapping.oldStacks, SmoothSwapping.currentStacks);
        if (!SmoothSwapping.clickSwap) {
            int changedStacksSize = changedStacks.size();
            if (changedStacksSize > 1) {
                ArrayList<SwapStacks> moreStacks = new ArrayList<>();
                ArrayList<SwapStacks> lessStacks = new ArrayList<>();
                int totalAmount = 0;
                for (Map.Entry<Integer, ItemStack> stackEntry : changedStacks.entrySet()) {
                    int slotID = stackEntry.getKey();
                    ItemStack newStack = stackEntry.getValue();
                    ItemStack oldStack = SmoothSwapping.oldStacks.get(slotID);
                    if (SwapUtil.getCount(newStack) > SwapUtil.getCount(oldStack) && this.menu.getSlot(slotID).allowModification(Minecraft.getInstance().player)) {
                        moreStacks.add(new SwapStacks(slotID, oldStack, newStack, SwapUtil.getCount(oldStack) - SwapUtil.getCount(newStack)));
                        totalAmount += SwapUtil.getCount(newStack) - SwapUtil.getCount(oldStack);
                        continue;
                    }
                    if (SwapUtil.getCount(newStack) >= SwapUtil.getCount(oldStack) || !this.menu.getSlot(slotID).allowModification(Minecraft.getInstance().player) || SmoothSwapping.clickSwapStack != null)
                        continue;
                    lessStacks.add(new SwapStacks(slotID, oldStack, newStack, SwapUtil.getCount(oldStack) - SwapUtil.getCount(newStack)));
                }
                if (SmoothSwapping.clickSwapStack != null) {
                    lessStacks.clear();
                    ItemStack newStack = this.menu.getSlot(SmoothSwapping.clickSwapStack).getItem();
                    ItemStack oldStack = SmoothSwapping.oldStacks.get(SmoothSwapping.clickSwapStack);
                    lessStacks.add(new SwapStacks(SmoothSwapping.clickSwapStack, oldStack, newStack, totalAmount));
                    SmoothSwapping.clickSwapStack = null;
                }
                if (moreStacks.isEmpty()) {
                    SwapUtil.assignI2CSwaps(lessStacks, new Vec2(mouseX - (double) this.leftPos, mouseY - (double) this.topPos), this.menu);
                } else {
                    SwapUtil.assignI2ISwaps(moreStacks, lessStacks, this.menu);
                }
            } else if (changedStacksSize == 1) {
                ItemStack currentCursorStack = SmoothSwapping.currentCursorStack.get();
                ItemStack oldCursorStack = SmoothSwapping.oldCursorStack;
                if (currentCursorStack != null && oldCursorStack != null && currentCursorStack.getItem() == oldCursorStack.getItem() && currentCursorStack.getCount() != oldCursorStack.getCount()) {
                    changedStacks.entrySet().stream().findFirst().ifPresent(changedStack -> {
                        ItemStack oldStack = SmoothSwapping.oldStacks.get(changedStack.getKey());
                        ItemStack currentStack = SmoothSwapping.currentStacks.get(changedStack.getKey());
                        int cursorStackCountDiff = currentCursorStack.getCount() - SmoothSwapping.oldCursorStack.getCount();
                        if (oldStack.getItem() == currentStack.getItem() && oldStack.getCount() - currentStack.getCount() == cursorStackCountDiff || currentStack.getItem() == Items.AIR) {
                            SwapStacks lessStack = new SwapStacks(changedStack.getKey(), oldStack, currentStack, SwapUtil.getCount(oldStack) - SwapUtil.getCount(currentStack));
                            SwapUtil.assignI2CSwaps(List.of(lessStack), new Vec2(mouseX - (double) this.leftPos, mouseY - (double) this.topPos), this.menu);
                        }
                    });
                }
            }
        }
        if (!this.areStacksEqual(SmoothSwapping.oldStacks, SmoothSwapping.currentStacks)) {
            SwapUtil.copyStacks(SmoothSwapping.currentStacks, SmoothSwapping.oldStacks);
            SmoothSwapping.oldCursorStack = SmoothSwapping.currentCursorStack.get();
        }
    }

    private Map<Integer, ItemStack> getChangedStacks(NonNullList<ItemStack> oldStacks, NonNullList<ItemStack> newStacks) {
        HashMap<Integer, ItemStack> changedStacks = new HashMap<Integer, ItemStack>();
        for (int slotID = 0; slotID < oldStacks.size(); ++slotID) {
            ItemStack newStack = newStacks.get(slotID);
            ItemStack oldStack = oldStacks.get(slotID);
            if (ItemStack.matches(oldStack, newStack)) continue;
            changedStacks.put(slotID, newStack.copy());
        }
        return changedStacks;
    }

    private boolean areStacksEqual(NonNullList<ItemStack> oldStacks, NonNullList<ItemStack> newStacks) {
        if (oldStacks == null || newStacks == null || oldStacks.size() != newStacks.size()) {
            return false;
        }
        for (int slotID = 0; slotID < oldStacks.size(); ++slotID) {
            ItemStack newStack = newStacks.get(slotID);
            ItemStack oldStack = oldStacks.get(slotID);
            if (ItemStack.matches(oldStack, newStack)) continue;
            return false;
        }
        return true;
    }
}
