package com.spectral.spectral_guns.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.items.ItemBase;

public class HandlerCommonFML extends HandlerBase
{
	//fml events for both sides here!
	
	//currently unused, but is needed to fix an annoying error in the log
	@SubscribeEvent
	public void playerUpdateEvent(PlayerTickEvent event)
	{
		
	}
}
