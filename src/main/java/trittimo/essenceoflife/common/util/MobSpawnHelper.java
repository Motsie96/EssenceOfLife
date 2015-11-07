package trittimo.essenceoflife.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class MobSpawnHelper {
	public static int getMobCount(ItemStack stack) {
		if (stack.hasTagCompound()) {
			return NBTHelper.getTagList(stack.getTagCompound(), "storedMobs").tagCount();
		}
		return 0;
	}

	public static Entity spawnMob(World world, NBTTagCompound mobTag, double x, double y, double z, int side)
			throws NoSuchFieldException, IllegalAccessException {
		// TODO fix the spawnMob method so that it doesn't spawn
		// animals in walls

		Entity entity = EntityList.createEntityFromNBT(mobTag, world);
		if (entity != null) {
			entity.readFromNBT(mobTag);
			int offsetX = Facing.offsetsXForSide[side];
			int offsetY = side == 0 ? -1 : 0;
			int offsetZ = Facing.offsetsZForSide[side];
			AxisAlignedBB bb = entity.boundingBox;

			entity.setLocationAndAngles(x + (bb.maxX - bb.minX) * 0.5 * offsetX,
					y + (bb.maxY - bb.minY) * 0.5 * offsetY, z + (bb.maxZ - bb.minZ) * 0.5 * offsetZ,
					world.rand.nextFloat() * 360.0F, 0.0F);

			world.spawnEntityInWorld(entity);

			Entity riddenByEntity = entity.riddenByEntity;
			while (riddenByEntity != null) {
				riddenByEntity.setLocationAndAngles(x, y, z, world.rand.nextFloat() * 360.0F, 0.0F);

				world.spawnEntityInWorld(riddenByEntity);
				if (riddenByEntity instanceof EntityLiving) {
					((EntityLiving) riddenByEntity).playLivingSound();
				}

				riddenByEntity = riddenByEntity.riddenByEntity;
			}
		}

		return entity;
	}

	public static void storeMob(ItemStack stack, Entity entity) {
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
		entityCompound.setString("id", (String) EntityList.classToStringMapping.get(entity.getClass()));

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
}
