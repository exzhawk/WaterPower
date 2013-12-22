package org.jackhuang.compactwatermills.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

import org.jackhuang.compactwatermills.inventory.SlotInventorySlot;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;

public class ContainerRotor extends ContainerFullInventory {
	public TileEntityBaseGenerator tileEntity;
	
	public ContainerRotor(EntityPlayer player, TileEntityBaseGenerator tileEntityCW) {
		super(player, tileEntityCW, 166);
		tileEntity = tileEntityCW;
		layoutContainer();
	}

	private void layoutContainer() {
		
		//Rotor inventory drawing
		addSlotToContainer(new SlotInventorySlot(tileEntity.invSlots.get(0), 0, 80, 26));
		
	}
}