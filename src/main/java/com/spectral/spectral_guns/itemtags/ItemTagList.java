package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagList;

public class ItemTagList extends ItemTagBase<NBTTagList, NBTTagList>
{
	public ItemTagList(String key, boolean noWobble)
	{
		super(key, new NBTTagList(), noWobble);
	}
	
	@Override
	protected NBTTagList rawToNBTTag(NBTTagList value)
	{
		return value;
	}
	
	@Override
	protected NBTTagList NBTTagToRaw(NBTTagList value)
	{
		return value;
	}
	
	@Override
	public NBTTagList getDefault2()
	{
		return new NBTTagList();
	}
}