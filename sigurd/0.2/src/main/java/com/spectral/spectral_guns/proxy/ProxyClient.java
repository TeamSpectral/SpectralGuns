package com.spectral.spectral_guns.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.spectral.spectral_guns.entity.projectile.EntitySmallFireballSpecial;
import com.spectral.spectral_guns.event.HandlerClient;
import com.spectral.spectral_guns.event.HandlerClientFML;

public class ProxyClient extends ProxyCommon
{
	@Override
	public void init()
	{
		super.init();

		//to be used for item rendering:
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		
		MinecraftForge.EVENT_BUS.register(new HandlerClient());
		FMLCommonHandler.instance().bus().register(new HandlerClientFML());
		HandlerClientFML.init();
		entityRender(renderManager);
	}
	
	public void entityRender(RenderManager rm)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallFireballSpecial.class, new RenderFireball(rm, 1.3F));
	}
}