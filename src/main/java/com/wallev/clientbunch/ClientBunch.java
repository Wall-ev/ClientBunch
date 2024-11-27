package com.wallev.clientbunch;

import com.wallev.clientbunch.compat.l2screentracker.FoodBookAccessAction;
import com.wallev.clientbunch.config.GeneralConfig;
import dev.xkmc.l2backpack.compat.PatchouliClickListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ClientBunch.MOD_ID)
public final class ClientBunch {
    public static final String MOD_ID = "client_bunch";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public ClientBunch() {
        initConfigureRegister();
        regisgerSolFood();
    }

    private static void initConfigureRegister() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());
    }

    private static void regisgerSolFood() {
        if (ModList.get().isLoaded("solcarrot") || ModList.get().isLoaded("solapplepie")) {
            new FoodBookAccessAction();
            MinecraftForge.EVENT_BUS.register(FoodBookAccessAction.class);
        }
    }
}
