package com.spectral.spectral_guns;

import com.spectral.spectral_guns.Stuff.HashMapStuff;
import com.spectral.spectral_guns.blocks.BlockGunWorkbench;
import com.spectral.spectral_guns.blocks.BlockOre2;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.ComponentBarrel;
import com.spectral.spectral_guns.components.ComponentGrip;
import com.spectral.spectral_guns.components.aim.ComponentScope;
import com.spectral.spectral_guns.components.aim.ComponentScopeLaser;
import com.spectral.spectral_guns.components.magazine.*;
import com.spectral.spectral_guns.components.misc.ComponentOverclocker;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanism;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanismAuto;
import com.spectral.spectral_guns.components.trigger_mechanism.ComponentTriggerMechanismBoosted;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.*;
import com.spectral.spectral_guns.proxy.ProxyCommon;
import com.spectral.spectral_guns.stats.AchievementHandler.Achievements;
import com.spectral.spectral_guns.stats.AchievementPageHandler.AchievementPages;
import com.spectral.spectral_guns.stats.Legendaries;
import com.spectral.spectral_guns.tabs.TabGeneric;
import com.spectral.spectral_guns.worldgen.WorldGenGem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.material.MapColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraftforge.fml.common.IWorldGenerator;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@Mod(modid = References.MODID, name = References.NAME, version = References.VERSION, guiFactory = References.GUI_FACTORY_CLASS)
public class M
{
	// if we are ever going to add mobs, enable this and notify me! i have some
	// nifty code for custom mob eggs that we can use! - sigurd4
	/** Register entity with egg **/
	/*
	 * public static void registerEntity(Class<? extends Entity> entityClass,
	 * String name, int entityID, int primaryColor, int secondaryColor)
	 * {
	 * EntityRegistry.registerGlobalEntityID(entityClass, name, entityID);
	 * ItemMonsterPlacer2.EntityList2.registerEntity(entityClass, entityID,
	 * name, primaryColor, secondaryColor);
	 * }
	 */
	
