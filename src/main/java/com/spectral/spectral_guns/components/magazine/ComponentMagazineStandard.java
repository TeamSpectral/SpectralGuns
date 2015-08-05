package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.ComponentEvents;

public abstract class ComponentMagazineStandard extends ComponentMagazine
{
	public final float kickback;
	public final float recoil;
	public final float speed;
	public final int projectileCount;
	public final float fireRate;
	
	public ComponentMagazineStandard(String id, String name, double heatLoss, float heatThreshold, ComponentMaterial material, int capacity, float kickback, float recoil, float speed, float fireRate, int projectileCount, float heating)
	{
		super(id, name, heatLoss, heatThreshold, material, capacity, heating);
		this.kickback = kickback;
		this.recoil = recoil;
		this.speed = speed;
		this.projectileCount = projectileCount;
		this.fireRate = fireRate;
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("Recoil:", this.ADDS(this.recoil(-1, 0, new ItemStack(M.gun), world, player))));
		tooltip.add(new String2("Kickback:", this.ADDS(this.kickback(-1, 0, new ItemStack(M.gun), world, player))));
		tooltip.add(new String2("Speed:", this.ADDS(this.speed(-1, 0, new ItemStack(M.gun), world, player))));
		tooltip.add(new String2("ProjectileCount:", "" + this.projectileCount(-1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("FireRate:", this.ADDS(this.fireRate(-1, 0, new ItemStack(M.gun), world, player))));
		tooltip.add(new String2("Delay:", this.ADDS(this.delay(-1, 0, new ItemStack(M.gun), world, player))));
	}
	
	protected abstract Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player);
	
	@Override
	public int projectileCount(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return this.projectileCount;
	}
	
	@Override
	public String projectileName()
	{
		try
		{
			World world = M.proxy.world(0);
			EntityPlayer player = M.proxy.player();
			if(world == null)
			{
				world = player.worldObj;
			}
			String s = this.projectile(-1, new ItemStack(M.gun), world, player).getName();
			if(StatCollector.translateToLocal("entity.generic.name").equals(s))
			{
				throw new Throwable("No name for entity");
			}
			return s;
		}
		catch(Throwable e)
		{
			return StatCollector.translateToLocal(this.ammoItem().getUnlocalizedName() + ".name");
		}
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player)
	{
		for(int i = 0; i < this.projectileCount(slot, stack, world, player); ++i)
		{
			e.add(this.projectile(slot, stack, world, player));
		}
		ComponentEvents.heatUp(stack, player, 0.5);
		return e;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback + this.kickback;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil + this.recoil;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed + this.speed;
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
