package com.spectral.spectral_guns.items;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemDynamicModel
{
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModelLocation(ItemStack stack);
}
