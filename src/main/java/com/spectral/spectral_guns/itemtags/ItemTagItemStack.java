package com.spectral.spectral_guns.itemtags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemTagItemStack extends ItemTagBase<ItemStack, NBTTagCompound>
{
	public ItemTagItemStack(String key, ItemStack defaultValue, boolean noWobble)
	{
		super(key, defaultValue, noWobble);
	}
	
	@Override
	protected NBTTagCompound rawToNBTTag(ItemStack value)
	{
		if(value == null || value.stackSize <= 0)
		{
			return null;
		}
		return value.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	protected ItemStack NBTTagToRaw(NBTTagCompound value)
	{
		return ItemStack.loadItemStackFromNBT(value);
	}
	
	@Override
	protected ItemStack isValid(NBTTagCompound compound, ItemStack value)
	{
		if(value == null || value.stackSize <= 0)
		{
			return null;
		}
		return value;
	}
	
	@Override
	public NBTTagCompound getDefault2()
	{
		return null;
	}
}