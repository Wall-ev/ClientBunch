package com.wallev.clientbunch.inventory.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wallev.clientbunch.client.tooltip.FoodTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import squeek.appleskin.ModConfig;
import squeek.appleskin.api.event.TooltipOverlayEvent;
import squeek.appleskin.api.food.FoodValues;
import squeek.appleskin.helpers.FoodHelper;
import squeek.appleskin.helpers.KeyHelper;
import squeek.appleskin.helpers.TextureHelper;

public class FoodTooltipComponentRenderer implements ClientTooltipComponent {
    private static final TextureOffsets rottenBarTextureOffsets = new TextureOffsets();
    private static final TextureOffsets normalBarTextureOffsets = new TextureOffsets();

    static {
        normalBarTextureOffsets.containerNegativeHunger = 43;
        normalBarTextureOffsets.containerExtraHunger = 133;
        normalBarTextureOffsets.containerNormalHunger = 16;
        normalBarTextureOffsets.containerPartialHunger = 124;
        normalBarTextureOffsets.containerMissingHunger = 34;
        normalBarTextureOffsets.shankMissingFull = 70;
        normalBarTextureOffsets.shankMissingPartial = normalBarTextureOffsets.shankMissingFull + 9;
        normalBarTextureOffsets.shankFull = 52;
        normalBarTextureOffsets.shankPartial = normalBarTextureOffsets.shankFull + 9;
    }

    static {
        rottenBarTextureOffsets.containerNegativeHunger = normalBarTextureOffsets.containerNegativeHunger;
        rottenBarTextureOffsets.containerExtraHunger = normalBarTextureOffsets.containerExtraHunger;
        rottenBarTextureOffsets.containerNormalHunger = normalBarTextureOffsets.containerNormalHunger;
        rottenBarTextureOffsets.containerPartialHunger = normalBarTextureOffsets.containerPartialHunger;
        rottenBarTextureOffsets.containerMissingHunger = normalBarTextureOffsets.containerMissingHunger;
        rottenBarTextureOffsets.shankMissingFull = 106;
        rottenBarTextureOffsets.shankMissingPartial = rottenBarTextureOffsets.shankMissingFull + 9;
        rottenBarTextureOffsets.shankFull = 88;
        rottenBarTextureOffsets.shankPartial = rottenBarTextureOffsets.shankFull + 9;
    }

    private FoodTooltipComponent foodTooltip;

    public FoodTooltipComponentRenderer(FoodTooltipComponent foodTooltip) {
        this.foodTooltip = foodTooltip;
    }

    @Override
    public int getHeight() {
        // hunger + spacing + saturation + arbitrary spacing,
        // for some reason 3 extra looks best
        return 9 + 2;
    }

