package trittimo.essenceoflife.common.util.nbtmob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class NBTMob {
    private NBTTagCompound mobCompound;

    public NBTMob(NBTTagCompound mobCompound) {
        this.mobCompound = mobCompound;
    }

    public NBTMob(Entity entity, World world) {
        this.mobCompound = new NBTTagCompound();
        entity.writeToNBT(mobCompound);
        this.mobCompound.setString("readableName", entity.getCommandSenderName());
        this.mobCompound.setString("id", (String) EntityList.classToStringMapping.get((entity.getClass())));
        if (entity instanceof EntityMob) {
            this.mobCompound.setByte("soulType", SoulType.DARK.getTypeAsByte());
        } else if (entity instanceof EntityAnimal) {
            this.mobCompound.setByte("soulType", SoulType.LIGHT.getTypeAsByte());
        } else {
            this.mobCompound.setByte("soulType", SoulType.UNKNOWN.getTypeAsByte());
        }
    }

    public String getID() throws InvalidMobTag {
        if (!this.mobCompound.hasKey("id")) {
            throw new InvalidMobTag("id");
        }
        return this.mobCompound.getString("id");
    }

    public String getReadableName() throws InvalidMobTag {
        if (!this.mobCompound.hasKey("readableName")) {
            throw new InvalidMobTag("readableName");
        }
        return this.mobCompound.getString("readableName");
    }

    public Location getLocation() throws InvalidMobTag {
        if (!this.mobCompound.hasKey("Pos")) {
            throw new InvalidMobTag("Pos");
        }

        NBTTagList list = this.mobCompound.getTagList("Pos", 6);
        return new Location(list.func_150309_d(0), list.func_150309_d(1), list.func_150309_d(2));
    }

    public SoulType getSoulType() throws InvalidMobTag {
        if (!this.mobCompound.hasKey("soulType")) {
            throw new InvalidMobTag("soulType");
        }

        byte soulType = this.mobCompound.getByte("soulType");
        for (SoulType type : SoulType.values()) {
            if (type.getTypeAsByte() == soulType) {
                return type;
            }
        }

        return SoulType.UNKNOWN;
    }


    public enum SoulType {
        LIGHT((byte) 0), DARK((byte) 1), NEUTRAL((byte) 2), UNKNOWN((byte) -1);

        public byte soulType;

        SoulType(byte soulType) {
            this.soulType = soulType;
        }

        public byte getTypeAsByte() {
            return this.soulType;
        }
    }

    public class Location {
        public double x;
        public double y;
        public double z;

        public Location(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public class InvalidMobTag extends Exception {
        public InvalidMobTag(String error) {
            super("Failed to find attribute '" + error + "' on NBTMob");
        }
    }
}
