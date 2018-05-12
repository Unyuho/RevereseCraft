package unyuho.reversecraft.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import unyuho.reversecraft.ReverseCraft;

public class SlotReverseCrafting extends SlotCrafting
{
    private ContainerReverseWorkbench eventHandler;
    private final IInventory craftMatrix;
    private final InventoryReverseCraftResult craftResult;
    private EntityPlayer thePlayer;

    public SlotReverseCrafting(ContainerReverseWorkbench container, int par4, int par5, int par6)
    {
        super(container.entityplayer, container.craftMatrix, container.craftResult, par4, par5, par6);
        thePlayer = container.entityplayer;
        craftMatrix = container.craftMatrix;
        craftResult = container.craftResult;
        eventHandler = container;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	return ReverseCraft.getInstance().matches(stack);
    }

}
