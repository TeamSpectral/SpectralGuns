package com.spectral.spectral_guns.items;

import java.util.Iterator;

import net.minecraft.item.Item;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.M.Id;

public abstract class ItemAmmo extends Item
{
	public final int multiplier;
	
	public ItemAmmo(int multiplier)
	{
		this.multiplier = multiplier;
	}
	
	@Override
	public Item setUnlocalizedName(String unlocalizedName)
	{
		return super.setUnlocalizedName("ammo." + unlocalizedName);
	}
	
	public abstract Item ammo();
	
	public abstract void recipe();
	
	public static boolean itemHasItemAmmo(Item item)
	{
		return getItemAmmo(item) != null;
	}
	
	public static ItemAmmo getItemAmmo(Item item)
	{
		Iterator<Id> ids = M.getIds();
		while(ids.hasNext())
		{
			Id id = ids.next();
			if(M.getItem(id) instanceof ItemAmmo)
			{
				ItemAmmo item2 = (ItemAmmo)M.getItem(id);
				if(item2.ammo() == item && item2.multiplier == 1)
				{
					return item2;
				}
			}
		}
		return null;
	}
}
