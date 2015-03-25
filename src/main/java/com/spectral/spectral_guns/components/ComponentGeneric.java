package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class ComponentGeneric extends Component
{
	final protected String name;
	final protected Component[] required;
	final protected Component[] incapatible;
	final protected String ITEMDAMAGE = "ItemDamage";
	
	public ComponentGeneric(String2 id, String2 name, Component[] required, Component[] incapatible, Type type, ComponentMaterial material)
	{
		super(id, type, material);
		this.name = name.s1 + "." + material.getDisplayName(type, this) + name.s2;
		this.required = required;
		this.incapatible = incapatible;
	}
	
	@Override
	public String getFancyName()
	{
		return this.name;
	}
	
	@Override
	public ArrayList<Component> getRequired()
	{
		return ArraysAndSuch.arrayToArrayList(this.required);
	}
	
	@Override
	public ArrayList<Component> getIncapatible()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatible);
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
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return delay;
	}
	
	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil;
	}
	
	@Override
	public float instability(float instability, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return instability;
	}
	
	@Override
	public float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return kickback;
	}
	
	@Override
	public float zoom(float zoom, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return zoom;
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
	protected void fireSound(Entity e, ItemStack stack, World world, EntityPlayer player)
	{
	}
	
	@Override
	public int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return null;
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
	public int durabilityDamage(ItemStack gun, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(gun);
		if(compound.hasKey(this.ITEMDAMAGE))
		{
			compound.setInteger(this.ITEMDAMAGE, 0);
		}
		if(compound.getInteger(this.ITEMDAMAGE) > this.material.durability)
		{
			ArraysAndSuch.removeFromArrayList(components, this);
			ItemGun.setComponents(gun, components);
		}
		return compound.getInteger(this.ITEMDAMAGE);
	}
	
	@Override
	public void update(ItemStack stack, World world, Entity entity, int slot, boolean isSelected, ArrayList<Component> components)
	{
	}
}
