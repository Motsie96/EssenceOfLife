package trittimo.essenceoflife.common.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import trittimo.essenceoflife.common.util.Constants;

public class ModItem extends Item {
    private String itemName;
    private String textureName;
    private CreativeTabs creativeTab;

    public ModItem(String itemName, String textureName, CreativeTabs creativeTab) {
        this.itemName = itemName;
        this.textureName = textureName;
        this.creativeTab = creativeTab;
    }

    /**
     * Only called by ItemHandler. Used to register the item and set its most
     * important initial values (e.g. texture, unlocalized name, etc.)
     *
     * @return the ModItem for easy chaining
     */
    public ModItem init() {
        this.setTextureName(Constants.MOD_ID + ":" + this.textureName);
        this.setUnlocalizedName(this.itemName);
        this.setCreativeTab(this.creativeTab);

        GameRegistry.registerItem(this, this.itemName);
        return this;
    }
}
