package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentScope extends ComponentGeneric
{
	public final float zoom;
	
	public ComponentScope(Component[] required, Component[] incapatible, ComponentMaterial material, float zoom)
	{
		super("scope", "scope", required, incapatible, Type.AIM, material);
		this.zoom = zoom;
	}

	@Override
	public float zoom(float zoom, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return zoom*this.zoom;
	}

	@Override
	public void registerRecipe()
	{
		switch(material)
		{
		case WOOD: GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.lens_convex, 'B', M.barrel_thin_wood.item, 'G', M.gear_wood}); break;
		case IRON: GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_wood.item, 'B', M.barrel_thin_iron.item, 'G', M.gear_iron}); break;
		case GOLD: GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_iron.item, 'B', M.barrel_thin_gold.item, 'G', M.gear_gold}); break;
		case DIAMOND: GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_gold.item, 'B', M.barrel_thin_diamond.item, 'G', M.gear_diamond}); break;
		}
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
