package trittimo.essenceoflife.common.items;

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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import trittimo.essenceoflife.common.util.Constants;
import trittimo.essenceoflife.common.util.NBTHelper;

public class ItemEssenceExtractor extends ModItem {
	public ItemEssenceExtractor(CreativeTabs creativeTab) {
		super(Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME, Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME,
				creativeTab);
		this.setMaxStackSize(1);
	}

	private static int getMobCount(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getTagList(stack.getTagCompound(), "storedMobs").tagCount();
		}
		return 0;
	}

	private static Entity spawnMob(World world, NBTTagCompound mobTag, double x, double y, double z, int side)
			throws NoSuchFieldException, IllegalAccessException {
		Entity e;
		e = EntityList.createEntityFromNBT(mobTag, world);
		if (e != null) {
			e.readFromNBT(mobTag);
			int offsetX = Facing.offsetsXForSide[side];
			int offsetY = side == 0 ? -1 : 0;
			int offsetZ = Facing.offsetsZForSide[side];
			AxisAlignedBB bb = e.boundingBox;

			e.setLocationAndAngles(x + (bb.maxX - bb.minX) * 0.5 * offsetX, y + (bb.maxY - bb.minY) * 0.5 * offsetY,
					z + (bb.maxZ - bb.minZ) * 0.5 * offsetZ, world.rand.nextFloat() * 360.0F, 0.0F);

			world.spawnEntityInWorld(e);
			if (e instanceof EntityLiving) {
				((EntityLiving) e).playLivingSound();
			}

			Entity riddenByEntity = e.riddenByEntity;
			while (riddenByEntity != null) {
				riddenByEntity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);

				world.spawnEntityInWorld(riddenByEntity);
				if (riddenByEntity instanceof EntityLiving) {
					((EntityLiving) riddenByEntity).playLivingSound();
				}

				riddenByEntity = riddenByEntity.riddenByEntity;
			}
		}

		return e;
	}

	private static void storeMob(ItemStack stack, Entity entity) {
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
			entityList = NBTHelper.getTagList(stackCompound, "storedMobs");
		} else {
			entityList = new NBTTagList();
		}

		entityList.appendTag(entityCompound);
		stackCompound.setTag("storedMobs", entityList);

		entity.setDead();
	}

	private IIcon active;

	private IIcon inactive;

	/**
	 * Adds information regarding the number and type of mobs stored in the
	 * extractor
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
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
	};

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
					if (spawnMob(world, mobCompound, x, y, z, side) != null) {
						// Maybe we need to do things here?
					}
				} catch (Exception e) {
					System.err.println("Failed to spawn mob");
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void registerIcons(IIconRegister register) {
		super.registerIcons(register);
		this.active = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_ACTIVE_NAME);
		this.inactive = register.registerIcon(Constants.MOD_ID + ":" + Constants.Items.ESSENCE_EXTRACTOR_INACTIVE_NAME);
	}

}
