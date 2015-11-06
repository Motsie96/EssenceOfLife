package trittimo.essenceoflife.common.util.startup;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.creativetab.CreativeTabs;
import trittimo.essenceoflife.common.items.ItemEssenceExtractor;
import trittimo.essenceoflife.common.items.ModItem;

public class ItemHandler {
	public static ModItem essenceExtractor;
	
	public static void init() {
		essenceExtractor = new ItemEssenceExtractor(CreativeTabs.tabTools).init();
	}
}
