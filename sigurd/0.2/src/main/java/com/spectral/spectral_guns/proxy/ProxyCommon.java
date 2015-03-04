package com.spectral.spectral_guns.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
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

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.event.HandlerCommon;
import com.spectral.spectral_guns.event.HandlerCommonFML;
import com.spectral.spectral_guns.event.HandlerServer;
import com.spectral.spectral_guns.event.HandlerServerFML;
import com.spectral.spectral_guns.packet.PacketEntityData;
import com.spectral.spectral_guns.packet.PacketKey;
import com.spectral.spectral_guns.packet.PacketPlayerData;
import com.spectral.spectral_guns.recipe.RecipeGun;

public class ProxyCommon
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new HandlerCommon());
		FMLCommonHandler.instance().bus().register(new HandlerCommonFML());

		packets();
		recipes();
		entities();
	}
	
	private void packets()
	{
		M.network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID + "Packets");
		M.network.registerMessage(PacketKey.Handler.class, PacketKey.class, 0, Side.SERVER);
		M.network.registerMessage(PacketEntityData.Handler.class, PacketEntityData.class, 1, Side.CLIENT);
		M.network.registerMessage(PacketPlayerData.Handler.class, PacketPlayerData.class, 2, Side.CLIENT);
	}
	
	private void recipes()
	{
		GameRegistry.addRecipe(new RecipeGun());
		registerNugget(M.iron_nugget, Items.iron_ingot);
		ComponentEvents.registerRecipes();
		GameRegistry.addShapedRecipe(new ItemStack(M.container), new Object[]{"OTO", "G G", "OOO", 'O', Item.getItemFromBlock(Blocks.obsidian), 'T', Item.getItemFromBlock(Blocks.iron_trapdoor), 'G', Item.getItemFromBlock(Blocks.glass_pane)});
	}
	
	private void registerNugget(Item nugget, Item bar)
	{
		GameRegistry.addShapedRecipe(new ItemStack(bar, 1), new Object[]{"NNN", "NNN", "NNN", 'N', nugget});
		GameRegistry.addShapedRecipe(new ItemStack(nugget, 9), new Object[]{"B", 'B', bar});
	}
	
	private void entities()
	{
		M.registerEntityNoEgg(EntityFood.class, "foodProjectile", 0);
	}
}
