package com.spectral.spectral_guns.components.magazine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IComponentProjectileCount
{
	public int projectileCount(int slot, ItemStack stack, World world, EntityPlayer player);
}
