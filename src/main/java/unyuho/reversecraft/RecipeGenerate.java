package unyuho.reversecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeBookCloning;
import net.minecraft.item.crafting.RecipeFireworks;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.item.crafting.RecipesMapCloning;
import net.minecraft.item.crafting.RecipesMapExtending;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import unyuho.reversecraft.recipes.IReverseRecipe;
import unyuho.reversecraft.recipes.ReverseOreRecipe;
import unyuho.reversecraft.recipes.ReverseRecipe;
import unyuho.reversecraft.util.ModRecipeReflection;

public class RecipeGenerate
{
	private RecipeGenerate()
	{

	}

	public static ArrayList<IReverseRecipe> generateRecipesList(ItemStack itemstack)
	{
		ArrayList<IReverseRecipe> reverseRecipeList = new ArrayList<IReverseRecipe>();

		ArrayList<IRecipe> recipeList = findMatchingRecipes(itemstack);
		Iterator<IRecipe> iterator = recipeList.iterator();

		IReverseRecipe reverseRecipe = null;

		while (iterator.hasNext())
		{
			IRecipe recipe = iterator.next();

			if(!IsCraftable(recipe))
			{
				continue;
			}

			ItemStack outputItemStack = recipe.getRecipeOutput();

			if(outputItemStack == null || outputItemStack.getItem() == null)
			{
				continue;
			}

			if(recipe instanceof ShapedRecipes)
			{
				reverseRecipe = generateShapedRecipe((ShapedRecipes)recipe);
			}
			else if(recipe instanceof ShapelessRecipes)
			{
				reverseRecipe = generateShapelessRecipe((ShapelessRecipes)recipe);
			}
			else if(recipe instanceof ShapedOreRecipe)
			{
				reverseRecipe = generateShapedOre((ShapedOreRecipe)recipe);
			}
			else if(recipe instanceof ShapelessOreRecipe)
			{
				reverseRecipe = generateShapelessOre((ShapelessOreRecipe)recipe);
			}

			if(reverseRecipe == null)
			{
				reverseRecipe = generateModShapeless(recipe);
			}

			//増殖レシピ以外であれば登録
			if(reverseRecipe != null && checkIntegrity(reverseRecipe))
			{
				reverseRecipeList.add(reverseRecipe);
			}
        }

		return reverseRecipeList;
	}


    /**
     * レシピ登録
     * @param recipeWidth
     * @param recipeHeight
     * @param recipeItems
     * @param recipeOutput
     */
	private static ReverseRecipe generateReverseRecipe(int recipeWidth, int recipeHeight, ItemStack recipeItems[], ItemStack recipeOutput, IRecipe recipe)
	{
		//電気ツール判定
		recipeOutput = getOutputRecipeItem(recipeWidth, recipeHeight, recipeOutput, recipeItems, recipe);

		//調整
		ItemStack[] copyRecipeItems = new ItemStack[recipeItems.length];
		for(int i = 0;i < recipeItems.length;i++)
		{
			if(recipeItems[i] != null)
			{
				ItemStack recipeItemstack = recipeItems[i].copy();

				recipeItemstack.setCount(1);
				if(ModRecipeReflection.IsElectricToolClass(recipeItemstack) && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
					//System.out.println(recipeItemstack.getDisplayName() + " : " + recipeItemstack.getItemDamage() + " , " + recipeItemstack.getMaxDamage());
					recipeItemstack.setItemDamage(recipeItemstack.getMaxDamage()-1);
				}
				else if(recipeItemstack.getHasSubtypes() && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
					recipeItemstack.setItemDamage(0);
				}
				else if(!recipeItemstack.getItem().isDamageable() && recipeItemstack.getItemDamage() == Short.MAX_VALUE)
				{
					recipeItemstack.setItemDamage(0);
				}

				copyRecipeItems[i] = recipeItemstack;
			}
		}

		ReverseRecipe reverserecipe = new ReverseRecipe(recipeWidth, recipeHeight, copyRecipeItems, recipeOutput);

		return reverserecipe;
    }

