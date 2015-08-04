package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagFloat;

public class ItemTagFloat extends ItemTagComparable<Float, NBTTagFloat>
{
	public ItemTagFloat(String key, Float defaultValue, Float min, Float max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	protected Float isValid(Float original)
	{
		if(original > max)
		{
			return max;
		}
		if(original < min)
		{
			return min;
		}
		return original;
	}
	
	@Override
	protected NBTTagFloat rawToNBTTag(Float value)
	{
		return new NBTTagFloat(value);
	}
	
	@Override
	protected Float NBTTagToRaw(NBTTagFloat value)
	{
		if(value != null)
		{
			return value.getFloat();
		}
		return 0F;
	}
	
	protected Float add(Float value1, Float value2)
	{
		return value1 + value2;
	}
}