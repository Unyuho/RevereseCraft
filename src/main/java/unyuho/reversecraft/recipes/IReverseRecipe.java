package unyuho.reversecraft.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface IReverseRecipe
{
	public boolean matches(ItemStack itemstack);

	public ItemStack[] getCraftingRecipe();

	public ItemStack getResultRecipe();

	public int getRecipeWidth();

	public int getRecipeHeight();

	public void add(List<IReverseRecipe> list);
}
