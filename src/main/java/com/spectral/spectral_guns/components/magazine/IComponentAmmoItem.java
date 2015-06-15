package com.spectral.spectral_guns.components.magazine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IComponentAmmoItem
{
	public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player);
}
