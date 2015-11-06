package trittimo.essenceoflife.common.items;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trittimo.essenceoflife.common.util.Constants;

public class ItemEssenceExtractor extends ModItem {
	private boolean containsMob = false;
	private IIcon active;
	private IIcon inactive;
	
	public ItemEssenceExtractor(CreativeTabs creativeTab) {
		super(Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME, Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME, creativeTab);
		this.setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		MovingObjectPosition position = Minecraft.getMinecraft().objectMouseOver;
		if (position != null && position.entityHit != null) {
			this.containsMob = true;
			this.setUnlocalizedName(Constants.Items.ESSENCE_EXTRACTOR_ACTIVE_NAME);
			this.itemIcon = active;
		} else if (position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			this.containsMob = false;
			this.setUnlocalizedName(Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME);
			this.itemIcon = inactive;
		}
		
		return item;
	}
	
	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		this.active = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_ACTIVE_NAME);
		this.inactive = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME);
	}

}
