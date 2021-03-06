/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.item.crafting;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.ItemBase;
import org.jackhuang.watercraft.common.recipe.IRecipeHandler;

public class ItemCrafting extends ItemBase {
	
	public static ItemCrafting instance;

	public ItemCrafting() {
		super(InternalName.cptItemCrafting);
		setHasSubtypes(true);
		// 
		instance = this;
	}

	@Override
	public String getTextureFolder() {
		return "crafting";
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		int meta = par1ItemStack.getItemDamage();
		int craftingType = meta / CraftingTypes.space;
		int levelType = meta % CraftingTypes.space;
		String temp = "cptwtrml.crafting." + LevelTypes.values()[levelType].name() + CraftingTypes.values()[craftingType].name();
		String temp2 = StatCollector.translateToLocal(temp);
		if(temp2.equals(temp))
			return LevelTypes.values()[levelType].getShowedName() + " " +
					CraftingTypes.values()[craftingType].getShowedName();
		else
			return temp2;
	}
	
	@Override
	public boolean stopScanning(ItemStack stack) {
		return false;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		try {
			int meta = itemstack.getItemDamage();
			int craftingType = meta / CraftingTypes.space;
			int levelType = meta % CraftingTypes.space;
			return "item." + "watermill." + CraftingTypes.values()[craftingType].name() +
					"." + LevelTypes.values()[levelType].name();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ItemStack get(CraftingTypes craftingTypes, LevelTypes levelTypes) {
		return new ItemStack(instance, 1, craftingTypes.ind() + levelTypes.ordinal());
	}
	
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs,
			List par3List) {
		for(CraftingTypes c : CraftingTypes.values())
			for(LevelTypes l : LevelTypes.values())
				par3List.add(get(c, l));
	}
	
}