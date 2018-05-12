package unyuho.reversecraft.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import unyuho.reversecraft.recipes.IReverseRecipe;

public class InventoryReverseCrafting extends InventoryCrafting implements IInventory
{
	public final NonNullList<ItemStack> stackList;
	private ContainerReverseWorkbench eventHandler;
    private final int inventoryWidth;
    private final int inventoryHeight;

	public InventoryReverseCrafting(ContainerReverseWorkbench par1Container)
	{
		super(par1Container, 3, 3);
		inventoryWidth = 3;
		inventoryHeight = 3;
		this.stackList = NonNullList.<ItemStack>withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
		eventHandler = par1Container;
	}

	public void clearItem()
	{
		for (int i = 0; i < stackList.size(); i++)
		{
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
	}

	public void setInventory(IReverseRecipe irecipe)
	{
		int Height = irecipe.getRecipeHeight();
		int Width = irecipe.getRecipeWidth();
		ItemStack recipe[] = irecipe.getCraftingRecipe();
		int cnt = 0;
		//念のため初期化
		clearItem();
		try{
			for (int i = 0; i < Height; i++)
			{
				for (int j = 0; j < Width; j++)
				{
					int Index = (i * inventoryWidth) + j;

					if (cnt < recipe.length && Index < getSizeInventory())
					{
						ItemStack stack = recipe[cnt++];
						setInventorySlotContents(Index, stack == null ? ItemStack.EMPTY : stack);
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


/*
	@Override
	public int getSizeInventory()
	{
		return stackList.size();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack)this.stackList.get(index);
	}

    public ItemStack getStackInRowAndColumn(int row, int column)
    {
        return row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight ? this.getStackInSlot(row + column * this.inventoryWidth) : ItemStack.EMPTY;
    }

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (!eventHandler.getModeCraft())
		{
			return ItemStack.EMPTY;
		}

        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);

        if (!itemstack.isEmpty())
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;

	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
        this.stackList.set(index, stack);

		if (!eventHandler.getModeReverse())
		{
			eventHandler.onCraftMatrixChanged(this);
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}


	public void clearItem()
	{
		for (int i = 0; i < stackList.size(); i++)
		{
			setInventorySlotContents(i, ItemStack.EMPTY);
		}
	}

	public void setInventory(IReverseRecipe irecipe)
	{
		int Height = irecipe.getRecipeHeight();
		int Width = irecipe.getRecipeWidth();
		ItemStack recipe[] = irecipe.getCraftingRecipe();
		int cnt = 0;
		//念のため初期化
		clearItem();
		try{
			for (int i = 0; i < Height; i++)
			{
				for (int j = 0; j < Width; j++)
				{
					int Index = (i * inventoryWidth) + j;

					if (cnt < recipe.length && Index < getSizeInventory())
					{
						setInventorySlotContents(Index, recipe[cnt++]);
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}



	@Override
	public void markDirty()
	{
		if (!eventHandler.getModeReverse())
		{
			eventHandler.onCraftMatrixChanged();
		}
	}


	@Override
	public String getName()
	{
		return "ReverseCrafting";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public boolean isEmpty()
	{
        for (ItemStack itemstack : this.stackList)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.stackList, index);
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getField(int id)
	{
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	@Override
    public void clear()
    {
        this.stackList.clear();
    }
*/
}
