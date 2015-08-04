package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

public class ItemTagInteger extends ItemTagComparable<Integer, NBTTagInt>
{
	public ItemTagInteger(String key, Integer defaultValue, Integer min, Integer max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	@Override
	protected Integer isValid(NBTTagCompound compound, Integer original)
	{
		if(original > this.max)
		{
			return this.max;
		}
		if(original < this.min)
		{
			return this.min;
		}
		return original;
	}
	
	@Override
	protected NBTTagInt rawToNBTTag(Integer value)
	{
		return new NBTTagInt(value);
	}
	
	@Override
	protected Integer NBTTagToRaw(NBTTagInt value)
	{
		if(value != null)
		{
			return value.getInt();
		}
		return 0;
	}
	
	@Override
	protected Integer add(Integer value1, Integer value2)
	{
		return value1 + value2;
	}
}