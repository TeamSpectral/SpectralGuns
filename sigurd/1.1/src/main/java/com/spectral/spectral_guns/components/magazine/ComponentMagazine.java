package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.String2;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemGun;
import com.sun.javafx.geom.Vec3f;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class ComponentMagazine extends ComponentGeneric
{
	public final int capacity;
	public final float kickback;
	public final float spread;
	public final int projectileCount;
	public final float fireRate;

	//nbt
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
		while(ammo > 0 && compound.getInteger(AMMO) < capacity)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO)+1);
			--ammo;
		}
		while(ammo < 0 && compound.getInteger(AMMO) > 0)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO)-1);
			++ammo;
		}

		capAmmo(compound);
		return ammo;
	}

	protected abstract Entity projectile(ItemStack stack, World world, EntityPlayer player);

	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		for(int i = 0; i < projectileCount; ++i)
		{
			e.add(projectile(stack, world, player));
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
		return kickback + this.kickback*0.63F;
	}

	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil + this.kickback*5.6F;
	}

	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return speed + 1.8F + this.kickback*3;
	}

	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate + this.fireRate;
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{

	}

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
		capAmmo(compound);
		return compound.getInteger(AMMO);
	}

	@Override
	public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player)
	{
		return stack.getItem() == ammoItem();
	}

	@Override
	public Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return ammoItem();
	}

	public abstract Item ammoItem();

	protected void capAmmo(NBTTagCompound c)
	{
		int a = c.getInteger(AMMO);
		if(c.getInteger(AMMO) > capacity)
		{
			c.setInteger(AMMO, capacity);
		}
		if(c.getInteger(AMMO) < 0)
		{
			c.setInteger(AMMO, 0);
		}
	}

	@Override
	public int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return capacity;
	}

	@Override
	public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
	{
		return delay + capacity/32;
	}
}
