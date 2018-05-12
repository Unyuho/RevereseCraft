package unyuho.reversecraft.client;

import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import unyuho.reversecraft.CommonProxy;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().world;
	}
}