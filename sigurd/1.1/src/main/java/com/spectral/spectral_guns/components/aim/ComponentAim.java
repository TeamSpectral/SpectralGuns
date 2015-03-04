package com.spectral.spectral_guns.components.aim;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.String2;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentAim extends ComponentGeneric
{
	public ComponentAim(Component[] required, Component[] incapatible, ComponentMaterial material)
	{
		super(new String2("aim", ""), new String2("aim", ""), required, incapatible, Type.AIM, material);
	}

	@Override
	public float zoom(float zoom, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return zoom;
	}

	@Override
	public void registerRecipe()
	{
		
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
