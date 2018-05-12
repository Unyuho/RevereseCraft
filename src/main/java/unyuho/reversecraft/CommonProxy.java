package unyuho.reversecraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import unyuho.reversecraft.gui.ContainerReverseWorkbench;
import unyuho.reversecraft.gui.GuiReverseCrafting;

public class CommonProxy implements IGuiHandler
{
	public World getClientWorld()
	{
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ContainerReverseWorkbench(player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GuiReverseCrafting(player, world, x, y, z);
	}
}