package com.spectral.spectral_guns.components.aim;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;

public class ComponentAim extends ComponentGeneric
{
	public ComponentAim(ComponentMaterial material, Type connected)
	{
		super(new String2("aim", ""), new String2("aim", ""), connected, 0.8, 1 * 0.24F * 2, 0.8F, Type.AIM, material);
	}
	
	@Override
	public float zoom(int slot, float zoom, ItemStack stack, World world, EntityPlayer player)
	{
		return zoom;
	}
	
	@Override
	public void registerRecipe()
	{
		
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
