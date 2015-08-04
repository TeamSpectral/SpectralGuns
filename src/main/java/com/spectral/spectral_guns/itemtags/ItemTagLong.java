package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagLong;

public class ItemTagLong extends ItemTagComparable<Long, NBTTagLong>
{
	public ItemTagLong(String key, Long defaultValue, Long min, Long max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	@Override
	protected Long isValid(NBTTagCompound compound, Long original)
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
	protected NBTTagLong rawToNBTTag(Long value)
	{
		return new NBTTagLong(value);
	}
	
	@Override
	protected Long NBTTagToRaw(NBTTagLong value)
	{
		if(value != null)
		{
			return value.getLong();
		}
		return 0L;
	}
	
	@Override
	protected Long add(Long value1, Long value2)
	{
		return value1 + value2;
	}
}