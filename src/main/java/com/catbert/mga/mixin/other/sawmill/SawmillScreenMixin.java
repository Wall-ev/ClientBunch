package com.catbert.mga.mixin.other.sawmill;

import net.mehvahdjukaar.sawmill.SawmillScreen;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SawmillScreen.class)
public interface SawmillScreenMixin {

    @Invoker(value = "updateSelectedIndex", remap = false)
    void updateSelectedIndex$mga();

    @Accessor(value = "filteredIndex", remap = false)
    int filteredIndex$mga();

    @Accessor(value = "searchBox", remap = false)
    EditBox searchBox$mga();

    @Invoker(value = "refreshSearchResults", remap = false)
    void refreshSearchResults$mga();
}
