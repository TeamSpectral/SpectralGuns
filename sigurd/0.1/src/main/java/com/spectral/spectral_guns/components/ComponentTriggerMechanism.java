package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.components.Component.ComponentRegister.ComponentType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ComponentTriggerMechanism extends ComponentGeneric
{
	public final float delay;
	public final boolean redstone;

	public ComponentTriggerMechanism(String id, String name, Component[] required, Component[] incapatible, float delay, boolean redstone)
	{
		super(id, name, required, incapatible, true, ComponentType.TRIGGER);
		this.delay = redstone ? delay/2 : delay;
		this.redstone = redstone;
	}

	@Override
	public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
	{
		return (redstone ? delay/2 : delay) + this.delay;
	}

	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return redstone ? recoil*3 : recoil;
	}
	
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return redstone ? rate/2 : rate;
	}

	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return redstone ? speed*2 : speed;
	}
	
	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
