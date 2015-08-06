package com.spectral.spectral_guns.itemtags;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import com.spectral.spectral_guns.Stuff;

public abstract class ItemTagBase<W, T extends NBTBase>
{
	private T defaultValue;
	public final String key;
	public final boolean noWobble;
	
	public ItemTagBase(String key, W defaultValue, boolean noWobble)
	{
		this.key = key;
		this.noWobble = noWobble;
		if(defaultValue != null)
		{
			this.defaultValue = this.rawToNBTTag(defaultValue);
		}
	}
	
	public W get(ItemStack stack)
	{
		return this.get(stack, true);
	}
	
	public W get(ItemStack stack, boolean createNew)
	{
		NBTTagCompound compound = this.getCompound(stack, createNew);
		if(compound == null)
		{
			return this.getDefault();
		}
		return this.get(compound, createNew);
	}
	
	public W get(NBTTagCompound compound, boolean createNew)
	{
		if(createNew)
		{
			this.set(compound, this.get(compound, false));
		}
		if(this.has(compound))
		{
			return this.NBTTagToRaw(this.isValid2(compound, (T)compound.getTag(this.key)));
		}
		else
		{
			T d = this.getDefault2();
			if(d != null)
			{
				return this.NBTTagToRaw(this.isValid2(compound, (T)this.getDefault2().copy()));
			}
			return null;
		}
	}
	
	public void set(ItemStack stack, W value)
	{
		this.wobbleCheck(stack);
		NBTTagCompound compound = this.getCompound(stack, true);
		this.set(compound, value);
	}
	
	public void set(ItemStack stackForWobble, NBTTagCompound compound, W value)
	{
		this.wobbleCheck(stackForWobble);
		this.set(compound, value);
	}
	
	public void set(NBTTagCompound compound, W value)
	{
		if(value != null)
		{
			T tag = this.rawToNBTTag(value);
			compound.setTag(this.key, tag);
		}
		else
		{
			this.remove(compound);
		}
	}
	
	public void wobbleCheck(ItemStack stack)
	{
		if(this.noWobble)
		{
			noWobble(stack);
		}
	}
	
	/**
	 * must be run every time before a value is set to prevent wobble
	 */
	public static void noWobble(ItemStack stack)
	{
		try
		{
			Stuff.Reflection.setItemToRender(stack);
		}
		catch(Throwable e)
		{
			System.out.println("Anti-wobble procedure failed miserably! This is why reflection sucks so much!!! FFFFFUUUUUUUU!!!!!!!!! (i blame notch)");
			e.printStackTrace();
		}
	}
	
	public boolean has(ItemStack stack)
	{
		NBTTagCompound compound = this.getCompound(stack, false);
		return compound != null ? this.has(compound) && this.get(compound, false) != null : false;
	}
	
	public boolean has(NBTTagCompound compound)
	{
		NBTBase tag = compound.getTag(this.key);
		T d = this.getDefault2();
		return tag != null && (d == null || d.getClass().isInstance(tag) && tag.getId() == d.getId());
	}
	
	public boolean remove(ItemStack stack)
	{
		NBTTagCompound compound = this.getCompound(stack, false);
		boolean b = compound != null ? this.has(compound) : false;
		if(compound.hasNoTags())
		{
			stack.setTagCompound(null);
		}
		return b;
	}
	
	public boolean remove(NBTTagCompound compound)
	{
		boolean b = this.has(compound);
		compound.removeTag(this.key);
		return b;
	}
	
	public W getDefault()
	{
		T d = this.getDefault2();
		if(d != null)
		{
			return this.NBTTagToRaw(d);
		}
		return null;
	}
	
	public T getDefault2()
	{
		return this.defaultValue;
	}
	
	protected abstract T rawToNBTTag(W value);
	
	protected abstract W NBTTagToRaw(T value);
	
	protected T isValid2(NBTTagCompound compound, T original)
	{
		return this.rawToNBTTag(this.isValid(compound, this.NBTTagToRaw(original)));
	}
	
	protected W isValid(NBTTagCompound compound, W original)
	{
		return original;
	}
	
	@Override
	public String toString()
	{
		return this.key;
	}
	
	protected final NBTTagCompound getCompound(ItemStack stack, boolean createNew)
	{
		NBTTagCompound compound = stack.getTagCompound();
		if(compound == null)
		{
			compound = new NBTTagCompound();
			if(createNew)
			{
				stack.setTagCompound(compound);
			}
		}
		return compound;
	}
}
