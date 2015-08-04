package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagString;

public class ItemTagString extends ItemTagComparable<String, NBTTagString>
{
	public ItemTagString(String key, String defaultValue, boolean noWobble)
	{
		super(key, defaultValue, "", "", noWobble);
	}
	
	protected String isValid(String original)
	{
		if(original == null)
		{
			original = "";
		}
		return original;
	}
	
	@Override
	protected NBTTagString rawToNBTTag(String value)
	{
		return new NBTTagString(value);
	}
	
	@Override
	protected String NBTTagToRaw(NBTTagString value)
	{
		if(value != null)
		{
			return value.getString();
		}
		return "";
	}
	
	protected String add(String value1, String value2)
	{
		return value1 + value2;
	}
}