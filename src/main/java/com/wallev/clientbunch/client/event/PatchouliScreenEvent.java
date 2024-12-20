package com.wallev.clientbunch.client.event;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import com.cazsius.solcarrot.item.SOLCarrotItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.tarinoita.solsweetpotato.item.FoodBookItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatchouliScreenEvent {
    private static final int SIZE = 6;
    private static final ResourceLocation DEFAULT_BOOK_TEXTURE = new ResourceLocation(PatchouliAPI.MOD_ID, "textures/gui/book_brown.png");
    private static Item pieBookItem;
//    private static int solIndex = 0;

    @SubscribeEvent
    public void screenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof GuiBook patchouliScreen) {
            int solIndex = 0;
            final int startX = -26, startY = 40;
            PatchouliAPI.IPatchouliAPI iPatchouliAPI = PatchouliAPI.get();
            Map<ResourceLocation, Book> books = BookRegistry.INSTANCE.books;

            Book guiBook = patchouliScreen.book;

            int leftPos = patchouliScreen.bookLeft;
            int topPos = patchouliScreen.bookTop;

            List<Book> values = books.values().stream().toList();
            List<GuiEventListener> guiEventListeners = new ArrayList<>();

            generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
            genrateSolCarrotBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
            genrateSolPieBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
            guiEventListeners.forEach(event::addListener);

////            if (values.size() > SIZE) {
//                Button preButton = Button.builder(Component.literal("^"), pButton -> {
//                    guiEventListeners.forEach(event::removeListener);
//                    guiEventListeners.clear();
////                    solIndex--;
//                    generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
//                    guiEventListeners.forEach(event::addListener);
//                }).bounds(leftPos - 13, topPos + GuiBook.FULL_HEIGHT - 22 - 5, 13, 10).build();
//                Button nextButton = Button.builder(Component.literal("*"), pButton -> {
//                    guiEventListeners.forEach(event::removeListener);
//                    guiEventListeners.clear();
////                    solIndex++;
//                    generateBookButton(values, leftPos, startX, topPos, startY, solIndex, iPatchouliAPI, guiBook, guiEventListeners);
//                    guiEventListeners.forEach(event::addListener);
//                }).bounds(leftPos - 13, topPos + GuiBook.FULL_HEIGHT - 10 - 5, 13, 10).build();
//                event.addListener(preButton);
//                event.addListener(nextButton);
////            }
        }
    }

    private static void genrateSolCarrotBookButton(List<Book> values, int leftPos, int startX, int topPos, int startY, int solIndex, PatchouliAPI.IPatchouliAPI iPatchouliAPI, Book guiBook, List<GuiEventListener> guiEventListeners) {
        if (!ModList.get().isLoaded("solcarrot")) {
            return;
        }
        int index = 0;
        ImageButton imageButton = new ImageButton(leftPos + startX, topPos + startY + values.size() * 22, 26, 20, 0, 0, DEFAULT_BOOK_TEXTURE, (b) -> {
            if (Minecraft.getInstance().player != null) {
                FoodBookScreen.open(Minecraft.getInstance().player);
            }
        }) {
            @Override
            public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                int uOffset = 272, vOffset = 160, uWidth = 11, uHeight = 10;
                int itemXOffset = 8;
                if (this.isHovered) {
                    uOffset = 285;
                    uWidth = 13;
                    itemXOffset = 4;
                }

                RenderSystem.enableDepthTest();
                graphics.pose().pushPose();
                graphics.pose().translate(getX() + width, getY() + height, 0);
                graphics.pose().scale(2, 2, 1);
                graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
                graphics.blit(this.resourceLocation, 0, 0, uOffset, vOffset, uWidth, uHeight, 512, 256);
                graphics.pose().popPose();

                graphics.renderItem(SOLCarrotItems.FOOD_BOOK.get().getDefaultInstance(), getX() + itemXOffset, getY() + 2);

                if (this.isHovered) {
                    graphics.renderTooltip(Minecraft.getInstance().font, SOLCarrotItems.FOOD_BOOK.get().getDefaultInstance(), mouseX, mouseY);
                }
            }
        };
        guiEventListeners.add(imageButton);
    }

    private static void genrateSolPieBookButton(List<Book> values, int leftPos, int startX, int topPos, int startY, int solIndex, PatchouliAPI.IPatchouliAPI iPatchouliAPI, Book guiBook, List<GuiEventListener> guiEventListeners) {
        if (!ModList.get().isLoaded("solapplepie")) {
            return;
        }
        if (pieBookItem == null) {
            pieBookItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation("solapplepie", "food_book"));
        }

        int index = ModList.get().isLoaded("solcarrot") ? 1 : 0;
        ImageButton imageButton = new ImageButton(leftPos + startX, topPos + startY + (values.size() + index) * 22, 26, 20, 0, 0, DEFAULT_BOOK_TEXTURE, (b) -> {
            if (Minecraft.getInstance().player != null) {
                com.tarinoita.solsweetpotato.client.gui.FoodBookScreen.open(Minecraft.getInstance().player);
            }
        }) {
            @Override
            public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                int uOffset = 272, vOffset = 160, uWidth = 11, uHeight = 10;
                int itemXOffset = 8;
                if (this.isHovered) {
                    uOffset = 285;
                    uWidth = 13;
                    itemXOffset = 4;
                }

                RenderSystem.enableDepthTest();
                graphics.pose().pushPose();
                graphics.pose().translate(getX() + width, getY() + height, 0);
                graphics.pose().scale(2, 2, 1);
                graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
                graphics.blit(this.resourceLocation, 0, 0, uOffset, vOffset, uWidth, uHeight, 512, 256);
                graphics.pose().popPose();

                graphics.renderItem(pieBookItem.getDefaultInstance(), getX() + itemXOffset, getY() + 2);

                if (this.isHovered) {
                    graphics.renderTooltip(Minecraft.getInstance().font, pieBookItem.getDefaultInstance(), mouseX, mouseY);
                }
            }
        };
        guiEventListeners.add(imageButton);
    }

    private static void generateBookButton(List<Book> values, int leftPos, int startX, int topPos, int startY, int solIndex, PatchouliAPI.IPatchouliAPI iPatchouliAPI, Book guiBook, List<GuiEventListener> guiEventListeners) {
        int index = 0;
        for (Book book : values.subList(solIndex * SIZE, values.size() > SIZE ? (solIndex + 1) * SIZE : values.size())) {
            ResourceLocation bookTexture = book.bookTexture;
            ImageButton imageButton = new ImageButton(leftPos + startX, topPos + startY + index++ * 22, 26, 20, 0, 0, bookTexture, (b) -> iPatchouliAPI.openBookGUI(book.id)) {
                @Override
                public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                    int uOffset = 272, vOffset = 160, uWidth = 11, uHeight = 10;
                    int itemXOffset = 8;
                    if (guiBook == book || this.isHovered) {
                        uOffset = 285;
                        uWidth = 13;
                        itemXOffset = 4;
                    }

                    RenderSystem.enableDepthTest();
                    graphics.pose().pushPose();
                    graphics.pose().translate(getX() + width, getY() + height, 0);
                    graphics.pose().scale(2, 2, 1);
                    graphics.pose().mulPose(Axis.ZP.rotationDegrees(180));
                    graphics.blit(this.resourceLocation, 0, 0, uOffset, vOffset, uWidth, uHeight, 512, 256);
                    graphics.pose().popPose();

                    graphics.renderItem(book.getBookItem(), getX() + itemXOffset, getY() + 2);

                    if (this.isHovered) {
                        graphics.renderTooltip(Minecraft.getInstance().font, book.getBookItem(), mouseX, mouseY);
                    }
                }
            };
            guiEventListeners.add(imageButton);
        }
    }
}
