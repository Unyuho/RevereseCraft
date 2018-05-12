package unyuho.reversecraft.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ReverseRecipe implements IReverseRecipe
{
	private int recipeWidth;
	private int recipeHeight;
	private ItemStack recipeOutput;
	private ItemStack recipeItems[];
	public boolean enabled;

	public ReverseRecipe(int recipeWidth, int recipeHeight, ItemStack recipeItems[], ItemStack recipeOutput)
	{
		this.recipeWidth = recipeWidth;
		this.recipeHeight = recipeHeight;
		this.recipeItems = recipeItems.clone();
		this.recipeOutput = recipeOutput.copy();
		this.enabled = true;

		/*
		for(int i = 0;i < recipeItems.length;i++)
		{
			if(recipeItems[i] != null)
			{
				ItemStack recipeItemstack = recipeItems[i].copy();

				recipeItemstack.stackSize = 1;
				if(ModRecipeReflection.IsElectricToolClass(recipeItemstack) && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
					System.out.println(recipeItemstack.getDisplayName() + " : " + recipeItemstack.getItemDamage() + " , " + recipeItemstack.getMaxDamage());
					recipeItemstack.setItemDamage(recipeItemstack.getMaxDamage()-1);
				}
				else if(recipeItemstack.getHasSubtypes() && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
				//	System.out.println(recipeItemstack.getDisplayName());
					recipeItemstack.setItemDamage(0);
				}
				else if(!recipeItemstack.getItem().isDamageable() && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
					recipeItemstack.setItemDamage(0);
				}

				this.recipeItems[i] = recipeItemstack;
			}
		}
		*/
	}

	@Override
	public boolean matches(ItemStack itemstack)
	{
		if(!enabled)
		{
			return false;
		}

		ItemStack checkStack = getResultRecipe();

		if(itemstack.getCount() < checkStack.getCount())
		{
			return false;
		}


		if(itemstack.isItemEqual(checkStack))
		{
			return true;
		}
		/*
		else if(ModRecipeReflection.IsElectricToolClass(itemstack) && itemstack.getItem() == checkStack.getItem())
		{
			return true;
		}
		*/

		return false;
	}

	@Override
	public ItemStack[] getCraftingRecipe()
	{
		return recipeItems.clone();
	}

	@Override
	public ItemStack getResultRecipe()
	{
		return recipeOutput.copy();
	}

	@Override
	public int getRecipeWidth()
	{
		return recipeWidth;
	}

	@Override
	public int getRecipeHeight()
	{
		return recipeHeight;
	}

	@Override
	public void add(List<IReverseRecipe> list)
	{
		list.add(this);
	}
}
