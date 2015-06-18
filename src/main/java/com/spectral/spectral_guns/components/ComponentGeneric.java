package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

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
	
	public ComponentGeneric(String2 id, String2 name, double heatLoss, float heatThreshold, Type type, ComponentMaterial material)
	{
		super(id, type, material);
		this.name = name.s1 + "." + material.getDisplayName(type, this) + name.s2;
		this.heatLoss = heatLoss;
		this.heatThreshold = heatThreshold;
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
	public double heatConductiveness(ItemStack stack, ArrayList<Component> components)
	{
		return this.heatLoss;
	}
	
	@Override
	public float heatThreshold(ItemStack stack, ArrayList<Component> components)
	{
		return this.heatThreshold;
	}
	
	@Override
	public void update(ItemStack stack, World world, EntityPlayer player, int slot, boolean isSelected, ArrayList<Component> components)
	{
		if(this.heat(stack, components) > this.heatThreshold(stack, components) || this.heat(stack, components) < -this.heatThreshold(stack, components))
		{
			this.addDurabilityDamage(-1, stack, player, components);
		}
		this.heatMix(stack, 0, 1, 0.3, components);
		loop:
		for(Component c : components)
		{
			if(c != this)
			{
				for(Type type : this.getRequiredTypes())
				{
					if(type == c.type)
					{
						this.heatMix(stack, c, components);
						continue loop;
					}
				}
			}
		}
	}
}
