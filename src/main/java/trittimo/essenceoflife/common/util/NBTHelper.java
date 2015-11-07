package trittimo.essenceoflife.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTHelper {

	/**
	 * Essentially a reimplementation of getTagAt, because again the default
	 * NBTTag methods are shit
	 *
	 * @param tagList
	 * @param key
	 * @return
	 */
	public static NBTBase getTagAt(NBTTagList tagList, int key) throws NoSuchFieldException, IllegalAccessException {
		Field list = tagList.getClass().getDeclaredField("tagList");
		list.setAccessible(true);
		return (NBTBase) ((ArrayList) list.get(tagList)).get(key);
	}

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
