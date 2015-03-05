package com.spectral.spectral_guns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.spectral.spectral_guns.Stuff.HashMapStuff;
import com.spectral.spectral_guns.blocks.BlockOre2;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.ComponentBarrel;
import com.spectral.spectral_guns.components.aim.ComponentScope;
import com.spectral.spectral_guns.components.aim.ComponentScopeLaser;
import com.spectral.spectral_guns.components.magazine.ComponentMagazine;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineLaser;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineSnowball;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineSmallFireball;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineFood;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanism;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanismAuto;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanismBoosted;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.ItemBase;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemFood2;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.items.ItemShuriken;
import com.spectral.spectral_guns.proxy.ProxyClient;
import com.spectral.spectral_guns.proxy.ProxyCommon;
import com.spectral.spectral_guns.proxy.ProxyServer;
import com.spectral.spectral_guns.tabs.TabGeneric;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = References.MODID, version = References.VERSION)
public class M
{
	//if we are ever going to add mobs, enable this and notify me! i have some nifty code for custom mob eggs that we can use! - sigurd4
	/**Register entity with egg**/
	/*public static void registerEntity(Class<? extends Entity> entityClass, String name, int entityID, int primaryColor, int secondaryColor)
	{
		EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
		ItemMonsterPlacer2.EntityList2.registerEntity(entityClass, entityID, name, primaryColor, secondaryColor);
	}*/

	/**Register entity without egg**/
	public static void registerEntityNoEgg(Class<? extends Entity> entityClass, String name, int entityID)
	{
		EntityRegistry.registerModEntity(entityClass, name, entityID, instance, 64, 1, true);
	}

	private static final HashMap<Object, Id> ids = new HashMap<Object, Id>();
	public static final ArrayList<Id> idsToBeRegistered = new ArrayList<Id>();
	public static final HashMap<Object, CreativeTabs[]> creativeTabs = new HashMap<Object, CreativeTabs[]>();

	public static Iterator<Id> getIds()
	{
		return ((HashMap<Object, Id>)ids.clone()).values().iterator();
	}

	public static Id getId(Item item)
	{
		if(ids.containsKey(item))
		{
			return ids.get(item);
		}
		return null;
	}

	public static Id getId(Block item)
	{
		if(ids.containsKey(item))
		{
			return ids.get(item);
		}
		return null;
	}

	public static Object getItem(Id id)
	{
		if(ids.containsValue(id))
		{
			Object v = HashMapStuff.getKeyFromValue((HashMap<Object, Id>)ids.clone(), id);
			if(v instanceof Item || v instanceof Block)
			{
				return v;
			}
		}
		return null;
	}

	public static boolean hasId(Id id)
	{
		return ids.containsValue(id);
	}

	public static boolean hasItem(Item item)
	{
		return hasItem(item);
	}

	public static boolean hasItem(Block block)
	{
		return hasItem(block);
	}

	@Deprecated
	private static boolean hasItem(Object item)
	{
		return ids.containsKey(item);
	}

	public static class Id
	{
		public final String id;
		public final String mod;

		public Id(String id, String mod)
		{
			this.id = id;
			this.mod = mod;
		}

		public Id(String id)
		{
			this.id = id;
			this.mod = References.MODID;
		}
	}

	public static Item registerItem(String id, Item item)
	{
		return registerItem(id, References.MODID, item);
	}

	public static Item registerItem(String id, String modid, Item item)
	{
		if(!ids.containsKey(item) && !ids.containsValue(id))
		{
			Id ID = new Id(id, modid);
			ids.put(item, ID);
			idsToBeRegistered.add(ID);
		}
		return item;
	}

	public static Block registerBlock(String id, Block block)
	{
		return registerBlock(id, References.MODID, block);
	}

	public static Block registerBlock(String id, String modid, Block block)
	{
		if(!ids.containsKey(block))
		{
			Id ID = new Id(id, modid);
			ids.put(block, ID);
			idsToBeRegistered.add(ID);
		}
		return block;
	}

	@Instance(References.MODID)
	public static M instance;

