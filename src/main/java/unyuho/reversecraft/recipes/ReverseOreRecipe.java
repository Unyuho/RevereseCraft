package unyuho.reversecraft.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ReverseOreRecipe implements IReverseRecipe
{
	private IReverseRecipe firstRecipe;
	private List<IReverseRecipe> recipelist;

	public ReverseOreRecipe(List<IReverseRecipe> list)
	{
		this.recipelist = list;
		this.firstRecipe = list.get(0);
	}

	@Override
	public boolean matches(ItemStack itemstack)
	{

		System.out.println("matches : " + itemstack.getDisplayName());


		for(IReverseRecipe recipe : recipelist)
		{
			if(recipe.matches(itemstack))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public ItemStack[] getCraftingRecipe()
	{
		return this.firstRecipe.getCraftingRecipe();
	}

	@Override
	public ItemStack getResultRecipe()
	{
		return this.firstRecipe.getResultRecipe();
	}

	@Override
	public int getRecipeWidth()
	{
		return this.firstRecipe.getRecipeWidth();
	}

	@Override
	public int getRecipeHeight()
	{
		return this.firstRecipe.getRecipeHeight();
	}

	@Override
	public void add(List<IReverseRecipe> list)
	{
		//this.firstRecipe.add(list);

		for(IReverseRecipe recipe : recipelist)
		{
			list.add(recipe);
		}
	}
}
