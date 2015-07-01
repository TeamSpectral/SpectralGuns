package com.spectral.spectral_guns.components;

import net.minecraft.item.ItemStack;

public interface IComponentHeatOnFire
{
	public void heatUp(int slot, ItemStack stack, double modifier);
}
