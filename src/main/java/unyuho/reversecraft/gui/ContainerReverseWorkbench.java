package unyuho.reversecraft.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import unyuho.reversecraft.PacketHandler;
import unyuho.reversecraft.ReverseCraft;
import unyuho.reversecraft.ReverseCraftingManager;
import unyuho.reversecraft.recipes.IReverseRecipe;

public class ContainerReverseWorkbench extends Container
{
	public InventoryReverseCrafting craftMatrix;
	public InventoryReverseCraftResult craftResult;
	public EntityPlayer entityplayer;
	public World world;
	private int posX;
	private int posY;
	private int posZ;
	private final BlockPos pos;
	private List<ItemStack> recipes;
	private List<IReverseRecipe> recipesR;
	private int recipesize;
	private int currentIndex;
	private SlotReverseCrafting slotR;

	private boolean modeReverse = false;
	private boolean modeCraft = false;

	private boolean matrixChanged = true;

	public ContainerReverseWorkbench(EntityPlayer par1Player, World par2World, int par3, int par4, int par5)
	{
		ReverseCraftingManager manager = ReverseCraft.getInstance();
		entityplayer = par1Player;
		InventoryPlayer par1InventoryPlayer = par1Player.inventory;
		craftMatrix = new InventoryReverseCrafting(this);
		craftResult = new InventoryReverseCraftResult(this);
		world = par2World;
		posX = par3;
		posY = par4;
		posZ = par5;
		pos = new BlockPos(posX, posY, posZ);
		slotR = (SlotReverseCrafting)addSlotToContainer(new SlotReverseCrafting(this, 0, 124, 35));

		for (int i = 0; i < 3; i++)
		{
			for (int l = 0; l < 3; l++)
			{
				addSlotToContainer(new SlotReverseCraftMatrix(this, l + i * 3, 30 + l * 18, 17 + i * 18));
			}
		}

		for (int j = 0; j < 3; j++)
		{
			for (int i1 = 0; i1 < 9; i1++)
			{
				addSlotToContainer(new Slot(par1InventoryPlayer, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
			}
		}

		for (int k = 0; k < 9; k++)
		{
			addSlotToContainer(new Slot(par1InventoryPlayer, k, 8 + k * 18, 142));
		}

		onCraftMatrixChanged();
	}

	@Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
	{
		if (this.world.getBlockState(this.pos).getBlock() != ReverseCraft.BLOCKS.reversecraft_block)
		{
			return false;
		}

		return par1EntityPlayer.getDistanceSq((double)posX + 0.5D, (double)posY + 0.5D, (double)posZ + 0.5D) <= 64D;
	}

	public boolean existsRecipes()
	{
		return recipesize > 1;
	}

	public boolean existsReverseRecipes()
	{
		return recipesize > 0;
	}


	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if (modeReverse && slotId >= 1 & slotId <= 9)
		{
			return ItemStack.EMPTY;
		}

		ItemStack itemstack = super.slotClick(slotId, dragType, clickTypeIn, player);

		onCraftMatrixChanged();

		return itemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);

		if (world.isRemote)
		{
			return;
		}

		ItemStack itemstack = craftResult.getStackInSlot(0);
		if (itemstack != ItemStack.EMPTY)
		{
			par1EntityPlayer.dropItem(itemstack, false);
		}
	}


