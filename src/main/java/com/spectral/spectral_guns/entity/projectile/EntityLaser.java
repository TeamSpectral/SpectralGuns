package com.spectral.spectral_guns.entity.projectile;

import io.netty.buffer.ByteBuf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.EntitiesInArea;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.particles.ParticleHandler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityLaser extends Entity implements IEntityAdditionalSpawnData
{
	public enum LaserColor
	{
		RED(1, 0, 0, EnumDyeColor.RED), GREEN(0, 1, 0, EnumDyeColor.GREEN), CYAN(0, 1, 1, EnumDyeColor.CYAN), VIOLET(85 / 255, 0, 1, EnumDyeColor.PURPLE);
		
		public final float r;
		public final float g;
		public final float b;
		public final EnumDyeColor dye;
		
		private LaserColor(float r, float g, float b, EnumDyeColor dye)
		{
			this.r = r;
			this.g = g;
			this.b = b;
			this.dye = dye;
		}
		
		public boolean goTrough(IBlockState bs)
		{
			if(bs.getBlock() instanceof BlockStainedGlass || bs.getBlock() instanceof BlockStainedGlassPane)
			{
				Object dye = bs.getValue(BlockStainedGlass.COLOR);
				if(dye instanceof EnumDyeColor)
				{
					return dye == (EnumDyeColor)dye;
				}
			}
			return false;
		}
	}
	
	public double strenght;
	public Entity shooter;
	public LaserColor color;
	public double Yoffset;
	
	public EntityLaser(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityLaser(World worldIn, EntityLivingBase shooter, double strenght, LaserColor color, double Yoffset)
	{
		super(worldIn);
		this.strenght = strenght;
		this.color = color;
		this.setShooter(shooter);
		this.Yoffset = Yoffset;
	}
	
	@Override
	public void onUpdate()
	{
		if(this.shooter != null)
		{
			setShooter(shooter);
		}
		
		double x = 0;
		double y = 0;
		double z = 0;
		MovingObjectPosition pos = null;
		MovingObjectPosition pos2 = null;
		Vec3 end = null;
		
		for(int i = 0; i < distance() * 5 && pos == null && Coordinates3D.distance(new Vec3(x, y, z)) < distance() / 2; ++i)
		{
			pos2 = this.worldObj.rayTraceBlocks(new Vec3(posX + x, posY + y, posZ + z), new Vec3(posX + x + motionX, posY + y + motionY, posZ + z + motionZ), true, false, false);
			
			List<Entity> es = EntitiesInArea.getEntitiesOnAxis(worldObj, new Vec3(posX, posY, posZ), new Vec3(posX + x, posY + y, posZ + z), 0.1);
			for(int i2 = 0; i2 < es.size(); ++i2)
			{
				if(es.get(i2) == shooter || !es.get(i2).canBeCollidedWith())
				{
					es.remove(i2);
					--i2;
				}
			}
			
			Entity entityHit = EntitiesInArea.getClosestEntity(es, this.getPositionVector());
			
			if(entityHit != null && EntitiesInArea.hit.containsKey(entityHit) && EntitiesInArea.hit.get(entityHit) != null)
			{
				if(pos2 == null)
				{
					pos2 = new MovingObjectPosition(entityHit, EntitiesInArea.hit.get(entityHit));
					x = pos2.hitVec.xCoord - posX;
					y = pos2.hitVec.yCoord - posY;
					z = pos2.hitVec.zCoord - posZ;
					end = pos2.hitVec;
					pos = pos2;
				}
				else if(pos2.hitVec == null || pos2.typeOfHit == MovingObjectType.MISS || Coordinates3D.distance(pos2.hitVec, this.getPositionVector()) < Coordinates3D.distance(EntitiesInArea.hit.get(entityHit), this.getPositionVector()))
				{
					pos2.entityHit = entityHit;
					pos2.typeOfHit = MovingObjectType.ENTITY;
					pos2.hitVec = EntitiesInArea.hit.get(entityHit);
					x = pos2.hitVec.xCoord - posX;
					y = pos2.hitVec.yCoord - posY;
					z = pos2.hitVec.zCoord - posZ;
					end = pos2.hitVec;
					pos = pos2;
				}
			}
			else if(pos2 != null && (pos2.typeOfHit != MovingObjectType.BLOCK || pos2.getBlockPos() == null || !(worldObj.getBlockState(pos2.getBlockPos()).getBlock() instanceof BlockGlass || worldObj.getBlockState(pos2.getBlockPos()).getBlock() instanceof BlockPane || (color != null && this.color.goTrough(worldObj.getBlockState(pos2.getBlockPos()))))))
			{
				pos = pos2;
				if(pos.hitVec == null)
				{
					pos.hitVec = new Vec3(posX + x, posY + y, posZ + z);
					if(pos.getBlockPos() != null && pos.typeOfHit == MovingObjectType.BLOCK)
					{
						double xc = Math.max(Math.min(pos.hitVec.xCoord, pos.getBlockPos().getX() + 1.1), pos.getBlockPos().getX() - 0.1);
						double yc = Math.max(Math.min(pos.hitVec.yCoord, pos.getBlockPos().getY() + 1.1), pos.getBlockPos().getY() - 0.1);
						double zc = Math.max(Math.min(pos.hitVec.zCoord, pos.getBlockPos().getZ() + 1.1), pos.getBlockPos().getZ() - 0.1);
						pos.hitVec = new Vec3(xc, yc, zc);
					}
				}
				end = pos.hitVec;
				break;
			}
			else
			{
				x += motionX;
				y += motionY;
				z += motionZ;
			}
			if(posY + y > worldObj.getActualHeight() || posY + y < 0 || !worldObj.isAreaLoaded(new BlockPos(posX + x, posY + y, posZ + z), new BlockPos(posX + x + motionX, posY + y + motionY, posZ + z + motionZ)))
			{
				break;
			}
		}
		if(end == null)
		{
			end = new Vec3(posX + x, posY + y, posZ + z);
		}
		if(pos != null)
		{
			if(pos.hitVec == null)
			{
				pos.hitVec = new Vec3(posX + x, posY + y, posZ + z);
				if(pos.getBlockPos() != null && pos.typeOfHit == MovingObjectType.BLOCK)
				{
					double xc = Math.max(Math.min(pos.hitVec.xCoord, pos.getBlockPos().getX() + 1.1), pos.getBlockPos().getX() - 0.1);
					double yc = Math.max(Math.min(pos.hitVec.yCoord, pos.getBlockPos().getY() + 1.1), pos.getBlockPos().getY() - 0.1);
					double zc = Math.max(Math.min(pos.hitVec.zCoord, pos.getBlockPos().getZ() + 1.1), pos.getBlockPos().getZ() - 0.1);
					pos.hitVec = new Vec3(xc, yc, zc);
				}
			}
			particles(pos, end);
			onImpact(pos);
		}
		else
		{
			particles(pos2, end);
		}
		this.setDead();
	}
	
	protected double distance()
	{
		return 500;
	}
	
	public void setShooter(Entity e)
	{
		this.shooter = e;
		Coordinates3D.throwThing(this.shooter, this, this.strenght);
		Coordinates3D.velocity(this, Coordinates3D.stabilize(Coordinates3D.velocity(this), 0.5));
		posX += motionX / 32;
		posY += motionY / 32;
		posZ += motionZ / 32;
		posY += Yoffset;
	}
	
	protected void particles(MovingObjectPosition pos, Vec3 v)
	{
		if(!worldObj.isRemote)
		{
			return;
		}
		if(pos != null && pos.hitVec != null)
		{
			v = pos.hitVec;
		}
		if(v == null)
		{
			v = this.getPositionVector();
		}
		if(pos != null && pos.getBlockPos() != null)
		{
			double xc = Math.max(Math.min(v.xCoord, pos.getBlockPos().getX() + 1.1), pos.getBlockPos().getX() - 0.1);
			double yc = Math.max(Math.min(v.yCoord, pos.getBlockPos().getY() + 1.1), pos.getBlockPos().getY() - 0.1);
			double zc = Math.max(Math.min(v.zCoord, pos.getBlockPos().getZ() + 1.1), pos.getBlockPos().getZ() - 0.1);
			v = new Vec3(xc, yc, zc);
		}
		Vec3 vec = Coordinates3D.subtract(v, this.getPositionVector());
		
		int t = (int)Math.round(Math.ceil(strenght * 2) * rand.nextFloat() * Coordinates3D.distance(vec) / 15);
		for(int i = 0; i < t; ++i)
		{
			double d = rand.nextDouble();
			Vec3 ppos = Coordinates3D.multiply(vec, rand.nextDouble());
			BlockPos blockPos = new BlockPos(this.getPositionVector().add(ppos));
			double light = worldObj.getLightFromNeighborsFor(EnumSkyBlock.SKY, blockPos);
			light *= (worldObj.getSunBrightnessFactor(1.0F) + 0.1) * 100;
			light = Math.max(light, worldObj.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, blockPos) / 16);
			light = Math.max(0, light - 0.1);
			double distance = Coordinates3D.distance(vec) / 20;
			if(worldObj.canSeeSky(blockPos) && worldObj.rainingStrength > 0)
			{
				light /= worldObj.rainingStrength * 8;
			}
			if(d * rand.nextDouble() / distance < distance() / light)
			{
				double pd = Coordinates3D.distance(Minecraft.getMinecraft().thePlayer.getPositionVector(), ppos);
				double rpd = 0.2;
				
				double hd = Coordinates3D.distance(v, ppos);
				double rhd = 0.3;
				
				if((pd > rpd && pd * rand.nextDouble() * 2 > rpd) || (hd < rhd && hd * rand.nextDouble() * 2 < rhd))
					particle(posX + ppos.xCoord, posY + ppos.yCoord, posZ + ppos.zCoord, 0.01F);
			}
		}
	}
	
	protected void onImpact(MovingObjectPosition pos)
	{
		if(pos.entityHit != null && strenght > 4 && !worldObj.isRemote && (pos.entityHit.isBurning() || ((strenght - 4) + 4) * rand.nextDouble() <= 4))
		{
			if(pos.entityHit.isBurning() && rand.nextBoolean())
			{
				pos.entityHit.attackEntityFrom(DamageSource.onFire, 1);
			}
			pos.entityHit.setFire(8);
		}
		if(pos.hitVec != null)
		{
			if(worldObj.isRemote)
			{
				double t = Math.min(Math.ceil(strenght * 5), 8);
				
				for(int i = 0; i < t; ++i)
				{
					Vec3 v = new Vec3((worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1);
					v = Coordinates3D.stabilize(v, rand.nextFloat() * t / 20 * i / t);
					particle(pos.hitVec.xCoord + v.xCoord, pos.hitVec.yCoord + v.yCoord, pos.hitVec.zCoord + v.zCoord, 0.9F);
				}
				worldObj.rand.nextFloat();
				
				if(strenght > 2 && pos.getBlockPos() != null)
				{
					t = Math.min(Math.ceil(strenght / (rand.nextFloat() * 8 + 8)), 5);
					
					for(int i = 0; i < t; ++i)
					{
						float m = 0.1F;
						Vec3 v = new Vec3((worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1);
						v = Coordinates3D.stabilize(v, rand.nextFloat() * t / 50 * i / t);
						if(worldObj.isRemote && rand.nextBoolean())
						{
							IBlockState bs = worldObj.getBlockState(pos.getBlockPos());
							if(bs.getBlock().getMaterial() != Material.air)
							{
								if(!bs.getBlock().addDestroyEffects(worldObj, pos.getBlockPos(), Minecraft.getMinecraft().effectRenderer))
									worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, true, pos.hitVec.xCoord + v.xCoord, pos.hitVec.yCoord + v.yCoord, pos.hitVec.zCoord + v.zCoord, rand.nextDouble() * m * 2 - m, rand.nextDouble() * m * 2 - m, rand.nextDouble() * m * 2 - m, new int[] {Block.getStateId(bs)});
							}
						}
					}
					
					if(pos.getBlockPos() != null && pos.sideHit != null)
					{
						t = t * 4 + t * (worldObj.getBlockState(pos.getBlockPos()).getBlock().getFireSpreadSpeed(worldObj, pos.getBlockPos(), pos.sideHit) + worldObj.getBlockState(pos.getBlockPos()).getBlock().getFlammability(worldObj, pos.getBlockPos(), pos.sideHit)) / 4;
					}
					else
					{
						t = t * 4;
					}
					
					EnumParticleTypes particle = EnumParticleTypes.SMOKE_NORMAL;
					if(t > 20)
					{
						t = 20;
						particle = EnumParticleTypes.SMOKE_LARGE;
					}
					for(int i = 0; i < t; ++i)
					{
						if(rand.nextInt(10) == 0)
						{
							particle = EnumParticleTypes.SMOKE_LARGE;
						}
						float m = 0.1F;
						Vec3 v = new Vec3((worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1, (worldObj.rand.nextGaussian() * 2) - 1);
						v = Coordinates3D.stabilize(v, rand.nextFloat() * t / 40 * i / t);
						if(worldObj.isRemote && rand.nextFloat() > 0.9)
						{
							IBlockState bs = worldObj.getBlockState(pos.getBlockPos());
							worldObj.spawnParticle(rand.nextBoolean() ? particle : EnumParticleTypes.SMOKE_NORMAL, true, pos.hitVec.xCoord + v.xCoord, pos.hitVec.yCoord + v.yCoord + rand.nextFloat() * rand.nextFloat() / 3 + 0.1, pos.hitVec.zCoord + v.zCoord, 0, 0, 0, new int[] {});
						}
					}
				}
			}
			
			if(!worldObj.isRemote && pos.getBlockPos() != null && worldObj.getBlockState(pos.getBlockPos()).getBlock().getFireSpreadSpeed(worldObj, pos.getBlockPos(), pos.sideHit) > 20 && worldObj.getBlockState(pos.getBlockPos()).getBlock().getFireSpreadSpeed(worldObj, pos.getBlockPos(), pos.sideHit) * rand.nextFloat() > 19 && strenght > 5 && ((strenght - 5) + 5) * rand.nextDouble() <= 1 && rand.nextBoolean() && !(worldObj.isRaining() && worldObj.canBlockSeeSky(pos.getBlockPos())))
			{
				if(worldObj.getBlockState(pos.getBlockPos()).getBlock().isReplaceable(worldObj, pos.getBlockPos()))
				{
					if(worldObj.getGameRules().getGameRuleBooleanValue("doFireTick"))
					{
						worldObj.setBlockState(pos.getBlockPos(), Blocks.fire.getDefaultState());
					}
				}
				else
				{
					Vec3i vec = Coordinates3D.getVecFromAxis(pos.sideHit.getAxis(), pos.sideHit.getAxisDirection());
					
					BlockPos p = pos.getBlockPos().add(vec);
					if(worldObj.isAirBlock(p))
					{
						if(worldObj.getGameRules().getGameRuleBooleanValue("doFireTick"))
						{
							worldObj.setBlockState(p, Blocks.fire.getDefaultState());
						}
					}
					else
					{
						for(int i = 0; i < 4; ++i)
						{
							vec = Coordinates3D.getVecFromAxis(Randomization.getRandom(Axis.values()), Randomization.getRandom(AxisDirection.values()));
							
							p = pos.getBlockPos().add(vec);
							if(worldObj.isAirBlock(p))
							{
								worldObj.setBlockState(p, Blocks.fire.getDefaultState());
								break;
							}
						}
					}
				}
			}
			else if(strenght > 4)
			{
				List<Entity> es = EntitiesInArea.getEntitiesWithinRadius(worldObj, pos.hitVec, 1);
				for(int i = 0; i < es.size(); ++i)
				{
					if(!(es.get(i) instanceof EntityLiving) || es.get(i) == shooter)
						;
					{
						es.remove(i);
						--i;
					}
				}
				Entity e = EntitiesInArea.getClosestEntity(es, pos.hitVec);
				if(e != null)
				{
					onImpact(new MovingObjectPosition(e));
				}
			}
		}
	}
	
	protected void particle(double x, double y, double z, float scale)
	{
		if(Coordinates3D.distance(new Vec3(x, y, z), this.getPositionVector()) > 200 || !worldObj.isAreaLoaded(new BlockPos(new Vec3(x, y, z)), 1))
		{
			return;
		}
		if(worldObj.isRemote && color != null)
		{
			float r = color.r;
			float g = color.g;
			float b = color.b;
			scale *= 100;
			com.spectral.spectral_guns.particles.ParticleHandler.particle(com.spectral.spectral_guns.particles.ParticleHandler.EnumParticleTypes2.REDSTONE2, this.worldObj, true, x, y, z, r * 3 - 1, g * 3, b * 3, new int[] {(int)(scale)});
		}
	}
	
	@Override
	protected void entityInit()
	{
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound c)
	{
		if(c == null)
		{
			return;
		}
		strenght = c.getDouble("Strenght");
		if(c.hasKey("ThrowerUUIDMost", new NBTTagString().getId()) && c.hasKey("ThrowerUUIDLeast", new NBTTagString().getId()))
		{
			UUID uuid = new UUID(c.getLong("ThrowerUUIDMost"), c.getLong("ThrowerUUIDLeast"));
			EntityPlayer newShooter = worldObj.getPlayerEntityByUUID(uuid);
			if(newShooter != null)
			{
				shooter = newShooter;
			}
		}
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound c)
	{
		c.setDouble("Strenght", strenght);
		if(shooter != null)
		{
			UUID uuid = shooter.getUniqueID();
			c.setLong("ThrowerUUIDMost", uuid.getMostSignificantBits());
			c.setLong("ThrowerUUIDLeast", uuid.getLeastSignificantBits());
		}
		c.setString("Color", color.toString());
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
