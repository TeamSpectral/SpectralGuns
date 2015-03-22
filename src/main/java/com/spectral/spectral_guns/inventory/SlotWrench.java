package com.spectral.spectral_guns.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spectral.spectral_guns.items.ItemWrench;

public class SlotWrench extends Slot
{
	public SlotWrench(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return stack == null || stack.getItem() instanceof ItemWrench;
	}
}
