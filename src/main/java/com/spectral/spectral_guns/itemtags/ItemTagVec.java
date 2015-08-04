package com.spectral.spectral_guns.itemtags;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;

import com.spectral.spectral_guns.Stuff;

public abstract class ItemTagVec<W, T extends NBTBase> extends ItemTagComparable<W, NBTTagList>
{
	public ItemTagVec(String key, W defaultValue, W min, W max, boolean noWobble)
	{
		super(key, defaultValue, min, max, noWobble);
	}
	
	protected abstract Vec3 toVec(W value);
	
	protected abstract W fromVec(Vec3 vec);
	
	protected abstract T toTag(double value);
	
	protected abstract double fromTag(T tag);
	
	@Override
	protected W isValid(NBTTagCompound compound, W original)
	{
		Vec3 vec = this.toVec(original);
		Vec3 vecMax = this.toVec(this.max);
		Vec3 vecMin = this.toVec(this.min);
		if(vec.xCoord > vecMax.xCoord)
		{
			vec = new Vec3(vecMax.xCoord, vec.yCoord, vec.zCoord);
		}
		if(vec.yCoord > vecMax.yCoord)
		{
			vec = new Vec3(vec.xCoord, vecMax.yCoord, vec.zCoord);
		}
		if(vec.zCoord > vecMax.zCoord)
		{
			vec = new Vec3(vec.xCoord, vec.yCoord, vecMax.zCoord);
		}
		
		if(vec.xCoord < vecMin.xCoord)
		{
			vec = new Vec3(vecMin.xCoord, vec.yCoord, vec.zCoord);
		}
		if(vec.yCoord < vecMin.yCoord)
		{
			vec = new Vec3(vec.xCoord, vecMin.yCoord, vec.zCoord);
		}
		if(vec.zCoord < vecMin.zCoord)
		{
			vec = new Vec3(vec.xCoord, vec.yCoord, vecMin.zCoord);
		}
		return this.fromVec(vec);
	}
	
	@Override
	protected NBTTagList rawToNBTTag(W value)
	{
		Vec3 vec = this.toVec(value);
		NBTTagList list = new NBTTagList();
		list.appendTag(this.toTag(vec.xCoord));
		list.appendTag(this.toTag(vec.yCoord));
		list.appendTag(this.toTag(vec.zCoord));
		return list;
	}
	
	@Override
	protected W NBTTagToRaw(NBTTagList value)
	{
		if(value != null && value.tagCount() >= 3 && value.getTagType() == new NBTTagDouble(0).getId())
		{
			return this.fromVec(new Vec3(this.fromTag((T)value.get(0)), this.fromTag((T)value.get(1)), this.fromTag((T)value.get(2))));
		}
		return this.fromVec(new Vec3(0, 0, 0));
	}
	
	@Override
	protected W add(W value1, W value2)
	{
		return this.fromVec(Stuff.Coordinates3D.add(this.toVec(value1), this.toVec(value2)));
	}
}