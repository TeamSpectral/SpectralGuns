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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff;
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
			MISC(false, new int[]{6, 7, 8, 9}), BARREL(true, 0), MAGAZINE(true, 1), TRIGGER(true, 2), GRIP(true, 3), STOCK(false, 4), AIM(false, 5);
			
			final public boolean isRequired;
			final public int[] slots;
			
			private Type(boolean isRequired, int slot)
			{
				this(isRequired, new int[]{slot});
			}
			
			private Type(boolean isRequired, int[] slots)
			{
				this.isRequired = isRequired;
				this.slots = slots;
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
			ArrayList<Component> cs = getAll();
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
		
		public static ArrayList<Type> getComponentTypes(HashMap<Integer, Component> cs)
		{
			ArrayList<Type> ts = new ArrayList();
			main:
			for(Integer slot : cs.keySet())
			{
				Component c = cs.get(slot);
				for(int i2 = 0; i2 < ts.size(); ++i2)
				{
					if(c.type == ts.get(i2))
					{
						continue main;
					}
				}
				ts.add(c.type);
			}
			
			return ts;
		}
	}
	
	public enum ComponentMaterial
	{
		WOOD(140, 0.04, 3, 0.1, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}), IRON(267, 3.3, 0.5, 7, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}), GOLD(122, 3.9, 1, 0.5, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}), DIAMOND(320, 0.2, 0.1, 1, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.AIM});
		
		public final Type[] types;
		public final int durability;
		public final double heatLoss;
		public final double heatThresholdMin;
		public final double heatThresholdMax;
		
		private ComponentMaterial(int durability, double heatLoss, double heatThresholdMin, double heatThresholdMax, Type[] types)
		{
			this.types = types;
			this.durability = durability;
			this.heatLoss = heatLoss;
			this.heatThresholdMin = heatThresholdMin;
			this.heatThresholdMax = heatThresholdMax;
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
		String Id = id.s1 + (type != Type.MISC ? "_" + material.getIDName(type, this) : "") + id.s2;
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
	
	public abstract int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player);
	
	public abstract ArrayList<Entity> fire(int slot, ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player);
	
	protected abstract void fireSound(int slot, Entity e, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float zoom(int slot, float zoom, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player);
	
	public abstract boolean automatic(int slot, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player);
	
	public abstract float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player);
	
	public abstract int ammo(int slot, ItemStack stack, World world, EntityPlayer player);
	
	public abstract boolean isAmmoItem(ItemStack stack);
	
	public abstract int capacity(int slot, ItemStack stack, World world, EntityPlayer player);
	
	public void setDurabilityDamage(int slot, int durability, ItemStack stack, EntityPlayer player)
	{
		if(durability < 0)
		{
			durability = 0;
		}
		this.getTagCompound(slot, stack).setInteger(ItemComponent.ITEMDAMAGE, durability);
	}
	
	public void addDurabilityDamage(int slot, int heat, ItemStack stack, EntityPlayer player)
	{
		this.setDurabilityDamage(slot, heat + this.durabilityDamage(slot, stack), stack, player);
	}
	
	public int durabilityDamage(int slot, ItemStack stack)
	{
		return this.getTagCompound(slot, stack).getInteger(ItemComponent.ITEMDAMAGE);
	}
	
	public abstract int durabilityMax(int slot, ItemStack stack);
	
	public abstract Item ejectableAmmo(int slot, ItemStack stack, World world, EntityPlayer player);
	
	public void setHeat(int slot, double heat, ItemStack stack)
	{
		if(heat > this.heatThreshold(slot, stack) * 3)
		{
			heat = this.heatThreshold(slot, stack) * 3;
		}
		if(heat < -this.heatThreshold(slot, stack) * 3)
		{
			heat = -this.heatThreshold(slot, stack) * 3;
		}
		this.getTagCompound(slot, stack).setDouble(ItemComponent.HEAT, heat);
	}
	
	public void addHeat(int slot, double heat, ItemStack stack)
	{
		this.setHeat(slot, heat + this.heat(slot, stack), stack);
	}
	
	public double heat(int slot, ItemStack stack)
	{
		double d = this.getTagCompound(slot, stack).getDouble(ItemComponent.HEAT);
		
		return d;
	}
	
	public abstract double heatConductiveness(int slot, ItemStack stack);
	
	public abstract float heatThreshold(int slot, ItemStack stack);
	
	public void heatMix(int slot, ItemStack stack, Component c)
	{
		double oldHeat1 = this.heat(slot, stack);
		double oldHeat2 = c.heat(slot, stack);
		double heat1 = this.heat(slot, stack);
		double cond1 = this.heatConductiveness(slot, stack) * this.material.heatLoss / 10;
		double heat2 = c.heat(slot, stack);
		double cond2 = c.heatConductiveness(slot, stack) * c.material.heatLoss / 10;
		double distr = heat1 * cond1 + heat2 * cond2;
		this.setHeat(slot, heat1 * (1 - cond1) + distr / 2, stack);
		c.setHeat(slot, heat2 * (1 - cond2) + distr / 2, stack);
		for(Type type : c.getRequiredTypes())
		{
			if(type == this.type)
			{
				this.setHeat(slot, (this.heat(slot, stack) + oldHeat1) / 2, stack);
				c.setHeat(slot, (c.heat(slot, stack) + oldHeat2) / 2, stack);
				break;
			}
		}
	}
	
	public void heatMix(int slot, ItemStack stack, double c2Heat, double c2Threshold, double c2HeatLoss)
	{
		double heat1 = this.heat(slot, stack) * this.heatThreshold(slot, stack);
		double cond1 = this.heatConductiveness(slot, stack) * this.material.heatLoss / 10;
		double heat2 = c2Heat * c2Threshold;
		double cond2 = c2HeatLoss / 10;
		double distr = heat1 * cond1 + heat2 * cond2;
		this.setHeat(slot, (heat1 * (1 - cond1) + distr / 2) / c2Threshold, stack);
	}
	
	public abstract void update(int slot, ItemStack stack, World world, EntityPlayer player, int invSlot, boolean isSelected);
	
	public abstract void registerRecipe();
	
	public boolean isValid(int slot, HashMap<Integer, Component> ecs)
	{
		if(true)
		{
			int count = 0;
			for(Integer cSlot : ecs.keySet())
			{
				if(ecs.get(cSlot).type == this.type)
				{
					++count;
				}
			}
			if(count > this.maxAmount)
			{
				return false;
			}
		}
		
		if(true)
		{
			ArrayList<Component> rcs = this.getRequired();
			ArrayList<Component> ics = this.getIncapatible();
			for(Integer cSlot : ecs.keySet())
			{
				if(ecs.get(cSlot) != null && Stuff.ArraysAndSuch.has(ics, ecs.get(cSlot)))
				{
					return false;
				}
			}
			for(Component rc : rcs)
			{
				if(rc != null && !Stuff.ArraysAndSuch.has(Stuff.ArraysAndSuch.hashMapToArrayList(ecs), rc))
				{
					return false;
				}
			}
		}
		
		if(true)
		{
			/*
			 * ArrayList<Type> rts = this.getRequiredTypes();
			 * ArrayList<Type> its = this.getIncapatibleTypes();
			 * ArrayList<Type> ets = new ArrayList();
			 * for(Component ec : ecs)
			 * {
			 * if(ec != null && !Stuff.ArraysAndSuch.has(ets, ec.type))
			 * {
			 * ets.add(ec.type);
			 * }
			 * }
			 * for(Type et : ets)
			 * {
			 * if(et != null && Stuff.ArraysAndSuch.has(its, et))
			 * {
			 * return false;
			 * }
			 * }
			 * for(Type rt : rts)
			 * {
			 * if(rt != null && !Stuff.ArraysAndSuch.has(ets, rt))
			 * {
			 * return false;
			 * }
			 * }
			 */
		}
		
		if(true)
		{
			ArrayList<ComponentMaterial> rms = this.getRequiredMats();
			ArrayList<ComponentMaterial> ims = this.getIncapatibleMats();
			ArrayList<ComponentMaterial> ems = new ArrayList();
			for(Integer cSlot : ecs.keySet())
			{
				if(ecs.get(cSlot) != null && !Stuff.ArraysAndSuch.has(ems, ecs.get(cSlot).material))
				{
					ems.add(ecs.get(cSlot).material);
				}
			}
			for(ComponentMaterial em : ems)
			{
				if(em != null && Stuff.ArraysAndSuch.has(ims, em))
				{
					return false;
				}
			}
			for(ComponentMaterial rm : rms)
			{
				if(rm != null && !Stuff.ArraysAndSuch.has(ems, rm))
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
	@SideOnly(Side.CLIENT)
	public abstract void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags);
	
	public final NBTTagCompound getComponentCompound(int slot, ItemStack stack)
	{
		return this.getComponentCompound(slot, stack.getTagCompound());
	}
	
	public NBTTagCompound getComponentCompound(int slot, NBTTagCompound compound)
	{
		if(!compound.hasKey(ItemGun.COMPONENTS))
		{
			compound.setTag(ItemGun.COMPONENTS, new NBTTagCompound());
		}
		NBTTagList components = compound.getTagList(ItemGun.COMPONENTS, new NBTTagCompound().getId());
		for(int i = 0; i < components.tagCount(); ++i)
		{
			NBTTagCompound cCompound = components.getCompoundTagAt(i);
			if(cCompound != null && cCompound.hasKey(ItemGun.COMPONENT_SLOT, new NBTTagInt(0).getId()) && cCompound.hasKey(ItemGun.COMPONENT_ID, new NBTTagString("").getId()))
			{
				int cSlot = cCompound.getInteger(ItemGun.COMPONENT_SLOT);
				String id = cCompound.getString(ItemGun.COMPONENT_ID);
				if(cSlot == slot)
				{
					if(!cCompound.hasKey(ItemGun.COMPONENT_COMPOUND, new NBTTagCompound().getId()))
					{
						cCompound.setTag(ItemGun.COMPONENT_COMPOUND, new NBTTagCompound());
					}
					return cCompound;
				}
			}
		}
		return new NBTTagCompound();
	}
	
	public final NBTTagCompound getTagCompound(int slot, ItemStack stack)
	{
		return this.getTagCompound(slot, stack.getTagCompound());
	}
	
	public NBTTagCompound getTagCompound(int slot, NBTTagCompound compound)
	{
		NBTTagCompound cCompound = this.getComponentCompound(slot, compound);
		if(cCompound.hasKey(ItemGun.COMPONENT_COMPOUND, new NBTTagCompound().getId()))
		{
			return cCompound.getCompoundTag(ItemGun.COMPONENT_COMPOUND);
		}
		return new NBTTagCompound();
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
	
	public ItemStack toItemStack(int slot, ItemStack gun)
	{
		NBTTagCompound compound = (NBTTagCompound)this.getTagCompound(slot, gun).copy();
		ItemStack stack = new ItemStack(this.item, 1, compound.getInteger(ItemComponent.ITEMDAMAGE));
		compound.removeTag(ItemComponent.ITEMDAMAGE);
		stack.setTagCompound(compound);
		return stack;
	}
}
