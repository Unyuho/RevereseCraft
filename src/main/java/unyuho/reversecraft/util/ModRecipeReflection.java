package unyuho.reversecraft.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipeReflection
{

	private static boolean noIc2 = false;
	private static Class electricToolClass = null;
	private static Class advRecipe = null;
	private static Class advShapelessRecipe = null;

	private static Class getAdvRecipeClass()
	{
		if(noIc2) return null;

		if(advRecipe == null)
		{
			try
			{
				advRecipe = Class.forName("ic2.core.AdvRecipe");
			}
			catch (ClassNotFoundException e)
			{
				noIc2 = true;
			}
		}

		return advRecipe;
	}

	private static Class getAdvShapelessRecipeClass()
	{
		if(noIc2) return null;

		if(advShapelessRecipe == null)
		{
			try
			{
				advShapelessRecipe = Class.forName("ic2.core.AdvShapelessRecipe");
			}
			catch (ClassNotFoundException e)
			{
				noIc2 = true;
			}
		}

		return advShapelessRecipe;
	}

	public static boolean IsElectricToolClass(ItemStack itemstack)
	{
		if(noIc2) return false;

		if(electricToolClass == null)
		{
			try
			{
				electricToolClass = Class.forName("ic2.api.item.IElectricItem");
			}
			catch (ClassNotFoundException e)
			{
				noIc2 = true;
				return false;
			}
		}

		return electricToolClass.isInstance(itemstack.getItem());
	}

	private static boolean IsRecipeClass(Class modRecipeClass, IRecipe recipe)
	{
		if(modRecipeClass == null) return false;
		if(modRecipeClass.isInstance(recipe)) return true;

		return false;
	}

	private ModRecipeReflection()
	{

	}


	/**
	 * 他MODのレシピクラスとして存在しているか(IC2しか対応してないけど。。。。)
	 * @param recipe
	 * @return
	 */
	public static boolean IsRecipeClassExists(IRecipe recipe)
	{
		if(noIc2) return false;

		if(IsRecipeClass(getAdvRecipeClass(), recipe)) return true;
		if(IsRecipeClass(getAdvShapelessRecipeClass(), recipe)) return true;

		return false;
	}

	public static List getRecipeInput(IRecipe recipe)
	{
		if(noIc2) return new ArrayList();
		if(!IsRecipeClassExists(recipe)) return new ArrayList();

		//取れるはず？
		Object input[] = (Object[])getPrivateValue(recipe, "input");
		if(input == null) return new ArrayList();

		Class recipeClass = getAdvRecipeClass();
		if(recipeClass == null) return new ArrayList();


		Method expandMethod;
		try
		{
			expandMethod = recipeClass.getMethod("expand", Object.class);
		}
		catch (Exception e)
		{
			return new ArrayList();
		}

		List recipeList =  new ArrayList();

		for(Object recipeItem : input)
		{
			try
			{
				List itemList = (List)expandMethod.invoke(null, recipeItem);
				recipeList.add(itemList);
			}
			catch (Exception e)
			{
				return new ArrayList();
			}
		}

		return recipeList;
	}

	public static int getRecipeWidth(IRecipe recipe)
	{
		int recipeWidth;

		int len = recipe.getIngredients().size();
		if (len > 2)
		{
			recipeWidth = 3;
		}
		else
		{
			recipeWidth = len;
		}

		if(noIc2) return recipeWidth;
		if(!IsRecipeClass(getAdvRecipeClass(), recipe)) return recipeWidth;

		Object obj = getPrivateValue(recipe, "inputWidth");
		if(obj instanceof Integer)
		{
			recipeWidth = (Integer)obj;
		}

		return recipeWidth;
	}

	public static int getRecipeHeight(IRecipe recipe)
	{
		int recipeHeight;
		int len = recipe.getIngredients().size();
		if (len > 2)
		{
			recipeHeight = (int)Math.ceil((double)len / 3);
		}
		else
		{
			recipeHeight = 1;
		}

		if(noIc2) return recipeHeight;
		if(!IsRecipeClass(getAdvRecipeClass(), recipe)) return recipeHeight;

		Object obj = getPrivateValue(recipe, "inputHeight");
		if(obj instanceof Integer)
		{
			recipeHeight = (Integer)obj;
		}

		return recipeHeight;
	}

    /**
     * クラス指定しつつわいわい
     * @param recipe
     * @param fieldIndex
     * @return
     */
	public static Object getPrivateValue(IRecipe recipe , int fieldIndex)
	{
		Class cls = recipe.getClass();

		if (recipe instanceof ShapedRecipes)
		{
			cls = ShapedRecipes.class;
		}
		else if (recipe instanceof ShapelessRecipes)
		{
			cls = ShapelessRecipes.class;
		}
		else if (recipe instanceof ShapelessOreRecipe)
		{
			cls = ShapelessOreRecipe.class;
		}
		else if (recipe instanceof ShapedOreRecipe)
		{
			cls = ShapedOreRecipe.class;
		}

		try
		{
			Object value = ObfuscationReflectionHelper.getPrivateValue(cls, recipe, fieldIndex);
			return value;
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//System.out.println("getPrivateValue:"+irecipe.getRecipeOutput().getDisplayName());
			return null;
		}
	}

    /**
     * クラス指定しつつわいわい
     * @param recipe
     * @param fieldname
     * @return
     */
	public static Object getPrivateValue(IRecipe recipe , String fieldname)
	{
		Class cls = recipe.getClass();

		if (recipe instanceof ShapedRecipes)
		{
			cls = ShapedRecipes.class;
		}
		else if (recipe instanceof ShapelessRecipes)
		{
			cls = ShapelessRecipes.class;
		}
		else if (recipe instanceof ShapelessOreRecipe)
		{
			cls = ShapelessOreRecipe.class;
		}
		else if (recipe instanceof ShapedOreRecipe)
		{
			cls = ShapedOreRecipe.class;
		}

		try
		{
			Object value = ObfuscationReflectionHelper.getPrivateValue(cls, recipe, fieldname);
			return value;
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			//System.out.println("getPrivateValue:"+irecipe.getRecipeOutput().getDisplayName());
			return null;
		}
	}


	public static void printClass(Object obj)
	{
		StringBuilder sb = new StringBuilder();

		Class cls = obj.getClass();

		sb.append(cls.getName()+"\n");
		sb.append("{"+"\n");
		printFields(cls, obj, sb);

		while(cls != null)
		{
			cls = cls.getSuperclass();
			if(cls == Object.class)
			{
				break;
			}
			sb.append("extends " + cls.getName() + "\n");
			printFields(cls, obj, sb);
		}

		sb.append("}");

	//	System.out.println("\n" + sb.toString());
	}

	private static void printFields(Class cls, Object obj, StringBuilder sb)
	{
		Field[] fields = cls.getDeclaredFields();
	    for (Field field : fields) {

	    	sb.append("	" + field.toString() + "\n");

	        try {

	            field.setAccessible(true);
	            Object value = field.get(obj);
	            Class fieldClass = field.getType();
	            if(fieldClass == null)
	            {
	            	sb.append("		field class is null : " + value + "\n");
	            }
	            else if(fieldClass.isArray())
	            {
	            	if(value == null)
	            	{
	            		sb.append("		Array null\n");
	            	}
	            	else
	            	{
		            	int size = Array.getLength(value);
		            	for(int i = 0;i < size;i++)
		            	{
		            		sb.append("		" + Array.get(value,i) + "\n");
		            	}
	            	}
	            }
	            else
	            {
	            	sb.append("		" + value + "\n");
	            }

	        } catch (IllegalAccessException e) {
	        	sb.append("		access denied\n");
	        }
	    }
	}

}
