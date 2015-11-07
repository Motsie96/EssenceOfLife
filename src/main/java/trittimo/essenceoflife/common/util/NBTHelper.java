package trittimo.essenceoflife.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {

	/**
	 * Necessary because the default getTagList is shit
	 *
	 * @param compound
	 * @param key
	 */
	public static NBTTagList getTagList(NBTTagCompound compound, String key) {
		if (compound.hasKey(key)) {
			return (NBTTagList) compound.getTag(key);
		}
		return new NBTTagList();
	}

}
