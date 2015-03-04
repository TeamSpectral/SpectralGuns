package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemComponent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ComponentTriggerMechanism extends ComponentGeneric
{
	public final float delay;
	public final boolean redstone;

	protected ComponentTriggerMechanism(String id, String name, Component[] required, Component[] incapatible, ComponentMaterial material, float delay, boolean redstone)
	{
		super("trigger" + (redstone ? "_redstone" : "") + id, "trigger" + (redstone ? ".redstone" : "") + name, required, incapatible, Type.TRIGGER, material);
		this.delay = delay;
		this.redstone = redstone;
	}

	public ComponentTriggerMechanism(Component[] required, Component[] incapatible, ComponentTriggerMechanism c)
	{
		this("", "", required, incapatible, c.material, c.delay/2, true);
	}

	public ComponentTriggerMechanism(Component[] required, Component[] incapatible, ComponentMaterial material, float delay)
	{
		this("", "", required, incapatible, material, delay, false);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		if(redstone)
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
		else
		{
			Item bar = Items.iron_ingot;
			switch(material)
			{
			case WOOD: bar = Item.getItemFromBlock(Blocks.planks); break;
			case IRON: bar = Items.iron_ingot; break;
			case GOLD: bar = Items.gold_ingot; break;
			case DIAMOND: bar = Items.diamond; break;
			}
			Item nugget = M.iron_nugget;
			switch(material)
			{
			case WOOD: nugget = Items.stick; break;
			case IRON: nugget = M.iron_nugget; break;
			case GOLD: nugget = Items.gold_nugget; break;
			}
			Item plate = Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate);
			switch(material)
			{
			case WOOD: plate = Item.getItemFromBlock(Blocks.wooden_pressure_plate); break;
			case IRON: plate = Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate); break;
			case GOLD: plate = Item.getItemFromBlock(Blocks.light_weighted_pressure_plate); break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"NNB", "LPB", " HB", 'N', nugget, 'B', bar, 'L', Item.getItemFromBlock(Blocks.lever), 'P', plate, 'H', Item.getItemFromBlock(Blocks.tripwire_hook)});
		}
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
