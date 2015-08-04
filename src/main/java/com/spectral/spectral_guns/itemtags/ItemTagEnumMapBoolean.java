package com.spectral.spectral_guns.itemtags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemTagEnumMapBoolean<K extends Enum> extends ItemTagEnumMap<Boolean, ItemTagBoolean, K>
{
	public ItemTagEnumMapBoolean(String key, Boolean defaultValue, boolean noWobble)
	{
		super(key, new ItemTagBoolean("", defaultValue, false), noWobble);
	}
	
	@Override
	public Boolean get(ItemStack stack, K key)
	{
		return this.get(stack, key, true);
	}
	
	@Override
	public Boolean get(ItemStack stack, K key, boolean createNew)
	{
		NBTTagCompound compound = this.getCompound(stack, createNew);
		return this.get(compound, key, createNew);
	}
	
	@Override
	public Boolean get(NBTTagCompound compound, K key, boolean createNew)
	{
		return this.newEntryTag(this.defaultEntry, this.getKey(key)).get(this.get(compound, createNew), createNew);
	}
	
	@Override
	public void set(ItemStack stack, K key, Boolean value)
	{
		NBTTagCompound compound = this.getCompound(stack, true);
		this.set(compound, key, value);
	}
	
	@Override
	public void set(NBTTagCompound compound, K key, Boolean value)
	{
		this.newEntryTag(this.defaultEntry, this.getKey(key)).set(this.get(compound, true), value);
	}
	
	@Override
	protected ItemTagBoolean newEntryTag(ItemTagBoolean defaultEntry, String key)
	{
		return new ItemTagBoolean(key, defaultEntry.getDefault(), false);
	}
}