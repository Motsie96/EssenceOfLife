package trittimo.essenceoflife.common.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import trittimo.essenceoflife.common.util.Constants;
import trittimo.essenceoflife.common.util.MobSpawnHelper;
import trittimo.essenceoflife.common.util.NBTHelper;

public class ItemSoulTablet extends ModItem {
	public ItemSoulTablet(CreativeTabs creativeTab) {
		super(Constants.Items.SOUL_TABLET_INACTIVE, Constants.Items.SOUL_TABLET_INACTIVE, creativeTab);
		this.setMaxStackSize(1);
	}

	private static NBTTagList getStoredMobs(ItemStack stack) {
		return NBTHelper.getTagList(stack.getTagCompound(), "storedMobs");
	}

	private Boolean active;

	/**
	 * Adds information regarding the number and type of mobs stored in the
	 * extractor
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
		infoList.add(EnumChatFormatting.BLUE + "Contains " + MobSpawnHelper.getMobCount(stack) + " mobs");

		if (GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
			NBTTagList storedEntities = getStoredMobs(stack);
			for (int i = 0; i < storedEntities.tagCount(); i++) {
				NBTTagCompound mob = storedEntities.getCompoundTagAt(i);
				infoList.add(EnumChatFormatting.DARK_PURPLE + mob.getString("mobStringName") + " captured at "
						+ mob.getString("mobCaptureLocation"));
			}

		} else if (stack.hasTagCompound()) {
			infoList.add(EnumChatFormatting.UNDERLINE + "Hold shift to see more information");
		}
	}

	public void changeActive(ItemStack stack) {
		if (getStoredMobs(stack).tagCount() == 0) {
			this.active = false;
			this.setUnlocalizedName(Constants.Items.SOUL_TABLET_INACTIVE);
		} else {
			this.active = true;
			this.setUnlocalizedName(Constants.Items.SOUL_TABLET_ACTIVE);
		}
	};

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect(ItemStack stack, int pass) {
		if (this.active == null) {
			changeActive(stack);
		}
		return this.active;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		if (!player.getEntityWorld().isRemote) {
			if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
				MobSpawnHelper.storeMob(stack, entity);
				changeActive(stack);
				return true;
			}
		}
		return false;
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		stack.setTagCompound(new NBTTagCompound());
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float iX,
			float iF, float iY) {
		if (!player.getEntityWorld().isRemote) {
			if (!stack.hasTagCompound()) {
				return false;
			}
			NBTTagList mobs = NBTHelper.getTagList(stack.getTagCompound(), "storedMobs");
			if (mobs.tagCount() > 0) {
				NBTTagCompound mobCompound = (NBTTagCompound) mobs.removeTag(mobs.tagCount() - 1);
				try {
					MobSpawnHelper.spawnMob(world, mobCompound, x + 0.5d, y + 1.0d, z + 0.5d, side);
				} catch (Exception e) {
					System.err.println("Failed to spawn mob");
					e.printStackTrace();
				}
				changeActive(stack);
				return true;
			}
		}

		return false;
	}

}
