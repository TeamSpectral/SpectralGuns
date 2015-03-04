package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;

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

public class EntitySnowball2 extends EntitySnowball implements IEntityAdditionalSpawnData
{
	public int damage = 1;

	public EntitySnowball2(World world)
	{
		super(world);
	}

	public EntitySnowball2(World world, EntityLivingBase source)
	{
		super(world, source);
	}

	public EntitySnowball2(World world, double x, double y, double z)
	{
		super(world, x, y, z);
	}

	public void onUpdate()
	{
		super.onUpdate();
		List<Entity> es = EntitiesInArea.getEntitiesWithinCube(this, this.width, true);
		ArrayList<EntitySnowball2> a = new ArrayList<EntitySnowball2>();
		for(int i = 0; i < es.size(); ++i)
		{
			if(es.get(i) instanceof EntitySnowball2)
			{
				a.add(((EntitySnowball2)es.get(i)));
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

		int h = (int)Math.ceil(16*damage);
		if(h > 16)
		{
			h = 32;
		}
		for(int i = 0; i < h && worldObj.isRemote; ++i)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, true, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}

		if(!this.worldObj.isRemote)
		{
			this.setDead();
		}
	}

	/**
	 * Called by the server when constructing the spawn packet.
	 * Data should be added to the provided stream.
	 *
	 * @param buffer The packet data stream
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
	 * @param data The packet data stream
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
