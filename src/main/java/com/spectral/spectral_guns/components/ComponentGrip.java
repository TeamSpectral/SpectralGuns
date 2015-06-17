package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentGrip extends ComponentGeneric
{
	public final float instabilityMultiplier;
	public final float recoilMultiplier;
	public final float kickbackMultiplier;
	
	public ComponentGrip(ComponentMaterial material, float instabilityMultiplier, float recoilMultiplier, float kickbackMultiplier)
	{
		super(new String2("grip", ""), new String2("grip", ""), 0.34, 3 * 9 * 1, Type.GRIP, material);
		this.instabilityMultiplier = instabilityMultiplier;
		this.recoilMultiplier = recoilMultiplier;
		this.kickbackMultiplier = kickbackMultiplier;
		this.requiredTypes = new Type[]{Type.TRIGGER};
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
	public float instability(float instability, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return instability * this.instabilityMultiplier;
	}
	
	@Override
	public float recoil(float instability, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return instability * this.recoilMultiplier;
	}
	
	@Override
	public float kickback(float instability, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return instability * this.kickbackMultiplier;
	}
	
	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
