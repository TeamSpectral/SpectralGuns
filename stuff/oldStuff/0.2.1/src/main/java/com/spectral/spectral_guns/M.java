package com.spectral.spectral_guns;

import java.util.ArrayList;
import java.util.List;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.ComponentBarrel;
import com.spectral.spectral_guns.components.ComponentMagazine;
import com.spectral.spectral_guns.components.ComponentTriggerMechanism;
import com.spectral.spectral_guns.components.Components;
import com.spectral.spectral_guns.components.Components.ComponentMagazineSnowball;
import com.spectral.spectral_guns.components.Components.ComponentMagazineSmallFireball;
import com.spectral.spectral_guns.components.Components.ComponentMagazineFood;
import com.spectral.spectral_guns.items.ItemBase;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemFood2;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.proxy.ProxyClient;
import com.spectral.spectral_guns.proxy.ProxyCommon;
import com.spectral.spectral_guns.proxy.ProxyServer;
import com.spectral.spectral_guns.tabs.TabGeneric;

import net.minecraft.block.BlockPressurePlate;
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
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = References.MODID, version = References.VERSION)
public class M
{
	//TODO if we are ever going to add mobs, enable this and notify me! i have some nifty code for custom mob eggs that we can use! - sigurd4
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
	
	
	//the gun
	public static ItemGun gun = (ItemGun)new ItemGun();
	
	//other stuff
	public static final ItemBase iron_nugget = (ItemBase)new ItemBase("iron_nugget").setUnlocalizedName("ironNugget").setCreativeTab(CreativeTabs.tabMaterials);
	public static final ItemBase container = (ItemBase)new ItemBase("container").setUnlocalizedName("container").setCreativeTab(tabCore);
	public static final ItemFood2 food_mush = (ItemFood2)new ItemFood2("food_mush", 3, 0.1F, false, 42).setUnlocalizedName("foodMush").setCreativeTab(CreativeTabs.tabFood);
	
	//components
	public static ItemComponent componentItem(Component c)
	{
		return c.item;
	}
	
	public static final ComponentMagazine magazine_snowball_wood = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 16, 0.2F, 10F, 1);
	public static final ComponentMagazine magazine_snowball_iron = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 20, 0.4F, 17F, 6);
	public static final ComponentMagazine magazine_snowball_gold = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 24, 0.6F, 21F, 13);
	public static final ComponentMagazine magazine_snowball_diamond = new ComponentMagazineSnowball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 28, 0.9F, 25F, 21);


	public static final ComponentMagazine magazine_small_fireball_iron = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 6, 0.35F, 33F, 1);
	public static final ComponentMagazine magazine_small_fireball_gold = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 10, 0.7F, 43F, 3);
	public static final ComponentMagazine magazine_small_fireball_diamond = new ComponentMagazineSmallFireball(new Component[]{}, new Component[]{}, ComponentMaterial.DIAMOND, 14, 1.4F, 53F, 9);

	public static final ComponentMagazine magazine_food_wood = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 14, 0.18F, 12F, 1);
	public static final ComponentMagazine magazine_food_iron = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 28, 0.72F, 16F, 6);
	public static final ComponentMagazine magazine_food_gold = new ComponentMagazineFood(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 42, 1.56F, 1F, 13);

	
	public static final ComponentTriggerMechanism trigger_wood = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.WOOD, 14F);
	public static final ComponentTriggerMechanism trigger_wood_redstone = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, trigger_wood);
	
	public static final ComponentTriggerMechanism trigger_iron = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.IRON, 10F);
	public static final ComponentTriggerMechanism trigger_iron_redstone = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, trigger_iron);
	
	public static final ComponentTriggerMechanism trigger_gold = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, ComponentMaterial.GOLD, 6F);
	public static final ComponentTriggerMechanism trigger_gold_redstone = new ComponentTriggerMechanism(new Component[]{}, new Component[]{}, trigger_gold);
	
	
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
}