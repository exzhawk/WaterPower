/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_Generic_Item;
import ic2.api.item.IC2Items;

import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jackhuang.watercraft.client.gui.CreativeTabWaterCraft;
import org.jackhuang.watercraft.client.render.RecolorableTextures;
import org.jackhuang.watercraft.common.CommonProxy;
import org.jackhuang.watercraft.common.block.BlockBase;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.machines.BlockMachines;
import org.jackhuang.watercraft.common.block.reservoir.BlockReservoir;
import org.jackhuang.watercraft.common.block.reservoir.ReservoirType;
import org.jackhuang.watercraft.common.block.reservoir.TileEntityReservoir;
import org.jackhuang.watercraft.common.block.simple.BlockOre;
import org.jackhuang.watercraft.common.block.simple.ItemOreDust;
import org.jackhuang.watercraft.common.block.turbines.BlockTurbine;
import org.jackhuang.watercraft.common.block.turbines.TileEntityTurbine;
import org.jackhuang.watercraft.common.block.watermills.BlockWatermill;
import org.jackhuang.watercraft.common.block.watermills.TileEntityWatermill;
import org.jackhuang.watercraft.common.block.watermills.WaterType;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.ItemBase;
import org.jackhuang.watercraft.common.item.crafting.ItemCrafting;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.others.ItemOthers;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.item.range.ItemPlugins;
import org.jackhuang.watercraft.common.item.range.ItemRange;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;
import org.jackhuang.watercraft.common.item.rotors.RotorType;
import org.jackhuang.watercraft.common.recipe.EasyRecipeHandler;
import org.jackhuang.watercraft.common.recipe.IRecipeHandler;
import org.jackhuang.watercraft.common.recipe.NormalRecipeHandler;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Reference.ModID, name = Reference.ModName, version = Reference.Version, dependencies = "required-after:IC2; after:gregtech; after:Thaumcraft;")
public class WaterCraft implements IWorldGenerator {

	@Mod.Instance(Reference.ModID)
	public static WaterCraft instance;

	@SidedProxy(clientSide = "org.jackhuang.watercraft.client.ClientProxy", serverSide = "org.jackhuang.watercraft.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs creativeTabWaterCraft = new CreativeTabWaterCraft(
			"creativeTabWatercraft");

	public static final int updateTick = 20;

	private Configuration config;
	
	private IRecipeHandler recipe;
	
	public IIconRegister iconRegister;

	public static boolean isGregTechLoaded, isThaumcraftLoaded, isIndustrialCraftLoaded;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		for (RotorType type : RotorType.values()) {
			type.getConfig(config);
		}

		Property updater = config.get("rule", Reference.configNeedRotor, true);
		updater.comment = "If needn't, the default effencity is 100%.";
		Reference.watermillNeedRotor = updater.getBoolean(true);

		config.save();

	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {

		isIndustrialCraftLoaded = Loader.isModLoaded("IC2");
		isGregTechLoaded = Loader.isModLoaded("gregtech");
		isThaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
		
		IRecipeHandler.initRecipeConfig(config);
		EasyRecipeHandler.initRecipeConfig(config);
		NormalRecipeHandler.initRecipeConfig(config);
		
		init();
		
		Property enableEasyRecipe = config.get("recipe", "enableEasyRecipe", false);
		if(enableEasyRecipe.getBoolean(false))
			recipe = new EasyRecipeHandler(config);
		Property enableNormalRecipe = config.get("recipe", "enableNormalRecipe", true);
		if(enableNormalRecipe.getBoolean(true))
			recipe = new NormalRecipeHandler(config);
		
		GameRegistry.registerWorldGenerator(this, 0);

		recipe.registerAllRecipes();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		config.save();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerRenderer();
	}

	protected void init() {
		
		GlobalBlocks.machine = new BlockMachines();
		
		GlobalItems.updater = new ItemOthers();
		GlobalItems.crafting = new ItemCrafting();
		GlobalItems.meterial = new ItemMaterial();
		GlobalItems.oreDust = new ItemOreDust();
		GlobalItems.range = new ItemRange();
		GlobalItems.plugins = new ItemPlugins();
		
		RotorType.initRotors(); RotorType.registerRotor();
		WaterType.initTrousers();
		
		GlobalBlocks.waterMill = new BlockWatermill();
		GlobalBlocks.turbine = new BlockTurbine();
		GlobalBlocks.reservoir = new BlockReservoir();
		new BlockOre();
	}

	/**
	 * isServer?
	 */
	public static boolean isSimulating() {
		return !FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	private static WorldGenMinable getMinable(ItemStack is, int number) {
		return new WorldGenMinable(Block.getBlockFromItem(is.getItem()), is.getItemDamage(), number, Blocks.stone);
	}
	
	private static void generateOre(ItemStack ore, int number, int baseCount,
			World world, Random random, int chunkX, int chunkZ, int low, int high) {
	    if(ore != null) {
	        int count = (int)Math.round(random.nextGaussian() * Math.sqrt(baseCount) + baseCount);

	        for (int n = 0; n < count; n++) {
	          int x = chunkX * 16 + random.nextInt(16);
	          int y = random.nextInt(high-low) + low;
	          int z = chunkZ * 16 + random.nextInt(16);
	          getMinable(ore, number).generate(world, random, x, y, z);
	        }
	    }
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        int baseHeight = world.provider.getAverageGroundLevel() + 1;
	    int baseScale = Math.round(baseHeight);
        int baseCount = 15 * baseScale / 64;
        
	    generateOre(GlobalBlocks.vanadiumOre, 2, baseCount, world, random, chunkX, chunkZ, 16, 32);
	    generateOre(GlobalBlocks.manganeseOre, 2, baseCount, world, random, chunkX, chunkZ, 16, 32);
	    generateOre(GlobalBlocks.monaziteOre, 2, baseCount, world, random, chunkX, chunkZ, 16, 32);
	    generateOre(GlobalBlocks.magnetOre, 10, baseCount, world, random, chunkX, chunkZ, 16, 32);
	    generateOre(GlobalBlocks.zincOre, 15, baseCount, world, random, chunkX, chunkZ, 0, 64);
	}
	
	@SideOnly(Side.CLIENT)
	public static World getWorld() {
		Minecraft mc = FMLClientHandler.instance().getClient();
		if(mc != null) return mc.theWorld;
		return null;
	}

	public void loadAllIcons() {
		RecolorableTextures.load();
	}
	
	public static boolean isDeobf() {
		try {
			new ChunkCoordinates(1,2,3).set(4, 5, 6);
			return true;
		} catch(Throwable e) {
			return false;
		}
	}
}
