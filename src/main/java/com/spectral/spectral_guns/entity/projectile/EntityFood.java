package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityFood extends EntityThrowable implements IEntityAdditionalSpawnData
{
	public ItemStack stack = null;
	
	public EntityFood(World worldIn)
	{
		super(worldIn);
		float size = rand.nextFloat() * 2;
		this.setSize(size, size);
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
		float size = rand.nextFloat() * 2;
		this.setSize(size, size);
	}
	
	public EntityFood(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		float size = rand.nextFloat() * 2;
		this.setSize(size, size);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		ItemStack stack = getItemStack();
		Item item = stack.getItem();
		for(int i = 0; i < 1 && worldObj.isRemote; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, -this.motionX / 15 + Randomization.r(0.1), -this.motionY / 15 + Randomization.r(0.1), -this.motionZ / 15 + Randomization.r(0.1), new int[] {Item.getIdFromItem(item)});
		}
	}
	
	protected void onImpact(MovingObjectPosition pos)
	{
		ItemStack stack = getItemStack();
		Item item = stack.getItem();
		
		int p = 16;
		
		if(pos.entityHit != null && pos.entityHit.canBeCollidedWith() && pos.entityHit != this.getThrower())
		{
			double d = Coordinates3D.distance(Coordinates3D.velocity(this)) * (MathWithMultiple.distance(this.height, this.width) * (0.2 + rand.nextDouble() * 0.3));
			if(pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), rand.nextFloat() * rand.nextFloat() * (float)d))
			{
				if(pos.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, rand.nextInt(10 * 20), rand.nextInt(3), false, false));
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.confusion.id, rand.nextInt(20), 0, false, false));
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.saturation.id, rand.nextInt(2 * 20), 0, false, false));
				}
				if(!this.worldObj.isRemote)
				{
					this.setDead();
				}
			}
		}
		if(pos.entityHit == null)
		{
			if(rand.nextFloat() < 5 / 6)
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
		
		Vec3 m = Coordinates3D.bounce(Coordinates3D.velocity(this), pos.sideHit, 0.6);
		
		if(worldObj.isRemote)
		{
			for(int i = 0; i < p * 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, m.xCoord / 10 + Randomization.r(0.3), m.yCoord / 10 + Randomization.r(0.3), m.zCoord / 10 + Randomization.r(0.3), new int[] {Item.getIdFromItem(item)});
			}
			for(int i = 0; i < p / 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, m.xCoord / 10 + Randomization.r(0.2), m.yCoord / 10 + Randomization.r(0.2), m.zCoord / 10 + Randomization.r(0.2), new int[] {Item.getIdFromItem(item)});
			}
			for(int i = 0; i < p; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, Randomization.r(0.3), Randomization.r(0.3), Randomization.r(0.3), new int[] {Item.getIdFromItem(item)});
			}
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
	
	protected float getGravityVelocity()
	{
		return 0.09F;
	}
	
	/**
	 * Called by the server when constructing the spawn packet.
	 * Data should be added to the provided stream.
	 *
	 * @param buffer
	 *            The packet data stream
	 */
	@Override
	public void writeSpawnData(ByteBuf buf)
	{
		NBTTagCompound compound = new NBTTagCompound();
		this.writeEntityToNBT(compound);
		ByteBufUtils.writeTag(buf, compound);
	}
	
	/**
	 * Called by the client when it receives a Entity spawn packet.
	 * Data should be read out of the stream in the same way as it was written.
	 *
	 * @param data
	 *            The packet data stream
	 */
	@Override
	public void readSpawnData(ByteBuf buf)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		if(compound != null)
		{
			this.readEntityFromNBT(compound);
		}
	}
}