package com.spectral.spectral_guns.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.M.Id;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;
import com.spectral.spectral_guns.event.HandlerCommon;
import com.spectral.spectral_guns.event.HandlerCommonFML;
import com.spectral.spectral_guns.event.HandlerServer;
import com.spectral.spectral_guns.event.HandlerServerFML;
import com.spectral.spectral_guns.packet.PacketKey;
import com.spectral.spectral_guns.recipe.RecipeGun;

public class ProxyCommon
{
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new HandlerCommon());
		FMLCommonHandler.instance().bus().register(new HandlerCommonFML());

		registerItems();
		oreDictionary();
		packets();
		entities();
		M.idsToBeRegistered.clear();
	}

	public void init()
	{
		recipes();
	}

	public void postInit()
	{

	}

	private void packets()
	{
		M.network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID + "Packets");
		M.network.registerMessage(PacketKey.Handler.class, PacketKey.class, 0, Side.SERVER);
		//M.network.registerMessage(PacketEntityData.Handler.class, PacketEntityData.class, 1, Side.CLIENT);
		//M.network.registerMessage(PacketPlayerData.Handler.class, PacketPlayerData.class, 2, Side.CLIENT);
	}

	private void recipes()
	{
		GameRegistry.addRecipe(new RecipeGun());
		ComponentEvents.registerRecipes();
		registerNugget(M.iron_nugget, Items.iron_ingot);
		registerNugget(M.ruby, Item.getItemFromBlock(M.ruby_block));
		registerGear(M.gear_wood, Item.getItemFromBlock(Blocks.planks));
		registerGear(M.gear_iron, Items.iron_ingot);
		registerGear(M.gear_gold, Items.gold_ingot);
		registerGear(M.gear_diamond, Items.diamond);
		GameRegistry.addShapedRecipe(new ItemStack(M.container, 4), new Object[]{"OTO", "G G", "OOO", 'O', Item.getItemFromBlock(Blocks.obsidian), 'T', Item.getItemFromBlock(Blocks.iron_trapdoor), 'G', Item.getItemFromBlock(Blocks.glass_pane)});
		GameRegistry.addShapedRecipe(new ItemStack(M.lens_convex, 5), new Object[]{" G ", "GGG", " G ", 'G', Item.getItemFromBlock(Blocks.glass_pane)});
		GameRegistry.addShapedRecipe(new ItemStack(M.lens_concave, 7), new Object[]{"GGG", " G ", "GGG", 'G', Item.getItemFromBlock(Blocks.glass_pane)});
		GameRegistry.addShapedRecipe(new ItemStack(M.prism), new Object[]{" G ", "GGG", 'G', Item.getItemFromBlock(Blocks.glass_pane)});
		GameRegistry.addShapedRecipe(new ItemStack(M.eyepiece), new Object[]{" I ", "lLl", " I ", 'l', M.lens_concave, 'L', M.lens_convex, 'I', Items.iron_ingot});
		GameRegistry.addShapedRecipe(new ItemStack(M.laser_diode_green), new Object[]{"IGr", "leg", "IGc", 'I', Items.iron_ingot, 'G', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'r', Items.redstone, 'l', M.eyepiece, 'e', Items.emerald, 'g', Items.glowstone_dust, 'c', Items.comparator});
		GameRegistry.addShapedRecipe(new ItemStack(M.laser_diode_red), new Object[]{"IGr", "leg", "IGc", 'I', Items.iron_ingot, 'G', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'r', Items.redstone, 'l', M.eyepiece, 'e', M.ruby, 'g', Items.glowstone_dust, 'c', Items.comparator});
		GameRegistry.addShapedRecipe(new ItemStack(M.laser_diode_green_strong), new Object[]{"IGr", "leg", "IGc", 'I', Items.iron_ingot, 'G', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'r', Item.getItemFromBlock(Blocks.redstone_block), 'l', M.eyepiece, 'e', Items.emerald, 'g', Item.getItemFromBlock(Blocks.glowstone), 'c', Items.comparator});
		GameRegistry.addShapedRecipe(new ItemStack(M.laser_diode_red_strong), new Object[]{"IGr", "leg", "IGc", 'I', Items.iron_ingot, 'G', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'r', Item.getItemFromBlock(Blocks.redstone_block), 'l', M.eyepiece, 'e', M.ruby, 'g', Item.getItemFromBlock(Blocks.glowstone), 'c', Items.comparator});
		GameRegistry.addShapedRecipe(new ItemStack(M.shuriken, 8), new Object[]{"I I", " i ", "I I", 'I', Items.iron_ingot, 'i', M.iron_nugget});
	}

	private void registerNugget(Item nugget, Item bar)
	{
		GameRegistry.addShapedRecipe(new ItemStack(bar, 1), new Object[]{"NNN", "NNN", "NNN", 'N', nugget});
		GameRegistry.addShapedRecipe(new ItemStack(nugget, 9), new Object[]{"B", 'B', bar});
	}

	private void registerGear(Item gear, Item bar)
	{
		GameRegistry.addShapedRecipe(new ItemStack(gear, 6), new Object[]{" B ", "BbB", " B ", 'B', bar, 'b', Item.getItemFromBlock(Blocks.stone_button)});
	}

	private void oreDictionary()
	{
		Iterator<Id> ids = M.idsToBeRegistered.iterator();
		while(ids.hasNext())
		{
			Id id = ids.next();
			if(id != null)
			{
				Object item = M.getItem(id);
				for(int i = 0; i < id.oreDictNames.length; ++i)
				{
					if(item != null && item instanceof Block)
					{
						OreDictionary.registerOre(id.oreDictNames[i], (Block)item);
					}
					if(item != null && item instanceof Item)
					{
						OreDictionary.registerOre(id.oreDictNames[i], (Item)item);
					}
				}
			}
		}
	}

	private void entities()
	{
		M.registerEntityNoEgg(EntityFood.class, "foodProjectile", 0);
		M.registerEntityNoEgg(EntityShuriken.class, "shuriken", 1);
		M.registerEntityNoEgg(EntityLaser.class, "laser", 2);
	}

	private void registerItems()
	{
		Iterator<Id> ids = M.idsToBeRegistered.iterator();
		while(ids.hasNext())
		{
			Id id = ids.next();
			if(id != null)
			{
				Object item = M.getItem(id);
				if(item != null && item instanceof Block)
				{
					GameRegistry.registerBlock((Block)item, id.id);
				}
				if(item != null && item instanceof Item)
				{
					GameRegistry.registerItem((Item)item, id.id);
				}

				if(id.replacedIfAlreadyAnOreDict)
				{
					id.visible = false;
					for(int i2 = 0; i2 < id.oreDictNames.length; ++i2)
					{
						List<ItemStack> oreDicts = OreDictionary.getOres(id.oreDictNames[i2]);
						if(oreDicts != null)
						{
							for(int i3 = 0; i3 < oreDicts.size(); ++i3)
							{
								if(oreDicts.get(i3) != null && oreDicts.get(i3).getItem() == item)
								{
									oreDicts.remove(i3);
									--i3;
								}
							}
						}
						if(oreDicts == null || oreDicts.size() <= 0)
						{
							id.visible = true;
						}
					}
				}
				else
				{
					id.visible = true;
				}
			}
		}
	}
}
