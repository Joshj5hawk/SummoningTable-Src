package com.joshj5hawk.main;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.joshj5hawk.block.BlockSummoningTable;
import com.joshj5hawk.handler.ConfigurationFile;
import com.joshj5hawk.handler.STFuelHandler;
import com.joshj5hawk.handler.STGUIHandler;
import com.joshj5hawk.item.ItemSummoningBook;
import com.joshj5hawk.lib.Strings;
import com.joshj5hawk.proxies.CommonProxy;
import com.joshj5hawk.recipies.STCraftingRecipies;
import com.joshj5hawk.recipies.STSmeltingRecipies;
import com.joshj5hawk.renderer.ItemRenderSummoningTable;
import com.joshj5hawk.tileentity.TileEntitySummoningTable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Strings.modid, name = Strings.name, version = Strings.version)
public class SummoningTable 
{
	//Blocks
	public static Block blockSummoningTableIdle;
	public static Block blockSummoningTableActive;
	
	// Items
	public static Item itemSummoningBook;

	//Crafting Orbs
	public static Item itemPassiveCraftingOrb;
	public static Item itemNeutralCraftingOrb;
	public static Item itemHostileCraftingOrb;
	public static Item itemSummoningCore;
	public static Item itemStackOfBooks;
	public static final int guiIDSummoningTable = 0;
	
	//Tabs
	public static CreativeTabs tabSummoningTable;
	
	@Instance(Strings.modid)
	public static SummoningTable instance;
	
	@SidedProxy(clientSide = "com.joshj5hawk.proxies.ClientProxy", serverSide = "com.joshj5hawk.proxies.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{
		//Config
		ConfigurationFile.init(preEvent.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationFile());
		
		//Renders
		proxy.initRenderers();
		
		
		//CreativeTab
		tabSummoningTable = new CreativeTabs("tabSummoningTable")
		{
			@SideOnly(Side.CLIENT)
			public Item getTabIconItem()
				{
					return Item.getItemFromBlock(SummoningTable.blockSummoningTableIdle);
				}
		};
		
		//Init
		//Blocks
		blockSummoningTableIdle = new BlockSummoningTable(false).setBlockName("blockSummoningTableIdle").setCreativeTab(tabSummoningTable).setHardness(3.5F).setBlockTextureName("");
		blockSummoningTableActive = new BlockSummoningTable(true).setBlockName("blockSummoningTableActive").setHardness(3.5F).setBlockTextureName("");
		
		//Items
		//Summoning Books
		String suffix = ConfigurationFile.lowRes ? "16" : "64";
		itemSummoningBook = new ItemSummoningBook().setUnlocalizedName("itemSummoningBook").setTextureName(Strings.modid + ":iconBook" + suffix);

		//Crafting Orbs
		itemPassiveCraftingOrb = new Item().setUnlocalizedName("itemPassiveCraftingOrb").setCreativeTab(tabSummoningTable).setTextureName(ConfigurationFile.lowRes ? Strings.modid + ":iconPassiveOrb16" : Strings.modid + ":iconPassiveOrb64").setMaxStackSize(1);
		itemNeutralCraftingOrb = new Item().setUnlocalizedName("itemNeutralCraftingOrb").setCreativeTab(tabSummoningTable).setTextureName(ConfigurationFile.lowRes ? Strings.modid + ":iconNeutralOrb16" : Strings.modid + ":iconNeutralOrb64").setMaxStackSize(1);
		itemHostileCraftingOrb = new Item().setUnlocalizedName("itemHostileCraftingOrb").setCreativeTab(tabSummoningTable).setTextureName(ConfigurationFile.lowRes ? Strings.modid + ":iconHostileOrb16" : Strings.modid + ":iconHostileOrb64").setMaxStackSize(1);
		itemSummoningCore = new Item().setUnlocalizedName("itemSummoningCore").setCreativeTab(tabSummoningTable).setTextureName(ConfigurationFile.lowRes ? Strings.modid + ":iconSummoningCore16" : Strings.modid + ":iconSummoningCore64");
		
		//Other
		itemStackOfBooks = new Item().setUnlocalizedName("itemStackOfBooks").setCreativeTab(tabSummoningTable).setTextureName(ConfigurationFile.lowRes ? Strings.modid + ":iconStackOfBooks" + suffix : Strings.modid + ":iconStackOfBooks" + suffix);
		//Register
		//blocks
		GameRegistry.registerBlock(blockSummoningTableIdle, "blockSummoningTableIdle");
		GameRegistry.registerBlock(blockSummoningTableActive, "blockSummoningTableActive");
		
		//Items
		GameRegistry.registerItem(itemSummoningBook, "itemSummoningBook");
		GameRegistry.registerItem(itemStackOfBooks, "itemStackOfBooks");
		
		GameRegistry.registerItem(itemPassiveCraftingOrb, "itemPassiveCraftingOrb");
		GameRegistry.registerItem(itemNeutralCraftingOrb, "itemNeutralCraftingOrb");
		GameRegistry.registerItem(itemHostileCraftingOrb, "itemHostileCraftingOrb");
		GameRegistry.registerItem(itemSummoningCore, "itemSummoningCore");
		
		//TileEntity
		GameRegistry.registerTileEntity(TileEntitySummoningTable.class, "summoningTable");
		
		//Renders
		proxy.initRenderers();
				
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new STGUIHandler());
		//Crafting
		STCraftingRecipies.mainRegistry();
		STSmeltingRecipies.mainRegistry();
		//Fuel Handler
		GameRegistry.registerFuelHandler(new STFuelHandler());
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent postEvent)
	{
		System.out.println(Strings.name + " version " + Strings.version + " loaded");
	}
	
}
