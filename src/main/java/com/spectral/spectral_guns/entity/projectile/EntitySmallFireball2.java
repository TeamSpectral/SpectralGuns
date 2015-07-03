package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff.Coordinates3D;

public class EntitySmallFireball2 extends EntitySmallFireball implements IEntityAdditionalSpawnData
{
	public boolean hasBounced = false;
	
	public EntitySmallFireball2(World world)
	{
		super(world);
		this.setSize(this.width * 2, this.height * 2);
	}
	
	public EntitySmallFireball2(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(world, shooter, accelX, accelY, accelZ);
	}
	
	public EntitySmallFireball2(World world, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(world, x, y, z, accelX, accelY, accelZ);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		return distance < 200;
	}
	
	@Override
	public void onUpdate()
	{
		if(this.firstUpdate)
		{
			this.accelerationX = this.motionX;
			this.accelerationY = this.motionY;
			this.accelerationZ = this.motionZ;
		}
		super.onUpdate();
		double max = 3;
		Vec3 mot = new Vec3(this.motionX, this.motionY, this.motionZ);
		if(Coordinates3D.distance(mot) > max)
		{
			mot = Coordinates3D.stabilize(mot, max);
		}
		this.setVelocity(mot.xCoord, mot.yCoord, mot.zCoord);
		if(Coordinates3D.distance(mot) <= 0.001)
		{
			this.setDead();
		}
	}
	
	@Override
	protected void onImpact(MovingObjectPosition pos)
	{
		if(pos.entityHit != null && (!pos.entityHit.canBeCollidedWith() || pos.entityHit == this.shootingEntity && this.ticksExisted < 15))
		{
			return;
		}
		if(!this.worldObj.isRemote)
		{
			boolean flag;
			
			if(pos.entityHit != null)
			{
				if(pos.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)pos.entityHit).hurtResistantTime = 0;
				}
				flag = pos.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 2.0F);
				if(pos.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)pos.entityHit).hurtResistantTime = 0;
				}
				flag = flag || pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shootingEntity), 3.0F);
				
				if(flag)
				{
					this.func_174815_a(this.shootingEntity, pos.entityHit);
					
					if(!pos.entityHit.isImmuneToFire())
					{
						pos.entityHit.setFire(5);
					}
				}
				this.setDead();
			}
			else
			{
				flag = true;
				
				if(this.shootingEntity != null && this.shootingEntity instanceof EntityLiving)
				{
					flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
				}
				
				if(flag)
				{
					BlockPos blockpos = pos.getBlockPos().offset(pos.sideHit);
					
					if(this.worldObj.isAirBlock(blockpos))
					{
						this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
					}
				}
				if(!this.hasBounced)
				{
					Coordinates3D.bounce(this, pos.sideHit, 1);
					this.hasBounced = true;
				}
				else
				{
					this.setDead();
				}
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(this.isEntityInvulnerable(source))
		{
			return false;
		}
		else
		{
			this.setBeenAttacked();
			
			if(source.getEntity() != null)
			{
				Vec3 vec3 = source.getEntity().getLookVec();
				
				if(vec3 != null)
				{
					this.motionX = vec3.xCoord;
					this.motionY = vec3.yCoord;
					this.motionZ = vec3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}
				
				if(source.getEntity() instanceof EntityLivingBase)
				{
					this.shootingEntity = (EntityLivingBase)source.getEntity();
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	@Override
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
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
