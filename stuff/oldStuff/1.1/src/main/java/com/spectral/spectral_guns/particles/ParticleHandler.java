package com.spectral.spectral_guns.particles;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ParticleHandler
{
	private static ArrayList<P> particles = new ArrayList<P>();
	private static class P
	{
		public final EntityFX particle;
		public final EnumParticleTypes2 type;
		public int[] par;

		public P(EntityFX particle, EnumParticleTypes2 type, int[] par)
		{
			this.particle = particle;
			this.type = type;
			this.par = par;
		}
	}

	public static enum EnumParticleTypes2
	{
		REDSTONE2()
		{
			@Override
			public EntityFX get(World world, double x, double y, double z, float mx, float my, float mz, int... par)
			{
				float scale = 1;
				if(par.length == 1)
				{
					scale = ((float)par[0])/100;
				}
				else
				{
					par = new int[]{100};
				}
				if(scale > 4)
				{
					scale = 4;
				}
				else if(scale < 0.1)
				{
					scale = 0.1F;
				}
					
				EntityReddust2FX p = (EntityReddust2FX)new EntityReddust2FX(world, x, y, z, scale, mx, my, mz);
				p.motionX = 0;
				p.motionY = 0;
				p.motionZ = 0;
				return p;
			}

			@Override
			public void update(P p)
			{
				
			}
		};

		private EnumParticleTypes2()
		{

		}

		public abstract EntityFX get(World world, double x, double y, double z, float mx, float my, float mz, int ... par);

		public abstract void update(P p);
	}

	@SideOnly(Side.CLIENT)
	public static EntityFX particle(EnumParticleTypes2 particleEnum, World world, boolean ignoreDistance, double x, double y, double z, float mx, float my, float mz, int ... par)
	{
		EntityFX particle = particleEnum.get(world, x, y, z, mx, my, mz, par);
		particle.prevPosX = particle.posX;
		particle.prevPosY = particle.posY;
		particle.prevPosZ = particle.posZ;
		particles.add(new P(particle, particleEnum, par));
		return spawnEntityFX(particle, ignoreDistance, x, y, z, mx, my, mz, par);
	}

	@SideOnly(Side.CLIENT)
	public static EntityFX spawnEntityFX(EntityFX particle, boolean ignoreDistance, double x, double y, double z, float mx, float my, float mz, int ... par)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null)
		{
			int k = mc.gameSettings.particleSetting;

			if(k == 1 && mc.theWorld.rand.nextInt(3) == 0)
			{
				k = 2;
			}

			double d6 = mc.getRenderViewEntity().posX - x;
			double d7 = mc.getRenderViewEntity().posY - y;
			double d8 = mc.getRenderViewEntity().posZ - z;

			double r = 32;
			if(!(ignoreDistance || (d6 * d6 + d7 * d7 + d8 * d8 <= r*r && k <= 1)))
			{
				particle = null;
			}
			if(particle != null)
			{
				mc.effectRenderer.addEffect(particle);
				return particle;
			}
		}
		return null;
	}

	public static void update()
	{
		for(int i = 0; i < particles.size(); ++i)
		{
			P p = particles.get(i);
			if(p.particle == null || p.particle.isDead)
			{
				particles.remove(i);
				--i;
			}
			else
			{
				p.type.update(p);
			}
		}
	}
}