	public static SimpleNetworkWrapper network;

	/**tabs**/
	public static TabGeneric tabCore = new TabGeneric("core")
	{
		@Override
		public Item getTabIconItem()
		{
			return M.gun;
		}
		@Override
		public ItemStack getIconItemStack()
		{
			ArrayList<ItemStack> a = new ArrayList<ItemStack>();
			M.gun.getSubItems(M.gun, this, a);
			return a.get(0);
		}
	};

	////ITEMS:
	//the gun
	public static ItemGun gun = (ItemGun)new ItemGun();

	//other stuff
	public static final ItemBase iron_nugget = (ItemBase)new ItemBase("iron_nugget").setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase container = (ItemBase)new ItemBase("container").setUnlocalizedName("container").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemFood2 food_mush = (ItemFood2)new ItemFood2("food_mush", 3, 0.1F, false, 42).setUnlocalizedName("foodMush").setCreativeTab(CreativeTabs.tabFood);
	public static final ItemBase gear_wood = (ItemBase)new ItemBase("gear_wood").setUnlocalizedName("gear.wood").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase gear_iron = (ItemBase)new ItemBase("gear_iron").setUnlocalizedName("gear.iron").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase gear_gold = (ItemBase)new ItemBase("gear_gold").setUnlocalizedName("gear.gold").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase gear_diamond = (ItemBase)new ItemBase("gear_diamond").setUnlocalizedName("gear.diamond").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase lens_convex = (ItemBase)new ItemBase("lens_convex").setUnlocalizedName("lens.convex").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase lens_concave = (ItemBase)new ItemBase("lens_concave").setUnlocalizedName("lens.concave").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase prism = (ItemBase)new ItemBase("prism").setUnlocalizedName("prism").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase eyepiece = (ItemBase)new ItemBase("eyepiece").setUnlocalizedName("eyepiece").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase ruby = (ItemBase)new ItemBase("ruby").setUnlocalizedName("ruby").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase laser_diode_green = (ItemBase)new ItemBase("laser_diode_green").setUnlocalizedName("laserDiode.green").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase laser_diode_red = (ItemBase)new ItemBase("laser_diode_red").setUnlocalizedName("laserDiode.red").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase laser_diode_green_strong = (ItemBase)new ItemBase("laser_diode_green_strong").setUnlocalizedName("laserDiode.green.strong").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase laser_diode_red_strong = (ItemBase)new ItemBase("laser_diode_red_strong").setUnlocalizedName("laserDiode.red.strong").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemShuriken shuriken = (ItemShuriken)new ItemShuriken("shuriken").setUnlocalizedName("shuriken").setCreativeTab(CreativeTabs.tabCombat);
	
	//components
	public static ItemComponent componentItem(Component c)
	{
		return c.item;
	}

