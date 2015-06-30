package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class ComponentMagazine extends ComponentGeneric implements IComponentAmmoItem
{
	public final int capacity;
	public final float kickback;
	public final float spread;
	public final int projectileCount;
	public final float fireRate;
	public final float heating;
	
	// nbt
	public static final String AMMO = "Ammo";
	
	public ComponentMagazine(String id, String name, double heatLoss, float heatThreshold, ComponentMaterial material, int capacity, float kickback, float spread, float fireRate, int projectileCount, float heating)
	{
		super(new String2("magazine_" + id, ""), new String2("magazine." + name, ""), heatLoss, heatThreshold, 4.6F, Type.MAGAZINE, material);
		this.capacity = capacity;
		this.kickback = kickback;
		this.spread = spread;
		this.projectileCount = projectileCount;
		this.fireRate = fireRate;
		this.heating = heating;
	}
	
	@Override
	public int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player)
	{
		NBTTagCompound compound = this.getTagCompound(slot, stack);
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
		this.addHeat(this.heating, stack, components);
		for(Component c : components)
		{
			if(c.type == Type.BARREL)
			{
				c.addHeat(this.heating / 2, stack, components);
			}
			else if(c.type == Type.TRIGGER)
			{
				c.addHeat(this.heating / 3, stack, components);
			}
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
	public abstract ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player);
	
	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	public boolean isValid(int slot, HashMap<Integer, Component> ecs)
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
	
		for(Integer cSlot : ecs.keySet())
		{
			if(cSlot != slot && ecs.get(cSlot).type == Type.MAGAZINE)
			{
				return false;
			}
		}
		return super.isValid(slot, ecs);
	}
	
	@Override
	public int ammo(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		NBTTagCompound compound = this.getTagCompound(slot, stack);
		this.capAmmo(compound);
		return compound.getInteger(AMMO);
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack)
	{
		return stack.getItem() == this.ammoItem();
	}
	
	@Override
	public Item ejectableAmmo(int slot, ItemStack stack, World world, EntityPlayer player)
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
	public int capacity(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return this.capacity;
	}
	
	@Override
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		return delay + this.capacity / 32;
	}
}