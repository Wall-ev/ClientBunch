package com.catbert.mga.mixin.vanilla;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(Item.class)
public interface ItemMixinAccessor {

//    @Invoker("getTooltipImage")
//    Optional<TooltipComponent> getTooltipImage$mga(ItemStack stack);

}
