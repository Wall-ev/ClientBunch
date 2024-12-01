package com.wallev.common.handler.obst;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.obscuria.tooltips.client.ResourceLoader;
import com.obscuria.tooltips.client.StyleManager;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.Effects;
import com.obscuria.tooltips.client.style.Effects.Order;
import com.obscuria.tooltips.client.style.TooltipStyle;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.wallev.clientbunch.client.event.ItemTooltipEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;

import java.awt.*;
import java.util.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber({Dist.CLIENT})
public final class ObsTooltipRenderer {
    private static @Nullable TooltipStyle renderStyle = null;
    private static List<TooltipEffect> renderEffects = new ArrayList<>();
    private static @Nullable TooltipFrame renderFrames= null;
    private static @Nullable ArmorStand renderStand;
    private static ItemStack renderStack;
    private static long tooltipStartMillis;
    private static float tooltipSeconds;
    public static final Point POINT_ZERO = new Point();

    static {
        renderStack = ItemStack.EMPTY;
        tooltipStartMillis = 0L;
        tooltipSeconds = 0.0F;
    }

    public ObsTooltipRenderer() {
    }

    public static boolean render(TooltipContext renderer, Point point, ItemStack stack, int x, int y) {
        updateStyle(stack);
        if (renderStyle != null) {
            renderer.define(renderStack, tooltipSeconds);
            renderer.pose().pushPose();
            Vec2 pos = new Vec2(x, y);

            // @todo
            // 背景效果
            if (false) {
                renderer.drawManaged(() -> {
                    renderEffects(Order.LAYER_3_TEXT$FRAME, renderer, pos, point,renderEffects);
                    renderer.push(() -> {
                        renderer.translate(0.0F, 0.0F, -50.F);
//                        renderFrames.render(renderer, pos, point);
                    });
//                    renderEffects(Order.LAYER_5_FRONT, renderer, pos, point, renderEffects);
//                    renderEffects(Order.LAYER_5_FRONT, renderer, pos, POINT_ZERO, renderEffects);
//                    renderStyle.renderEffects(Order.LAYER_5_FRONT, renderer, pos, POINT_ZERO);
                });
            }

            // 需要的东西
            if (true) {
                renderer.drawManaged(() -> {
                    renderStyle.renderFront(renderer, pos, point);
//                    renderStyle.renderFront(renderer, pos, POINT_ZERO);
                });
            }

            // @todo
            // 粒子效果：珍珠
            if (true) {
                renderer.drawManaged(() -> {
//                    renderEffects(Order.LAYER_5_FRONT, renderer, pos, point, renderEffects);
//                    renderEffects(Order.LAYER_5_FRONT, renderer, pos, POINT_ZERO, renderEffects);
                    renderStyle.renderEffects(Order.LAYER_5_FRONT, renderer, pos, point);
                });
            }

            renderer.pose().popPose();
            renderer.flush();
            return true;
        } else {
            return false;
        }
    }

    public static void renderEffects(Effects.Order order, TooltipContext renderer, Vec2 pos, Point size, List<TooltipEffect> effects) {
        renderer.push(() -> {
            float var10003 = switch (order) {
                case LAYER_1_BACK -> 0.0F;
                case LAYER_2_BACK$TEXT -> 100.0F;
                case LAYER_3_TEXT$FRAME -> 400.0F;
                case LAYER_4_FRAME$ICON -> 500.0F;
                case LAYER_5_FRONT -> 1000.0F;
                default -> throw new IncompatibleClassChangeError();
            };

            renderer.translate(0.0F, 0.0F, var10003);

            for (TooltipEffect effect : effects) {
                if (effect.order().equals(order)) {
                    effect.render(renderer, pos, size);
                }
            }

        });
    }


