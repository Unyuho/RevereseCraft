package unyuho.reversecraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class InventoryReverseCraftResult extends InventoryCraftResult implements IInventory
{
	private final NonNullList<ItemStack> stackList;
	private ContainerReverseWorkbench eventHandler;

    private final int inventoryWidth;
    private final int inventoryHeight;

	public InventoryReverseCraftResult(ContainerReverseWorkbench par1Container)
	{
        this.inventoryWidth = 1;
        this.inventoryHeight = 1;
        this.stackList = NonNullList.<ItemStack>withSize(inventoryWidth * inventoryHeight, ItemStack.EMPTY);
        this.eventHandler = par1Container;

	}

	@Override
	public String getName()
	{
		return "ReverseResult";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	@Override
	public int getSizeInventory()
	{
		return this.stackList.size();
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
	public ItemStack getStackInSlot(int index)
	{
		return index >= this.getSizeInventory() ? ItemStack.EMPTY : (ItemStack)this.stackList.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);

        if (!itemstack.isEmpty())
        {
            this.eventHandler.onCraftMatrixChanged(this);
        }

        return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.stackList, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stackList.set(index, stack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}


	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}

	@Override
	public void clear()
	{
		this.stackList.clear();
	}




	@Override
	public void markDirty()
	{
		// TODO 自動生成されたメソッド・スタブ
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
	public int getField(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getFieldCount() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}



}
