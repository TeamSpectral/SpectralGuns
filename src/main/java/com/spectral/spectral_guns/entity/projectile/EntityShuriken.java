package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Randomization;

public class EntityShuriken extends Entity implements IProjectile, IEntityAdditionalSpawnData
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private int inData;
	private boolean inGround;
	public int arrowShake;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage = 3.0D;
	private int knockbackStrength;
	public float spin = 0;
	public float rotationRoll = 90;
	
	public EntityShuriken(World worldIn)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.spin = Randomization.r(180);
	}
	
	public EntityShuriken(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(x, y, z);
		this.spin = Randomization.r(180);
	}
	
	public EntityShuriken(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float inaccuracy)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shooter;
		
		this.spin = Randomization.r(180);
		this.rotationRoll = 180 + shooter.rotationPitch;
		
		this.posY = shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D;
		double d0 = target.posX - shooter.posX;
		double d1 = target.getEntityBoundingBox().minY + target.height / 3.0F - this.posY;
		double d2 = target.posZ - shooter.posZ;
		double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		
		if(d3 >= 1.0E-7D)
		{
			float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float)-(Math.atan2(d1, d3) * 180.0D / Math.PI);
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
			float f4 = (float)(d3 * 0.20000000298023224D);
			this.setThrowableHeading(d0, d1 + f4, d2, velocity, inaccuracy);
		}
		this.rotationPitch = shooter.rotationPitch;
	}
	
	public EntityShuriken(World worldIn, EntityLivingBase shooter, float velocity)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.shootingEntity = shooter;
		
		this.spin = Randomization.r(180);
		this.rotationRoll = 90 - shooter.rotationPitch;
		
		this.setSize(0.5F, 0.5F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.posY -= 0.10000000149011612D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI);
		this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI);
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
		
		this.rotationPitch = shooter.rotationPitch;
	}
	
	@Override
	protected void entityInit()
	{
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}
	
	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy)
	{
		float f2 = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= f2;
		y /= f2;
		z /= f2;
		x += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		y += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		z += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		x *= velocity;
		y *= velocity;
		z *= velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f3 = MathHelper.sqrt_double(x * x + z * z);
		this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, f3) * 180.0D / Math.PI);
		this.ticksInGround = 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
	{
		this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		this.setRotation(p_180426_7_, p_180426_8_);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z)
	{
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(x * x + z * z);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(x, z) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(y, f) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
			this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
			this.ticksInGround = 0;
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if(this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
		{
			float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0D / Math.PI);
		}
		
		BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
		Block block = iblockstate.getBlock();
		
		if(block.getMaterial() != Material.air)
		{
			block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);
			
			if(axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ)))
			{
				this.inGround = true;
			}
		}
		
		if(this.arrowShake > 0)
		{
			--this.arrowShake;
		}
		
		if(this.inGround)
		{
			int j = block.getMetaFromState(iblockstate);
			
			if(block == this.inTile && j == this.inData)
			{
				++this.ticksInGround;
				
				if(this.ticksInGround >= 1200)
				{
					this.setDead();
				}
			}
			else
			{
				this.inGround = false;
				this.motionX *= this.rand.nextFloat() * 0.2F;
				this.motionY *= this.rand.nextFloat() * 0.2F;
				this.motionZ *= this.rand.nextFloat() * 0.2F;
				this.ticksInGround = 0;
				this.ticksInAir = 0;
			}
		}
		else
		{
			++this.ticksInAir;
			this.spin += 23;
			Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
			Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
			vec31 = new Vec3(this.posX, this.posY, this.posZ);
			vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			
			if(movingobjectposition != null)
			{
				vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}
			
			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			int i;
			float f1 = 0;
			
			for(i = 0; i < list.size(); ++i)
			{
				Entity entity1 = (Entity)list.get(i);
				
				if(entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
				{
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f1, f1, f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
					
					if(movingobjectposition1 != null)
					{
						double d1 = vec31.distanceTo(movingobjectposition1.hitVec);
						
						if(d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}
			
			if(entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}
			
			if(movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
			{
				EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
				
				if(entityplayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))
				{
					movingobjectposition = null;
				}
			}
			
			float f2 = 0;
			float f3 = 0;
			float f4 = 0;
			
			if(movingobjectposition != null)
			{
				float[] fs = this.onImpact(movingobjectposition, iblockstate);
				f2 = fs[0];
				f3 = fs[1];
				f4 = fs[2];
			}
			
			if(this.getIsCritical())
			{
				for(i = 0; i < 4; ++i)
				{
					this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * i / 4.0D, this.posY + this.motionY * i / 4.0D, this.posZ + this.motionZ * i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
				}
			}
			
			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
			
			for(this.rotationPitch = (float)(Math.atan2(this.motionY, f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
				;
			}
			
			while(this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}
			
			while(this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}
			
			while(this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}
			
			while(this.spin < -180.0F)
			{
				this.spin += 360.0F;
			}
			
			while(this.spin >= 180.0F)
			{
				this.spin -= 360.0F;
			}
			
			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			f3 = 0.99F;
			f1 = 0.05F;
			
			if(this.isInWater())
			{
				for(int l = 0; l < 4; ++l)
				{
					f4 = 0.25F;
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
				}
				
				f3 = 0.6F;
			}
			
			if(this.isWet())
			{
				this.extinguish();
			}
			
			this.motionX *= f3;
			this.motionY *= f3;
			this.motionZ *= f3;
			this.motionY -= f1;
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}
	}
	
	protected float[] onImpact(MovingObjectPosition pos, IBlockState iblockstate)
	{
		float f2 = 0;
		float f3 = 0;
		float f4 = 0;
		
		if(pos.entityHit != null)
		{
			f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			int k = MathHelper.ceiling_double_int(f2 * this.damage);
			
			if(this.getIsCritical())
			{
				k += this.rand.nextInt(k / 2 + 2);
			}
			
			DamageSource damagesource;
			
			if(this.shootingEntity == null)
			{
				damagesource = DamageSource.causeThrownDamage(this, this);
			}
			else
			{
				damagesource = DamageSource.causeThrownDamage(this, this.shootingEntity);
			}
			
			if(pos.entityHit.attackEntityFrom(damagesource, (float)k / 2))
			{
				if(pos.entityHit instanceof EntityLivingBase)
				{
					EntityLivingBase entitylivingbase = (EntityLivingBase)pos.entityHit;
					
					f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
					
					float knockback = -0.5F;
					
					if(f4 > 0.0F)
					{
						pos.entityHit.addVelocity(this.motionX * knockback * 0.6000000238418579D / f4, 0.1D, this.motionZ * knockback * 0.6000000238418579D / f4);
					}
					
					if(this.shootingEntity != null && pos.entityHit != this.shootingEntity && pos.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
					{
						((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
					}
				}
				
				this.playSound("random.bowhit", 1.1F, 2.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
				
				if(!(pos.entityHit instanceof EntityEnderman))
				{
					this.setDead();
				}
			}
			else
			{
				this.motionX *= 0.10000000149011612D;
				this.motionY *= 0.10000000149011612D;
				this.motionZ *= 0.10000000149011612D;
				// this.rotationYaw += 180.0F;
				// this.prevRotationYaw += 180.0F;
				this.ticksInAir = 0;
			}
		}
		else
		{
			BlockPos blockpos1 = pos.getBlockPos();
			this.xTile = blockpos1.getX();
			this.yTile = blockpos1.getY();
			this.zTile = blockpos1.getZ();
			iblockstate = this.worldObj.getBlockState(blockpos1);
			this.inTile = iblockstate.getBlock();
			this.inData = this.inTile.getMetaFromState(iblockstate);
			this.motionX = (float)(pos.hitVec.xCoord - this.posX);
			this.motionY = (float)(pos.hitVec.yCoord - this.posY);
			this.motionZ = (float)(pos.hitVec.zCoord - this.posZ);
			f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			this.posX -= this.motionX / f3 * 0.05000000074505806D;
			this.posY -= this.motionY / f3 * 0.05000000074505806D;
			this.posZ -= this.motionZ / f3 * 0.05000000074505806D;
			this.playSound("random.bowhit", 0.2F, 2.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
			this.inGround = true;
			this.arrowShake = 7;
			this.setIsCritical(false);
			
			if(this.inTile.getMaterial() != Material.air)
			{
				this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate, this);
			}
		}
		return new float[]{f2, f3, f4};
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setShort("xTile", (short)this.xTile);
		tagCompound.setShort("yTile", (short)this.yTile);
		tagCompound.setShort("zTile", (short)this.zTile);
		tagCompound.setShort("life", (short)this.ticksInGround);
		ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
		tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setByte("inData", (byte)this.inData);
		tagCompound.setByte("shake", (byte)this.arrowShake);
		tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		tagCompound.setDouble("damage", this.damage);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		this.xTile = tagCompund.getShort("xTile");
		this.yTile = tagCompund.getShort("yTile");
		this.zTile = tagCompund.getShort("zTile");
		this.ticksInGround = tagCompund.getShort("life");
		
		if(tagCompund.hasKey("inTile", 8))
		{
			this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
		}
		else
		{
			this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
		}
		
		this.inData = tagCompund.getByte("inData") & 255;
		this.arrowShake = tagCompund.getByte("shake") & 255;
		this.inGround = tagCompund.getByte("inGround") == 1;
		
		if(tagCompund.hasKey("damage", 99))
		{
			this.damage = tagCompund.getDouble("damage");
		}
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if(!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
		{
			if(entityIn.inventory.addItemStackToInventory(new ItemStack(M.shuriken, 1)))
			{
				this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				entityIn.onItemPickup(this, 1);
				this.setDead();
			}
		}
	}
	
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}
	
	public void setDamage(double p_70239_1_)
	{
		this.damage = p_70239_1_;
	}
	
	public double getDamage()
	{
		return this.damage;
	}
	
	public void setKnockbackStrength(int p_70240_1_)
	{
		this.knockbackStrength = p_70240_1_;
	}
	
	@Override
	public boolean canAttackWithItem()
	{
		return false;
	}
	
	public void setIsCritical(boolean p_70243_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		
		if(p_70243_1_)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
		}
	}
	
	public boolean getIsCritical()
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);
		return (b0 & 1) != 0;
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
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
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
		this.posX = buf.readDouble();
		this.posY = buf.readDouble();
		this.posZ = buf.readDouble();
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		if(compound != null)
		{
			this.readFromNBT(compound);
		}
	}
}