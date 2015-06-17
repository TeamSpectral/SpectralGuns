package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;

public class ComponentTriggerMechanismBoosted extends ComponentTriggerMechanism
{
	protected ComponentTriggerMechanismBoosted(ComponentMaterial material, float delay)
	{
		super(new String2("", "_boosted"), new String2("", ".boosted"), material, delay);
	}
	
	public ComponentTriggerMechanismBoosted(ComponentTriggerMechanism c)
	{
		this(c.material, c.delay);
	}
	
	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil * 4;
	}
	
	@Override
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return delay / 2 + this.delay;
	}
	
	@Override
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate / 3;
	}
	
	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return speed * 2;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item trigger = M.trigger_iron.item;
		switch(this.material)
		{
		case WOOD:
			trigger = M.trigger_wood.item;
			break;
		case IRON:
			trigger = M.trigger_iron.item;
			break;
		case GOLD:
			trigger = M.trigger_gold.item;
			break;
		}
		GameRegistry.addShapelessRecipe(new ItemStack(this.item), new Object[]{Items.redstone, trigger});
	}
}
