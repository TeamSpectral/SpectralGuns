package com.spectral.spectral_guns.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class HandlerServerFML extends HandlerCommonFML
{
	// fml events for server only here!
	
	@Override
	@SubscribeEvent
	public void playerUpdateEvent(PlayerTickEvent event)
	{
	}
}
