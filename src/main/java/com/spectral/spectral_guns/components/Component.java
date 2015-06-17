package com.spectral.spectral_guns.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class Component
{
	public static abstract class ComponentRegister
	{
		public static enum Type
		{
			MISC(false),
			BARREL(true),
			MAGAZINE(true),
			TRIGGER(true),
			GRIP(true),
			STOCK(false),
			AIM(false);
			
			final public boolean isRequired;
			
			private Type(boolean isRequired)
			{
				this.isRequired = isRequired;
			}
		}
		
		private static final HashMap<String, Component> components = new HashMap<String, Component>();
		
		public static ArrayList<Component> getAll()
		{
			return ArraysAndSuch.hashMapToArrayList(components);
		}
		
		public static boolean hasID(Component c)
		{
			return getID(c) != null;
		}
		
		public static boolean hasComponent(String id)
		{
			return getComponent(id) != null;
		}
		
		public static ArrayList<String> getIDs()
		{
			ArrayList<String> s = ArraysAndSuch.hashMapKeysToArrayList(components);
			
			return s;
		}
		
		public static ItemComponent getItem(Component c)
		{
			return c.item;
		}
		
		public static String getID(Component c)
		{
			ArrayList<String> s = ArraysAndSuch.hashMapKeysToArrayList(components);
			for(int i = 0; i < s.size(); ++i)
			{
				String id = s.get(i);
				if(components.get(id) == c)
				{
					return id;
				}
			}
			
			return null;
		}
		
		public static Component getRandomComponent(Random rand)
		{
			ArrayList<Component> cs = new ArrayList<Component>();
			Iterator<Component> values = components.values().iterator();
			while(values.hasNext())
			{
				Component c = values.next();
				cs.add(c);
			}
			if(cs.size() > 0)
			{
				if(cs.size() > 1)
				{
					return cs.get(rand.nextInt(cs.size() - 1));
				}
				else
				{
					return cs.get(0);
				}
			}
			else
			{
				return null;
			}
		}
		
		public static Component getComponent(String id)
		{
			return components.get(id);
		}
		
		public static ArrayList<Component> getComponents(String[] ids)
		{
			ArrayList<Component> cs = new ArrayList<Component>();
			
			for(int i = 0; i < ids.length; ++i)
			{
				Component c = getComponent(ids[i]);
				if(c != null)
				{
					cs.add(c);
				}
			}
			
			return cs;
		}
		
		public static ArrayList<Type> getRequiredTypes()
		{
			ArrayList<Type> ts = new ArrayList<Type>();
			Iterator<Component> values = components.values().iterator();
			while(values.hasNext())
			{
				Component c = values.next();
				if(c.type.isRequired && !ArraysAndSuch.has(ts, c.type))
				{
					ts.add(c.type);
				}
			}
			return ts;
		}
		
		public static ArrayList<Type> getComponentTypes(ArrayList<Component> cs)
		{
			ArrayList<Type> ts = new ArrayList<Type>();
			
			for(int i = 0; i < cs.size(); ++i)
			{
				Component c = cs.get(i);
				boolean b = true;
				for(int i2 = 0; i2 < ts.size(); ++i2)
				{
					if(c.type == ts.get(i2))
					{
						b = false;
						break;
					}
				}
				if(b)
				{
					ts.add(c.type);
				}
			}
			
			return ts;
		}
	}
	
	public enum ComponentMaterial
	{
		WOOD(
				40,
				0.01,
				new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}),
		IRON(
				167,
				3.3,
				new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}),
		GOLD(
				22,
				3.9,
				new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}),
		DIAMOND(
				520,
				0.4,
				new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.AIM});
		
		public final Type[] types;
		public final int durability;
		public final double heatLoss;
		
		private ComponentMaterial(int durability, double heatLoss, Type[] types)
		{
			this.types = types;
			this.durability = durability;
			this.heatLoss = heatLoss;
		}
		
		public String getDisplayName(Type type, Component c)
		{
			return this.getName(type, c);
		}
		
		public String getIDName(Type type, Component c)
		{
			return this.getName(type, c);
		}
		
		private String getName(Type type, Component c)
		{
			return this.name().toLowerCase();
		}
	}
	
	public final ItemComponent item;
	public final Type type;
	public final ComponentMaterial material;
	public int maxAmount = 1;
	
	public Component(String2 id, Type type, ComponentMaterial material)
	{
		String Id = id.s1 + "_" + material.getIDName(type, this) + id.s2;
		ComponentRegister.components.put(Id, this);
		this.item = new ItemComponent(this);
		this.type = type;
		if(!ArraysAndSuch.has(material.types, type))
		{
			try
			{
				throw new IllegalArgumentException(String.format("The component %s has been registered with an incapatible material; %s.", this.getID(), material.getIDName(type, this)));
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
		this.material = material;
	}
	
	public final String getID()
	{
		return ComponentRegister.getID(this);
	}
	
	public abstract String getFancyName();
	
	public abstract ArrayList<Component> getRequired();
	
	public abstract ArrayList<Component> getIncapatible();
	
	public abstract ArrayList<Type> getRequiredTypes();
	
	public abstract ArrayList<Type> getIncapatibleTypes();
	
	public abstract ArrayList<ComponentMaterial> getRequiredMats();
	
	public abstract ArrayList<ComponentMaterial> getIncapatibleMats();
	
	public abstract int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract ArrayList<Entity> fire(ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	protected abstract void fireSound(Entity e, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float instability(float instability, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float zoom(float zoom, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract boolean automatic(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public abstract boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player);
	
	public abstract int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public void setDurabilityDamage(int durability, ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		if(durability >= this.item.getMaxDamage())
		{
			ItemGun.dropAllComponents(player, stack);
		}
		if(durability < 0)
		{
			durability = 0;
		}
		stack.getTagCompound().setInteger(ItemComponent.ITEMDAMAGE, durability);
	}
	
	public void addDurabilityDamage(int heat, ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		this.setDurabilityDamage(heat + this.durabilityDamage(stack, components), stack, player, components);
	}
	
	public int durabilityDamage(ItemStack stack, ArrayList<Component> components)
	{
		return stack.getTagCompound().getInteger(ItemComponent.ITEMDAMAGE);
	}
	
	public abstract Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);
	
	public void setHeat(double heat, ItemStack stack, ArrayList<Component> components)
	{
		if(heat > this.heatThreshold(stack, components) * 2)
		{
			heat = this.heatThreshold(stack, components) * 2;
		}
		if(heat < -this.heatThreshold(stack, components) * 2)
		{
			heat = -this.heatThreshold(stack, components) * 2;
		}
		stack.getTagCompound().setDouble(ItemComponent.HEAT, heat);
	}
	
	public void addHeat(double heat, ItemStack stack, ArrayList<Component> components)
	{
		this.setHeat(heat + this.heat(stack, components), stack, components);
	}
	
	public double heat(ItemStack stack, ArrayList<Component> components)
	{
		return stack.getTagCompound().getDouble(ItemComponent.HEAT);
	}
	
	public abstract double heatConductiveness(ItemStack stack, ArrayList<Component> components);
	
	public abstract float heatThreshold(ItemStack stack, ArrayList<Component> components);
	
	public void heatMix(ItemStack stack, Component c, ArrayList<Component> components)
	{
		double oldHeat1 = this.heat(stack, components);
		double oldHeat2 = c.heat(stack, components);
		double heat1 = this.heat(stack, components) / this.heatThreshold(stack, components);
		double cond1 = this.heatConductiveness(stack, components) * this.material.heatLoss / 10;
		double heat2 = c.heat(stack, components) / c.heatThreshold(stack, components);
		double cond2 = c.heatConductiveness(stack, components) * c.material.heatLoss / 10;
		double distr = heat1 * cond1 + heat2 * cond2;
		this.setHeat((heat1 * (1 - cond1) + distr / 2) * this.heatThreshold(stack, components), stack, components);
		c.setHeat((heat2 * (1 - cond2) + distr / 2) * c.heatThreshold(stack, components), stack, components);
		for(Type type : c.getRequiredTypes())
		{
			if(type == this.type)
			{
				this.setHeat((this.heat(stack, components) + oldHeat1) / 2, stack, components);
				c.setHeat((c.heat(stack, components) + oldHeat2) / 2, stack, components);
				break;
			}
		}
	}
	
	public void heatMix(ItemStack stack, double c2Heat, double c2Threshold, double c2HeatLoss, ArrayList<Component> components)
	{
		double heat1 = this.heat(stack, components) / this.heatThreshold(stack, components);
		double cond1 = this.heatConductiveness(stack, components) * this.material.heatLoss / 10;
		double heat2 = c2Heat / c2Threshold;
		double cond2 = c2HeatLoss / 10;
		double distr = heat1 * cond1 + heat2 * cond2;
		this.setHeat((heat1 * (1 - cond1) + distr / 2) * c2Threshold, stack, components);
	}
	
	public abstract void update(ItemStack stack, World world, EntityPlayer player, int slot, boolean isSelected, ArrayList<Component> components);
	
	public abstract void registerRecipe();
	
	public boolean isValid(ArrayList<Component> ecs)
	{
		int count = 0;
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).type == this.type)
			{
				++count;
			}
		}
		if(count > this.maxAmount)
		{
			return false;
		}
		ArrayList<Component> rcs = this.getRequired();
		ArrayList<Component> ics = this.getIncapatible();
		ArrayList<Component> a = new ArrayList<Component>();
		for(int i = 0; i < ecs.size(); ++i)
		{
			Component ec = ecs.get(i);
			for(int i2 = 0; i2 < rcs.size(); ++i2)
			{
				Component rc = rcs.get(i2);
				if(ec == rc)
				{
					a.add(ec);
				}
			}
		}
		if(a.size() < rcs.size())
		{
			return false;
		}
		for(int i = 0; i < ecs.size(); ++i)
		{
			Component ec = ecs.get(i);
			for(int i2 = 0; i2 < ics.size(); ++i2)
			{
				Component ic = ics.get(i2);
				if(ec == ic)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * I do have custom rendered models planned. this will be used. until then,
	 * ignore this.
	 **/
	// - sigurd4
	@SideOnly(Side.CLIENT)
	public abstract void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags);
	
	public final NBTTagCompound getTagCompound(ItemStack stack)
	{
		return this.getTagCompound(stack.getTagCompound());
	}
	
	public NBTTagCompound getTagCompound(NBTTagCompound compound)
	{
		if(!compound.hasKey(ItemGun.COMPONENTS))
		{
			compound.setTag(ItemGun.COMPONENTS, new NBTTagCompound());
		}
		NBTTagCompound components = compound.getCompoundTag(ItemGun.COMPONENTS);
		if(!components.hasKey(this.getID()))
		{
			return new NBTTagCompound();
		}
		
		return components.getCompoundTag(this.getID());
	}
	
	public static class String2
	{
		public String s1;
		public String s2;
		
		public String2()
		{
			this.s1 = "";
			this.s2 = "";
		}
		
		public String2(String s1, String s2)
		{
			this.s1 = s1;
			this.s2 = s2;
		}
		
		public String2 add(String2 s)
		{
			this.s1 += s.s1;
			this.s2 += s.s2;
			return this;
		}
	}
	
	public ItemStack toItemStack(ItemStack gun)
	{
		return ItemStack.loadItemStackFromNBT(gun.getTagCompound().getCompoundTag(ItemGun.COMPONENTS).getCompoundTag(this.getID()));
	}
}
