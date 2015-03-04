package com.spectral.spectral_guns.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityLargeFireballSpecial extends EntityLargeFireball
{
	public EntityLargeFireballSpecial(World world)
	{
		super(world);
	}

	public EntityLargeFireballSpecial(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
	{
		super(world, shooter, accelX, accelY, accelZ);
	}

	public EntityLargeFireballSpecial(World world, double x, double y, double z, double accelX, double accelY, double accelZ)
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
            this.worldObj.newExplosion(this.shootingEntity, this.posX, this.posY, this.posZ, (float)this.explosionPower, flag, flag);
            this.setDead();
        }
	}
}
