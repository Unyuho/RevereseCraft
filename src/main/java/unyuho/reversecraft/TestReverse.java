package unyuho.reversecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public class TestReverse
{
	public TestReverse()
	{
        for (IRecipe irecipe : CraftingManager.REGISTRY)
        {
        	NonNullList<Ingredient> ingredients = irecipe.getIngredients();

/*
        	if(!irecipe.getRecipeOutput().getDisplayName().equals("Wooden Pickaxe"))
        	{
        		continue;
        	}
*/

        	//レシピ一覧を返す処理

			if(ingredients.size() == 0)
			{
				continue;
			}

			String s = irecipe.getRecipeOutput().getDisplayName() + " : ";
			List<String> result = test(ingredients);
			for(String ss : result)
			{
				System.out.println(s + ss);
			}

        }
	}


	public List<String> test(NonNullList<Ingredient> ingredients)
	{
		List<String> result = new ArrayList<String>();

    	//レシピ一覧を返す処理
		if(ingredients.size() == 0)
		{
			return result;
		}

		//全組み合わせにならないように、同じ種類でまとめてるらしい

		//キャッシュ作成
    	ArrayList<Ingredient> cacheIndex = new ArrayList<Ingredient>();
    	HashMap<Integer, Ingredient> cache = new HashMap<Integer, Ingredient>();
    	for(Ingredient ingredient : ingredients)
    	{
    		boolean equalFlg = true;
    		ItemStack[] itemStacks = ingredient.getMatchingStacks().clone();

        	for(Ingredient cacheIngredient : cacheIndex)
        	{
        		ItemStack[] cacheItemStacks = cacheIngredient.getMatchingStacks();
        		if(itemStacks.length != cacheItemStacks.length)
        		{
        			continue;
        		}

        		equalFlg = false;
        		for(int i = 0; i < itemStacks.length; i++)
        		{
        			//System.out.println("itemStacks[" + i + "] = " + itemStacks[i].getDisplayName());
            		if(!itemStacks[i].isItemEqual(cacheItemStacks[i]))
            		{
            			equalFlg = true;
            			break;
            		}
        		}
        		if(equalFlg) break;
        	}
        	if(equalFlg)
        	{
        		//System.out.println((itemStacks.length > 0 ? itemStacks[0].getDisplayName() : "Null"));
        		cacheIndex.add(ingredient);
        		cache.put(cacheIndex.size()-1, ingredient);
        	}
    	}

    	//レシピをキャッシュのインデックスに置き換え
		Integer ingredientIndex[] = new Integer[ingredients.size()];
		for(int i = 0; i < ingredients.size(); i++)
    	{
    		Ingredient ingredient = ingredients.get(i);
    		ItemStack[] itemStacks = ingredient.getMatchingStacks();

    		int j = 0;
        	for(Ingredient cacheIngredient : cacheIndex)
        	{
        		ItemStack[] cacheItemStacks = cacheIngredient.getMatchingStacks();
        		if(itemStacks.length != cacheItemStacks.length)
        		{
        			j++;
        			continue;
        		}

        		boolean equalFlg = true;
        		for(int k = 0; k < itemStacks.length; k++)
        		{
            		if(!itemStacks[k].isItemEqual(cacheItemStacks[k]))
            		{
            			equalFlg = false;
            			break;
            		}
        		}
        		if(equalFlg)
        		{
        			break;
        		}

        		j++;
        	}

        	//System.out.println(j);
    		ingredientIndex[i] = j;
    	}

		Integer indexes[] = new Integer[cacheIndex.size()];
		Integer maxIndexes[] = new Integer[cacheIndex.size()];

		for(int i = 0; i < cacheIndex.size(); i++)
    	{
    		Ingredient ingredient = cache.get(i);
    		ItemStack[] itemstacks = ingredient.getMatchingStacks();

			indexes[i] = 0;
			maxIndexes[i] = itemstacks.length - 1;
    	}




		//レシピ組み合わせ
		int maxIndex = cacheIndex.size() - 1;
		while(indexes[0] < maxIndexes[0])
		{
			ItemStack newRecipes[] = new ItemStack[ingredientIndex.length];
			for(int i = 0; i < ingredientIndex.length; i++)
			{
        		Ingredient ingredient = cache.get(ingredientIndex[i]);
        		ItemStack[] itemstacks = ingredient.getMatchingStacks();
        		if(itemstacks.length > 0){
        			newRecipes[i] = itemstacks[indexes[ingredientIndex[i]]];
        		}
			}

			//レシピをあれこれする処理
			//String s = irecipe.getRecipeOutput().getDisplayName() + " : ";
			String s = "";
			for(ItemStack itemstack : newRecipes)
			{
				s += (itemstack != null ? itemstack.getDisplayName() : "Null") + ", ";
			}
			//System.out.println(s);
			result.add(s);

			/*
			ReverseRecipe reverseRecipe = generateReverseRecipe(recipeWidth, recipeHeight, newRecipes, recipeOutput, irecipe);
			if(reverseRecipe != null)
			{
				oreRecipeList.add(reverseRecipe);
			}
			*/

			indexes[maxIndex]++;
			for(int i = maxIndex; i > 0; i--)
			{
				if(indexes[i] >= maxIndexes[i])
				{
					indexes[i] = 0;
					indexes[i-1]++;
				}
			}
		}

		/*
		if(oreRecipeList.isEmpty())
		{
			return null;
		}
		else
		{
			return new ReverseOreRecipe(oreRecipeList);
		}
		*/

		return result;

	}



}
