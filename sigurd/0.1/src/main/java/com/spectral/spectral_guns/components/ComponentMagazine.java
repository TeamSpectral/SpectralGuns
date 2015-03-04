package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Coordinates3D.Coords3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component.ComponentRegister.ComponentType;
import com.spectral.spectral_guns.items.ItemGun;
import com.sun.javafx.geom.Vec3f;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityThrowable;
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

	public ComponentMagazine(String id, String name, Component[] required, Component[] incapatible, int capacity, float kickback, float spread, float fireRate, int projectileCount)
	{
		super(id, name, required, incapatible, true, ComponentType.MAGAZINE);
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

	protected final Entity projectile()
	{
		return projectile(new ItemStack(M.gun), MinecraftServer.getServer().getEntityWorld(), (EntityPlayer)MinecraftServer.getServer().worldServers[0].playerEntities.get(0));
	}

	protected abstract Entity projectile(ItemStack stack, World world, EntityPlayer player);

	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(stack);

		if(ItemGun.ammo(stack, player) > 0)
		{
			for(int i = 0; i < projectileCount; ++i)
			{
				e.add(projectile(stack, world, player));
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
		return kickback + this.kickback;
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
			if(ecs.get(i) instanceof ComponentMagazine && ecs.get(i).type == ComponentType.MAGAZINE)
			{
				if(ecs.get(i) instanceof ComponentMagazine)
				{
					if(((ComponentMagazine)ecs.get(i)).projectile() != null && projectile() != null && ((ComponentMagazine)ecs.get(i)).projectile().getClass() == projectile().getClass())
					{
						++count;
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
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
		c.setInteger(AMMO, capacity);
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
