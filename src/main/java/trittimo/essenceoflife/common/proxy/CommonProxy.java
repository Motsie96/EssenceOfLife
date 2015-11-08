package trittimo.essenceoflife.common.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import trittimo.essenceoflife.common.util.startup.*;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ConfigurationManager.init(event.getSuggestedConfigurationFile());
        ItemHandler.init();
        BlockHandler.init();
        EntityHandler.init();
        RecipeHandler.init();
    }

    public void init(FMLInitializationEvent event) {
        EventHandler.init();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