    @Override
    public int getWidth(Font font) {
        int hungerBarsWidth = foodTooltip.getHungerBars() * 9;
        if (foodTooltip.getHungerBarsText() != null)
            hungerBarsWidth += font.width(foodTooltip.getHungerBarsText());

        int saturationBarsWidth = foodTooltip.getSaturationBars() * 7;
        if (foodTooltip.getSaturationBarsText() != null)
            saturationBarsWidth += font.width(foodTooltip.getSaturationBarsText());

        return hungerBarsWidth + 6 + saturationBarsWidth + 2; // right padding
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        ItemStack itemStack = foodTooltip.getItemStack();
        Minecraft mc = Minecraft.getInstance();
        if (!shouldShowTooltip(itemStack, mc.player))
            return;

        Screen gui = mc.screen;
        if (gui == null)
            return;

        FoodValues defaultFood = foodTooltip.getDefaultFood();
        FoodValues modifiedFood = foodTooltip.getModifiedFood();

        // Notify everyone that we should render tooltip overlay
        TooltipOverlayEvent.Render renderEvent = new TooltipOverlayEvent.Render(itemStack, x, y, guiGraphics, defaultFood, modifiedFood);
        MinecraftForge.EVENT_BUS.post(renderEvent);
        if (renderEvent.isCanceled())
            return;

        x = renderEvent.x;
        y = renderEvent.y;
        guiGraphics = renderEvent.guiGraphics;

        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int offsetX = x;
        int offsetY = y;

        int defaultHunger = defaultFood.hunger;
        int modifiedHunger = modifiedFood.hunger;

        // Render from right to left so that the icons 'face' the right way
        offsetX += (foodTooltip.getHungerBars() - 1) * 9;

        TextureOffsets offsets = FoodHelper.isRotten(itemStack, mc.player) ? rottenBarTextureOffsets : normalBarTextureOffsets;
        for (int i = 0; i < foodTooltip.getHungerBars() * 2; i += 2) {

            if (modifiedHunger < 0)
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, offsets.containerNegativeHunger, 27, 9, 9, 256, 256);
            else if (modifiedHunger > defaultHunger && defaultHunger <= i)
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, offsets.containerExtraHunger, 27, 9, 9, 256, 256);
            else if (modifiedHunger > i + 1 || defaultHunger == modifiedHunger)
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, offsets.containerNormalHunger, 27, 9, 9, 256, 256);
            else if (modifiedHunger == i + 1)
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, offsets.containerPartialHunger, 27, 9, 9, 256, 256);
            else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, .5F);
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, offsets.containerMissingHunger, 27, 9, 9, 256, 256);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, .25F);
            guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, defaultHunger - 1 == i ? offsets.shankMissingPartial : offsets.shankMissingFull, 27, 9, 9, 256, 256);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            if (modifiedHunger > i)
                guiGraphics.blit(TextureHelper.MC_ICONS, offsetX, offsetY, 0, modifiedHunger - 1 == i ? offsets.shankPartial : offsets.shankFull, 27, 9, 9, 256, 256);

            offsetX -= 9;
        }
        if (foodTooltip.getHungerBarsText() != null) {
            offsetX += 18;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(0.75f, 0.75f, 0.75f);
            guiGraphics.drawString(font, foodTooltip.getHungerBarsText(), 2, 2, 0xFFAAAAAA);
            poseStack.popPose();
        }

        int saturationBarsWidth = foodTooltip.getSaturationBars() * 7;
        if (foodTooltip.getSaturationBarsText() != null)
            saturationBarsWidth += font.width(foodTooltip.getSaturationBarsText());
        offsetX = x + 6 + saturationBarsWidth;
//        offsetY += 10;

        float modifiedSaturationIncrement = modifiedFood.getSaturationIncrement();
        float absModifiedSaturationIncrement = Math.abs(modifiedSaturationIncrement);

        // Render from right to left so that the icons 'face' the right way
        offsetX += (foodTooltip.getSaturationBars() - 1) * 7;

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < foodTooltip.getSaturationBars() * 2; i += 2) {
            float effectiveSaturationOfBar = (absModifiedSaturationIncrement - i) / 2f;

            boolean shouldBeFaded = absModifiedSaturationIncrement <= i;
            if (shouldBeFaded)
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, .5F);

            guiGraphics.blit(TextureHelper.MOD_ICONS, offsetX, offsetY, 0, effectiveSaturationOfBar >= 1 ? 21 : effectiveSaturationOfBar > 0.5 ? 14 : effectiveSaturationOfBar > 0.25 ? 7 : effectiveSaturationOfBar > 0 ? 0 : 28, modifiedSaturationIncrement >= 0 ? 27 : 34, 7, 7, 256, 256);

            if (shouldBeFaded)
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            offsetX -= 7;
        }
        if (foodTooltip.getSaturationBarsText() != null) {
            offsetX += 14;
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(0.75f, 0.75f, 0.75f);
            guiGraphics.drawString(font, foodTooltip.getSaturationBarsText(), 2, 1, 0xFFAAAAAA);
            poseStack.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TextureHelper.MC_ICONS);

        // reset to drawHoveringText state
        RenderSystem.disableDepthTest();
    }

    static class TextureOffsets {
        int containerNegativeHunger;
        int containerExtraHunger;
        int containerNormalHunger;
        int containerPartialHunger;
        int containerMissingHunger;
        int shankMissingFull;
        int shankMissingPartial;
        int shankFull;
        int shankPartial;
    }

    private static boolean shouldShowTooltip(ItemStack hoveredStack, Player player) {
        if (hoveredStack.isEmpty())
            return false;

        if (!FoodHelper.isFood(hoveredStack, player))
            return false;

        return true;
    }
}