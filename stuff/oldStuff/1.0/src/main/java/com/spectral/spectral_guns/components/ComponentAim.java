package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentAim extends ComponentGeneric
{
	public ComponentAim(String id, String name, Component[] required, Component[] incapatible, ComponentMaterial material)
	{
		super("aim", "aim", required, incapatible, Type.AIM, material);
	}

	@Override
	public float zoom(float zoom, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return zoom-0.2F;
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
