package com.spectral.spectral_guns.itemtags;

import java.util.ArrayList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

public abstract class ItemTagArrayList<W> extends ItemTagBase<ArrayList<W>, NBTTagList>
{
	public ItemTagArrayList(String key, boolean noWobble)
	{
		super(key, new ArrayList(), noWobble);
	}
	
	@Override
	protected NBTTagList rawToNBTTag(ArrayList<W> value)
	{
		NBTTagList nbtList = new NBTTagList();
		for(W entry : value)
		{
			nbtList.appendTag(this.rawToNBTTagEntry(entry));
		}
		return nbtList;
	}
	
	@Override
	protected ArrayList<W> NBTTagToRaw(NBTTagList value)
	{
		ArrayList<W> arrayList = new ArrayList();
		for(int i = 0; i < value.tagCount(); ++i)
		{
			NBTBase entry = value.get(i);
			arrayList.add(this.NBTTagToRawEntry(entry));
		}
		return arrayList;
	}
	
	@Override
	public NBTTagList getDefault2()
	{
		return new NBTTagList();
	}
	
	protected abstract NBTBase rawToNBTTagEntry(W value);
	
	protected abstract W NBTTagToRawEntry(NBTBase value);
}