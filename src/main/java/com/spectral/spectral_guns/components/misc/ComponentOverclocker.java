package com.spectral.spectral_guns.components.misc;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentOverclocker extends ComponentAddon
{
	public ComponentOverclocker()
	{
		super("overclocker", "overclocker", 0.2, 2 * 3 * 2, 1.2F, ComponentMaterial.IRON, 5);
		this.requiredTypes = new Type[]{Type.MAGAZINE};
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item, 1), new Object[]{"HRL", "RGR", "rRC", 'H', Blocks.hopper, 'R', "blockRedstone", 'L', Blocks.lever, 'G', M.gear_diamond, 'r', Items.repeater, 'C', Items.comparator}));
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate * 0.8F;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed * 2;
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player)
	{
		this.addHeat(slot, 30, stack);
		return projectiles;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil * 2.2F;
	}
	
	@Override
	public float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability * 1.8F;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback * 1.2F;
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
