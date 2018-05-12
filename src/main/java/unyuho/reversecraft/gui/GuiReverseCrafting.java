package unyuho.reversecraft.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;


public class GuiReverseCrafting extends GuiContainer
{
	private static final ResourceLocation resource = new ResourceLocation("textures/gui/container/crafting_table.png");
	private static ContainerReverseWorkbench eventHandler;

	private GuiButton nextButton;
	private GuiButton prevButton;
	private GuiButton reverseButton;
	private String strMode = "";

	public GuiReverseCrafting(EntityPlayer par1Player, World par2World, int par3, int par4, int par5)
	{
		super(new ContainerReverseWorkbench(par1Player, par2World, par3, par4, par5));
		eventHandler = (ContainerReverseWorkbench)inventorySlots;
	}

	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		reverseButton = new GuiButton(0, j + 107, k + 60, 50 , 20 , "Reverse");
		nextButton = new GuiButton(1, j + 60, k + 71, 10 , 10 , ">");
		prevButton = new GuiButton(2, j + 40, k + 71, 10 , 10 , "<");

		buttonList.add(reverseButton);
		buttonList.add(nextButton);
		buttonList.add(prevButton);

		hiddenButton(reverseButton);
		hiddenButton(nextButton);
		hiddenButton(prevButton);
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		strMode = "ReverseMode";

		if(eventHandler.existsReverseRecipes())
		{
			visibleButton(reverseButton);
		}
		else
		{
			hiddenButton(reverseButton);
		}

		if (eventHandler.existsRecipes())
		{
			visibleButton(nextButton);
			visibleButton(prevButton);
		}
		else
		{
			hiddenButton(nextButton);
			hiddenButton(prevButton);
		}
/*
		if (eventHandler.getModeReverse())
		{
			strMode = "ReverseMode";

			if(eventHandler.existsReverseRecipes())
			{
				visibleButton(reverseButton);
			}
			else
			{
				hiddenButton(reverseButton);
			}

			if (eventHandler.existsRecipes())
			{
				visibleButton(nextButton);
				visibleButton(prevButton);
			}
			else
			{
				hiddenButton(nextButton);
				hiddenButton(prevButton);
			}
		}
		else
		{
			strMode = "";

			if (eventHandler.existsRecipes())
			{
				visibleButton(nextButton);
				visibleButton(prevButton);
			}
			else
			{
				hiddenButton(nextButton);
				hiddenButton(prevButton);
			}

			hiddenButton(reverseButton);
		}
*/
	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		fontRenderer.drawString("ReverseCraft", 28, 6, 0x404040);
		//fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, (ySize - 96) + 2, 0x404040);
		//fontRenderer.drawString(strMode, 118, 20, 0x404040);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(resource);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (!par1GuiButton.enabled)
		{
			return;
		}

		eventHandler.actionPerformed(par1GuiButton.id);
	}

	private void hiddenButton(GuiButton button)
	{
		button.visible = false;
	}

	private void visibleButton(GuiButton button)
	{
		button.visible = true;
	}
}
