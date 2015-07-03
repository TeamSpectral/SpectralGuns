package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityLargeFireball2 extends EntityLargeFireball implements IEntityAdditionalSpawnData
{
	public EntityLargeFireball2(World world)
	{
		super(world);
	}
	
	public EntityLargeFireball2(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(world, shooter, accelX, accelY, accelZ);
	}
	
	public EntityLargeFireball2(World world, double x, double y, double z, double accelX, double accelY, double accelZ)
	{
		super(world, x, y, z, accelX, accelY, accelZ);
	}
	
	@Override
	protected void onImpact(MovingObjectPosition pos)
	{
		if(!this.worldObj.isRemote)
		{
			if(pos.entityHit != null)
			{
				pos.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
				this.func_174815_a(this.shootingEntity, pos.entityHit);
			}
			
			boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
			this.worldObj.newExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
			this.setDead();
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
