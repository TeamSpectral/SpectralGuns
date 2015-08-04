package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagCompound;

public class ItemTagCompound extends ItemTagBase<NBTTagCompound, NBTTagCompound>
{
	public ItemTagCompound(String key, boolean noWobble)
	{
		super(key, new NBTTagCompound(), noWobble);
	}
	
	@Override
	protected NBTTagCompound rawToNBTTag(NBTTagCompound value)
	{
		return value;
	}
	
	@Override
	protected NBTTagCompound NBTTagToRaw(NBTTagCompound value)
	{
		return value;
	}
	
	@Override
	public NBTTagCompound getDefault2()
	{
		return new NBTTagCompound();
	}
}