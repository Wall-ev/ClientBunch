package com.wallev.clientbunch.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

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

    @Inject(at = @At("TAIL"), method = "renderTooltip", remap = false)
    public void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY, CallbackInfo ci) {

        if (minecraft == null) return;
        if (!isKeyDown()) return;
        if (!(this.getMenu().getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem())) return;

        ItemStack hoverStack = this.hoveredSlot.getItem();
        Item hoverItem = hoverStack.getItem();
        if (!(hoverItem instanceof Equipable)) return;

        EquipmentSlot slot = Mob.getEquipmentSlotForItem(hoverStack);
        if (slot == EquipmentSlot.MAINHAND) return;

        if (minecraft.player == null) return;
        ItemStack equitmentStack = minecraft.player.getItemBySlot(slot);
        if (equitmentStack.isEmpty()) return;

        List<Component> tooltipFromEquitmentItem = this.getTooltipFromContainerItem(equitmentStack);
        Optional<TooltipComponent> equitmentStackTooltipImage = equitmentStack.getTooltipImage();
        int guiScaledWidth = this.minecraft.getWindow().getGuiScaledWidth();
        int guiScaledHeight = this.minecraft.getWindow().getGuiScaledHeight();

        int equitmentStackWidth = 0, equitmentStackHeight = 0;
        List<ClientTooltipComponent> eList = ForgeHooksClient.gatherTooltipComponents(equitmentStack, tooltipFromEquitmentItem, equitmentStackTooltipImage, pX, guiScaledWidth, guiScaledHeight, font);
        for (ClientTooltipComponent clientTooltipComponent : eList) {
            equitmentStackWidth = Math.max(equitmentStackWidth, clientTooltipComponent.getWidth(font));
            equitmentStackHeight += clientTooltipComponent.getHeight();
        }

        pGuiGraphics.renderTooltip(this.font, tooltipFromEquitmentItem, equitmentStackTooltipImage, equitmentStack, pX - equitmentStackWidth - 20, pY);

        int textY = pY - 20;
        if (this.height - equitmentStackHeight < pY) {
            textY = this.height - equitmentStackHeight - 10;
        }
        Component text = Component.literal("Equipped").withStyle(ChatFormatting.YELLOW);
        if (font.width(text) < equitmentStackWidth) {
            int leftWidth = (equitmentStackWidth - font.width(text)) / 2;
            int amount = leftWidth / font.width(" ");
            text = Component.literal(" ".repeat(amount)).append(text).append(Component.literal(" ".repeat(amount)));
            pGuiGraphics.renderTooltip(this.font, text, pX - equitmentStackWidth - ((font.width(text) - equitmentStackWidth) / 2) - 20, textY);
        } else {
            pGuiGraphics.renderTooltip(this.font, text, pX - font.width(text) - ((equitmentStackWidth - font.width(text)) / 2) - 20, textY);
        }
    }
}