	/**
	 * 鉱石辞書用のレシピ登録
	 * @param recipeOutput
	 * @param recipeItems
	 * @param oreList
	 */
	private static IReverseRecipe generateOreReverseRecipes(int recipeWidth, int recipeHeight, ItemStack recipeOutput, IRecipe irecipe)
	{
		NonNullList<Ingredient> ingredients = irecipe.getIngredients();

    	//レシピ一覧を返す処理
		if(ingredients.size() == 0)
		{
			return null;
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
			if(maxIndexes[0] == -1)
			{
				maxIndexes[0] = 0;
			}
			/*
			if(maxIndexes[i] > -1)
			{
				maxIndexes[0] = maxIndexes[i] > 0 ? maxIndexes[i] : 0;
			}
			*/
    	}

		//レシピ組み合わせ
		int maxIndex = cacheIndex.size() - 1;
		List oreRecipeList = new ArrayList<IReverseRecipe>();

		while(indexes[0] <= maxIndexes[0])
		{
			ItemStack newRecipes[] = new ItemStack[ingredientIndex.length];
			for(int i = 0; i < ingredientIndex.length; i++)
			{
        		Ingredient ingredient = cache.get(ingredientIndex[i]);
        		ItemStack[] itemstacks = ingredient.getMatchingStacks();
        		if(itemstacks.length > 0){
        			System.out.println("newRecipes["+i+"] = itemstacks[indexes["+ingredientIndex[i]+"]];");
        			newRecipes[i] = itemstacks[indexes[ingredientIndex[i]]];
        			if(newRecipes[i] == null)
        			{
        				newRecipes[i] = ItemStack.EMPTY;
        			}
        		}
			}

			//レシピをあれこれする処理
			ReverseRecipe reverseRecipe = generateReverseRecipe(recipeWidth, recipeHeight, newRecipes, recipeOutput, irecipe);
			if(reverseRecipe != null)
			{
				oreRecipeList.add(reverseRecipe);
			}

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

		if(oreRecipeList.isEmpty())
		{
			return null;
		}
		else
		{
			return new ReverseOreRecipe(oreRecipeList);
		}
	}

	/**
     * ShapedRecipesクラス用のリバースレシピ作成
     *
     * @param	irecipe		調査対象のレシピ
	 * @return	リバースレシピ
	 */
	private static IReverseRecipe generateShapedRecipe(ShapedRecipes recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();

		//レシピクラスから情報取得
		int recipeWidth = recipe.recipeWidth;
		int recipeHeight = recipe.recipeHeight;

		return generateOreReverseRecipes(recipeWidth, recipeHeight, recipeOutput, recipe);
	}

	/**
     * ShapelessRecipesクラス用のレシピ調査メソッド
     *
     * @param	recipe		調査対象のレシピ
	 * @return	リバースレシピ
	 */
	private static IReverseRecipe generateShapelessRecipe(ShapelessRecipes recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();

		int recipeWidth = 0;
		int recipeHeight = 0;
		int length = recipe.recipeItems.size();
		if (length > 2)
		{
			recipeWidth = 3;
			recipeHeight = (int)Math.ceil((double)length / 3);
		}
		else
		{
			recipeWidth = length;
			recipeHeight = 1;
		}

		return generateOreReverseRecipes(recipeWidth, recipeHeight, recipeOutput, recipe);
	}


    /**
     * ShapelessOreRecipesクラス用のレシピ調査メソッド
     *
     * @param	recipe		調査対象のレシピ
     */
	private static IReverseRecipe generateShapedOre(ShapedOreRecipe recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();

		try{
			//レシピクラスから情報取得
			int recipeWidth = recipe.getRecipeWidth();
			int recipeHeight = recipe.getRecipeHeight();

			IReverseRecipe reverseRecipe = generateOreReverseRecipes(recipeWidth, recipeHeight, recipeOutput, recipe);

			return reverseRecipe;

		}
		catch(Exception e)
		{
			e.printStackTrace();

			System.out.println("generateShapedOre:"+recipe.getClass().getName()+ " : " + recipeOutput.getDisplayName());
			System.out.println(e.getMessage());

			return null;
		}
    }


    /**
     * ShapelessOreRecipesクラス用のレシピ調査メソッド
     *
     * @param	recipe		調査対象のレシピ
     */
	private static IReverseRecipe generateShapelessOre(ShapelessOreRecipe recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();

		int recipeWidth;
		int recipeHeight;
		int len = recipe.getIngredients().size();
		if (len > 2)
		{
			recipeWidth = 3;
			recipeHeight = (int)Math.ceil((double)len / 3);
		}
		else
		{
			recipeWidth = len;
			recipeHeight = 1;
		}

		IReverseRecipe reverseRecipe = generateOreReverseRecipes(recipeWidth, recipeHeight, recipeOutput, recipe);
		return reverseRecipe;
	}


	private static ArrayList<Class> clsArray = new ArrayList<Class>();
	/**
	 * ItemStackから作成可能レシピ一覧の取得
	 * @param itemstack	クラフト後アイテム
	 * @return 作成可能レシピ一覧
	 */
	private static ArrayList<IRecipe> findMatchingRecipes(ItemStack itemstack)
	{
		if(itemstack == null || itemstack.getItem() == null)
		{
			return null;
		}

		Item item = itemstack.getItem();

		//int itemID = getItemID(itemstack);
		ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>();

		Iterator<IRecipe> iterator = CraftingManager.REGISTRY.iterator();
		while(iterator.hasNext())
		{
			IRecipe recipe = iterator.next();

			//クラフト可能か
			if(!IsCraftable(recipe))
			{
				continue;
			}

			ItemStack outputItemStack = recipe.getRecipeOutput();
			if(outputItemStack != null)
			{
				if(outputItemStack.getItem() == itemstack.getItem())
				{
					recipeList.add(recipe);

					if(!clsArray.contains(recipe.getClass()))
					{
						clsArray.add(recipe.getClass());
					//	ModRecipeReflection.printClass(recipe);
					}
					else if(ReverseCraft.allprint)
					{
						ModRecipeReflection.printClass(recipe);
					}


				}
				else if(item == outputItemStack.getItem() && ModRecipeReflection.IsElectricToolClass(outputItemStack))
				{
					//電気ツールの場合だけ、とりあえず戻せるようにしとくかなぁ
					Item outputItem = outputItemStack.getItem();
					if(item.isDamageable())
					{
						recipeList.add(recipe);

						if(!clsArray.contains(recipe.getClass()))
						{
							clsArray.add(recipe.getClass());
						//	ModRecipeReflection.printClass(recipe);
						}
						else if(ReverseCraft.allprint)
						{
							ModRecipeReflection.printClass(recipe);
						}
					}
				}
			}
		}

		if(recipeList.isEmpty())
		{
			return recipeList;
		}
		else
		{
			return recipeList;
		}
	}

	/**
	 * クラフトできるアイテムか
	 * @param recipe
	 * @return	True:クラフト可能	False:地図拡張など
	 */
	private static boolean IsCraftable(IRecipe recipe)
	{
		//対象外
		if(recipe instanceof RecipesMapExtending)
		{
			return false;
		}

		if(recipe instanceof RecipesArmorDyes)
		{
			return false;
		}

		if(recipe instanceof RecipeFireworks)
		{
			return false;
		}

		if(recipe instanceof RecipesMapCloning)
		{
			return false;
		}

		if(recipe instanceof RecipeBookCloning)
		{
			return false;
		}

		return true;
	}


    /**
     * MODの独自レシピクラス用のレシピ調査メソッド
     *
     * @param	recipe		調査対象のレシピ
     * @param	inv			調査対象のレシピ
     * @return
     **/
	private static IReverseRecipe generateModShapeless(IRecipe recipe)
	{
		ItemStack recipeOutput = recipe.getRecipeOutput();
		if (recipeOutput.getItem() == null)
		{
			return null;
		}

		try
		{
			int recipeWidth;
			int recipeHeight;
			int len = recipe.getIngredients().size();
			if (len > 2)
			{
				recipeWidth = 3;
				recipeHeight = (int)Math.ceil((double)len / 3);
			}
			else
			{
				recipeWidth = len;
				recipeHeight = 1;
			}

			IReverseRecipe reverseRecipe = generateOreReverseRecipes(recipeWidth, recipeHeight, recipeOutput, recipe);
			return reverseRecipe;

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("generateModShapeless:"+recipeOutput.getDisplayName());
			ModRecipeReflection.printClass(recipe);
			return null;
		}
	}



	/**
	 * 電気ツール判定
	 * @param recipeOutput
	 * @param recipeItems
	 * @param irecipe
	 * @return
	 */
	private static ItemStack getOutputRecipeItem(int recipeWidth, int recipeHeight, ItemStack recipeOutput, ItemStack recipeItems[], IRecipe irecipe)
	{
		/*
		Class electricToolClass = ReverseCraft.getElectricToolClass(recipeOutput);
		if (electricToolClass != null && electricToolClass.isInstance(recipeOutput.getItem()))
		{

			try{

				Method method = electricToolClass.getMethod ("getMaxCharge", ItemStack.class);
				method.invoke(recipeOutput, recipeOutput);
			} catch (Exception e) {
			}

			getMaxCharge	引数なしか？？？？？



		}
		*/
		/*

		try
		{
			electricToolClass = Class.forName("ic2.api.item.IElectricItem");
		}
		catch (ClassNotFoundException e)
		{
			electricToolClass = null;
		}


		http://ic2api.player.to/ic2api/html/interfaceic2_1_1api_1_1item_1_1_i_electric_item.html

		int ic2.api.item.IElectricItem.getMaxCharge	(	ItemStack 	itemStack	)

		//IC2の電気ツール用
		//ダメージ値の最小値が0ではないため、実際にクラフトして取得する
		if (electricToolClass != null && electricToolClass.isInstance(recipeOutput.getItem()))
		{
			ItemStack[] stacks = new ItemStack[9];
			System.arraycopy(recipeItems, 0, stacks, 0, recipeItems.length);
			InventoryCrafting inventory = new InventoryCrafting(new ContainerDummy(), 3, 3);
			ObfuscationReflectionHelper.setPrivateValue(InventoryCrafting.class, inventory, stacks, 0);
			recipeOutput = irecipe.getCraftingResult(inventory);
		}

		*/
/*
		try
		{

			InventoryDummy inventory = new InventoryDummy(new ContainerDummy(), 3, 3);
			for(int cnt = 0;cnt < recipeItems.length;cnt++)
			{
				inventory.setInventorySlotContents(cnt, recipeItems[cnt]);
			}
			ItemStack recipeOutput2 = irecipe.getCraftingResult(inventory);

			System.out.println("recipe:" + irecipe.getClass().getName() + ", matches : " + irecipe.matches(inventory, null));

			if(recipeOutput2 != null)
			{
				recipeOutput = recipeOutput2;
				System.out.println("hit : " + recipeOutput.getDisplayName());
			}
			else
			{
				String s = "recipeWidth : " + recipeWidth + " , recipeHeight : " + recipeHeight + "[";
				for(ItemStack itemstack : recipeItems)
				{
					if(itemstack != null)
					{
						s += itemstack.getDisplayName() + ":" + itemstack.getItemDamage() + ", ";
					}
					else
					{
						s += "null, ";
					}

				}
				System.out.println(s);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
*/
		return recipeOutput;
	}

    /**
     * 増殖レシピ対策
     * @param recipes
     * @return
     */
	private static boolean checkIntegrity(IReverseRecipe reverseRecipe)
	{
		if(reverseRecipe == null) return false;

		ItemStack checkItemstack = reverseRecipe.getResultRecipe();
		ItemStack[] recipeItems = reverseRecipe.getCraftingRecipe();

		Item checkItem = checkItemstack.getItem();
		if(checkItem == null)
		{
			return false;
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
