package trittimo.essenceoflife.common.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import trittimo.essenceoflife.common.util.Constants;
import trittimo.essenceoflife.common.util.NBTHelper;

public class ItemEssenceExtractor extends ModItem {
	private IIcon active;
	private IIcon inactive;

	public ItemEssenceExtractor(CreativeTabs creativeTab) {
		super(Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME, Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME,
				creativeTab);
		this.setMaxStackSize(1);
	}

	/**
	 * Adds information regarding the number and type of mobs stored in the
	 * extractor
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
		// TODO finish addInformation method
		infoList.add(EnumChatFormatting.BLUE + "Contains " + getMobCount(stack) + " mobs");

		if (GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
			// Display all the types of mobs contained in this extractor

			NBTTagList storedEntities = NBTHelper.getTagList(stack.getTagCompound(), "storedMobs");
			for (int i = 0; i < storedEntities.tagCount(); i++) {
				NBTTagCompound mob = storedEntities.getCompoundTagAt(i);
				infoList.add(EnumChatFormatting.DARK_PURPLE + mob.getString("mobStringName") + " captured at "
						+ mob.getString("mobCaptureLocation"));
			}

		} else if (stack.hasTagCompound()) {
			infoList.add(EnumChatFormatting.UNDERLINE + "Hold shift to see more information");
		}
	}

	public int getMobCount(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getTagList(stack.getTagCompound(), "storedMobs").tagCount();
		}
		return 0;
	}

	public ArrayList<NBTTagCompound> getStoredMobs(ItemStack stack) {
		// TODO write getStoredMobs
		return null;
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
		if (!player.getEntityWorld().isRemote) {
			if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {
				storeMob(stack, entity);
				System.out.println(stack.getTagCompound().getInteger("mobCount"));
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
		// TODO write onItemUse
		if (!player.getEntityWorld().isRemote) {
			// Implementation
		}
		return false;
	};

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		this.active = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_ACTIVE_NAME);
		this.inactive = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME);
	}

	public void storeMob(ItemStack stack, Entity entity) {
		NBTTagCompound entityCompound = new NBTTagCompound();
		entity.writeToNBT(entityCompound);

		String mobType = EntityList.getEntityString(entity);

		NBTTagCompound stackCompound = stack.getTagCompound();
		if (!stack.hasTagCompound()) {
			stackCompound = new NBTTagCompound();
			stack.setTagCompound(stackCompound);
		}

		String mobLocation = "[" + ((int) entity.posX) + ", " + ((int) entity.posY) + ", " + ((int) entity.posZ) + "]";

		entityCompound.setString("mobStringName", mobType);
		entityCompound.setString("mobCaptureLocation", mobLocation);

		NBTTagList entityList;

		if (stackCompound.hasKey("storedMobs")) {
			// In the case that the mob type is already present in the
			// extractor, add it to that list
			entityList = NBTHelper.getTagList(stackCompound, "storedMobs");
		} else {
			entityList = new NBTTagList();
		}

		entityList.appendTag(entityCompound);
		stackCompound.setTag("storedMobs", entityList);

		entity.setDead();
	}

}
