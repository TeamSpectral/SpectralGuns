package com.spectral.spectral_guns.stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff;

public abstract class LegendaryParticles extends LegendaryBase
{
	public final double timesFire;
	public final double timesUpdate;
	public final double radiusOffset;
	
	public LegendaryParticles(String name, EnumChatFormatting color, int xpReward, ItemStack achievementIcon, double timesFire, double timesUpdate, double radiusOffset, String desc)
	{
		super(name, color, xpReward, achievementIcon, desc);
		this.timesFire = timesFire;
		this.timesUpdate = timesUpdate;
		this.radiusOffset = radiusOffset;
	}
	
	@Override
	public final void onFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier)
	{
		if(this.onFire2(e, stack, world, player, modifier))
		{
			double modifier2 = modifier * this.timesFire;
			int i = (int)Math.floor(modifier2);
			double r = modifier2 - i;
			if(i + r > 0)
			{
				if(world.rand.nextDouble() <= r)
				{
					++i;
				}
				for(int i2 = 0; i2 < i; ++i2)
				{
					Vec3 vec = Stuff.Coordinates3D.stabilize(new Vec3(world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble()), world.rand.nextDouble() * world.rand.nextDouble());
					Vec3 origo = this.getOrigoOnFire(e, stack, world, player, modifier);
					double offset = this.radiusOffset;
					this.particleOnFire(e, stack, world, player, modifier, origo.xCoord + vec.xCoord * (player.width + offset), origo.yCoord + vec.yCoord * (player.height + offset), origo.zCoord + vec.zCoord * (player.width + offset));
				}
			}
		}
	}
	
	private Vec3 getOrigoOnFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier)
	{
		return new Vec3(e.posX, e.posY + e.getYOffset() + e.height / 2, e.posZ);
	}
	
	public boolean onFire2(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier)
	{
		return true;
	}
	
	public abstract void particleOnFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier, double x, double y, double z);
	
	@Override
	public final void onUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected)
	{
		if(this.onUpdate2(stack, world, player, invSlot, isSelected) && isSelected)
		{
			double modifier2 = this.timesUpdate;
			int i = (int)Math.floor(modifier2);
			double r = modifier2 - i;
			if(i + r > 0)
			{
				if(world.rand.nextDouble() <= r)
				{
					++i;
				}
				for(int i2 = 0; i2 < i; ++i2)
				{
					Vec3 vec = Stuff.Coordinates3D.stabilize(new Vec3(world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble()), world.rand.nextDouble() * world.rand.nextDouble());
					Vec3 origo = this.getOrigoOnUpdate(stack, world, player, invSlot, isSelected);
					double offset = this.radiusOffset;
					this.particleOnUpdate(stack, world, player, invSlot, isSelected, origo.xCoord + vec.xCoord * (player.width + offset), origo.yCoord + vec.yCoord * (player.height + offset), origo.zCoord + vec.zCoord * (player.width + offset));
				}
			}
		}
	}
	
	public final boolean onUpdate2(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected)
	{
		return true;
	}
	
	public abstract void particleOnUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected, double x, double y, double z);
	
	public Vec3 getOrigoOnUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected)
	{
		return new Vec3(player.posX, player.posY + player.getYOffset() + player.getEyeHeight(), player.posZ);
	}
}