	/** Register entity without egg **/
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
		return ids.containsKey(item) && ids.get(item).visible;
	}
	
	public static boolean visible(Object item)
	{
		return !ids.containsKey(item) || ids.get(item).visible;
	}
	
	public static HashMap<Integer, ArrayList<String>> getTypes(Item item)
	{
		HashMap<Integer, ArrayList<String>> types = new HashMap();
		for(int meta = 0; meta < item.getMaxDamage() || meta == 0; ++meta)
		{
			types.put(meta, new ArrayList());
			if(item instanceof IItemTextureVariants)
			{
				String[] variants = ((IItemTextureVariants)item).getTextureVariants(meta);
				for(int i = 0; i < variants.length; ++i)
				{
					types.get(meta).add(variants[i].toLowerCase());
				}
			}
			if(types.get(meta).size() <= 0)
			{
				if(M.getId(item) != null)
				{
					Id id = M.getId(item);
					types.get(meta).add(id.mod + ":" + id.id);
				}
				else
				{
					types.get(meta).add("" + Item.itemRegistry.getNameForObject(item));
				}
			}
		}
		return types;
	}
	
	public static class Id
	{
		public final String id;
		public final String mod;
		public final String[] oreDictNames;
		public final boolean replacedIfAlreadyAnOreDict;
		public Class<? extends ItemBlock> blockItem;
		
		public boolean shouldBeReplaced()
		{
			return this.oreDictNames.length <= 0 || !this.replacedIfAlreadyAnOreDict;
		};
		
		public boolean visible;
		public final boolean dungeonLoot;
		public final int dungeonLootMin;
		public final int dungeonLootMax;
		public final int dungeonLootChance;
		
		public Id(String id, String mod, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
		{
			this.id = id;
			this.mod = mod;
			this.replacedIfAlreadyAnOreDict = replacedIfAlreadyAnOreDict;
			this.oreDictNames = oreDictNames;
			
			this.dungeonLoot = false;
			this.dungeonLootMin = 0;
			this.dungeonLootMax = 0;
			this.dungeonLootChance = 0;
		}
		
		private Id(String id, String mod, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, int dungeonLootMin, int dungeonLootMax, int dungeonLootChance)
		{
			this.id = id;
			this.mod = mod;
			this.replacedIfAlreadyAnOreDict = replacedIfAlreadyAnOreDict;
			this.oreDictNames = oreDictNames;
			
			dungeonLootMin = dungeonLootMin >= 1 ? dungeonLootMin : 1;
			dungeonLootMax = dungeonLootMax <= 64 ? dungeonLootMax : 64;
			dungeonLootMin = dungeonLootMin <= dungeonLootMax ? dungeonLootMin : dungeonLootMax;
			dungeonLootMax = dungeonLootMax >= dungeonLootMin ? dungeonLootMax : dungeonLootMin;
			
			this.dungeonLoot = true;
			this.dungeonLootMin = dungeonLootMin;
			this.dungeonLootMax = dungeonLootMax;
			this.dungeonLootChance = dungeonLootChance;
		}
		
		public Id(String id, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
		{
			this(id, References.MODID, replacedIfAlreadyAnOreDict, oreDictNames);
		}
	}
	
	public static <T extends Component> T registerComponent(T component)
	{
		ItemComponent item = ComponentRegister.getItem(component);
		registerItem("component_" + component.getID(), item, false, new String[]{});
		return component;
	}
	
	public static <T extends Item & IItemIdFrom> T registerItem(T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerItem(item.getId(), References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Item> T registerItem(String id, T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerItem(id, References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Item> T registerItem(String id, String modid, T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerItem(id, modid, item, replacedIfAlreadyAnOreDict, oreDictNames, 0, 0, 0);
	}
	
	public static <T extends Item & IItemIdFrom> T registerItem(T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, int min, int max, int chance)
	{
		return registerItem(item.getId(), References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames, min, max, chance);
	}
	
	public static <T extends Item> T registerItem(String id, T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, int min, int max, int chance)
	{
		return registerItem(id, References.MODID, item, replacedIfAlreadyAnOreDict, oreDictNames, min, max, chance);
	}
	
	public static <T extends Item> T registerItem(String id, String modid, T item, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, int min, int max, int chance)
	{
		if(!ids.containsKey(item) && !ids.containsValue(id))
		{
			Id ID = new Id(id, modid, replacedIfAlreadyAnOreDict, oreDictNames);
			if(chance > 0)
			{
				ID = new Id(id, modid, replacedIfAlreadyAnOreDict, oreDictNames, min, max, chance);
			}
			ids.put(item, ID);
			idsToBeRegistered.add(ID);
		}
		return item;
	}
	
	public static <T extends Block> T registerBlock(String id, T block, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerBlock(id, block, (Class<? extends ItemBlock>)null, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Block> T registerBlock(String id, String modid, T block, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerBlock(id, modid, block, (Class<? extends ItemBlock>)null, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Block> T registerBlock(String id, T block, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, Class<? extends ItemBlock> blockItem)
	{
		return registerBlock(id, block, blockItem, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Block> T registerBlock(String id, String modid, T block, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames, Class<? extends ItemBlock> blockItem)
	{
		return registerBlock(id, modid, block, blockItem, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Block> T registerBlock(String id, T block, Class<? extends ItemBlock> blockItem, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		return registerBlock(id, References.MODID, block, blockItem, replacedIfAlreadyAnOreDict, oreDictNames);
	}
	
	public static <T extends Block> T registerBlock(String id, String modid, T block, Class<? extends ItemBlock> blockItem, boolean replacedIfAlreadyAnOreDict, String[] oreDictNames)
	{
		if(!ids.containsKey(block))
		{
			Id ID = new Id(id, modid, replacedIfAlreadyAnOreDict, oreDictNames);
			if(blockItem != null)
			{
				ID.blockItem = blockItem;
			}
			ids.put(block, ID);
			idsToBeRegistered.add(ID);
		}
		return block;
	}
	
	@Instance(References.MODID)
	public static M instance;
	
	public static SimpleNetworkWrapper network;
	
	/** tabs **/
	public static TabGeneric tabCore = new TabGeneric("core")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return M.gun;
		}
	};
	
	// //ITEMS:
	// the gun
	public static final ItemGun gun = registerItem("gun", new ItemGun(), false, new String[]{});
	
	// ammo
	public static final ItemAmmo snow_ampulla = registerItem("ammo_snow_ampulla", (ItemAmmo)new ItemAmmo(1)
	{
		@Override
		public Item ammo()
		{
			return Items.snowball;
		}
		
		@Override
		public void registerRecipe()
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 64), new Object[]{"sss", "sGs", "gCg", 's', Items.snowball, 'g', "gearWood", 'C', M.container, 'G', Items.gunpowder}));
		}
	}.setUnlocalizedName("snowAmpulla").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemAmmo snow_capsule = registerItem("ammo_snow_capsule", (ItemAmmo)new ItemAmmo(8)
	{
		@Override
		public Item ammo()
		{
			return Items.snowball;
		}
		
		@Override
		public void registerRecipe()
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 64), new Object[]{"SSS", "SGS", "gCg", 'S', Blocks.snow, 'g', "gearIron", 'C', M.container, 'G', Items.gunpowder}));
		}
	}.setUnlocalizedName("snowCapsule").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemAmmo fireball_bolt = registerItem("ammo_fireball_bolt", (ItemAmmo)new ItemAmmo(1)
	{
		@Override
		public Item ammo()
		{
			return Items.fire_charge;
		}
		
		@Override
		public void registerRecipe()
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 64), new Object[]{"GIG", "FLF", "gBg", 'G', "gearIron", 'I', "blockIron", 'F', Items.fire_charge, 'L', Items.lava_bucket, 'g', Items.gunpowder, 'B', M.barrel_normal_iron.item}));
		}
	}.setUnlocalizedName("fireballBolt").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemAmmo mush_can = registerItem("ammo_mush_can", (ItemAmmo)new ItemAmmo(2)
	{
		@Override
		public Item ammo()
		{
			return M.food_mush;
		}
		
		@Override
		public void registerRecipe()
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1), new Object[]{"FFF", "FFF", "iCi", 'F', "food", 'i', "ingotIron", 'C', M.container}));
		}
	}.setUnlocalizedName("mushCan").setCreativeTab(M.tabCore), false, new String[]{});
	
	// other stuff
	public static final Item iron_nugget = registerItem("iron_nugget", new Item().setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.tabMaterials), true, new String[]{"nuggetIron"});
	public static final Item container = registerItem("container", new Item().setUnlocalizedName("container").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemFood2 food_mush = registerItem("food_mush", (ItemFood2)new ItemFood2(3, 0.1F, false, 42).setUnlocalizedName("foodMush").setCreativeTab(M.tabCore), false, new String[]{"food"});
	public static final Item gear_wood = registerItem("gear_wood", new Item().setUnlocalizedName("gear.wood").setCreativeTab(M.tabCore), false, new String[]{"gearWood", "gearWooden"});
	public static final Item gear_iron = registerItem("gear_iron", new Item().setUnlocalizedName("gear.iron").setCreativeTab(M.tabCore), false, new String[]{"gearIron"});
	public static final Item gear_gold = registerItem("gear_gold", new Item().setUnlocalizedName("gear.gold").setCreativeTab(M.tabCore), false, new String[]{"gearGold", "gearGolden"});
	public static final Item gear_diamond = registerItem("gear_diamond", new Item().setUnlocalizedName("gear.diamond").setCreativeTab(M.tabCore), false, new String[]{"gearDiamond"});
	public static final Item lens_convex = registerItem("lens_convex", new Item().setUnlocalizedName("lens.convex").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item lens_concave = registerItem("lens_concave", new Item().setUnlocalizedName("lens.concave").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item prism = registerItem("prism", new Item().setUnlocalizedName("prism").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item eyepiece = registerItem("eyepiece", new Item().setUnlocalizedName("eyepiece").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item ruby = registerItem("ruby", new Item().setUnlocalizedName("ruby").setCreativeTab(CreativeTabs.tabMaterials), true, new String[]{"gemRuby"});
	public static final Item laser_diode_green = registerItem("laser_diode_green", new Item().setUnlocalizedName("laserDiode.green").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item laser_diode_red = registerItem("laser_diode_red", new Item().setUnlocalizedName("laserDiode.red").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item laser_diode_green_strong = registerItem("laser_diode_green_strong", new Item().setUnlocalizedName("laserDiode.green.strong").setCreativeTab(M.tabCore), false, new String[]{});
	public static final Item laser_diode_red_strong = registerItem("laser_diode_red_strong", new Item().setUnlocalizedName("laserDiode.red.strong").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemShuriken shuriken = registerItem("shuriken", (ItemShuriken)new ItemShuriken().setUnlocalizedName("shuriken").setCreativeTab(M.tabCore), false, new String[]{});
	public static final ItemWrench wrench = registerItem("wrench", (ItemWrench)new ItemWrench().setUnlocalizedName("wrench").setCreativeTab(M.tabCore), false, new String[]{"toolWrench"});
	
	// components
	public static ItemComponent componentItem(Component c)
	{
		return ComponentRegister.getItem(c);
	}
	
	public static final ComponentMagazineSnowball magazine_snowball_wood = registerComponent(new ComponentMagazineSnowball(ComponentMaterial.WOOD, 16, 0.2F, 0.2F * 5.6F, 1.5F, 1F, 1, 1));
	public static final ComponentMagazineSnowball magazine_snowball_iron = registerComponent(new ComponentMagazineSnowball(ComponentMaterial.IRON, 20, 0.4F, 0.4F * 5.6F, 1.5F, 2F, 6, 2));
	public static final ComponentMagazineSnowball magazine_snowball_gold = registerComponent(new ComponentMagazineSnowball(ComponentMaterial.GOLD, 24, 0.6F, 0.6F * 5.6F, 1.5F, 3F, 13, 4));
	public static final ComponentMagazineSnowball magazine_snowball_diamond = registerComponent(new ComponentMagazineSnowball(ComponentMaterial.DIAMOND, 28, 0.9F, 0.9F * 5.6F, 1.5F, 4F, 21, 8));
	
	public static final ComponentMagazineSmallFireball magazine_small_fireball_iron = registerComponent(new ComponentMagazineSmallFireball(ComponentMaterial.IRON, 6, 0.35F, 0.35F * 7.6F, 7.4F, 33F, 1, 1));
	public static final ComponentMagazineSmallFireball magazine_small_fireball_gold = registerComponent(new ComponentMagazineSmallFireball(ComponentMaterial.GOLD, 10, 0.7F, 0.7F * 7.6F, 7.4F, 43F, 3, 2));
	public static final ComponentMagazineSmallFireball magazine_small_fireball_diamond = registerComponent(new ComponentMagazineSmallFireball(ComponentMaterial.DIAMOND, 14, 1.4F, 1.4F * 7.6F, 7.4F, 53F, 9, 4));
	
	public static final ComponentMagazineFood magazine_food_wood = registerComponent(new ComponentMagazineFood(ComponentMaterial.WOOD, 10, 0.18F, 0.46F * 9.6F, 1.1F, 12F, 5, 1));
	public static final ComponentMagazineFood magazine_food_iron = registerComponent(new ComponentMagazineFood(ComponentMaterial.IRON, 14, 0.72F, 0.92F * 9.6F, 1.1F, 16F, 14, 2));
	public static final ComponentMagazineFood magazine_food_gold = registerComponent(new ComponentMagazineFood(ComponentMaterial.GOLD, 18, 1.56F, 1.38F * 9.6F, 1.1F, 20F, 23, 4));
	
	public static final ComponentMagazineLaser magazine_laser_iron_red = registerComponent(new ComponentMagazineLaser(ComponentMaterial.IRON, LaserColor.RED, 4, 2, 0.001F));
	public static final ComponentMagazineLaser magazine_laser_gold_red = registerComponent(new ComponentMagazineLaser(ComponentMaterial.GOLD, LaserColor.RED, 8, 4, 0.01F));
	public static final ComponentMagazineLaser magazine_laser_diamond_red = registerComponent(new ComponentMagazineLaser(ComponentMaterial.DIAMOND, LaserColor.RED, 16, 8, 0.1F));
	
	public static final ComponentMagazineLaser magazine_laser_iron_green = registerComponent(new ComponentMagazineLaser(ComponentMaterial.IRON, LaserColor.GREEN, 4, 2, 0.001F));
	public static final ComponentMagazineLaser magazine_laser_gold_green = registerComponent(new ComponentMagazineLaser(ComponentMaterial.GOLD, LaserColor.GREEN, 8, 4, 0.01F));
	public static final ComponentMagazineLaser magazine_laser_diamond_green = registerComponent(new ComponentMagazineLaser(ComponentMaterial.DIAMOND, LaserColor.GREEN, 16, 8, 0.1F));
	
	public static final ComponentMagazineShuriken magazine_shuriken_wood = registerComponent(new ComponentMagazineShuriken(ComponentMaterial.WOOD, 4, 0.01F, 0.01F * 5.6F, 1.9F, 1F, 1, 1));
	public static final ComponentMagazineShuriken magazine_shuriken_iron = registerComponent(new ComponentMagazineShuriken(ComponentMaterial.IRON, 8, 0.08F, 0.08F * 5.6F, 1.9F, 2F, 1, 2));
	public static final ComponentMagazineShuriken magazine_shuriken_gold = registerComponent(new ComponentMagazineShuriken(ComponentMaterial.GOLD, 16, 0.16F, 0.16F * 5.6F, 1.9F, 4F, 1, 3));
	public static final ComponentMagazineShuriken magazine_shuriken_diamond = registerComponent(new ComponentMagazineShuriken(ComponentMaterial.DIAMOND, 32, 0.32F, 0.32F * 5.6F, 1.9F, 6, 1, 4));
	
	public static final ComponentTriggerMechanism trigger_wood = registerComponent(new ComponentTriggerMechanism(ComponentMaterial.WOOD, 14F, 5));
	public static final ComponentTriggerMechanism trigger_wood_redstone = registerComponent(new ComponentTriggerMechanismBoosted(trigger_wood));
	public static final ComponentTriggerMechanism trigger_wood_auto = registerComponent(new ComponentTriggerMechanismAuto(trigger_wood));
	
	public static final ComponentTriggerMechanism trigger_iron = registerComponent(new ComponentTriggerMechanism(ComponentMaterial.IRON, 10F, 10));
	public static final ComponentTriggerMechanism trigger_iron_redstone = registerComponent(new ComponentTriggerMechanismBoosted(trigger_iron));
	public static final ComponentTriggerMechanism trigger_iron_auto = registerComponent(new ComponentTriggerMechanismAuto(trigger_iron));
	
	public static final ComponentTriggerMechanism trigger_gold = registerComponent(new ComponentTriggerMechanism(ComponentMaterial.GOLD, 6F, 15));
	public static final ComponentTriggerMechanism trigger_gold_redstone = registerComponent(new ComponentTriggerMechanismBoosted(trigger_gold));
	public static final ComponentTriggerMechanism trigger_gold_auto = registerComponent(new ComponentTriggerMechanismAuto(trigger_gold));
	
	public static final ComponentBarrel barrel_thin_wood = registerComponent(new ComponentBarrel.ComponentBarrelThin(ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_normal_wood = registerComponent(new ComponentBarrel.ComponentBarrelNormal(ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_wide_wood = registerComponent(new ComponentBarrel.ComponentBarrelWide(ComponentMaterial.WOOD, 0.4F));
	public static final ComponentBarrel barrel_musket_wood = registerComponent(new ComponentBarrel.ComponentBarrelMusket(ComponentMaterial.WOOD, 0.5F));
	
	public static final ComponentBarrel barrel_thin_iron = registerComponent(new ComponentBarrel.ComponentBarrelThin(ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_normal_iron = registerComponent(new ComponentBarrel.ComponentBarrelNormal(ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_wide_iron = registerComponent(new ComponentBarrel.ComponentBarrelWide(ComponentMaterial.IRON, 0.8F));
	public static final ComponentBarrel barrel_musket_iron = registerComponent(new ComponentBarrel.ComponentBarrelMusket(ComponentMaterial.IRON, 0.85F));
	
	public static final ComponentBarrel barrel_thin_gold = registerComponent(new ComponentBarrel.ComponentBarrelThin(ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_normal_gold = registerComponent(new ComponentBarrel.ComponentBarrelNormal(ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_wide_gold = registerComponent(new ComponentBarrel.ComponentBarrelWide(ComponentMaterial.GOLD, 0.92F));
	public static final ComponentBarrel barrel_musket_gold = registerComponent(new ComponentBarrel.ComponentBarrelMusket(ComponentMaterial.GOLD, 0.97F));
	
	public static final ComponentBarrel barrel_thin_diamond = registerComponent(new ComponentBarrel.ComponentBarrelThin(ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_normal_diamond = registerComponent(new ComponentBarrel.ComponentBarrelNormal(ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_wide_diamond = registerComponent(new ComponentBarrel.ComponentBarrelWide(ComponentMaterial.DIAMOND, 0.92F));
	public static final ComponentBarrel barrel_musket_diamond = registerComponent(new ComponentBarrel.ComponentBarrelMusket(ComponentMaterial.DIAMOND, 0.97F));
	
	public static final ComponentScope scope_wood = registerComponent(new ComponentScope(ComponentMaterial.WOOD, 1 + 0.4F));
	public static final ComponentScope scope_iron = registerComponent(new ComponentScope(ComponentMaterial.IRON, 1 + 0.8F));
	public static final ComponentScope scope_gold = registerComponent(new ComponentScope(ComponentMaterial.GOLD, 1 + 1.2F));
	public static final ComponentScope scope_diamond = registerComponent(new ComponentScope(ComponentMaterial.DIAMOND, 1 + 1.6F));
	
	public static final ComponentScopeLaser scope_wood_laser_red = registerComponent(new ComponentScopeLaser(scope_wood, LaserColor.RED));
	public static final ComponentScopeLaser scope_wood_laser_green = registerComponent(new ComponentScopeLaser(scope_wood, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_iron_laser_red = registerComponent(new ComponentScopeLaser(scope_iron, LaserColor.RED));
	public static final ComponentScopeLaser scope_iron_laser_green = registerComponent(new ComponentScopeLaser(scope_iron, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_gold_laser_red = registerComponent(new ComponentScopeLaser(scope_gold, LaserColor.RED));
	public static final ComponentScopeLaser scope_gold_laser_green = registerComponent(new ComponentScopeLaser(scope_gold, LaserColor.GREEN));
	public static final ComponentScopeLaser scope_diamond_laser_red = registerComponent(new ComponentScopeLaser(scope_diamond, LaserColor.RED));
	public static final ComponentScopeLaser scope_diamond_laser_green = registerComponent(new ComponentScopeLaser(scope_diamond, LaserColor.GREEN));
	
	public static final ComponentGrip grip_wood = registerComponent(new ComponentGrip(ComponentMaterial.WOOD, 0.5F, 1.6F, 0.8F));
	public static final ComponentGrip grip_iron = registerComponent(new ComponentGrip(ComponentMaterial.IRON, 0.3F, 0.9F, 1.2F));
	public static final ComponentGrip grip_gold = registerComponent(new ComponentGrip(ComponentMaterial.GOLD, 0.1F, 0.7F, 1.4F));
	
	public static final ComponentOverclocker overclocker = registerComponent(new ComponentOverclocker());
	
	// //BLOCKS:
	// ores
	public static final BlockOre2 ruby_ore = registerBlock("ruby_ore", (BlockOre2)new BlockOre2(ruby, 3, 4)
	{
		@Override
		public IWorldGenerator worldGen()
		{
			return new WorldGenGem(this.getDefaultState(), new Class[]{BiomeGenHills.class});
		}
	}.setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("oreRuby"), true, new String[]{"oreRuby"});
	
	// compact blocks
	public static final BlockCompressed ruby_block = registerBlock("ruby_block", (BlockCompressed)new BlockCompressed(MapColor.redColor).setHardness(3.0F).setResistance(5.0F).setStepSound(Block.soundTypePiston).setUnlocalizedName("blockRuby").setCreativeTab(CreativeTabs.tabBlock), true, new String[]{"blockRuby"});
	
	// tile entity blocks
	public static final BlockGunWorkbench gun_assembly_station = registerBlock("gun_assembly_station", (BlockGunWorkbench)new BlockGunWorkbench().setHardness(4.0F).setResistance(6.0F).setStepSound(Block.soundTypeMetal).setUnlocalizedName("gunAssemblyStation").setCreativeTab(M.tabCore), false, new String[]{}, ItemBlockGunWorkbench.class);
	
	public M()
	{
		new AchievementPages();
		new Achievements();
		new Legendaries();
	}
	
	@SidedProxy(clientSide = References.CLIENT_PROXY_CLASS, serverSide = References.SERVER_PROXY_CLASS)
	public static ProxyCommon proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}
	
	@EventHandler
	public void init(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}