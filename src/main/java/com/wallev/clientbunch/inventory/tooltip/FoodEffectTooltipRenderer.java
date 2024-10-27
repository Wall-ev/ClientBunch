package com.wallev.clientbunch.inventory.tooltip;

import com.mojang.datafixers.util.Pair;
import com.wallev.clientbunch.client.tooltip.FoodEffectTooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class FoodEffectTooltipRenderer implements ClientTooltipComponent {
  private final FoodEffectTooltipComponent component;

  public FoodEffectTooltipRenderer(FoodEffectTooltipComponent component) {
    this.component = component;
  }

  @Override
  public int getHeight() {
    return getSpacing() * component.effects().size();
  }

  @Override
  public int getWidth(@NotNull Font font) {
    int width = 0;
    for (int i = 0; i < component.effects().size(); i++) {
      width = Math.max(width, font.width(getEffectDescription(component.effects().get(i))) + 12);
    }
    return width;
  }

  @Override
  public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
    Minecraft minecraft = Minecraft.getInstance();
//    RenderSystem.setShader(GameRenderer::getPositionTexShader);
//    RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    for (int i = 0; i < component.effects().size(); i++) {
      MobEffectInstance effect = component.effects().get(i).getFirst();
      renderEffectIcon(x, y + getSpacing() * i, graphics, minecraft, effect);
    }
  }

  private void renderEffectIcon(
      int x, int y, @NotNull GuiGraphics graphics, Minecraft minecraft, MobEffectInstance effect) {
    MobEffect mobeffect = effect.getEffect();
    MobEffectTextureManager textureManager = minecraft.getMobEffectTextures();
    TextureAtlasSprite sprite = textureManager.get(mobeffect);
    graphics.blit(x, y, 0, 9, 9, sprite);
  }

  @Override
  public void renderText(
      @NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource buffer) {
    int color = 0xAABBCC;
    for (int i = 0; i < component.effects().size(); i++) {
      Component description = getEffectDescription(component.effects().get(i));
      int textX = x + 12;
      int textY = y + 1 + getSpacing() * i;
      Font.DisplayMode mode = Font.DisplayMode.NORMAL;
      font.drawInBatch(description, textX, textY, color, true, matrix, buffer, mode, 0, 15728880);
    }
  }

  public int getSpacing() {
    return Minecraft.getInstance().font.lineHeight + 2;
  }

  private Component getEffectDescription(Pair<MobEffectInstance, Float> pair) {
    MobEffectInstance effect = pair.getFirst();
    float chance = pair.getSecond();
    MutableComponent description = null;
    int amplifier = effect.getAmplifier();
    if (description == null) {
      description = Component.translatable(effect.getDescriptionId());
      if (amplifier > 0) {
        Component amplifierDescription = Component.translatable("potion.potency." + amplifier);
        description =
            Component.translatable("potion.withAmplifier", description, amplifierDescription);
      }
    }
    String time = " (" + MobEffectUtil.formatDuration(effect, 1f).getString() + ")";
    description.append(time);
    if (chance < 1f) {
      description.append(" (" + (int) (chance * 100) + "%)");
    }
    return description;
  }
}
