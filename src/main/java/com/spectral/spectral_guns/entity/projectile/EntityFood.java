package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.MathWithMultiple;
import com.spectral.spectral_guns.Stuff.Randomization;

public class EntityFood extends EntityThrowable implements IEntityAdditionalSpawnData
{
	public ItemStack stack = null;
	
	public EntityFood(World worldIn)
	{
		super(worldIn);
		float size = this.rand.nextFloat() * 2;
		this.setSize(size, size);
	}
	
	public EntityFood(World worldIn, EntityLivingBase thrower, ItemStack stack)
	{
		this(worldIn, thrower);
		if(stack.getItem() instanceof ItemFood)
		{
			stack.stackSize = 1;
			this.stack = stack;
		}
	}
	
	public EntityFood(World worldIn, EntityLivingBase thrower)
	{
		super(worldIn, thrower);
		float size = this.rand.nextFloat() * 2;
		this.setSize(size, size);
	}
	
	public EntityFood(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		float size = this.rand.nextFloat() * 2;
		this.setSize(size, size);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		ItemStack stack = this.getItemStack();
		Item item = stack.getItem();
		for(int i = 0; i < 1 && this.worldObj.isRemote; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, -this.motionX / 15 + Randomization.r(0.1), -this.motionY / 15 + Randomization.r(0.1), -this.motionZ / 15 + Randomization.r(0.1), new int[]{Item.getIdFromItem(this.worldObj.rand.nextInt(3) == 0 ? item : M.food_mush)});
		}
	}
	
	@Override
	protected void onImpact(MovingObjectPosition pos)
	{
		ItemStack stack = this.getItemStack();
		Item item = stack.getItem();
		
		int p = 16;
		
		if(pos.entityHit != null && pos.entityHit.canBeCollidedWith() && pos.entityHit != this.getThrower())
		{
			double d = Coordinates3D.distance(Coordinates3D.velocity(this)) * (MathWithMultiple.distance(this.height, this.width) * (0.2 + this.rand.nextDouble() * 0.3)) * 3F;
			if(pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), this.rand.nextFloat() * this.rand.nextFloat() * (float)d))
			{
				if(pos.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, this.rand.nextInt(10), this.rand.nextInt(3), false, false));
					((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.saturation.id, this.rand.nextInt(2 * 20), 0, false, false));
				}
				if(pos.entityHit instanceof EntityPlayer)
				{
					stack.getItem().onItemUseFinish(stack, this.worldObj, (EntityPlayer)pos.entityHit);
				}
				if(!this.worldObj.isRemote)
				{
					this.setDead();
				}
			}
		}
		if(pos.entityHit == null)
		{
			if(this.rand.nextFloat() < 5 / 6)
			{
				p /= 3;
				EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, this.getItemStack());
				entityitem.motionX = this.motionX;
				entityitem.motionY = this.motionY;
				entityitem.motionZ = this.motionZ;
				Coordinates3D.bounce(entityitem, pos.sideHit, 0.1);
				entityitem.setDefaultPickupDelay();
				if(!this.worldObj.isRemote)
				{
					this.worldObj.spawnEntityInWorld(entityitem);
				}
			}
			if(!this.worldObj.isRemote)
			{
				this.setDead();
			}
		}
		
		Vec3 m = Coordinates3D.bounce(Coordinates3D.velocity(this), pos.sideHit, 0.6);
		
		if(this.worldObj.isRemote)
		{
			for(int i = 0; i < p * 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, m.xCoord / 10 + Randomization.r(0.3), m.yCoord / 10 + Randomization.r(0.3), m.zCoord / 10 + Randomization.r(0.3), new int[]{Item.getIdFromItem(this.worldObj.rand.nextInt(3) == 0 ? item : M.food_mush)});
			}
			for(int i = 0; i < p / 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, m.xCoord / 10 + Randomization.r(0.2), m.yCoord / 10 + Randomization.r(0.2), m.zCoord / 10 + Randomization.r(0.2), new int[]{Item.getIdFromItem(this.worldObj.rand.nextInt(3) == 0 ? item : M.food_mush)});
			}
			for(int i = 0; i < p; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, this.posX, this.posY, this.posZ, Randomization.r(0.3), Randomization.r(0.3), Randomization.r(0.3), new int[]{Item.getIdFromItem(this.worldObj.rand.nextInt(3) == 0 ? item : M.food_mush)});
			}
		}
	}
	
	public ItemStack getItemStack()
	{
		if(this.stack != null && this.stack.getItem() instanceof ItemFood)
		{
			this.stack.stackSize = 1;
			return this.stack;
		}
		return new ItemStack(M.food_mush);
	}
	
	public Item getItem()
	{
		if(this.stack != null && this.stack.getItem() instanceof ItemFood)
		{
			return this.stack.getItem();
		}
		return M.food_mush;
	}
	
	@Override
	protected float getGravityVelocity()
	{
		return 0.09F;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		NBTTagCompound stack = this.stack.writeToNBT(new NBTTagCompound());
		if(stack != null)
		{
			tagCompound.setTag("Stack", stack);
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompound)
	{
		super.readEntityFromNBT(tagCompound);
		if(tagCompound.getCompoundTag("Stack") != null)
		{
			ItemStack stack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("Stack"));
			if(stack != null)
			{
				this.stack = stack;
			}
		}
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