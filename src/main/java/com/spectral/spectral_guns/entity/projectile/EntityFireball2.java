package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
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

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.Coordinates3D;

public class EntityFireball2 extends EntitySmallFireball implements IEntityAdditionalSpawnData, IEntityGunProjectile
{
	public boolean hasBounced = false;
	private double mass = 2;
	public double damage = 3;
	public int burn = 5;
	
	public EntityFireball2(World world)
	{
		super(world);
		this.setSize(this.width * 2, this.height * 2);
	}
	
	public EntityFireball2(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(world, shooter, accelX, accelY, accelZ);
	}
	
	public EntityFireball2(World world, double x, double y, double z, double accelX, double accelY, double accelZ)
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
				double damage = this.getMass() * this.getSpeed();
				if(this.hasBounced)
				{
					damage /= 2;
				}
				damage += this.damage;
				GunProjectileDamageHandler.putDamage(pos.entityHit, DamageSource.causeFireballDamage(this, this.shootingEntity), damage * 2 / 5, new GunProjectileDamageHandler.ConsumerDamageDealt()
				{
					
					@Override
					public void action(Entity entityHit, DamageSource ds, double amount)
					{
						if(entityHit instanceof EntityLivingBase)
						{
							((EntityLivingBase)entityHit).hurtResistantTime = 0;
						}
						if(entityHit.attackEntityFrom(DamageSource.causeThrownDamage(EntityFireball2.this, EntityFireball2.this.shootingEntity), (float)(amount / (2 / 5)) * 3 / 5))
						{
							EntityFireball2.this.func_174815_a(EntityFireball2.this.shootingEntity, entityHit);
							
							if(!entityHit.isImmuneToFire())
							{
								entityHit.setFire(EntityFireball2.this.burn);
							}
						}
					}
				});
				if(!this.worldObj.isRemote)
				{
					this.setDead();
				}
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
	
	@Override
	public void setMass(double mass)
	{
		this.setSize(this.width / (float)this.mass, this.height / (float)this.mass);
		this.mass = mass;
		this.setSize(this.width * (float)this.mass, this.height * (float)this.mass);
	}
	
	@Override
	public void setSpeed(double velocity)
	{
		Stuff.Coordinates3D.velocity(this, Stuff.Coordinates3D.stabilize(Stuff.Coordinates3D.velocity(this), velocity));
	}
	
	@Override
	public void setForce(double force)
	{
		this.setSpeed(force / this.getMass());
	}
	
	@Override
	public double getMass()
	{
		return this.mass;
	}
	
	@Override
	public double getSpeed()
	{
		return Stuff.Coordinates3D.distance(Stuff.Coordinates3D.velocity(this));
	}
	
	@Override
	public double getForce()
	{
		return this.getSpeed() * this.getMass();
	}
}
