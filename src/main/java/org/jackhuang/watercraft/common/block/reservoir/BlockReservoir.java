package org.jackhuang.watercraft.common.block.reservoir;

import gregtech.api.interfaces.IDebugableBlock;
import ic2.api.item.IC2Items;
import ic2.api.tile.IWrenchable;

import java.util.ArrayList;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.common.block.BlockMeta;
import org.jackhuang.watercraft.common.block.BlockMultiID;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.recipe.IRecipeHandler;

import thaumcraft.api.ItemApi;

import com.google.common.base.Throwables;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockReservoir extends BlockMeta implements IDebugableBlock {

	public BlockReservoir() {
		super(InternalName.cptBlockReservoir, Material.iron,
				ItemReservoir.class);

		registerReservoir();
		
		GameRegistry.registerTileEntity(TileEntityReservoir.class,
				"cptwtrml.reservoir");
	}

	@Override
	protected int getTextureIndex(IBlockAccess iBlockAccess, int x, int y,
			int z, int meta) {
		TileEntity tTileEntity = iBlockAccess.getTileEntity(x, y, z);
		if (tTileEntity instanceof TileEntityReservoir) {
			TileEntityReservoir te = (TileEntityReservoir) tTileEntity;
			if (te.type != null)
				return te.type.ordinal();
		}
		return meta;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return ReservoirType.makeTileEntity(0);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return ReservoirType.makeTileEntity(metadata);
	}

	@Override
	protected String getTextureFolder(int index) {
		return "reservoir";
	}

	@Override
	protected int maxMetaData() {
		return ReservoirType.values().length;
	}

	@Override
	public ArrayList<String> getDebugInfo(EntityPlayer aPlayer, int aX, int aY,
			int aZ, int aLogLevel) {
		ArrayList<String> al = new ArrayList<String>();
		TileEntity tileEntity = aPlayer.worldObj.getTileEntity(aX, aY, aZ);
		if (tileEntity instanceof TileEntityReservoir) {
			TileEntityReservoir te = (TileEntityReservoir) tileEntity;
			if (te.type == null)
				al.add("Type: null");
			else
				al.add("Type: " + te.type.name());
			al.add("Water: " + te.getWater());
		} else {
			al.add("Not a watermill tile entity.");
		}
		return al;
	}

	public void registerReservoir() {

		// Reservoir recipes registering
		addReservoirRecipe(new ItemStack(this, 8, 0), "logWood");
		addReservoirRecipe(new ItemStack(this, 8, 1), Blocks.stone);
		addReservoirRecipe(new ItemStack(this, 8, 2), Blocks.lapis_block);
		addReservoirRecipe(new ItemStack(this, 8, 3), "blockTin"); // Items.getItem("tinBlock"));
		addReservoirRecipe(new ItemStack(this, 8, 4), "blockCopper"); // Items.getItem("copperBlock"));
		addReservoirRecipe(new ItemStack(this, 8, 5), "blockLead"); // Items.getItem("leadBlock"));
		addReservoirRecipe(new ItemStack(this, 8, 6), Blocks.quartz_block);
		addReservoirRecipe(new ItemStack(this, 8, 7), "blockBronze"); // Items.getItem("bronzeBlock"));
		addReservoirRecipe(new ItemStack(this, 8, 8), Blocks.iron_block);
		addReservoirRecipe(new ItemStack(this, 8, 9), Blocks.nether_brick);
		addReservoirRecipe(new ItemStack(this, 8, 10), Blocks.obsidian);
		addReservoirRecipe(new ItemStack(this, 8, 12), IC2Items.getItem("machine"));
		addReservoirRecipe(new ItemStack(this, 8, 13), Blocks.gold_block);
		addReservoirRecipe(new ItemStack(this, 8, 14),
				IC2Items.getItem("carbonPlate"));
		addReservoirAdvancedRecipe(new ItemStack(this, 8, 15),
				IC2Items.getItem("advancedAlloy"));
		addReservoirAdvancedRecipe(new ItemStack(this, 8, 16),
				Blocks.emerald_block);
		addReservoirAdvancedRecipe(new ItemStack(this, 8, 17),
				Blocks.diamond_block);
		addReservoirAdvancedRecipe(new ItemStack(this, 8, 18),
				IC2Items.getItem("iridiumOre"));
		addReservoirAdvancedRecipe(new ItemStack(this, 8, 19),
				IC2Items.getItem("iridiumPlate"));
		/* Silver */addReservoirRecipe(new ItemStack(this, 8, 11),
				"blockSilver"); // GregTech_API.getGregTechBlock(0, 1, 3));
		/* Zinc */addReservoirRecipe(new ItemStack(this, 8, 20), "blockZinc"); // GregTech_API.getGregTechBlock(4,
																				// 1,
																				// 2));
		/* Brass */addReservoirRecipe(new ItemStack(this, 8, 21), "blockBrass"); // GregTech_API.getGregTechBlock(0,
																					// 1,
																					// 12));
		/* Aluminum */addReservoirRecipe(new ItemStack(this, 8, 22),
				"blockAluminum"); // GregTech_API.getGregTechBlock(0, 1,
									// 7));
		/* Steel */addReservoirAdvancedRecipe(new ItemStack(this, 8, 23),
				"blockSteel"); // GregTech_API.getGregTechBlock(0,
								// 1, 11));
		/* Invar */addReservoirRecipe(new ItemStack(this, 8, 24), "blockInvar"); // GregTech_API.getGregTechBlock(4,
																					// 1,
																					// 10));
		/* Electrum */addReservoirRecipe(new ItemStack(this, 8, 25),
				"blockElectrum"); // GregTech_API.getGregTechBlock(4, 1,
									// 1));
		/* Nickel */addReservoirRecipe(new ItemStack(this, 8, 26),
				"blockNickel"); // GregTech_API.getGregTechBlock(4, 1, 7));
		/* Osmium */addReservoirAdvancedRecipe(new ItemStack(this, 8, 27),
				"blockOsmium"); // GregTech_API.getGregTechBlock(4,
								// 1, 11));
		/* Titanium */addReservoirAdvancedRecipe(new ItemStack(this, 8, 28),
				"blockTitanium"); // GregTech_API.getGregTechBlock(0,
									// 1, 8));
		/* Platinum */addReservoirAdvancedRecipe(new ItemStack(this, 8, 29),
				"blockPlatinum"); // GregTech_API.getGregTechBlock(4,
									// 1, 5));
		/* Tungsten */addReservoirAdvancedRecipe(new ItemStack(this, 8, 30),
				"blockTungsten"); // GregTech_API.getGregTechBlock(4,
									// 1, 6));
		/* Chrome */addReservoirAdvancedRecipe(new ItemStack(this, 8, 31),
				"blockChrome"); // GregTech_API.getGregTechBlock(0,
								// 1, 9));
		/* Tungsten Steel */addReservoirAdvancedRecipe(new ItemStack(this, 8,
				32), "blockTungstenSteel"); // GregTech_API.getGregTechBlock(4,
											// 1,
											// 8));
		if (WaterCraft.isThaumcraftLoaded) {
			addReservoirAdvancedRecipe(new ItemStack(this, 8, 33),
					ItemApi.getBlock("blockCosmeticSolid", 4));
		}
	}

	void addReservoirRecipe(ItemStack output, Object S) {
		IRecipeHandler.addRecipeByOreDictionary(output, "SSS", "SIS", "SSS",
				'S', S, 'I', ItemType.ReservoirCore.item());
	}

	void addReservoirAdvancedRecipe(ItemStack output, Object S) {
		IRecipeHandler.addRecipeByOreDictionary(output, "SSS", "SIS", "SSS",
				'S', S, 'I', ItemType.ReservoirCoreAdvanced.item());
	}

}