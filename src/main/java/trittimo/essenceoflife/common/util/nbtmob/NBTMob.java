package trittimo.essenceoflife.common.util.nbtmob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public class NBTMob {
    private NBTTagCompound mobCompound;
    private Entity entity;
    private boolean isSimple;

    public NBTMob(Entity entity) {
        this.entity = entity;
    }

    public NBTMob(Entity entity, boolean isSimple) {
        this.entity = entity;
        this.isSimple = isSimple;
    }

    public NBTMob(NBTTagCompound mobCompound, boolean isSimple) {
        this.mobCompound = mobCompound;
        this.isSimple = isSimple;
    }

    public Entity constructNewEntity(World world) throws InvalidMobTag {
        try {
            if (!this.isSimple) {
                return EntityList.createEntityFromNBT(this.mobCompound, world);
            }

            String classString = this.mobCompound.getString("id");
            Class entityClass = (Class) EntityList.stringToClassMapping.get(classString);
            return (Entity) entityClass
                    .getConstructor(new Class[]{World.class})
                    .newInstance(world);
        } catch (Exception e) {
            throw new InvalidMobTag("NBTTagCompound was improperly formed");
        }
    }

    public NBTTagCompound getMobCompound() {
        if (this.mobCompound == null) {
            this.mobCompound = this.isSimple ? generateSimpleCompound() : generateFullCompound();
        }
        return this.mobCompound;
    }

    public void spawnInWorld(World world, double x, double y, double z, int side) throws InvalidMobTag {
        Entity entity = constructNewEntity(world);

        int offsetX = Facing.offsetsXForSide[side];
        int offsetY = side == 0 ? -1 : 0;
        int offsetZ = Facing.offsetsZForSide[side];
        AxisAlignedBB bb = entity.boundingBox;

        entity.setLocationAndAngles(
                x + (bb.maxX - bb.minX) * 0.5 * offsetX,
                y + (bb.maxY - bb.minY) * 0.5 * offsetY,
                z + (bb.maxZ - bb.minZ) * 0.5 * offsetZ,
                world.rand.nextFloat() * 360.0F,
                0.0F);

        if (this.isSimple) {
            entity.motionX = entity.motionY = entity.motionZ = 0.0d;
            entity.prevPosX = entity.lastTickPosX = entity.posX;
            entity.prevPosY = entity.lastTickPosY = entity.posY;
            entity.prevPosZ = entity.lastTickPosZ = entity.posZ;
            entity.fallDistance = 0.0f;
            entity.setAir(0);
            entity.onGround = true;
        }
    }

    public String getMobName() {
        if (this.entity == null) {
            // I have string : classToStringMapping
            //
        }

        // TODO
        return null;
    }

    private NBTTagCompound generateSimpleCompound() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", (String) EntityList.classToStringMapping.get(this.entity.getClass()));
        return compound;
    }

    private NBTTagCompound generateFullCompound() {
        NBTTagCompound compound = generateSimpleCompound();

        NBTTagList location = new NBTTagList();
        location.appendTag(new NBTTagDouble(this.entity.posX));
        location.appendTag(new NBTTagDouble(this.entity.posY));
        location.appendTag(new NBTTagDouble(this.entity.posZ));
        compound.setTag("location", compound);

        this.entity.writeToNBT(compound);

        return compound;
    }

    public class InvalidMobTag extends Exception {
        public InvalidMobTag(String error) {
            super(error);
        }
    }
}
