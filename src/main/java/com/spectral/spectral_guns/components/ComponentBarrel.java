package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public abstract class ComponentBarrel extends ComponentGeneric
{
	public final float spread;
	public final float velocity;
	
	public ComponentBarrel(String id, String name, ComponentMaterial material, float spread, float velocity, float maxDurability)
	{
		super(new String2("barrel_" + id, ""), new String2("barrel." + name, ""), 0.5, 10 * 4 * 4 / 3, maxDurability, Type.BARREL, material);
		this.spread = spread;
		this.velocity = velocity;
		this.requiredTypes = new Type[]{Type.MAGAZINE};
	}
	
	@Override
	public float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player)
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
	protected void fireSound(int slot, Entity e, ItemStack stack, World world, EntityPlayer player)
	{
		for(int i = 0; i < 3; ++i)
		{
			float v = (this.spread + world.rand.nextFloat() * 20 - 16) / 30;
			if(v < 0)
			{
				v = 0;
			}
			float p = 30 - v;
			if(p < 0)
			{
				p = 0;
			}
			e.playSound("mob.blaze.hit", v / 16 + 0.15F, p * 4 - world.rand.nextFloat() * 0.1F - p * 2);
		}
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		if(this.velocity <= 2 && this.velocity > 0)
		{
			speed *= this.velocity;
		}
		return speed;
	}
	
	@Override
	public abstract void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags);
	
	@Override
	public abstract void registerRecipe();
	
	public static class ComponentBarrelThin extends ComponentBarrel
	{
		public ComponentBarrelThin(ComponentMaterial material, float velocity)
		{
			super("thin", "thin", material, 0.3F, velocity, 1.8F);
		}
		
		@Override
		public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{
			
		}
		
		@Override
		public void registerRecipe()
		{
			Item barrel = M.barrel_normal_iron.item;
			switch(this.material)
			{
			case WOOD:
				barrel = M.barrel_normal_wood.item;
				break;
			case IRON:
				barrel = M.barrel_normal_iron.item;
				break;
			case GOLD:
				barrel = M.barrel_normal_gold.item;
				break;
			case DIAMOND:
				barrel = M.barrel_normal_diamond.item;
				break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2), new Object[]{"##", '#', barrel});
		}
	}
	
	public static class ComponentBarrelNormal extends ComponentBarrel
	{
		public ComponentBarrelNormal(ComponentMaterial material, float velocity)
		{
			super("normal", "normal", material, 8.5F, velocity, 2.1F);
		}
		
		@Override
		public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{
			
		}
		
		@Override
		public void registerRecipe()
		{
			Object resource = Items.iron_ingot;
			switch(this.material)
			{
			case WOOD:
				resource = "plankWood";
				break;
			case IRON:
				resource = "ingotIron";
				break;
			case GOLD:
				resource = "ingotGold";
				break;
			case DIAMOND:
				resource = "gemDiamond";
				break;
			}
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item, 1), new Object[]{"###", "   ", "###", '#', resource}));
		}
	}
	
	public static class ComponentBarrelWide extends ComponentBarrel
	{
		public ComponentBarrelWide(ComponentMaterial material, float velocity)
		{
			super("wide", "wide", material, 38.0F, velocity, 2.8F);
		}
		
		@Override
		public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{
			
		}
		
		@Override
		public void registerRecipe()
		{
			Item barrel = M.barrel_normal_iron.item;
			switch(this.material)
			{
			case WOOD:
				barrel = M.barrel_normal_wood.item;
				break;
			case IRON:
				barrel = M.barrel_normal_iron.item;
				break;
			case GOLD:
				barrel = M.barrel_normal_gold.item;
				break;
			case DIAMOND:
				barrel = M.barrel_normal_diamond.item;
				break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2), new Object[]{"#", "#", '#', barrel});
		}
	}
	
	public static class ComponentBarrelMusket extends ComponentBarrel
	{
		public ComponentBarrelMusket(ComponentMaterial material, float velocity)
		{
			super("musket", "musket", material, 70.0F, velocity, 2.4F);
		}
		
		@Override
		public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
		{
			
		}
		
		@Override
		public void registerRecipe()
		{
			Item barrel1 = M.barrel_normal_iron.item;
			switch(this.material)
			{
			case WOOD:
				barrel1 = M.barrel_normal_wood.item;
				break;
			case IRON:
				barrel1 = M.barrel_normal_iron.item;
				break;
			case GOLD:
				barrel1 = M.barrel_normal_gold.item;
				break;
			case DIAMOND:
				barrel1 = M.barrel_normal_diamond.item;
				break;
			}
			Item barrel2 = M.barrel_wide_iron.item;
			switch(this.material)
			{
			case WOOD:
				barrel2 = M.barrel_wide_wood.item;
				break;
			case IRON:
				barrel2 = M.barrel_wide_iron.item;
				break;
			case GOLD:
				barrel2 = M.barrel_wide_gold.item;
				break;
			case DIAMOND:
				barrel2 = M.barrel_wide_diamond.item;
				break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item, 2), new Object[]{"@#", '#', barrel1, '@', barrel2});
		}
	}
}
