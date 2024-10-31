package com.wallev.clientbunch.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.wallev.clientbunch.client.event.ItemTooltipEvent;
import com.wallev.clientbunch.handler.EquityHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen<T extends AbstractContainerMenu> extends Screen implements MenuAccess<T> {

    @Shadow
    @Nullable
    protected Slot hoveredSlot;

    protected MixinAbstractContainerScreen(Component pTitle) {
        super(pTitle);
    }

    private boolean isShiftKeyDown() {
        long handle = minecraft.getWindow().getWindow();
        return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_SHIFT);
    }

    private boolean isAltKeyDown() {
        long handle = minecraft.getWindow().getWindow();
        return InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_LEFT_ALT) || InputConstants.isKeyDown(handle, GLFW.GLFW_KEY_RIGHT_ALT);
    }

    private boolean isKeyDown() {
        return isAltKeyDown();
    }

    @Shadow
    protected abstract List<Component> getTooltipFromContainerItem(ItemStack pStack);

    @Shadow
    public abstract T getMenu();

    @Inject(at = @At("TAIL"), method = "renderTooltip", remap = false)
    public void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY, CallbackInfo ci) {

        if (minecraft == null) return;
        if (!isKeyDown()) {
            ItemTooltipEvent.setCompare(false);
            return;
        }
        if (!(this.getMenu().getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem())) return;

        ItemStack hoverStack = this.hoveredSlot.getItem();

        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(hoverStack);

        if (minecraft.player == null) return;
        ItemStack equitmentStack = minecraft.player.getItemBySlot(equipmentSlot);

        if (equitmentStack.isEmpty()) return;

        minecraft.player.getMainHandItem();

        if (equipmentSlot == EquipmentSlot.MAINHAND && (hoverStack == equitmentStack || !EquityHandler.canCompareHandItem(hoverStack, equitmentStack))) {
            return;
        }

        ItemTooltipEvent.setCompare(true);
        ItemTooltipEvent.setComparedStack(equitmentStack);

        EquityHandler.ContainerInfo containerInfo = new EquityHandler.ContainerInfo((AbstractContainerScreen<?>) (Object) (this), this.minecraft, this.hoveredSlot, equipmentSlot) {
            @Override
            public List<Component> getTooltipFromContainerStack(ItemStack stack) {
                return getTooltipFromContainerItem(stack);
            }
        };
        EquityHandler.renderTooltip(containerInfo, pGuiGraphics, pX, pY);
    }
}
