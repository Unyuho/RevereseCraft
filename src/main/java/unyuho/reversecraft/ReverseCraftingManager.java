package unyuho.reversecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import unyuho.reversecraft.recipes.IReverseRecipe;
import unyuho.reversecraft.util.ItemUtil;

public class ReverseCraftingManager
{

	//レシピ保持
	private Map<Integer, List<IReverseRecipe>> reverseRecipeMap = new HashMap<Integer, List<IReverseRecipe>>();
	//private Map<Integer, Map<Integer, List<IReverseRecipe>>> reverseRecipeMap = new HashMap();


	//IC2の電気ツール判定クラス
	Class electricToolClass;

	//IC2の電気ツール判定クラス
	Class ic2RecipeClass;

	public ReverseCraftingManager()
	{
		/*
		try
		{
			electricToolClass = Class.forName("ic2.api.item.ElectricItem");
		}
		catch (ClassNotFoundException e)
		{
			electricToolClass = null;
		}

		try
		{
			ic2RecipeClass = Class.forName("ic2.api.recipe.IRecipeInput");
		}
		catch (ClassNotFoundException e)
		{
			ic2RecipeClass = null;
		}
		*/


		//analyzeAllRecipes();
	}


	/**
	 * ItemStackのアイテムID取得
	 * @param itemstack
	 * @return	アイテムID
	 */
	private int getItemID(ItemStack itemstack)
	{
		if(itemstack == ItemStack.EMPTY || itemstack.getItem() == null)
		{
			return -1;
		}

		return Item.getIdFromItem(itemstack.getItem());
	}

	private List<IReverseRecipe> getReverseRecipeList(ItemStack itemstack)
	{
		int itemID = getItemID(itemstack);
		if(itemID == -1) return new ArrayList();
/*
		int damage = itemstack.getItemDamage();

		Map<Integer, List<IReverseRecipe>> damageRecipeMap;

		if(reverseRecipeMap.containsKey(itemID))
		{
			damageRecipeMap = reverseRecipeMap.get(itemID);
			if(damageRecipeMap.containsKey(damage))
			{
				return damageRecipeMap.get(damage);
			}
		}
		else
		{
			damageRecipeMap = new HashMap();
		}

		List<IReverseRecipe> reverseRecipeList = RecipeGenerate.generateRecipesList(itemstack);
		damageRecipeMap.put(damage, reverseRecipeList);
		reverseRecipeMap.put(itemID, damageRecipeMap);
*/
		List<IReverseRecipe> reverseRecipeList;
		List<IReverseRecipe> matchRecipeList = new ArrayList<IReverseRecipe>();

		if(reverseRecipeMap.containsKey(itemID))
		{
			reverseRecipeList = reverseRecipeMap.get(itemID);
			System.out.println("itemID:"+itemID + ", " + reverseRecipeList.size());

			reverseRecipeList = RecipeGenerate.generateRecipesList(itemstack);
		}
		else
		{
			reverseRecipeList = RecipeGenerate.generateRecipesList(itemstack);
			reverseRecipeMap.put(itemID, reverseRecipeList);
		}

		for(IReverseRecipe reverseRecipe : reverseRecipeList)
		{
			if(ItemUtil.isItemEqual(reverseRecipe.getResultRecipe(), itemstack))
			{
				matchRecipeList.add(reverseRecipe);
			}
		}

		return matchRecipeList;
	}

    /**
     * アイテムからレシピを取得可能か判定
     *
     * @param	craftItemStack		分解対象アイテム
     * @return	レシピを取得可能か否か
     */
	public boolean matches(ItemStack craftItemStack)
	{
		System.out.println("************************* matches start *************************");

		List<IReverseRecipe> reverseRecipeList = getReverseRecipeList(craftItemStack);

		for (int k = 0; k < reverseRecipeList.size(); k++)
		{
			IReverseRecipe irecipe = (IReverseRecipe)reverseRecipeList.get(k);

			if (irecipe.matches(craftItemStack))
			{
				System.out.println("************************* matches true end *************************");
				return true;
			}
		}


		//ModRecipeReflection.printClass(craftItemStack);
		//ModRecipeReflection.printClass(craftItemStack.getItem());
		System.out.println("************************* matches false end *************************");

		return false;
	}

    /**
     * アイテムからレシピを取得する
     *
     * @param	craftItem		分解対象アイテム
     * @return	レシピ一覧
     */
	public List<IReverseRecipe> findMatchingRecipe(ItemStack craftItem)
	{
		List<IReverseRecipe> list = new ArrayList<IReverseRecipe>();

		if (craftItem != null)
		{
			List<IReverseRecipe> reverseRecipeList = getReverseRecipeList(craftItem);

			for (int k = 0; k < reverseRecipeList.size(); k++)
			{
				IReverseRecipe iRecipes = reverseRecipeList.get(k);

				if (iRecipes.matches(craftItem))
				{
					iRecipes.add(list);
				}
			}
		}

		return list;
	}

    /**
     * 増殖レシピ対策
     * @param recipes
     * @return
     */
	private boolean checkIntegrity(IReverseRecipe recipes)
	{
		ItemStack checkItemstack = recipes.getResultRecipe();
		ItemStack[] recipeItems = recipes.getCraftingRecipe();

		Item checkItem = checkItemstack.getItem();
		if(checkItem == null)
		{
			return false;
		}

		if(checkItem.getHasSubtypes()){
			//checkItem.isDamageable()
		}

		for(ItemStack recipeItemstack : recipeItems)
		{
			if(recipeItemstack != null)
			{
				Item recipeItem = recipeItemstack.getItem();
				if(recipeItem == null)
				{
					return false;
				}

				//Itemとダメージ値が同じ場合はアウト
				if (recipeItemstack.isItemEqual(checkItemstack))
				{
					return false;
				}
			}
		}

		return true;
    }
}
