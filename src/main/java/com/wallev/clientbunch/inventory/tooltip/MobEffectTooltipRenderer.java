package com.wallev.clientbunch.inventory.tooltip;

import com.wallev.clientbunch.client.tooltip.MobEffectTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class MobEffectTooltipRenderer implements ClientTooltipComponent {
    private final MobEffectTooltipComponent effectTooltipComponent;
    private final int imageOffsetX;
    private final int textOffsetX;

    public MobEffectTooltipRenderer(MobEffectTooltipComponent effectTooltipComponent) {
        this.effectTooltipComponent = effectTooltipComponent;
        int[] offsetX = getOffsetX();
        this.imageOffsetX = offsetX[0];
        this.textOffsetX = offsetX[1];
    }

    private int[] getOffsetX() {
        Minecraft mc = Minecraft.getInstance();
        MutableComponent description = effectTooltipComponent.mutableComponent();

        String string = description.getString();
        int spaceCount = 0;
        String spaceString = CommonComponents.SPACE.getString();
        if (string.startsWith(spaceString)) {
            for (String s : string.split(spaceString)) {
                if (!s.isEmpty()) {
                    break;
                }
                spaceCount++;
            }

            int a1 = 0;
            if (description.getContents() instanceof LiteralContents literalContents) {
                a1 += mc.font.width(literalContents.text());
            }

            int offsetX = mc.font.width(CommonComponents.SPACE.getString().repeat(spaceCount));
            if (effectTooltipComponent.renderIcon()) {
                return new int[]{offsetX, offsetX + 10 - a1};
            } else {
                return new int[]{offsetX, offsetX - a1};
            }
        } else {
            if (effectTooltipComponent.renderIcon()) {
                return new int[]{0, 10};
            } else {
                return new int[]{0, 0};
            }
        }
    }

    @Override
    public int getHeight() {
        return Minecraft.getInstance().font.lineHeight + 2;
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return font.width(effectTooltipComponent.mutableComponent()) + 9 + 2;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
        Minecraft minecraft = Minecraft.getInstance();
//        RenderSystem.setShader(GameRenderer::getPositionTexShader);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        if (!effectTooltipComponent.renderIcon()) return;
        renderEffectIcon(x + imageOffsetX, y + 1, graphics, minecraft, effectTooltipComponent.mobEffect());
    }

    private void renderEffectIcon(
            int x, int y, @NotNull GuiGraphics graphics, Minecraft minecraft, MobEffect mobeffect) {
        MobEffectTextureManager textureManager = minecraft.getMobEffectTextures();
        ResourceLocation key = ForgeRegistries.MOB_EFFECTS.getKey(mobeffect);
        TextureAtlasSprite sprite1 = textureManager.getSprite(key);
        graphics.blit(x, y, 0, 9, 9, sprite1);
    }

    @Override
    public void renderText(
            @NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource buffer) {
        MutableComponent description = effectTooltipComponent.mutableComponent();
        int color = description.getStyle().getColor() == null ? 0xAABBCC : description.getStyle().getColor().getValue();
        font.drawInBatch(description, x + textOffsetX, y + 1, color, true, matrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
