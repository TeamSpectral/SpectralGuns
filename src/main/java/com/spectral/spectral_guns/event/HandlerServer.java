package com.spectral.spectral_guns.event;

import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class HandlerServer extends HandlerBase
{
	// minecraftforge events for server only here!
	
	@SubscribeEvent
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
	}
}
