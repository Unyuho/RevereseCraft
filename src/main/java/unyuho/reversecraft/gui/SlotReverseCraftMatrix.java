package unyuho.reversecraft.gui;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotReverseCraftMatrix extends Slot
{
    private ContainerReverseWorkbench eventHandler;

    public SlotReverseCraftMatrix(ContainerReverseWorkbench container, int par4, int par5, int par6)
    {
        super(container.craftMatrix, par4, par5, par6);
        eventHandler = container;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return false;
    }
}
