package com.catbert.mga.mixin.fd.youkaishc;

import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlock;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KettleBlock.class)
public interface KettleBlockAccessor {

    @Final
    @Accessor(value = "MAP", remap = false)
    Lazy<Map<Ingredient, Integer>> getWaterMap$mga();


}
