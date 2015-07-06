package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.ComponentEvents;

public abstract class ComponentMagazineStandard extends ComponentMagazine
{
	public final float kickback;
	public final float spread;
	public final int projectileCount;
	public final float fireRate;
	
	public ComponentMagazineStandard(String id, String name, double heatLoss, float heatThreshold, ComponentMaterial material, int capacity, float kickback, float spread, float fireRate, int projectileCount, float heating)
	{
		super(id, name, heatLoss, heatThreshold, material, capacity, heating);
		this.kickback = kickback;
		this.spread = spread;
		this.projectileCount = projectileCount;
		this.fireRate = fireRate;
	}
	
	protected abstract Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player);
	
	public int projectileCount(ItemStack stack, World world, EntityPlayer player)
	{
		return this.projectileCount;
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player)
	{
		for(int i = 0; i < this.projectileCount(stack, world, player); ++i)
		{
			e.add(this.projectile(slot, stack, world, player));
		}
		ComponentEvents.heatUp(stack, player, 0.5);
		return e;
	}
	
	@Override
	public float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player)
	{
		return this.spread;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback + this.kickback * 0.63F;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil + this.kickback * 5.6F;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed + 1.8F + this.kickback * 3;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate + this.fireRate;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.capacity / 32;
	}
}
