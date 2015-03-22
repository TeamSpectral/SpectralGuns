package com.spectral.spectral_guns.components;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentGrip extends ComponentGeneric
{
	public ComponentGrip(Component[] required, Component[] incapatible, ComponentMaterial material)
	{
		super(new String2("grip", ""), new String2("grip", ""), required, incapatible, Type.GRIP, material);
	}
	
	@Override
	public void registerRecipe()
	{
		Object resource = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			resource = "plankWood";
			break;
		case IRON:
			resource = "ingotIron";
			break;
		case GOLD:
			resource = "ingotGold";
			break;
		case DIAMOND:
			resource = "gemDiamond";
			break;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item, 1), new Object[]{"# ", "# ", "##", '#', resource}));
	}
	
	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
