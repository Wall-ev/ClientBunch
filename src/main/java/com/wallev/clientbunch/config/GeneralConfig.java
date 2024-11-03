package com.wallev.clientbunch.config;

import com.wallev.clientbunch.config.subconfig.RenderConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        RenderConfig.init(builder);
        return builder.build();
    }
}
