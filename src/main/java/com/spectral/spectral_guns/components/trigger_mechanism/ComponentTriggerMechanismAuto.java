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

public class ComponentTriggerMechanismAuto extends ComponentTriggerMechanism
{
	protected ComponentTriggerMechanismAuto(Component[] required, Component[] incapatible, ComponentMaterial material, float delay)
	{
		super(new String2("", "_auto"), new String2("", ".automatic"), required, incapatible, material, delay);
	}
	
	public ComponentTriggerMechanismAuto(Component[] required, Component[] incapatible, ComponentTriggerMechanism c)
	{
		this(required, incapatible, c.material, c.delay);
	}
	
	@Override
	public boolean automatic(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return true;
	}
	
	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return recoil * 3;
	}
	
	@Override
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return delay + this.delay * 1.3F;
	}
	
	@Override
	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return rate / 3;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		switch(material)
		{
		case WOOD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]
				{
						" G ",
						" TR",
						" G ",
						'G',
						M.gear_wood,
						'T',
						M.trigger_wood.item,
						'R',
						Items.redstone
				});
			break;
		case IRON:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]
				{
						" G ",
						" TR",
						" G ",
						'G',
						M.gear_iron,
						'T',
						M.trigger_iron.item,
						'R',
						Items.redstone
				});
			break;
		case GOLD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]
				{
						" G ",
						" TR",
						" G ",
						'G',
						M.gear_gold,
						'T',
						M.trigger_gold.item,
						'R',
						Items.redstone
				});
			break;
		// case DIAMOND: GameRegistry.addShapedRecipe(new ItemStack(this.item),
		// new Object[]{" G ", " TR", " G ", 'G', M.gear_diamond, 'T',
		// M.trigger_diamond.item, 'R', Items.redstone}); break;
		}
	}
}
