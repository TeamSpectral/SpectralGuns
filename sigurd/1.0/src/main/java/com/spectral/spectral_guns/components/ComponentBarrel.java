package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemComponent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ComponentBarrel extends ComponentGeneric
{
	public final float spread;
	public final float velocity;

	public ComponentBarrel(String id, String name, Component[] required, Component[] incapatible, ComponentMaterial material, float spread, float velocity)
	{
		super("barrel_" + id, "barrel." + name, required, incapatible, Type.BARREL, material);
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
	public abstract void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags);

	@Override
	public abstract void registerRecipe();
	
	public static class ComponentBarrelThin extends ComponentBarrel
	{
		public ComponentBarrelThin(Component[] required, Component[] incapatible, ComponentMaterial material, float velocity)
		{
			super("thin", "thin", required, incapatible, material, 0.3F, velocity);
		}

		@Override
		public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{

		}

		@Override
		public void registerRecipe()
		{
			Item barrel = M.barrel_normal_iron.item;
			switch(material)
			{
			case WOOD: barrel = M.barrel_normal_wood.item; break;
			case IRON: barrel = M.barrel_normal_iron.item; break;
			case GOLD: barrel = M.barrel_normal_gold.item; break;
			case DIAMOND: barrel = M.barrel_normal_diamond.item; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2),  new Object[]{"##", '#', barrel});
		}
	}
	
	public static class ComponentBarrelNormal extends ComponentBarrel
	{
		public ComponentBarrelNormal(Component[] required, Component[] incapatible, ComponentMaterial material, float velocity)
		{
			super("normal", "normal", required, incapatible, material, 8.5F, velocity);
		}

		@Override
		public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{

		}

		@Override
		public void registerRecipe()
		{
			Item resource = Items.iron_ingot;
			switch(material)
			{
			case WOOD: resource = Item.getItemFromBlock(Blocks.planks); break;
			case IRON: resource = Items.iron_ingot; break;
			case GOLD: resource = Items.gold_ingot; break;
			case DIAMOND: resource = Items.diamond; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 1),  new Object[]{"###", "   ", "###", '#', resource});
		}
	}
	
	public static class ComponentBarrelWide extends ComponentBarrel
	{
		public ComponentBarrelWide(Component[] required, Component[] incapatible, ComponentMaterial material, float velocity)
		{
			super("wide", "wide", required, incapatible, material, 38.0F, velocity);
		}

		@Override
		public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{

		}

		@Override
		public void registerRecipe()
		{
			Item barrel = M.barrel_normal_iron.item;
			switch(material)
			{
			case WOOD: barrel = M.barrel_normal_wood.item; break;
			case IRON: barrel = M.barrel_normal_iron.item; break;
			case GOLD: barrel = M.barrel_normal_gold.item; break;
			case DIAMOND: barrel = M.barrel_normal_diamond.item; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2),  new Object[]{"#", "#", '#', barrel});
		}
	}
	
	public static class ComponentBarrelMusket extends ComponentBarrel
	{
		public ComponentBarrelMusket(Component[] required, Component[] incapatible, ComponentMaterial material, float velocity)
		{
			super("musket", "musket", required, incapatible, material, 70.0F, velocity);
		}

		@Override
		public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{
			
		}

		@Override
		public void registerRecipe()
		{
			Item barrel1 = M.barrel_normal_iron.item;
			switch(material)
			{
			case WOOD: barrel1 = M.barrel_normal_wood.item; break;
			case IRON: barrel1 = M.barrel_normal_iron.item; break;
			case GOLD: barrel1 = M.barrel_normal_gold.item; break;
			case DIAMOND: barrel1 = M.barrel_normal_diamond.item; break;
			}
			Item barrel2 = M.barrel_wide_iron.item;
			switch(material)
			{
			case WOOD: barrel2 = M.barrel_wide_wood.item; break;
			case IRON: barrel2 = M.barrel_wide_iron.item; break;
			case GOLD: barrel2 = M.barrel_wide_gold.item; break;
			case DIAMOND: barrel2 = M.barrel_wide_diamond.item; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2),  new Object[]{"@#", '#', barrel1, '@', barrel2});
		}
	}
}