    public static boolean render(TooltipContext renderer, ItemStack stack, Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner) {
        updateStyle(stack);
        if (renderStyle != null && !components.isEmpty()) {
            renderer.define(renderStack, tooltipSeconds);
            Component summaryField = getRarityName(stack);
            Point size = calculateSize(font, components, summaryField);
            Vector2ic rawPos = positioner.positionTooltip(renderer.width(), renderer.height(), x, y, size.x, size.y);
            Vec2 pos = new Vec2((float) rawPos.x(), (float) rawPos.y());
            renderer.pose().pushPose();
            // 边框
            if (false) {
                renderer.drawManaged(() -> {
                    renderStyle.renderEffects(Order.LAYER_1_BACK, renderer, pos, size);
                });
            }
            // 边框
            if (false) {
                renderer.drawManaged(() -> {
                    renderStyle.renderBack(renderer, pos, size, true);
                });
            }
            // 文字
            if (false) {
                renderer.pose().translate(0.0F, 0.0F, 400.0F);
                GuiGraphics var10000 = renderer.context();
                int var10003 = (int) pos.x + 26;
                var10000.drawString(Minecraft.getInstance().font, summaryField, var10003, (int) pos.y + 13, -11513776);
            }
            if (false) {
                renderer.drawManaged(() -> {
                    renderStyle.renderEffects(Order.LAYER_2_BACK$TEXT, renderer, pos, size);
                });
            }
            // 原本的文字
            if (false) {
                renderText(renderer, font, components, pos);
            }
            // 原本的ImageTooltip
            if (false) {
                renderImages(renderer, font, components, pos);
            }

            Point point = new Point();
            // 需要的东西
            if (true) {
                renderer.drawManaged(() -> {
                    renderStyle.renderFront(renderer, pos, size);
//                    renderStyle.renderFront(renderer, pos, point);
//                    renderStyle.renderFront(renderer, pos, size);
                });
            }

            // 粒子效果：珍珠
            if (true) {
                renderer.drawManaged(() -> {
                    renderStyle.renderEffects(Order.LAYER_5_FRONT, renderer, pos, size);
                });
            }

            renderer.pose().popPose();
//            Vec2 center;
//            if (stack.getItem() instanceof ArmorItem && (Boolean)Client.displayArmorModels.get()) {
//                center = renderSecondPanel(renderer, pos);
//                equip(stack);
//                renderStand(renderer, center.add(new Vec2(0.0F, 26.0F)));
//            } else if (stack.getItem() instanceof TieredItem && (Boolean)Client.displayToolModels.get()) {
//                center = renderSecondPanel(renderer, pos);
//                renderer.pose().pushPose();
//                renderer.pose().translate(center.x, center.y, 500.0F);
//                renderer.pose().scale(2.75F, 2.75F, 2.75F);
//                renderer.pose().mulPose(Axis.XP.rotationDegrees(-30.0F));
//                renderer.pose().mulPose(Axis.YP.rotationDegrees((float)((double)System.currentTimeMillis() / 1000.0 % 360.0) * -20.0F));
//                renderer.pose().mulPose(Axis.ZP.rotationDegrees(-45.0F));
//                renderer.pose().pushPose();
//                renderer.pose().translate(-8.0F, -8.0F, -150.0F);
//                renderer.context().renderItem(stack, 0, 0);
//                renderer.pose().popPose();
//                renderer.pose().popPose();
//            }

            renderer.flush();
            return true;
        } else {
            return false;
        }
    }

    public static void renderFront(TooltipStyle renderStyle1, TooltipContext renderer, Vec2 pos, Point size) {
        if (true) return;
        renderStyle.renderEffects(Order.LAYER_3_TEXT$FRAME, renderer, pos, size);
        renderer.push(() -> {
            renderer.translate(0.0F, 0.0F, -50.0F);
//            renderStyle.FRAME.render(renderer, pos, size);
        });
        renderStyle.renderEffects(Order.LAYER_4_FRAME$ICON, renderer, pos, size);
        renderer.push(() -> {
            renderer.translate(pos.x + 12.0F, pos.y + 12.0F, 500.0F);
            renderer.push(() -> {
//                (a1).getICON().render(renderer, -8, -8);
//                if (renderStyle1 instanceof TooltipStyleAccessor)
//                ((TooltipStyleAccessor1)renderStyle1).getICON().render(renderer, -8, -8);
            });
        });
    }

    private static Vec2 renderSecondPanel(TooltipContext renderer, Vec2 pos) {
        renderer.drawManaged(() -> {
            renderStyle.renderBack(renderer, pos.add(new Vec2(-55.0F, 0.0F)), new Point(30, 60), false);
        });
        return pos.add(new Vec2(-40.0F, 30.0F));
    }

    @Contract("_ -> new")
    private static Component getRarityName(ItemStack stack) {
        return Component.translatable("rarity." + stack.getRarity().name().toLowerCase() + ".name");
    }

    private static Point calculateSize(Font font, List<ClientTooltipComponent> components, Component summaryField) {
        int width = 26 + ((ClientTooltipComponent) components.get(0)).getWidth(font);
        int height = 14;

        ClientTooltipComponent component;
        for (Iterator<ClientTooltipComponent> var5 = components.iterator(); var5.hasNext(); height += component.getHeight()) {
            component = var5.next();
            int componentWidth = component.getWidth(font);
            if (componentWidth > width) {
                width = componentWidth;
            }
        }

        int SummaryWidth = 26 + font.width(summaryField.getString());
        if (SummaryWidth > width) {
            width = SummaryWidth;
        }

        return new Point(width, height);
    }

