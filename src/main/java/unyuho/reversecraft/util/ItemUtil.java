package unyuho.reversecraft.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil
{
	private ItemUtil()
	{

	}

	public static boolean isItemEqual(ItemStack outputItemStack, ItemStack itemstack)
	{
		if(outputItemStack == null || outputItemStack.getItem() == null)
		{
			return false;
		}

		if(itemstack == null || itemstack.getItem() == null)
		{
			return false;
		}

		if(outputItemStack.isItemEqual(itemstack))
		{
			/*
			NBTTagCompound outputStackTagCompound = outputItemStack.getTagCompound();
			NBTTagCompound stackTagCompound = itemstack.getTagCompound();

			if(!outputItemStack.hasTagCompound())
			{
				return true;
			}

			if(outputStackTagCompound.hasNoTags())
			{
				return true;
			}

			if(!itemstack.hasTagCompound())
			{
				return false;
			}

			if(stackTagCompound.hasNoTags())
			{
				return false;
			}

			Set outputKeySet = outputStackTagCompound.getKeySet();
	        Iterator iterator = outputKeySet.iterator();

	        while (iterator.hasNext())
	        {
	            String strKey = (String)iterator.next();
	            if(!stackTagCompound.hasKey(strKey))
	            {
	            	return false;
	            }

	            if(!stackTagCompound.hasKey(strKey))
	            {
	            	return false;
	            }

	            NBTBase outputNBT = outputStackTagCompound.getTag(strKey);
	            if(!outputNBT.equals(stackTagCompound.getTag(strKey)))
	            {
	            	return false;
	           	}
	        }
			*/

			return true;
		}
		/*
		else if(itemstack.getItem() == outputItemStack.getItem() && ModRecipeReflection.IsElectricToolClass(outputItemStack))
		{
			return true;
		}
		*/

		return false;
	}


	/**
	 * ItemStackのアイテムID取得
	 * @param itemstack
	 * @return	アイテムID
	 */
	public static int getItemID(ItemStack itemstack)
	{
		if(itemstack == null || itemstack.getItem() == null)
		{
			return -1;
		}

		return Item.getIdFromItem(itemstack.getItem());
	}

	/**
	 * ArrayListから鉱石辞書のIDを取得
	 * @param stackList
	 * @return	鉱石辞書のID
	 */
	/*
	public static int getOreID(ArrayList<ItemStack> stackList)
	{
		Iterator<ItemStack> iterator = stackList.iterator();
		if (iterator.hasNext())
		{
			ItemStack itemstack = iterator.next();
			int[] oreIDs = OreDictionary.getOreIDs(itemstack);
			for(int i : oreIDs)
			{
				if(stackList.equals(OreDictionary.getOres(i)))
				{
					return i;
				}
			}
			return oreIDs[0];
		}
		else
		{
			return -1;
		}
	}
	*/
}
