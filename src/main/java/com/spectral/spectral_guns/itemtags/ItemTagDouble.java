package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;

public class ItemTagDouble extends ItemTagComparable<Double, NBTTagDouble>
{
	public ItemTagDouble(String key, Double defaultValue, Double min, Double max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	@Override
	protected Double isValid(NBTTagCompound compound, Double original)
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
	protected NBTTagDouble rawToNBTTag(Double value)
	{
		return new NBTTagDouble(value);
	}
	
	@Override
	protected Double NBTTagToRaw(NBTTagDouble value)
	{
		if(value != null)
		{
			return value.getDouble();
		}
		return 0D;
	}
	
	@Override
	protected Double add(Double value1, Double value2)
	{
		return value1 + value2;
	}
}