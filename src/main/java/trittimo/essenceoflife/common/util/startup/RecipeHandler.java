package trittimo.essenceoflife.common.util.startup;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RecipeHandler {
    public static void init() {
        addItemRecipes();
        addBlockRecipes();
    }

    public static void addItemRecipes() {
        GameRegistry.addRecipe(new ItemStack(ItemHandler.essenceExtractor), "DDD",
                "DWD",
                "DDD",
                'D', Items.diamond,
                'W', Items.nether_star);
    }

    public static void addBlockRecipes() {

    }


}
