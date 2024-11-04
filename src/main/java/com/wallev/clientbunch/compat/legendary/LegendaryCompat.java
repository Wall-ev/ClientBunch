package com.wallev.clientbunch.compat.legendary;

import com.anthonyhilyard.iceberg.util.Tooltips;
import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public final class LegendaryCompat {
    private LegendaryCompat() {

    }
    public static void fixTitleBreak(List<Either<FormattedText, TooltipComponent>> tooltipElements) {
        if (tooltipElements.size() <= 2) return;
        tooltipElements.removeIf(tooltipEither -> tooltipEither.right().isPresent() && tooltipEither.right().get() instanceof Tooltips.TitleBreakComponent);
        tooltipElements.add(2, Either.right(new LegendaryCompat.TitleBreakComponentRender()));
    }

    public static class TitleBreakComponentRender extends Tooltips.TitleBreakComponent{
        @Override
        public int getHeight() {
            return 2;
        }
    }
}
