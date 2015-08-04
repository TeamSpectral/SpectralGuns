package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.Vec3;

public class ItemTagVecDouble extends ItemTagVec<Vec3, NBTTagDouble>
{
	public ItemTagVecDouble(String key, Vec3 defaultValue, Vec3 min, Vec3 max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	@Override
	protected Vec3 toVec(Vec3 value)
	{
		return value;
	}
	
	@Override
	protected Vec3 fromVec(Vec3 vec)
	{
		return vec;
	}
	
	@Override
	protected NBTTagDouble toTag(double value)
	{
		return new NBTTagDouble(value);
	}
	
	@Override
	protected double fromTag(NBTTagDouble tag)
	{
		return tag.getDouble();
	}
}