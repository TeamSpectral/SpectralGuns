package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.String2;
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

	protected ComponentTriggerMechanism(String2 id, String2 name, Component[] required, Component[] incapatible, ComponentMaterial material, float delay)
	{
		super(new String2("trigger", "").add(id), new String2("trigger", "").add(name), required, incapatible, Type.TRIGGER, material);
		this.delay = delay;
	}

	public ComponentTriggerMechanism(Component[] required, Component[] incapatible, ComponentMaterial material, float delay)
	{
		this(new String2(), new String2(), required, incapatible, material, delay);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
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
		Item gear = M.gear_iron;
		switch(material)
		{
		case WOOD: gear = M.gear_wood; break;
		case IRON: gear = M.gear_iron; break;
		case GOLD: gear = M.gear_gold; break;
		case DIAMOND: bar = M.gear_diamond; break;
		}
		GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"NNB", "LGB", " HB", 'N', nugget, 'B', bar, 'L', Item.getItemFromBlock(Blocks.lever), 'G', gear, 'H', Item.getItemFromBlock(Blocks.tripwire_hook)});
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{

	}
}
