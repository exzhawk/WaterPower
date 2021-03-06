package org.jackhuang.watercraft.common.block.machines;

import java.util.ArrayList;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.api.BasicMachineRecipeManager;
import org.jackhuang.watercraft.api.MyRecipes;
import org.jackhuang.watercraft.client.gui.DefaultGuiIds;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGeneric;
import org.jackhuang.watercraft.common.inventory.InventorySlotProcessableGreg;
import org.jackhuang.watercraft.common.tileentity.TileEntityStandardWaterMachine;
import org.jackhuang.watercraft.util.StackUtil;

import gregtech.api.util.GT_Recipe;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityCentrifuge extends TileEntityStandardWaterMachine {

	public TileEntityCentrifuge() {
		super(80, 10 * 20, 4);

		if (WaterCraft.isGregTechLoaded)
			this.inputSlot = new InventorySlotProcessableGreg(this, "input",
					2, MyRecipes.centrifuge_gt, GT_Recipe.sSawmillRecipes);
		else
			this.inputSlot = new InventorySlotProcessableGeneric(this, "input",
					1, MyRecipes.centrifuge_gt);
	}

	public static void init() {
		MyRecipes.centrifuge_gt = new BasicMachineRecipeManager();

	}

	@Override
	public String getInventoryName() {
		return "Sawmill";
	}

	public float getWrenchDropRate() {
		return 1f;
	}

	@Override
	public int getGuiId() {
		return DefaultGuiIds.get("tileEntitySawmill").id;
	}
}
