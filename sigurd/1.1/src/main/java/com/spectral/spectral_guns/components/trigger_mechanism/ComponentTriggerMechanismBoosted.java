package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.String2;

public class ComponentTriggerMechanismBoosted extends ComponentTriggerMechanism
{
	protected ComponentTriggerMechanismBoosted(Component[] required, Component[] incapatible, ComponentMaterial material, float delay)
	{
		super(new String2("", "_boosted"), new String2("", ".boosted"), required, incapatible, material, delay);
	}

	public ComponentTriggerMechanismBoosted(Component[] required, Component[] incapatible, ComponentTriggerMechanism c)
	{
		this(required, incapatible, c.material, c.delay/2);
	}

	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil*4;
	}

	@Override
	public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
	{
		return delay/2 + this.delay;
	}

	@Override
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate/3;
	}

	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return speed*2;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item trigger = M.trigger_iron.item;
		switch(material)
		{
		case WOOD: trigger = M.trigger_wood.item; break;
		case IRON: trigger = M.trigger_iron.item; break;
		case GOLD: trigger = M.trigger_gold.item; break;
		}
		GameRegistry.addShapelessRecipe(new ItemStack(this.item),  new Object[]{Items.redstone, trigger});
	}
}
