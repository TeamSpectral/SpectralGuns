package com.spectral.spectral_guns.entity.projectile;

import java.util.ArrayList;
import java.util.List;

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.Stuff.EntitiesInArea;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySnowballSpecial extends EntitySnowball
{
	public int damage = 1;

	public EntitySnowballSpecial(World world)
	{
		super(world);
	}

	public EntitySnowballSpecial(World world, EntityLivingBase source)
	{
		super(world, source);
	}

	public EntitySnowballSpecial(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	public void onUpdate()
	{
		super.onUpdate();
		List<Entity> es = EntitiesInArea.getEntitiesWithinCube(this, this.width, true);
		ArrayList<EntitySnowballSpecial> a = new ArrayList<EntitySnowballSpecial>();
		for(int i = 0; i < es.size(); ++i)
		{
			if(es.get(i) instanceof EntitySnowballSpecial)
			{
				a.add(((EntitySnowballSpecial)es.get(i)));
			}
		}
		if(a.size() > 3)
		{
			if(rand.nextBoolean())
			{
				this.setDead();
				a.get(rand.nextInt(a.size()-1)).damage += this.damage*2/3;
			}
			else
			{
				a.get(rand.nextInt(a.size()-1)).setDead();
			}
		}
	}

	protected void onImpact(MovingObjectPosition pos)
	{
		float damage = 0.5F;

		if(pos.entityHit != null)
		{
			if(pos.entityHit instanceof EntityBlaze)
			{
				damage += 3;
			}
		}

		double m = Coordinates3D.distance(new Vec3(motionX, motionY, motionZ));
		damage *= m-0.3;


		if(pos.entityHit != null)
		{
			if (pos.entityHit instanceof EntityBlaze)
			{
				damage *= 3;
				damage -= 3;

				if(damage < 3)
				{
					damage = 3;
				}
			}
		}

		damage *= this.damage;
		damage -= 0.5F;

		if(damage < 0)
		{
			damage = 0;
		}

		if(pos.entityHit != null)
		{
			pos.entityHit.velocityChanged = false;
			pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
			if(pos.entityHit instanceof EntityLivingBase)
			{
				((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, (int)(rand.nextFloat()*(2/3*20)+20/3), (int)(rand.nextFloat()), false, false));
				((EntityLivingBase)pos.entityHit).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, (int)(rand.nextFloat()*(2/3*20)+20/3), (int)(rand.nextFloat()), false, false));
			}
			pos.entityHit.extinguish();
			float f = 0.01F;
			pos.entityHit.addVelocity(motionX*f, motionY*f, motionZ*f);
		}

		for(int i = 0; i < 8*damage; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}

		if(rand.nextInt(3) == 0)
		{
			Stuff.Coordinates3D.bounce(this, pos.sideHit, 0.5);
		}
		else
		{
			if(!this.worldObj.isRemote)
			{
				this.setDead();
			}
		}
	}
}
