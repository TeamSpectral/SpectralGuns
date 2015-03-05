package com.spectral.spectral_guns.particles;

import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class EntityReddust2FX extends EntityReddustFX
{
	public EntityReddust2FX(World world, double x, double y, double z, float mx, float my, float mz)
	{
		super(world, x, y, z, mx, my, mz);
		this.particleMaxAge = Math.max(particleMaxAge, 4);
		
	}

	public EntityReddust2FX(World world, double x, double y, double z, float size, float mx, float my, float mz)
	{
		super(world, x, y, z, size, mx, my, mz);
		this.particleMaxAge = Math.max(particleMaxAge, 4);
	}

	@Override
    public void func_180434_a(WorldRenderer wr, Entity e, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
		wr.setBrightness(200);
    	super.func_180434_a(wr, e, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
}
