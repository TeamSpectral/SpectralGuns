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
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
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
		return hasItem((Object)item);
	}

	public static boolean hasItem(Block block)
	{
		return hasItem((Object)block);
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
		public final String[] oreDictNames;
		public final boolean replacedIfAlreadyAnOreDict;
		public boolean shouldBeReplaced(){return oreDictNames.length <= 0 || !replacedIfAlreadyAnOreDict;};
		public boolean visible;

		public Id(String id, String mod, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
		{
			this.id = id;
			this.mod = mod;
			this.replacedIfAlreadyAnOreDict = replacedIfAlreadyAnOreDict;
			this.oreDictNames = oreDictNames;
		}

		public Id(String id, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
		{
			this(id, References.MODID, replacedIfAlreadyAnOreDict, oreDictNames);
		}
	}

	public static <T extends Component> T registerComponent(T component)
	{
		ItemComponent item = component.item;
		registerItem(item, false, new String[]{});
		return component;
	}

	public static <T extends Item & IDAble> T registerItem(T item, boolean replacedIfAlreadyAnOreDict, String [] oreDictNames)
	{
		return registerItem(item.getId(), References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames);
	}
		
	public static <T extends Item> T registerItem(String id, T item, boolean replacedIfAlreadyAnOreDict, String [] oreDictNames)
	{
		return registerItem(id, References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames);
	}

	public static <T extends Item> T registerItem(String id, String modid, T item, boolean replacedIfAlreadyAnOreDict, String [] oreDictNames)
	{
		if(!ids.containsKey(item) && !ids.containsValue(id))
		{
			Id ID = new Id(id, modid, replacedIfAlreadyAnOreDict, oreDictNames);
			ids.put(item, ID);
			idsToBeRegistered.add(ID);
		}
		return item;
	}

	public static <T extends Block> T registerBlock(String id, T block, boolean replacedIfAlreadyAnOreDict, String [] oreDictNames)
	{
		return registerBlock(id, References.MODID, block, replacedIfAlreadyAnOreDict, oreDictNames);
	}

	public static <T extends Block> T registerBlock(String id, String modid, T block, boolean replacedIfAlreadyAnOreDict, String [] oreDictNames)
	{
		if(!ids.containsKey(block))
		{
			Id ID = new Id(id, modid, replacedIfAlreadyAnOreDict, oreDictNames);
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
	public static ItemGun gun = registerItem((ItemGun)new ItemGun(), false, new String[]{});

	//other stuff
	public static final ItemBase iron_nugget = registerItem((ItemBase)new ItemBase("iron_nugget").setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{"nuggetIron"});
	public static final ItemBase container = registerItem((ItemBase)new ItemBase("container").setUnlocalizedName("container").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemFood2 food_mush = registerItem((ItemFood2)new ItemFood2("food_mush", 3, 0.1F, false, 42).setUnlocalizedName("foodMush").setCreativeTab(CreativeTabs.tabFood), false, new String[]{});
	public static final ItemBase gear_wood = registerItem((ItemBase)new ItemBase("gear_wood").setUnlocalizedName("gear.wood").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{"gearWood"});
	public static final ItemBase gear_iron = registerItem((ItemBase)new ItemBase("gear_iron").setUnlocalizedName("gear.iron").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{"gearIron"});
	public static final ItemBase gear_gold = registerItem((ItemBase)new ItemBase("gear_gold").setUnlocalizedName("gear.gold").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{"gearGold"});
	public static final ItemBase gear_diamond = registerItem((ItemBase)new ItemBase("gear_diamond").setUnlocalizedName("gear.diamond").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{"gearDiamond"});
	public static final ItemBase lens_convex = registerItem((ItemBase)new ItemBase("lens_convex").setUnlocalizedName("lens.convex").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase lens_concave = registerItem((ItemBase)new ItemBase("lens_concave").setUnlocalizedName("lens.concave").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase prism = registerItem((ItemBase)new ItemBase("prism").setUnlocalizedName("prism").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase eyepiece = registerItem((ItemBase)new ItemBase("eyepiece").setUnlocalizedName("eyepiece").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase ruby = registerItem((ItemBase)new ItemBase("ruby").setUnlocalizedName("ruby").setCreativeTab(CreativeTabs.tabMaterials), true, new String[]{"gemRuby"});
	public static final ItemBase laser_diode_green = registerItem((ItemBase)new ItemBase("laser_diode_green").setUnlocalizedName("laserDiode.green").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase laser_diode_red = registerItem((ItemBase)new ItemBase("laser_diode_red").setUnlocalizedName("laserDiode.red").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase laser_diode_green_strong = registerItem((ItemBase)new ItemBase("laser_diode_green_strong").setUnlocalizedName("laserDiode.green.strong").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemBase laser_diode_red_strong = registerItem((ItemBase)new ItemBase("laser_diode_red_strong").setUnlocalizedName("laserDiode.red.strong").setCreativeTab(CreativeTabs.tabMaterials), false, new String[]{});
	public static final ItemShuriken shuriken = registerItem((ItemShuriken)new ItemShuriken("shuriken").setUnlocalizedName("shuriken").setCreativeTab(CreativeTabs.tabCombat), false, new String[]{});

	//components
	public static ItemComponent componentItem(Component c)
	{
		return c.item;
	}

	public static final ComponentMagazineSnowball magazine_snowball_wood = registerComponent(new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 16, 0.2F, 5F, 1));
	public static final ComponentMagazineSnowball magazine_snowball_iron = registerComponent(new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 20, 0.4F, 8F, 6));
	public static final ComponentMagazineSnowball magazine_snowball_gold = registerComponent(new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 24, 0.6F, 11F, 13));
	public static final ComponentMagazineSnowball magazine_snowball_diamond = registerComponent(new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 28, 0.9F, 14F, 21));

	public static final ComponentMagazineSmallFireball magazine_small_fireball_iron = registerComponent(new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 6, 0.35F, 33F, 1));
	public static final ComponentMagazineSmallFireball magazine_small_fireball_gold = registerComponent(new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 10, 0.7F, 43F, 3));
	public static final ComponentMagazineSmallFireball magazine_small_fireball_diamond = registerComponent(new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 14, 1.4F, 53F, 9));

	public static final ComponentMagazineFood magazine_food_wood = registerComponent(new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 10, 0.18F, 12F, 5));
	public static final ComponentMagazineFood magazine_food_iron = registerComponent(new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 14, 0.72F, 16F, 14));
	public static final ComponentMagazineFood magazine_food_gold = registerComponent(new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 18, 1.56F, 20F, 23));

	public static final ComponentMagazineLaser magazine_laser_iron_red = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, LaserColor.RED, 1, 2));
	public static final ComponentMagazineLaser magazine_laser_gold_red = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, LaserColor.RED, 2, 4));
	public static final ComponentMagazineLaser magazine_laser_diamond_red = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, LaserColor.RED, 4, 8));

	public static final ComponentMagazineLaser magazine_laser_iron_green = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, LaserColor.GREEN, 1, 2));
	public static final ComponentMagazineLaser magazine_laser_gold_green = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, LaserColor.GREEN, 2, 4));
	public static final ComponentMagazineLaser magazine_laser_diamond_green = registerComponent(new ComponentMagazineLaser(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, LaserColor.GREEN, 4, 8));


	public static final ComponentTriggerMechanism trigger_wood = registerComponent(new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 14F));
	public static final ComponentTriggerMechanism trigger_wood_redstone = registerComponent(new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_wood));
	public static final ComponentTriggerMechanism trigger_wood_auto = registerComponent(new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_wood));

	public static final ComponentTriggerMechanism trigger_iron = registerComponent(new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 10F));
	public static final ComponentTriggerMechanism trigger_iron_redstone = registerComponent(new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_iron));
	public static final ComponentTriggerMechanism trigger_iron_auto = registerComponent(new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_iron));

	public static final ComponentTriggerMechanism trigger_gold = registerComponent(new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 6F));
	public static final ComponentTriggerMechanism trigger_gold_redstone = registerComponent(new ComponentTriggerMechanismBoosted(new Component[]{}, new Component[]{}, trigger_gold));
	public static final ComponentTriggerMechanism trigger_gold_auto = registerComponent(new ComponentTriggerMechanismAuto(new Component[]{}, new Component[]{}, trigger_gold));


	public static final ComponentBarrel barrel_thin_wood = registerComponent(new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_normal_wood = registerComponent(new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_wide_wood = registerComponent(new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_musket_wood = registerComponent(new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 0.5F));

	public static final ComponentBarrel barrel_thin_iron = registerComponent(new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_normal_iron = registerComponent(new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_wide_iron = registerComponent(new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_musket_iron = registerComponent(new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 0.85F));

	public static final ComponentBarrel barrel_thin_gold = registerComponent(new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_normal_gold = registerComponent(new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_wide_gold = registerComponent(new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_musket_gold = registerComponent(new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 0.97F));

	public static final ComponentBarrel barrel_thin_diamond = registerComponent(new ComponentBarrel.ComponentBarrelThin(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_normal_diamond = registerComponent(new ComponentBarrel.ComponentBarrelNormal(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_wide_diamond = registerComponent(new ComponentBarrel.ComponentBarrelWide(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_musket_diamond = registerComponent(new ComponentBarrel.ComponentBarrelMusket(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 0.97F));

	public static final ComponentScope scope_wood = registerComponent(new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 1+0.4F));
	public static final ComponentScope scope_iron = registerComponent(new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 1+0.8F));
	public static final ComponentScope scope_gold = registerComponent(new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 1+1.2F));
	public static final ComponentScope scope_diamond = registerComponent(new ComponentScope(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 1+1.6F));

	public static final ComponentScopeLaser scope_wood_laser_red = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_wood, LaserColor.RED));
	public static final ComponentScopeLaser scope_wood_laser_green = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_wood, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_iron_laser_red = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_iron, LaserColor.RED));
	public static final ComponentScopeLaser scope_iron_laser_green = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_iron, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_gold_laser_red = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_gold, LaserColor.RED));
	public static final ComponentScopeLaser scope_gold_laser_green = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_gold, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_diamond_laser_red = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_diamond, LaserColor.RED));
	public static final ComponentScopeLaser scope_diamond_laser_green = registerComponent(new ComponentScopeLaser(new Component[]{}, new Component[]{}, scope_diamond, LaserColor.GREEN));

	////BLOCKS:
	//ores
	public static final BlockOre2 ruby_ore = registerBlock("ruby_ore", (BlockOre2)new BlockOre2(ruby, 3, 4).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRuby"), true, new String[]{"oreRuby"});

	//compact blocks
	public static final BlockCompressed ruby_block = registerBlock("ruby_block", (BlockCompressed)new BlockCompressed(MapColor.redColor).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockRuby").setCreativeTab(CreativeTabs.tabBlock), true, new String[]{"blockRuby"});

	public M()
	{
		
	}

	@SidedProxy(clientSide = References.Client, serverSide = References.Server)
	public static ProxyCommon proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@EventHandler
	public void init(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}
}