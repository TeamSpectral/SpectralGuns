package com.spectral.spectral_guns.components.aim;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;

public class ComponentAim extends ComponentGeneric
{
	public ComponentAim(ComponentMaterial material)
	{
		super(new String2("aim", ""), new String2("aim", ""), 0.8, 1 * 0.24F * 2, Type.AIM, material);
		this.requiredTypes = new Type[]{Type.TRIGGER};
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
