package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class ComponentGeneric extends Component
{
	final protected String name;
	protected Component[] required;
	protected Component[] incapatible;
	protected Type[] requiredTypes;
	protected Type[] incapatibleTypes;
	protected ComponentMaterial[] requiredMats;
	protected ComponentMaterial[] incapatibleMats;
	final protected double heatLoss;
	final protected float heatThreshold;
	final protected float maxDurability;
	
	public ComponentGeneric(String2 id, String2 name, double heatLoss, float heatThreshold, float maxDurability, Type type, ComponentMaterial material)
	{
		super(id, type, material);
		this.name = name.s1 + (type != Type.MISC ? "." + material.getDisplayName(type, this) : "") + name.s2;
		this.heatLoss = heatLoss;
		this.heatThreshold = heatThreshold;
		this.maxDurability = maxDurability;
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
	
	@Override
	public ArrayList<Type> getRequiredTypes()
	{
		return ArraysAndSuch.arrayToArrayList(this.requiredTypes);
	}
	
	@Override
	public ArrayList<Type> getIncapatibleTypes()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatibleTypes);
	}
	
	@Override
	public ArrayList<ComponentMaterial> getRequiredMats()
	{
		return ArraysAndSuch.arrayToArrayList(this.requiredMats);
	}
	
	@Override
	public ArrayList<ComponentMaterial> getIncapatibleMats()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatibleMats);
	}
	
	public int setAmmo(ItemStack stack, World world, EntityPlayer player, int ammo)
	{
		return ammo;
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player)
	{
		return projectiles;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil;
	}
	
	@Override
	public float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback;
	}
	
	@Override
	public float zoom(int slot, float zoom, ItemStack stack, World world, EntityPlayer player)
	{
		return zoom;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate;
	}
	
	@Override
	public float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player)
	{
		return spread;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed;
	}
	
	@Override
	protected void fireSound(int slot, Entity e, ItemStack stack, World world, EntityPlayer player)
	{
	}
	
	@Override
	public int ammo(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public Item ejectableAmmo(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player)
	{
		return ammo;
	}
	
	@Override
	public int capacity(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public boolean automatic(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public double heatConductiveness(int slot, ItemStack stack)
	{
		return this.heatLoss;
	}
	
	@Override
	public float heatThreshold(int slot, ItemStack stack)
	{
		return (float)(this.heatThreshold * (this.heat(slot, stack) > 0 ? this.material.heatThresholdMax : this.material.heatThresholdMin));
	}
	
	@Override
	public int durabilityMax(int slot, ItemStack stack)
	{
		return (int)(this.material.durability * this.maxDurability);
	}
	
	@Override
	public void update(int slot, ItemStack stack, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		if(this.heat(slot, stack) > this.heatThreshold(slot, stack) || this.heat(slot, stack) < -this.heatThreshold(slot, stack))
		{
			if(player.ticksExisted % 10 == 1)
			{
				this.addDurabilityDamage(slot, 1, stack, player);
			}
		}
		loop:
		for(Component c : ItemGun.getComponents(stack).values())
		{
			if(c != this)
			{
				for(Type type : this.getRequiredTypes())
				{
					if(type == c.type)
					{
						this.heatMix(slot, stack, c);
						continue loop;
					}
				}
			}
		}
		this.heatMix(slot, stack, player.isInWater() ? -20 : 0, 1, 0.0001, 0.51);
		if(this.heat(slot, stack) > 0)
		{
			this.addHeat(slot, -0.01, stack);
		}
		else if(this.heat(slot, stack) < 0)
		{
			this.addHeat(slot, 0.01, stack);
		}
	}
}
