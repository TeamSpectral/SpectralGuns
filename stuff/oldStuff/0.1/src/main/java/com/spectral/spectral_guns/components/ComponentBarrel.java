package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.components.Component.ComponentRegister.ComponentType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ComponentBarrel extends ComponentGeneric
{
	public final float spread;
	public final float velocity;

	public ComponentBarrel(String id, String name, Component[] required, Component[] incapatible, float spread, float velocity)
	{
		super(id, name, required, incapatible, true, ComponentType.BARREL);
		this.spread = spread;
		this.velocity = velocity;
	}

	@Override
	public float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		if(spread > this.spread)
		{
			return this.spread;
		}
		else
		{
			return spread;
		}
	}

	@Override
	protected void fireSound(Entity e, ItemStack stack, World world, EntityPlayer player)
	{
		for(int i = 0; i < 3; ++i)
		{
			float v = (spread+world.rand.nextFloat()*20-16)/30;
			if(v < 0)
			{
				v = 0;
			}
			float p = 30-v;
			if(p < 0)
			{
				p = 0;
			}
			e.playSound("mob.blaze.hit", v/16+0.15F, p*4-world.rand.nextFloat()*0.1F-p*2);
		}
	}

	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		if(velocity <= 1 && velocity > 0)
		{
			for(int i = 0; i < projectiles.size(); ++i)
			{
				Entity e = projectiles.get(i);
				if(e != null)
				{
					e.motionX *= velocity;
					e.motionY *= velocity;
					e.motionZ *= velocity;
				}
			}
		}
		return projectiles;
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{

	}
}
