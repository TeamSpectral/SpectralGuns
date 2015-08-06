package com.spectral.spectral_guns.itemtags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemTagComparable<W, T extends NBTBase> extends ItemTagBase<W, T>
{
	public final W min;
	public final W max;
	
	public ItemTagComparable(String key, W defaultValue, W min, W max, boolean noWobble)
	{
		super(key, defaultValue, noWobble);
		this.min = min;
		this.max = max;
	}
	
	@Override
	protected W isValid(NBTTagCompound compound, W original)
	{
		return original;
	}
	
	public void add(ItemStack stack, W amount)
	{
		NBTTagCompound compound = this.getCompound(stack, true);
		this.wobbleCheck(stack);
		this.add(compound, amount);
	}
	
	public void add(ItemStack stackForWobble, NBTTagCompound compound, W amount)
	{
		this.wobbleCheck(stackForWobble);
		this.add(compound, amount);
	}
	
	public void add(NBTTagCompound compound, W amount)
	{
		W value = this.get(compound, true);
		this.set(compound, this.add(value, amount));
	}
	
	protected abstract W add(W value1, W value2);
}