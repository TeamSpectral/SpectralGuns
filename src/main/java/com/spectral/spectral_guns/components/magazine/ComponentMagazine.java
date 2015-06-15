package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;

public abstract class ComponentMagazine extends ComponentGeneric implements IComponentAmmoItem
{
	public final int capacity;
	public final float kickback;
	public final float spread;
	public final int projectileCount;
	public final float fireRate;
	
	// nbt
	public static final String AMMO = "Ammo";
	
	public ComponentMagazine(String id, String name, Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float spread, float fireRate, int projectileCount)
	{
		super(new String2("magazine_" + id, ""), new String2("magazine." + name, ""), required, incapatible, Type.MAGAZINE, material);
		this.capacity = capacity;
		this.kickback = kickback;
		this.spread = spread;
		this.projectileCount = projectileCount;
		this.fireRate = fireRate;
	}
	
	@Override
	public int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(stack);
		if(!compound.hasKey(AMMO))
		{
			compound.setInteger(AMMO, 0);
		}
		while(ammo > 0 && compound.getInteger(AMMO) < this.capacity)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO) + 1);
			--ammo;
		}
		while(ammo < 0 && compound.getInteger(AMMO) > 0)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO) - 1);
			++ammo;
		}
		
		this.capAmmo(compound);
		return ammo;
	}
	
	protected abstract Entity projectile(ItemStack stack, World world, EntityPlayer player);
	
	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		for(int i = 0; i < this.projectileCount; ++i)
		{
			e.add(this.projectile(stack, world, player));
		}
		return e;
	}
	
	@Override
	public float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return this.spread;
	}
	
	@Override
	public float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return kickback + this.kickback * 0.63F;
	}
	
	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil + this.kickback * 5.6F;
	}
	
	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return speed + 1.8F + this.kickback * 3;
	}
	
	@Override
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate + this.fireRate;
	}
	
	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
	
	@Override
	public boolean isValid(ArrayList<Component> ecs)
	{
		int count = 0;
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).type == Type.MAGAZINE)
			{
				++count;
			}
		}
		if(count > 2)
		{
			return false;
		}
		return super.isValid(ecs);
	}
	
	@Override
	public int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(stack);
		this.capAmmo(compound);
		return compound.getInteger(AMMO);
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player)
	{
		return stack.getItem() == this.ammoItem();
	}
	
	@Override
	public Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return this.ammoItem();
	}
	
	public abstract Item ammoItem();
	
	protected void capAmmo(NBTTagCompound c)
	{
		int a = c.getInteger(AMMO);
		if(c.getInteger(AMMO) > this.capacity)
		{
			c.setInteger(AMMO, this.capacity);
		}
		if(c.getInteger(AMMO) < 0)
		{
			c.setInteger(AMMO, 0);
		}
	}
	
	@Override
	public int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return this.capacity;
	}
	
	@Override
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return delay + this.capacity / 32;
	}
}
