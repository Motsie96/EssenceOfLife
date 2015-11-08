package trittimo.essenceoflife.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import trittimo.essenceoflife.common.util.Constants;

import java.util.List;

public class ItemSoulTablet extends ModItem {

    public ItemSoulTablet(CreativeTabs creativeTab) {
        super(Constants.Items.SOUL_TABLET_INACTIVE, Constants.Items.SOUL_TABLET_INACTIVE, creativeTab);
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean advancedTooltips) {
//		infoList.add(EnumChatFormatting.BLUE + "Contains " + MobSpawnHelper.getMobCount(stack) + " mobs");
//
//		if (GuiScreen.isShiftKeyDown() && stack.hasTagCompound()) {
//			NBTTagList storedEntities = getStoredMobs(stack);
//			for (int i = 0; i < storedEntities.tagCount(); i++) {
//				NBTTagCompound mob = storedEntities.getCompoundTagAt(i);
//				infoList.add(EnumChatFormatting.DARK_PURPLE + mob.getString("mobStringName") + " captured at "
//						+ mob.getString("mobCaptureLocation"));
//			}
//
//		} else if (stack.hasTagCompound()) {
//			infoList.add(EnumChatFormatting.UNDERLINE + "Hold shift to see more information");
//		}
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {
        // TODO
        return false;
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        if (!player.getEntityWorld().isRemote) {
            if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float iX,
                             float iF, float iY) {
        // TODO
//		if (!player.getEntityWorld().isRemote) {
//			if (!stack.hasTagCompound()) {
//				return false;
//			}
//			NBTTagList mobs = NBTHelper.getTagList(stack.getTagCompound(), "storedMobs");
//			if (mobs.tagCount() > 0) {
//				NBTTagCompound mobCompound = (NBTTagCompound) mobs.removeTag(mobs.tagCount() - 1);
//				try {
//					MobSpawnHelper.spawnMob(world, mobCompound, x + 0.5d, y + 1.0d, z + 0.5d, side);
//				} catch (Exception e) {
//					System.err.println("Failed to spawn mob");
//					e.printStackTrace();
//				}
//				changeActive(stack);
//				return true;
//			}
//		}
//
        return false;
    }

}
