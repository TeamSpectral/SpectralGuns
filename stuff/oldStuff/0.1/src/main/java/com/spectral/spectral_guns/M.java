package com.spectral.spectral_guns;

import java.util.ArrayList;
import java.util.List;

import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentBarrel;
import com.spectral.spectral_guns.components.ComponentMagazine;
import com.spectral.spectral_guns.components.ComponentTriggerMechanism;
import com.spectral.spectral_guns.components.Components;
import com.spectral.spectral_guns.components.Components.ComponentMagazineSnowball;
import com.spectral.spectral_guns.items.ItemBase;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.proxy.ProxyClient;
import com.spectral.spectral_guns.proxy.ProxyCommon;
import com.spectral.spectral_guns.proxy.ProxyServer;
import com.spectral.spectral_guns.tabs.TabGeneric;

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
	
	//components
	public static ItemComponent componentItem(Component c)
	{
		return c.item;
	}
	
	public static final ComponentMagazine magazine_snowball_wood = new ComponentMagazineSnowball("magazine_snowball_wood", "magazineSnowballWood", new Component[]{}, new Component[]{}, 15, 0.2F, 60F, 10F, 1);
	
	public static final ComponentMagazine magazine_snowball_iron = new ComponentMagazineSnowball("magazine_snowball_iron", "magazineSnowballIron", new Component[]{}, new Component[]{}, 15, 0.4F, 60F, 17F, 6);
	
	public static final ComponentMagazine magazine_snowball_gold = new ComponentMagazineSnowball("magazine_snowball_gold", "magazineSnowballGold", new Component[]{}, new Component[]{}, 30, 0.6F, 60F, 21F, 13);
	
	public static final ComponentMagazine magazine_snowball_diamond = new ComponentMagazineSnowball("magazine_snowball_diamond", "magazineSnowballDiamond", new Component[]{}, new Component[]{}, 60, 0.9F, 60F, 25F, 21);

	
	public static final ComponentTriggerMechanism trigger_wood = new ComponentTriggerMechanism("trigger_wood", "triggerWood", new Component[]{}, new Component[]{}, 14F, false);
	public static final ComponentTriggerMechanism trigger_wood_redstone = new ComponentTriggerMechanism("trigger_wood_redstone", "triggerWoodRedstone", new Component[]{}, new Component[]{}, trigger_wood.delay, true);
	
	public static final ComponentTriggerMechanism trigger_iron = new ComponentTriggerMechanism("trigger_iron", "triggerIron", new Component[]{}, new Component[]{}, 10F, false);
	public static final ComponentTriggerMechanism trigger_iron_redstone = new ComponentTriggerMechanism("trigger_iron_redstone", "triggerIronRedstone", new Component[]{}, new Component[]{}, trigger_iron.delay, true);
	
	public static final ComponentTriggerMechanism trigger_gold = new ComponentTriggerMechanism("trigger_gold", "triggerGold", new Component[]{}, new Component[]{}, 6F, false);
	public static final ComponentTriggerMechanism trigger_gold_redstone = new ComponentTriggerMechanism("trigger_gold_redstone", "triggerGoldRedstone", new Component[]{}, new Component[]{}, trigger_gold.delay, true);
	
	
	public static final ComponentBarrel barrel_thin_wood = new ComponentBarrel("barrel_thin_wood", "barrelThinWood", new Component[]{}, new Component[]{}, Components.thin, 0.4F);
	public static final ComponentBarrel barrel_normal_wood = new ComponentBarrel("barrel_normal_wood", "barrelNormalWood", new Component[]{}, new Component[]{}, Components.normal, 0.4F);
	public static final ComponentBarrel barrel_wide_wood = new ComponentBarrel("barrel_wide_wood", "barrelWideWood", new Component[]{}, new Component[]{}, Components.wide, 0.4F);
	public static final ComponentBarrel barrel_musket_wood = new ComponentBarrel("barrel_musket_wood", "barrelMusketWood", new Component[]{}, new Component[]{}, Components.musket, 0.5F);

	public static final ComponentBarrel barrel_thin_iron = new ComponentBarrel("barrel_thin_iron", "barrelThinIron", new Component[]{}, new Component[]{}, Components.thin, 0.8F);
	public static final ComponentBarrel barrel_normal_iron = new ComponentBarrel("barrel_normal_iron", "barrelNormalIron", new Component[]{}, new Component[]{}, Components.normal, 0.8F);
	public static final ComponentBarrel barrel_wide_iron = new ComponentBarrel("barrel_wide_iron", "barrelWideIron", new Component[]{}, new Component[]{}, Components.wide, 0.8F);
	public static final ComponentBarrel barrel_musket_iron = new ComponentBarrel("barrel_musket_iron", "barrelMusketIron", new Component[]{}, new Component[]{}, Components.musket, 0.85F);

	public static final ComponentBarrel barrel_thin_gold = new ComponentBarrel("barrel_thin_gold", "barrelThinGold", new Component[]{}, new Component[]{}, Components.thin, 0.92F);
	public static final ComponentBarrel barrel_normal_gold = new ComponentBarrel("barrel_normal_gold", "barrelNormalGold", new Component[]{}, new Component[]{}, Components.normal, 0.92F);
	public static final ComponentBarrel barrel_wide_gold = new ComponentBarrel("barrel_wide_gold", "barrelWideGold", new Component[]{}, new Component[]{}, Components.wide, 0.92F);
	public static final ComponentBarrel barrel_musket_gold = new ComponentBarrel("barrel_musket_gold", "barrelMusketGold", new Component[]{}, new Component[]{}, Components.musket, 0.97F);
	
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