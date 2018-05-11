package unyuho.reversecraft;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@Mod(modid = ReverseCraft.MOD_ID, name = ReverseCraft.MOD_NAME, version = ReverseCraft.MOD_VERSION, dependencies = ReverseCraft.MOD_DEPENDENCIES, acceptedMinecraftVersions = ReverseCraft.MOD_ACCEPTED_MC_VERSIONS, useMetadata = true)
@EventBusSubscriber
public class ReverseCraft{

    public static final String MOD_ID = "reversecraft";
    public static final String MOD_NAME = "Reversecraft";
    public static final String MOD_VERSION = "0.0.1";

    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.11-13.19.0.2130,)";

    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12.2]";

    @ObjectHolder(MOD_ID)
    public static class BLOCKS{
        public static final Block reversecraft_block = null;
    }

    @ObjectHolder(MOD_ID)
    public static class ITEMS{
        public static final Item reversecraft_block = null;
    }

    /**
     * アイテム登録用イベント
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().registerAll(
                new ItemBlock(BLOCKS.reversecraft_block).setRegistryName(MOD_ID, "reversecraft_block")
        );
    }

    /**
     * ブロック登録用イベント
     */
    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(
                new BlockReverseWorkbench()
                        .setRegistryName(MOD_ID, "reversecraft_block")		/*登録名の設定*/
                        .setCreativeTab(CreativeTabs.BUILDING_BLOCKS)		/*クリエイティブタブの選択*/
                        .setUnlocalizedName("reverseworkbench")				/*システム名の設定*/
                        .setHardness(1.5F)									/*硬さ*/
                        .setResistance(1.0F)								/*爆破耐性*/
        );
    }

    @EventHandler
    public void preInit(FMLInitializationEvent event){
        if (event.getSide().isClient())
            ModelLoader.setCustomModelResourceLocation(ITEMS.reversecraft_block, 0, new ModelResourceLocation(ITEMS.reversecraft_block.getRegistryName(), "inventory"));
    }
}
