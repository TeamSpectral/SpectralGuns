package com.spectral.spectral_guns.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.spectral.spectral_guns.event.HandlerServer;
import com.spectral.spectral_guns.event.HandlerServerFML;

public class ProxyServer extends ProxyCommon
{
	@Override
	public void preInit()
	{
		super.preInit();
		
		MinecraftForge.EVENT_BUS.register(new HandlerServer());
		FMLCommonHandler.instance().bus().register(new HandlerServerFML());
	}
	
	@Override
	public void init()
	{
		super.init();
	}
	
	@Override
	public World world(int dimension)
	{
		return MinecraftServer.getServer().worldServers[dimension];
	}
}
