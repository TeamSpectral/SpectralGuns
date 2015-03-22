package com.spectral.spectral_guns.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Component
{
	public static abstract class ComponentRegister
	{
		public static enum Type
		{
			MISC,
			BARREL,
			MAGAZINE,
			TRIGGER,
			GRIP,
			STOCK,
			AIM
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
					return cs.get(rand.nextInt(cs.size()-1));
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
			return getComponentTypes(getRequiredComponents());
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

		public static ArrayList<Component> getRequiredComponents()
		{
			ArrayList<Component> cs = new ArrayList<Component>();
			Iterator<Component> values = components.values().iterator();
			while(values.hasNext())
			{
				Component c = values.next();
				if(c.isRequired())
				{
					cs.add(c);
				}
			}
			return cs;
		}
	}

	public enum ComponentMaterial
	{
		WOOD(40, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}),
		IRON(167, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.GRIP, Type.STOCK, Type.AIM}),
		GOLD(22, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.TRIGGER, Type.AIM}),
		DIAMOND(520, new Type[]{Type.MISC, Type.BARREL, Type.MAGAZINE, Type.AIM});

		public final Type[] types;
		public final int durability;

		private ComponentMaterial(int durability, Type[] types)
		{
			this.types = types;
			this.durability = durability;
		}

		public String getDisplayName(Type type, Component c)
		{
			return getName(type, c);
		}

		public String getIDName(Type type, Component c)
		{
			return getName(type, c);
		}

		private String getName(Type type, Component c)
		{
			return this.name().toLowerCase();
		}
	}

	public final ItemComponent item;
	public final Type type;
	public final ComponentMaterial material;

	public Component(String id, Type type, ComponentMaterial material)
	{
		id = id + "_" + material.getIDName(type, this);
		ComponentRegister.components.put(id, this);
		item = new ItemComponent(this);
		this.type = type;
		if(!ArraysAndSuch.has(material.types, type))
		{
			try
			{
				throw new IllegalArgumentException(String.format("The component %s has been registered with an incapatible material; %s.", this.getID(), material.getIDName(type, this)));
			}
			catch (Throwable e)
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

	public abstract boolean isRequired();

	public abstract ArrayList<Component> getRequired();

	public abstract ArrayList<Component> getIncapatible();

	public abstract int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract ArrayList<Entity> fire(ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	protected abstract void fireSound(Entity e, ItemStack stack, World world, EntityPlayer player);

	public abstract float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract boolean automatic(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player);

	public abstract int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract int durabilityDamage(ItemStack stack, ArrayList<Component> components);

	public abstract Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components);

	public abstract void update(ItemStack stack, World world, Entity entity, int slot, boolean isSelected, ArrayList<Component> components);

	public abstract void registerRecipe();

	public boolean isValid(ArrayList<Component> ecs)
	{
		int count = 0;
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).type == type)
			{
				++count;
			}
		}
		if(count > 1)
		{
			return false;
		}
		ArrayList<Component> rcs = getRequired();
		ArrayList<Component> ics = getIncapatible();
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

	/**I do have custom rendered models planned. this will be used. until then, ignore this.**/ //- sigurd4
	@SideOnly(Side.CLIENT)
	public abstract void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags);

	public final NBTTagCompound getTagCompound(ItemStack stack)
	{
		return getTagCompound(stack.getTagCompound());
	}

	public NBTTagCompound getTagCompound(NBTTagCompound compound)
	{
		if(!compound.hasKey(ItemGun.COMPONENTS))
		{
			compound.setTag(ItemGun.COMPONENTS, new NBTTagCompound());
		}
		NBTTagCompound components = compound.getCompoundTag(ItemGun.COMPONENTS);
		if(!components.hasKey(getID()))
		{
			return new NBTTagCompound();
		}

		return components.getCompoundTag(getID());
	}
}
