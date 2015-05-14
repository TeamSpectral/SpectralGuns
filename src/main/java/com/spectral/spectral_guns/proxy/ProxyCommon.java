package com.spectral.spectral_guns.proxy;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.M.Id;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.blocks.BlockOre2;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;
import com.spectral.spectral_guns.event.HandlerCommon;
import com.spectral.spectral_guns.event.HandlerCommonFML;
import com.spectral.spectral_guns.gui.GuiHandler;
import com.spectral.spectral_guns.packet.PacketKey;
import com.spectral.spectral_guns.packet.PacketPlayerData;
import com.spectral.spectral_guns.recipe.RecipeGun;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public abstract class ProxyCommon
{
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new HandlerCommon());
		FMLCommonHandler.instance().bus().register(new HandlerCommonFML());
		
		NetworkRegistry.INSTANCE.registerGuiHandler(M.instance, new GuiHandler());
		
		this.registerConfig(event.getSuggestedConfigurationFile());
		this.registerItems();
		this.oreDictionary();
		this.packets();
		this.entities();
		this.worldGen();
		M.idsToBeRegistered.clear();
	}
	
	public void init(FMLInitializationEvent event)
	{
		this.recipes();
		this.tileEntities();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public abstract World world(int dimension);
	
	private void packets()
	{
		M.network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID + "Packets");
		M.network.registerMessage(PacketKey.Handler.class, PacketKey.class, 0, Side.SERVER);
		M.network.registerMessage(PacketPlayerData.Handler.class, PacketPlayerData.class, 1, Side.CLIENT);
		// M.network.registerMessage(PacketEntityData.Handler.class, PacketEntityData.class, 1, Side.CLIENT);
		// M.network.registerMessage(PacketPlayerData.Handler.class, PacketPlayerData.class, 2, Side.CLIENT);
	}
	
	private void recipes()
	{
		if(Config.canCraftGunInCraftingTable.get())
		{
			RecipeSorter.register(References.MODID + ":" + "gun_recipe", RecipeGun.class, Category.SHAPELESS, "after:minecraft:shaped");
			GameRegistry.addRecipe(new RecipeGun());
		}
		ComponentEvents.registerRecipes();
		this.registerNugget(M.iron_nugget, Items.iron_ingot);
		this.registerNugget(M.ruby, Item.getItemFromBlock(M.ruby_block));
		this.registerGear(M.gear_wood, "plankWood");
		this.registerGear(M.gear_iron, "ingotIron");
		this.registerGear(M.gear_gold, "ingotGold");
		this.registerGear(M.gear_diamond, "gemDiamond");
		if(M.visible(M.container))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.container, 4), new Object[]{"OTO", "G G", "OOO", 'O', Item.getItemFromBlock(Blocks.obsidian), 'T', Item.getItemFromBlock(Blocks.iron_trapdoor), 'G', "paneGlassColorless"}));
		}
		if(M.visible(M.lens_convex))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.lens_convex, 5), new Object[]{" G ", "GGG", " G ", 'G', "paneGlassColorless"}));
		}
		if(M.visible(M.lens_concave))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.lens_concave, 7), new Object[]{"GGG", " G ", "GGG", 'G', "paneGlassColorless"}));
		}
		if(M.visible(M.prism))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.prism), new Object[]{" G ", "GGG", 'G', "paneGlassColorless"}));
		}
		if(M.visible(M.eyepiece) && M.visible(M.lens_convex) && M.visible(M.lens_concave))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.eyepiece), new Object[]{" I ", "lLl", " I ", 'l', M.lens_concave, 'L', M.lens_convex, 'I', "ingotIron"}));
		}
		if(M.visible(M.laser_diode_green) && M.visible(M.eyepiece))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.laser_diode_green), new Object[]{"IGr", "leg", "IGc", 'I', "ingotIron", 'G', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'r', "dustRedstone", 'l', M.eyepiece, 'e', "gemEmerald", 'g', "dustGlowstone", 'c', Items.comparator}));
		}
		if(M.visible(M.laser_diode_red) && M.visible(M.eyepiece))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.laser_diode_red), new Object[]{"IGr", "leg", "IGc", 'I', "ingotIron", 'G', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'r', "dustRedstone", 'l', M.eyepiece, 'e', "gemRuby", 'g', "dustGlowstone", 'c', Items.comparator}));
		}
		if(M.visible(M.laser_diode_green_strong) && M.visible(M.eyepiece))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.laser_diode_green_strong), new Object[]{"IGr", "leg", "IGc", 'I', "ingotIron", 'G', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'r', "blockRedstone", 'l', M.eyepiece, 'e', "gemEmerald", 'g', "glowstone", 'c', Items.comparator}));
		}
		if(M.visible(M.laser_diode_red_strong) && M.visible(M.eyepiece))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.laser_diode_red_strong), new Object[]{"IGr", "leg", "IGc", 'I', "ingotIron", 'G', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'r', "blockRedstone", 'l', M.eyepiece, 'e', "gemRuby", 'g', "glowstone", 'c', Items.comparator}));
		}
		if(M.visible(M.shuriken))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.shuriken, 8), new Object[]{"I I", " i ", "I I", 'I', "ingotIron", 'i', "nuggetIron"}));
		}
		if(M.visible(M.gun_assembly_station))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.gun_assembly_station), new Object[]{"gCg", "GAG", "BFB", 'g', "gearIron", 'C', Item.getItemFromBlock(Blocks.crafting_table), 'G', "gearGold", 'A', Item.getItemFromBlock(Blocks.anvil), 'B', new ItemStack(Blocks.stone, 1, 6), 'F', Item.getItemFromBlock(Blocks.furnace)}));
		}
		if(M.visible(M.wrench))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(M.wrench), new Object[]{"  i", "ig ", " i ", 'g', "gearIron", 'i', "ingotIron"}));
		}
	}
	
	private void registerNugget(Item nugget, Item bar)
	{
		if(M.visible(nugget) && M.visible(bar))
		{
			GameRegistry.addShapedRecipe(new ItemStack(bar, 1), new Object[]{"NNN", "NNN", "NNN", 'N', nugget});
			GameRegistry.addShapedRecipe(new ItemStack(nugget, 9), new Object[]{"B", 'B', bar});
		}
	}
	
	private void registerGear(Item gear, String bar)
	{
		if(M.visible(gear))
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(gear, 6), new Object[]{" B ", "BbB", " B ", 'B', bar, 'b', Item.getItemFromBlock(Blocks.stone_button)}));
		}
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
					if(M.visible(item))
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
	
	private void worldGen()
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
					if(item != null && item instanceof BlockOre2)
					{
						IWorldGenerator worldGen = ((BlockOre2)item).worldGen();
						if(worldGen != null)
						{
							GameRegistry.registerWorldGenerator(worldGen, 0);
						}
					}
				}
			}
		}
	}
	
	private void tileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityGunWorkbench.class, TileEntityGunWorkbench.getID());
	}
	
	private void registerConfig(File file)
	{
		if(Config.config == null)
		{
			Config.config = new Configuration(file);
		}
		for(int i = 0; i < Config.entries.size(); ++i)
		{
			Config.entries.get(i).set(Config.config);
		}
		if(Config.config.hasChanged())
		{
			Config.config.save();
		}
		
		//stuff on init
		if(!Config.enableRubies.get())
		{
			M.getId(M.ruby).visible = false;
			M.ruby.setCreativeTab(null);
			M.getId(M.ruby_block).visible = false;
			M.ruby_block.setCreativeTab(null);
			M.getId(M.ruby_ore).visible = false;
			M.ruby_ore.setCreativeTab(null);
		}
		if(!Config.enableRubies.get())
		{
			M.getId(M.iron_nugget).visible = false;
			M.iron_nugget.setCreativeTab(null);
		}
	}
}
