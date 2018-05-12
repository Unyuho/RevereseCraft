package unyuho.reversecraft;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import unyuho.reversecraft.gui.ContainerReverseWorkbench;


public class PacketHandler
{
	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event)
	{
		FMLProxyPacket packet = event.getPacket();

		if(packet.channel().equals("reverse_action"))
		{
			NetHandlerPlayServer handler = (NetHandlerPlayServer)event.getHandler();
			EntityPlayerMP entityplayer = handler.player;

			ByteBufInputStream bos = new ByteBufInputStream(packet.payload());
			int id;
			try
			{
				id = bos.readInt();
				if (entityplayer.openContainer instanceof ContainerReverseWorkbench)
				{
					ContainerReverseWorkbench container = (ContainerReverseWorkbench)entityplayer.openContainer;
					container.actionPerformed(id);
				}
				bos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event)
	{
	//	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
	//	ByteBufInputStream bbis = new ByteBufInputStream(event.packet.payload());
	}

	public static void sendPacket(int id)
	{
		ByteBuf data = Unpooled.buffer();
		data.writeInt(id);
		CPacketCustomPayload packet = new CPacketCustomPayload("reverse_action", new PacketBuffer(data));

		ReverseCraft.channel.sendToServer(new FMLProxyPacket(packet));
	}
}
