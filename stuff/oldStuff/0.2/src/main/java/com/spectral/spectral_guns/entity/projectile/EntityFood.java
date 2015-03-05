package com.spectral.spectral_guns.entity.projectile;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.MathWithMultiple;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.items.ItemFood2;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityFood extends EntityThrowable
{
	public ItemStack stack = null;

	public EntityFood(World worldIn)
	{
		super(worldIn);
		this.setSize(rand.nextFloat()*0.5F, rand.nextFloat()*0.5F);
	}

	public EntityFood(World worldIn, ItemStack stack)
	{
		this(worldIn);
		if(stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemFood2)
		{
			stack.stackSize = 1;
			this.stack = stack;
		}
	}

	public EntityFood(World worldIn, EntityLivingBase thrower)
	{
		super(worldIn, thrower);
		this.setSize(rand.nextFloat()*0.5F, rand.nextFloat()*0.5F);
	}

	public EntityFood(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setSize(rand.nextFloat()*0.5F, rand.nextFloat()*0.5F);
	}

	public void onUpdate()
	{
		super.onUpdate();
		ItemStack stack = getItemStack();
		Item item = stack.getItem();
		for (int i = 0; i < 3; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX/3+Randomization.r(0.3), -this.motionY/3+Randomization.r(0.3), -this.motionZ/3+Randomization.r(0.3), new int[]{Item.getIdFromItem(item)});
		}
	}

	protected void onImpact(MovingObjectPosition pos)
	{
		ItemStack stack = getItemStack();
		Item item = stack.getItem();

		int p = 16;
		
		if(pos.entityHit != null)
		{
			double d = (Coordinates3D.distance(Coordinates3D.velocity(this))*4)*(MathWithMultiple.distance(this.height, this.width)*(0.2 + rand.nextDouble()*0.3));
			pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)d);
		}
		else if(rand.nextFloat() < 2/3)
		{
			p /= 3;
			EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getItemStack());
			entityitem.motionX = this.motionX;
			entityitem.motionY = this.motionY;
			entityitem.motionZ = this.motionZ;
			Coordinates3D.bounce(entityitem, pos.sideHit, 0.5);
			entityitem.setDefaultPickupDelay();
			this.worldObj.spawnEntityInWorld(entityitem);
		}

		for(int i = 0; i < p*2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX+Randomization.r(0.3), -this.motionY+Randomization.r(0.3), -this.motionZ+Randomization.r(0.3), new int[]{Item.getIdFromItem(item)});
		}
		for(int i = 0; i < p*3; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX*rand.nextFloat(), -this.motionY*rand.nextFloat(), -this.motionZ*rand.nextFloat(), new int[]{Item.getIdFromItem(item)});
		}
		for(int i = 0; i < p; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, Randomization.r(0.8), Randomization.r(0.8), Randomization.r(0.8), new int[]{Item.getIdFromItem(item)});
		}

		if(!this.worldObj.isRemote)
		{
			this.setDead();
		}
	}

	public ItemStack getItemStack()
	{
		if(stack != null && (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemFood2))
		{
			return stack;
		}
		return new ItemStack(M.food_mush);
	}

	public Item getItem()
	{
		if(stack != null && (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemFood2))
		{
			return stack.getItem();
		}
		return M.food_mush;
	}
}