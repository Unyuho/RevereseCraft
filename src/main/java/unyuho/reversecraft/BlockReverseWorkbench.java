package unyuho.reversecraft;

import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockReverseWorkbench extends BlockWorkbench
{
	public BlockReverseWorkbench()
	{
		super();
	}

	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            //playerIn.displayGui(new BlockWorkbench.InterfaceCraftingTable(worldIn, pos));
            //playerIn.openGui(ReverseCraft.instance, ReverseCraft.getGuiId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
    }
/*
	@Override
	public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer entityplayer, int par6, float par7, float par8, float par9)
	{
		entityplayer.openGui(ReverseCraft.instance, ReverseCraft.getGuiId(), world, posX, posY, posZ);
		return true;
	}
*/
}