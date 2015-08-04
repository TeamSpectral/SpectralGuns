package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagInt;

public class ItemTagEnum<T extends Enum> extends ItemTagBase<T, NBTTagInt>
{
	private final T defaultValue2;
	
	public ItemTagEnum(String key, T defaultValue, boolean noWobble)
	{
		super(key, defaultValue, noWobble);
		this.defaultValue2 = defaultValue;
	}
	
	@Override
	protected NBTTagInt rawToNBTTag(T value)
	{
		return new NBTTagInt(value.ordinal());
	}
	
	@Override
	protected T NBTTagToRaw(NBTTagInt value)
	{
		try
		{
			return (T)this.defaultValue2.getClass().getDeclaredFields()[value.getInt()].get(null);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return this.defaultValue2;
	}
}