	public void reverseCraft()
	{
		if (currentIndex < 0)
		{
			return;
		}

		ItemStack itemstack = craftResult.getStackInSlot(0);

		if (itemstack == ItemStack.EMPTY)
		{
			return;
		}

		IReverseRecipe irecipe = (IReverseRecipe)recipesR.get(currentIndex);
		ItemStack resultstack = irecipe.getResultRecipe().copy();
		ItemStack reversestack = itemstack.copy();
		int Size = (int)Math.ceil(reversestack.getCount() / resultstack.getCount());

		for (int i = 0; i < craftMatrix.getSizeInventory(); i++)
		{
			ItemStack craftingItemStack = craftMatrix.getStackInSlot(i);

			if (craftingItemStack != ItemStack.EMPTY)
			{
				Item craftingItem = craftingItemStack.getItem();
				ItemStack containerStack = craftingItem.getContainerItem(craftingItemStack);

				if (containerStack != ItemStack.EMPTY)
				{
					if(containerStack.getItem() != null)
					{
						//戻せる場合だけ先にアイテム消してるのか
						/*
						if (!entityplayer.inventory.consumeInventoryItem(containerStack.getItem()))
						{
							continue;
						}
						*/
						continue;
					}
					else
					{
						continue;
					}
				}

				int stackSize = craftingItemStack.getCount() * Size;

				while (stackSize > 0)
				{
					ItemStack tempItemStack = craftingItemStack.copy();

					if (stackSize > tempItemStack.getMaxStackSize())
					{
						tempItemStack.setCount(tempItemStack.getMaxStackSize());
						stackSize -= tempItemStack.getMaxStackSize();
					}
					else
					{
						tempItemStack.setCount(stackSize);
						stackSize = 0;
					}

					if (entityplayer.inventory.addItemStackToInventory(tempItemStack))
					{
						entityplayer.inventory.markDirty();
					}
					else
					{
						if (tempItemStack.getCount() > 0)
						{
							entityplayer.dropItem(tempItemStack.copy(), false);
						}
					}
				}
			}
		}

		reversestack.grow((-1)*resultstack.getCount() * Size);

		if (reversestack.getCount() < 1)
		{
			reversestack = ItemStack.EMPTY;
		}

		craftResult.setInventorySlotContents(0, reversestack);
		onCraftMatrixChanged(craftResult);
		onCraftMatrixChanged();
	}

	public void setNextRecipe(int AddValue)
	{
		if (currentIndex < 0)
		{
			return;
		}

		currentIndex = (currentIndex + AddValue + recipesize) % recipesize;
		Object obj = recipesR.get(currentIndex);

		if(obj instanceof ItemStack)
		{
			craftResult.setInventorySlotContents(0, (ItemStack)obj);
		}
		else
		{
			craftMatrix.setInventory((IReverseRecipe)obj);
		}
	}

	public void onCraftMatrixChanged()
	{
		ReverseCraftingManager manager = ReverseCraft.getInstance();
		recipesR = manager.findMatchingRecipe(craftResult.getStackInSlot(0));

		if (!recipesR.isEmpty())
		{
			recipesize = recipesR.size();
			currentIndex = 0;
			IReverseRecipe irecipe = (IReverseRecipe)recipesR.get(currentIndex);
			if(!world.isRemote) craftMatrix.setInventory(irecipe);
		}
		else
		{
			recipesize = -1;
			currentIndex = -1;
			if(!world.isRemote) craftMatrix.clearItem();
		}
	}

	public void actionPerformed(int id)
	{
		if (world.isRemote)
		{
			PacketHandler.sendPacket(id);
		}

			if (id == 0)
			{
				reverseCraft();
			}
			else if (id == 1)
			{
				setNextRecipe(1);
			}
			else if (id == 2)
			{
				setNextRecipe(-1);
			}

	}




	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot)inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 0)
			{
				if (!mergeItemStack(itemstack1, 10, 46, true))
				{
					return ItemStack.EMPTY;
				}

				if (modeCraft)
				{
					slot.onSlotChange(itemstack1, itemstack);
				}
			}
			else if (par2 >= 10 && par2 < 37)
			{
				if (!modeCraft)
				{
					if (slotR.isItemValid(itemstack1) && !mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if (!mergeItemStack(itemstack1, 1, 10, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			else if (par2 >= 37 && par2 < 46)
			{
				if (!modeCraft)
				{
					if (slotR.isItemValid(itemstack1) && !mergeItemStack(itemstack1, 0, 1, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if (!mergeItemStack(itemstack1, 1, 10, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			else if (modeCraft)
			{
				if (!mergeItemStack(itemstack1, 10, 46, false))
				{
					return ItemStack.EMPTY;
				}
			}
			else
			{
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() != itemstack.getCount())
			{
				slot.onTake(par1EntityPlayer, itemstack1);
			}
			else
			{
				return ItemStack.EMPTY;
			}
		}

		return itemstack;
	}










}
