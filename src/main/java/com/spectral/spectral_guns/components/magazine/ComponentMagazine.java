package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import com.spectral.spectral_guns.itemtags.ItemTagInteger;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.IComponentHeatOnFire;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class ComponentMagazine extends ComponentGeneric implements IComponentAmmoItem, IComponentHeatOnFire, IComponentProjectileCount
{
	public final int capacity;
	public final float heating;
	
	// nbt
	public final ItemTagInteger AMMO = new ItemTagInteger("Ammo", 0, 0, Integer.MAX_VALUE, true);
	
	public ComponentMagazine(String id, String name, double heatLoss, float heatThreshold, ComponentMaterial material, int capacity, float heating)
	{
		this(new String2(id, ""), new String2(name, ""), heatLoss, heatThreshold, material, capacity, heating);
	}
	
	public ComponentMagazine(String2 id, String2 name, double heatLoss, float heatThreshold, ComponentMaterial material, int capacity, float heating)
	{
		super(new String2("magazine_" + id.s1, id.s2), new String2("magazine." + name.s1, name.s2), heatLoss, heatThreshold, 4.6F, Type.MAGAZINE, material);
		this.capacity = capacity;
		this.heating = heating;
	}
	
	@Override
	public int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player)
	{
		while(ammo > 0 && this.AMMO.get(this.getTagCompound(slot, stack), true) < Math.min(this.AMMO.max, this.capacity(slot, stack, world, player)))
		{
			this.AMMO.add(this.getTagCompound(slot, stack), 1);
			--ammo;
		}
		while(ammo < 0 && this.AMMO.get(this.getTagCompound(slot, stack), true) > this.AMMO.min)
		{
			this.AMMO.add(this.getTagCompound(slot, stack), -1);
			++ammo;
		}
		return ammo;
	}
	
	@Override
	public abstract ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player);
	
	@Override
	public boolean isValid(int slot, HashMap<Integer, Component> ecs)
	{
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
		return this.AMMO.get(this.getTagCompound(slot, stack), true);
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
	
	@Override
	public int capacity(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return this.capacity;
	}
	
	@Override
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		this.addHeat(slot, this.heating * modifier, stack);
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot2 : cs.keySet())
		{
			Component c = cs.get(slot2);
			if(c.type == Type.BARREL)
			{
				c.addHeat(slot2, this.heating * modifier, stack);
			}
			else if(c.type == Type.TRIGGER)
			{
				c.addHeat(slot2, this.heating / 4 * modifier, stack);
			}
		}
	}
}