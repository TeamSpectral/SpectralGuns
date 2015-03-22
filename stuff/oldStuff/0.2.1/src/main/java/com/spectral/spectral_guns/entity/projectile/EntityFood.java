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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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

	public EntityFood(World worldIn, EntityLivingBase thrower, ItemStack stack)
	{
		this(worldIn, thrower);
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
		for (int i = 0; i < 1; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX/15+Randomization.r(0.1), -this.motionY/15+Randomization.r(0.1), -this.motionZ/15+Randomization.r(0.1), new int[]{Item.getIdFromItem(item)});
		}
	}

	protected void onImpact(MovingObjectPosition pos)
	{
		ItemStack stack = getItemStack();
		Item item = stack.getItem();

		int p = 16;

		if(pos.entityHit != null && pos.entityHit.canBeCollidedWith() && pos.entityHit != this.getThrower())
		{
			double d = (Coordinates3D.distance(Coordinates3D.velocity(this))*4)*(MathWithMultiple.distance(this.height, this.width)*(0.2 + rand.nextDouble()*0.3));
			if(pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)d))
			{
				if(pos.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, rand.nextInt(10*20), rand.nextInt(1), false, false));
				}
				if(!this.worldObj.isRemote)
				{
					this.setDead();
				}
			}
		}
		if(pos.entityHit == null)
		{
			if(rand.nextFloat() < 2/3)
			{
				p /= 3;
				EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getItemStack());
				entityitem.motionX = this.motionX;
				entityitem.motionY = this.motionY;
				entityitem.motionZ = this.motionZ;
				Coordinates3D.bounce(entityitem, pos.sideHit, 0.1);
				entityitem.setDefaultPickupDelay();
				this.worldObj.spawnEntityInWorld(entityitem);
			}
			if(!this.worldObj.isRemote)
			{
				this.setDead();
			}
		}

		for(int i = 0; i < p*2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX/10+Randomization.r(0.2), -this.motionY/10+Randomization.r(0.2), -this.motionZ/10+Randomization.r(0.2), new int[]{Item.getIdFromItem(item)});
		}
		for(int i = 0; i < p/2; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, -this.motionX/10+Randomization.r(0.3), -this.motionY/10+Randomization.r(0.3), -this.motionZ/10+Randomization.r(0.3), new int[]{Item.getIdFromItem(item)});
		}
		for(int i = 0; i < p; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, Randomization.r(0.3), Randomization.r(0.3), Randomization.r(0.3), new int[]{Item.getIdFromItem(item)});
		}
	}

	public ItemStack getItemStack()
	{
		if(stack != null && (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemFood2))
		{
			stack.stackSize = 1;
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