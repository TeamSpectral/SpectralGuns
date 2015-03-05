package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.ComponentType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ComponentGeneric extends Component
{
	final protected String name;
	final protected Component[] required;
	final protected Component[] incapatible;
	final protected boolean isRequired;
	
	public ComponentGeneric(String id, String name, Component[] required, Component[] incapatible, boolean isRequired, ComponentType type)
	{
		super(id, type);
		this.name = name;
		this.required = required;
		this.incapatible = incapatible;
		this.isRequired = isRequired;
	}

	@Override
	public String getFancyName()
	{
		return name;
	}

	@Override
	public ArrayList<Component> getRequired()
	{
		return ArraysAndSuch.arrayToArrayList(required);
	}

	@Override
	public ArrayList<Component> getIncapatible()
	{
		return ArraysAndSuch.arrayToArrayList(incapatible);
	}
	
	public int setAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components, int ammo)
	{
		return ammo;
	}

	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return projectiles;
	}

	@Override
	public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
	{
		return delay;
	}

	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil;
	}

	@Override
	public float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return kickback;
	}

	@Override
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate;
	}

	@Override
	public float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return spread;
	}

	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return speed;
	}

	@Override
	protected void fireSound(Entity e, ItemStack stack, World world, EntityPlayer player) {}

	@Override
	public int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return ammo;
	}

	@Override
	public int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public boolean automatic(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return false;
	}

	@Override
	public void update(ItemStack stack, World world, Entity entity, int slot, boolean isSelected, ArrayList<Component> components) {}

	@Override
	public boolean isRequired()
	{
		return isRequired;
	}
}
