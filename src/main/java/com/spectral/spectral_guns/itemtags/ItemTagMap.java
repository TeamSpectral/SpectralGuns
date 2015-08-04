package com.spectral.spectral_guns.itemtags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemTagMap<W, T extends ItemTagBase<W, ?>, K> extends ItemTagCompound
{
	public final T defaultEntry;
	
	public ItemTagMap(String key, T defaultEntry, boolean noWobble)
	{
		super(key, noWobble);
		this.defaultEntry = defaultEntry;
	}
	
	protected abstract String getKey(K key);
	
	protected abstract T newEntryTag(T defaultEntry, String key);
	
	public W get(ItemStack stack, K key)
	{
		return this.get(stack, key, true);
	}
	
	public W get(ItemStack stack, K key, boolean createNew)
	{
		NBTTagCompound compound = this.getCompound(stack, createNew);
		return this.get(compound, key, createNew);
	}
	
	public W get(NBTTagCompound compound, K key, boolean createNew)
	{
		return this.newEntryTag(this.defaultEntry, this.getKey(key)).get(this.get(compound, createNew), createNew);
	}
	
	public void set(ItemStack stack, K key, W value)
	{
		NBTTagCompound compound = this.getCompound(stack, true);
		this.set(compound, key, value);
	}
	
	public void set(NBTTagCompound compound, K key, W value)
	{
		this.newEntryTag(this.defaultEntry, this.getKey(key)).set(this.get(compound, true), value);
	}
}