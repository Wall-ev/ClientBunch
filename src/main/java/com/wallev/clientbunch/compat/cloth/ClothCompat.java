package com.wallev.clientbunch.compat.cloth;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ClothCompat {
    public static void init() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MenuIntegration.registerModsPage();
        }
    }
}