    private static void renderText(TooltipContext renderer, Font font, List<ClientTooltipComponent> components, Vec2 pos) {
        int offset = (int) pos.y + 3;

        for (int i = 0; i < components.size(); ++i) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, (int) pos.x + (i == 0 ? 26 : 0), offset, renderer.pose().last().pose(), renderer.bufferSource());
            offset += component.getHeight() + (i == 0 ? 13 : 0);
        }

    }

    private static void renderImages(TooltipContext renderer, Font font, List<ClientTooltipComponent> components, Vec2 pos) {
        int offset = (int) pos.y + 4;

        for (int i = 0; i < components.size(); ++i) {
            ClientTooltipComponent component = components.get(i);
            component.renderImage(font, (int) pos.x, offset, renderer.context());
            offset += component.getHeight() + (i == 0 ? 13 : 0);
        }

    }

    private static void renderStand(TooltipContext renderer, Vec2 pos) {
        if (renderStand != null && Minecraft.getInstance().player != null) {
            renderer.push(() -> {
                renderer.translate(pos.x, pos.y, 500.0F);
                renderer.scale(-30.0F, -30.0F, 30.0F);
                renderer.mul(Axis.XP.rotationDegrees(25.0F));
                renderer.mul(Axis.YP.rotationDegrees((float) ((double) System.currentTimeMillis() / 1000.0 % 360.0) * 20.0F));
                Lighting.setupForEntityInInventory();
                EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
                entityrenderdispatcher.setRenderShadow(false);
                RenderSystem.runAsFancy(() -> {
                    entityrenderdispatcher.render(renderStand, 0.0, 0.0, 0.0, 0.0F, 1.0F, renderer.pose(), renderer.bufferSource(), 15728880);
                });
                renderer.flush();
                entityrenderdispatcher.setRenderShadow(true);
            });
            Lighting.setupFor3DItems();
        }
    }

    private static void equip(ItemStack stack) {
        if (renderStand != null) {
            Item var2 = stack.getItem();
            if (var2 instanceof ArmorItem armorItem) {
                renderStand.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                renderStand.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
                renderStand.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
                renderStand.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
                renderStand.setItemSlot(armorItem.getEquipmentSlot(), stack);
            }

        }
    }

    private static void updateStyle(ItemStack stack) {
        if (renderStand == null && Minecraft.getInstance().level != null) {
            renderStand = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
        }

        if (stack.isEmpty()) {
            reset();
        } else {
            tooltipSeconds = (float) (System.currentTimeMillis() - tooltipStartMillis) / 1000.0F;
//            if (stack == renderStack) {
//                return;
//            }

            if (ItemTooltipEvent.isCompare() || ItemStack.matches(stack, renderStack)) {
                renderStack = stack;
                return;
            }

            reset();
            renderStack = stack;
            renderStyle = getStyleFor(stack).orElse(null);
            renderEffects = getStyleEffect(stack);
            if (renderStyle != null) {
                renderStyle.reset();
            }
            if (!renderEffects.isEmpty()) {
                for (TooltipEffect renderEffect : renderEffects) {
                    renderEffect.reset();
                }
            }
        }

        renderStack = stack;
    }

    // @todo
    public static Optional<TooltipStyle> getStyleFor(ItemStack stack) {
        TooltipStylePreset preset = ResourceLoader.getStyleFor(stack).orElse(null);
        return preset == null ? StyleManager.defaultStyle() : Optional.of((new TooltipStyle.Builder())
                .withPanel(preset.getPanel().orElse(StyleManager.DEFAULT_PANEL))
                .withFrame(preset.getFrame().orElse(StyleManager.DEFAULT_FRAME))
                .withIcon(preset.getIcon().orElse(StyleManager.DEFAULT_ICON))
//                .withEffects(Collections.emptyList()).build());
                .withEffects(preset.getEffects()).build());
    }

    public static List<TooltipEffect> getStyleEffect(ItemStack stack) {
        TooltipStylePreset preset = ResourceLoader.getStyleFor(stack).orElse(null);
        return preset == null ? Collections.emptyList() : preset.getEffects();
    }

//    public static List<TooltipEffect> getStyleEffect(ItemStack stack) {
//        TooltipStylePreset preset = ResourceLoader.getStyleFor(stack).orElse(null);
//        return preset == null ? Collections.emptyList() : preset.getEffects();
//    }

    private static void reset() {
        if (renderStyle != null) {
            renderStyle.reset();
        }

        renderStyle = null;
        tooltipStartMillis = System.currentTimeMillis();
        tooltipSeconds = 0.0F;
    }

    public static void clear() {
        renderStack = ItemStack.EMPTY;
        if (renderStyle != null) {
            renderStyle.reset();
        }

        renderStyle = null;
    }
}
