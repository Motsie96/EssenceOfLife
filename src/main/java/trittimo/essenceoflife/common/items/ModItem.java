package trittimo.essenceoflife.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModItem extends Item {
	private static String itemName;
	private static String textureName;
	private static CreativeTabs creativeTab;
	
	public ModItem(String itemName, String textureName, CreativeTabs creativeTab) {
		this.itemName = itemName;
		this.textureName = textureName;
		this.creativeTab = creativeTab;
	}
	
	public ModItem init() {
		this.setTextureName(textureName);
		this.setUnlocalizedName(itemName);
		this.setCreativeTab(creativeTab);
		
		return this;
	}
}
