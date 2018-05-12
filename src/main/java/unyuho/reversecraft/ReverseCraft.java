package unyuho.reversecraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = ReverseCraft.MOD_ID, name = ReverseCraft.MOD_NAME, version = ReverseCraft.MOD_VERSION, dependencies = ReverseCraft.MOD_DEPENDENCIES, acceptedMinecraftVersions = ReverseCraft.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)
@EventBusSubscriber
public class ReverseCraft{

    public static final String MOD_ID = "reversecraft";
    public static final String MOD_NAME = "Reversecraft";
    public static final String MOD_VERSION = "0.0.1";

    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.11-13.19.0.2130,)";

    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";

	@SidedProxy(clientSide = "unyuho.reversecraft.client.ClientProxy", serverSide = "unyuho.reversecraft.CommonProxy")
	public static CommonProxy proxy;

	@Instance("reversecraft")
	public static ReverseCraft instance;

	public static FMLEventChannel channel;


	public static boolean allrecipe = false;
	public static boolean allprint = false;


    private static int guiId = 222;
	public static int getGuiId()
	{
		return guiId;
	}

    private static ReverseCraftingManager manager;
	public static ReverseCraftingManager getInstance()
	{
		if (manager == null)
		{
			manager = new ReverseCraftingManager();
		}

		return manager;
	}



    @ObjectHolder(MOD_ID)
    public static class BLOCKS
    {
        public static Block reversecraft_block = new BlockReverseWorkbench()
                .setRegistryName(MOD_ID, "reversecraft_block")		/*登録名の設定*/
                .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)		/*クリエイティブタブの選択*/
                .setUnlocalizedName("reverseworkbench")				/*システム名の設定*/
                .setHardness(1.5F)									/*硬さ*/
                .setResistance(1.0F);								/*爆破耐性*/
    }

    @ObjectHolder(MOD_ID)
    public static class ITEMS
    {
        public static Item reversecraft_block = new ItemBlock(BLOCKS.reversecraft_block).setRegistryName(MOD_ID, "reversecraft_block");
    }

    /**
     * アイテム登録用イベント
     */
    @SubscribeEvent
    protected void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
                ITEMS.reversecraft_block
        );
    }

    /**
     * ブロック登録用イベント
     */
    @SubscribeEvent
    protected void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(
                BLOCKS.reversecraft_block
        );
    }

    @EventHandler
    public void construction(FMLConstructionEvent event)
    {
        MinecraftForge.EVENT_BUS.register(this);
        if (event.getSide().isClient())
        {
            ModelLoader.setCustomModelResourceLocation(ITEMS.reversecraft_block, 0, new ModelResourceLocation(ITEMS.reversecraft_block.getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        event.getRegistry().registerAll(
                new ShapedOreRecipe(new ResourceLocation(MOD_ID, "reverseworkbench"), ITEMS.reversecraft_block,
                        "X",
                        "Y",
                        'X', new ItemStack(Blocks.CRAFTING_TABLE),
                        'Y', new ItemStack(Items.REDSTONE)
                ).setRegistryName(MOD_ID, "reverseworkbench")
        );
    }


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());

		try
		{
			cfg.load();
			allrecipe = cfg.getBoolean("allrecipe", "Debug", false, "debug mode");
			allprint = cfg.getBoolean("allprint", "Debug", true, "debug mode");
		}
		catch (Exception e)
		{
			//FMLLog.log(Level.SEVERE, e, "Error Massage");
		}
		finally
		{
			cfg.save();
		}
    }

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, (IGuiHandler)proxy);

		channel = NetworkRegistry.INSTANCE.newEventDrivenChannel("reverse_action");
		channel.register(new PacketHandler());
	}




}
