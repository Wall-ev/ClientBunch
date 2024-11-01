package com.wallev.clientbunch.client.event;

import com.mojang.datafixers.util.Either;
import com.wallev.clientbunch.ClientBunch;
import com.wallev.clientbunch.client.tooltip.ArmorTooltipComponent;
import com.wallev.clientbunch.client.tooltip.ItemTooltipComponent;
import com.wallev.clientbunch.client.tooltip.LineTooltipComponent;
import com.wallev.clientbunch.handler.ItemType;
import com.wallev.clientbunch.inventory.tooltip.ArmorTooltipRenderer;
import com.wallev.clientbunch.util.MouseHandlerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = ClientBunch.MOD_ID, value = Dist.CLIENT)
public final class ItemTooltipEvent {
    private static boolean compare = false;
    private static ItemStack comparedStack = ItemStack.EMPTY;
    private static ItemStack lastRenderStack = ItemStack.EMPTY;
    private static long lastMillis = 0;

    private ItemTooltipEvent() {
    }

    private static void setLastRenderStack(ItemStack stack) {
        lastRenderStack = stack;
    }

    public static void setComparedStack(ItemStack stack) {
        comparedStack = stack;
    }

    @SubscribeEvent
    public static void addItemRendererTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack tooltipStack = event.getItemStack();

        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();

        if (tooltipStack.isEmpty()) return;

        Component stackName;
        if (tooltipElements.get(0).left().isPresent()) {
            stackName = (Component) tooltipElements.get(0).left().get();
        } else {
            stackName = tooltipStack.getHoverName();
        }

        float scale = 0;
        if (isCompare() && ItemStack.matches(comparedStack, tooltipStack)) {
            scale = 1.25f;
        } else {
            if (!ItemStack.matches(lastRenderStack, tooltipStack)) {
                setLastRenderStack(tooltipStack);
                lastMillis = System.currentTimeMillis();
            } else {
                float tooltipSeconds = (System.currentTimeMillis() - lastMillis) / 1000.0F;
                scale = tooltipSeconds < 0.25 ? (float) ((1.0F - Math.pow((1.0F - tooltipSeconds * 4.0F), 3.0)) * 1.5F) : (tooltipSeconds < 0.5 ? 1.5F - (1.0F - (float) Math.pow((1.0F - (tooltipSeconds - 0.25F) * 4.0F), 3.0)) * 0.25F : 1.25F);
            }
        }

        tooltipElements.set(0, Either.right(new ItemTooltipComponent(tooltipStack, stackName, ItemType.getItemGroupName(tooltipStack), scale)));
    }

    /* LineTooltipComponent */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addLineComponentTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack tooltipStack = event.getItemStack();
        List<Either<FormattedText, TooltipComponent>> tooltipElements = event.getTooltipElements();
        if (tooltipElements.size() <= 1) return;
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        int guiScaledWidth = mc.getWindow().getGuiScaledWidth();
        int width = 0, height = 0;
        for (ClientTooltipComponent clientTooltipComponent : gatherTooltipComponents(event, font, MouseHandlerUtil.getMouseX(), guiScaledWidth)) {
            width = Math.max(width, clientTooltipComponent.getWidth(font));
            height += clientTooltipComponent.getHeight();
        }

        if (tooltipStack.getItem() instanceof Equipable equipable) {

            if (height <= ArmorTooltipRenderer.HEIGHT) {
                tooltipElements.add(1, Either.right(new LineTooltipComponent(width)));
                tooltipElements.add(Either.right(new ArmorTooltipComponent(tooltipStack, height / 2, ArmorTooltipRenderer.WIDTH + width + ArmorTooltipRenderer.MARGIN, 0, width + (height / 2), 0)));
            } else {
                tooltipElements.add(1, Either.right(new LineTooltipComponent(ArmorTooltipRenderer.WIDTH + width + ArmorTooltipRenderer.MARGIN)));
                tooltipElements.add(Either.right(new ArmorTooltipComponent(tooltipStack, ArmorTooltipRenderer.DEFAULT_SIZE, ArmorTooltipRenderer.WIDTH + width + ArmorTooltipRenderer.MARGIN, 0, width + ArmorTooltipRenderer.WIDTH / 2, (ArmorTooltipRenderer.HEIGHT - height) / 2)));
            }
        } else {
            tooltipElements.add(1, Either.right(new LineTooltipComponent(width)));
        }
    }

    private static List<ClientTooltipComponent> gatherTooltipComponents(RenderTooltipEvent.GatherComponents event, Font font, int mouseX, int screenWidth) {
        // text wrapping
        int tooltipTextWidth = event.getTooltipElements().stream()
                .mapToInt(either -> either.map(font::width, component -> 0))
                .max()
                .orElse(0);

        boolean needsWrap = false;

        int tooltipX = mouseX + 12;
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth;
            if (tooltipX < 4) // if the tooltip doesn't fit on the screen
            {
                if (mouseX > screenWidth / 2)
                    tooltipTextWidth = mouseX - 12 - 8;
                else
                    tooltipTextWidth = screenWidth - 16 - mouseX;
                needsWrap = true;
            }
        }

        if (event.getMaxWidth() > 0 && tooltipTextWidth > event.getMaxWidth()) {
            tooltipTextWidth = event.getMaxWidth();
            needsWrap = true;
        }

        int tooltipTextWidthF = tooltipTextWidth;
        if (needsWrap) {
            return event.getTooltipElements().stream()
                    .flatMap(either -> either.map(
                            text -> splitLine(text, font, tooltipTextWidthF),
                            component -> Stream.of(ClientTooltipComponent.create(component))
                    ))
                    .toList();
        }
        return event.getTooltipElements().stream()
                .map(either -> either.map(
                        text -> ClientTooltipComponent.create(text instanceof Component ? ((Component) text).getVisualOrderText() : Language.getInstance().getVisualOrder(text)),
                        ClientTooltipComponent::create
                ))
                .toList();
    }

    private static Stream<ClientTooltipComponent> splitLine(FormattedText text, Font font, int maxWidth) {
        if (text instanceof Component component && component.getString().isEmpty()) {
            return Stream.of(component.getVisualOrderText()).map(ClientTooltipComponent::create);
        }
        return font.split(text, maxWidth).stream().map(ClientTooltipComponent::create);
    }

    private static void parseComponent(MutableComponent original, MutableComponent... groupName) {
        for (MutableComponent mutableComponent : groupName) {
            if (!original.equals(Component.empty())) {
                original.append("、");
            }
            original.append(mutableComponent);
        }
    }

    public static boolean isCompare() {
        return compare;
    }

    public static void setCompare(boolean equipped) {
        ItemTooltipEvent.compare = equipped;
        if (!equipped && !comparedStack.isEmpty()) {
            comparedStack = ItemStack.EMPTY;
        }
    }
}