	public static final ComponentMagazineSnowball magazine_snowball_wood = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 16, 0.2F, 5F, 1);
	public static final ComponentMagazineSnowball magazine_snowball_iron = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 20, 0.4F, 8F, 6);
	public static final ComponentMagazineSnowball magazine_snowball_gold = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 24, 0.6F, 11F, 13);
	public static final ComponentMagazineSnowball magazine_snowball_diamond = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 28, 0.9F, 14F, 21);

	public static final ComponentMagazineSmallFireball magazine_small_fireball_iron = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 6, 0.35F, 33F, 1);
	public static final ComponentMagazineSmallFireball magazine_small_fireball_gold = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 10, 0.7F, 43F, 3);
	public static final ComponentMagazineSmallFireball magazine_small_fireball_diamond = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 14, 1.4F, 53F, 9);

	public static final ComponentMagazineFood magazine_food_wood = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 10, 0.18F, 12F, 5);
	public static final ComponentMagazineFood magazine_food_iron = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 14, 0.72F, 16F, 14);
	public static final ComponentMagazineFood magazine_food_gold = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 18, 1.56F, 20F, 23);

	public static final ComponentMagazineLaser magazine_laser_iron_red = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, LaserColor.RED, 1);
	public static final ComponentMagazineLaser magazine_laser_gold_red = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, LaserColor.RED, 2);
	public static final ComponentMagazineLaser magazine_laser_diamond_red = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, LaserColor.RED, 4);

	public static final ComponentMagazineLaser magazine_laser_iron_green = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, LaserColor.GREEN, 1);
	public static final ComponentMagazineLaser magazine_laser_gold_green = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, LaserColor.GREEN, 2);
	public static final ComponentMagazineLaser magazine_laser_diamond_green = new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, LaserColor.GREEN, 4);


	public static final ComponentTriggerMechanism trigger_wood = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 14F);
	public static final ComponentTriggerMechanism trigger_wood_redstone = new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_wood);
	public static final ComponentTriggerMechanism trigger_wood_auto = new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_wood);

	public static final ComponentTriggerMechanism trigger_iron = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 10F);
	public static final ComponentTriggerMechanism trigger_iron_redstone = new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_iron);
	public static final ComponentTriggerMechanism trigger_iron_auto = new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_iron);

	public static final ComponentTriggerMechanism trigger_gold = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 6F);
	public static final ComponentTriggerMechanism trigger_gold_redstone = new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_gold);
	public static final ComponentTriggerMechanism trigger_gold_auto = new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_gold);


	public static final ComponentBarrel barrel_thin_wood = new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F);
	public static final ComponentBarrel barrel_normal_wood = new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F);
	public static final ComponentBarrel barrel_wide_wood = new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F);
	public static final ComponentBarrel barrel_musket_wood = new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.5F);

	public static final ComponentBarrel barrel_thin_iron = new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F);
	public static final ComponentBarrel barrel_normal_iron = new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F);
	public static final ComponentBarrel barrel_wide_iron = new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F);
	public static final ComponentBarrel barrel_musket_iron = new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.85F);

	public static final ComponentBarrel barrel_thin_gold = new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F);
	public static final ComponentBarrel barrel_normal_gold = new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F);
	public static final ComponentBarrel barrel_wide_gold = new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F);
	public static final ComponentBarrel barrel_musket_gold = new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.97F);

	public static final ComponentBarrel barrel_thin_diamond = new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F);
	public static final ComponentBarrel barrel_normal_diamond = new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F);
	public static final ComponentBarrel barrel_wide_diamond = new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F);
	public static final ComponentBarrel barrel_musket_diamond = new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.97F);

	public static final ComponentScope scope_wood = new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 1+0.4F);
	public static final ComponentScope scope_iron = new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 1+0.8F);
	public static final ComponentScope scope_gold = new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 1+1.2F);
	public static final ComponentScope scope_diamond = new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 1+1.6F);

	public static final ComponentScopeLaser scope_wood_laser_red = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_wood, LaserColor.RED);
	public static final ComponentScopeLaser scope_wood_laser_green = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_wood, LaserColor.GREEN);
	public static final ComponentScopeLaser scope_iron_laser_red = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_iron, LaserColor.RED);
	public static final ComponentScopeLaser scope_iron_laser_green = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_iron, LaserColor.GREEN);
	public static final ComponentScopeLaser scope_gold_laser_red = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_gold, LaserColor.RED);
	public static final ComponentScopeLaser scope_gold_laser_green = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_gold, LaserColor.GREEN);
	public static final ComponentScopeLaser scope_diamond_laser_red = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_diamond, LaserColor.RED);
	public static final ComponentScopeLaser scope_diamond_laser_green = new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_diamond, LaserColor.GREEN);

	////BLOCKS:
	//ores
	public static final BlockOre2 ruby_ore = (BlockOre2)new BlockOre2("ruby_ore", ruby, 3, 4).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRuby");

	//compact blocks
	public static final BlockCompressed ruby_block = (BlockCompressed)registerBlock("ruby_block", new BlockCompressed(MapColor.redColor).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockRuby").setCreativeTab(CreativeTabs.tabBlock));

	public M()
	{

	}

	@SidedProxy(clientSide = References.Client, serverSide = References.Server)
	public static ProxyCommon proxy;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}
}