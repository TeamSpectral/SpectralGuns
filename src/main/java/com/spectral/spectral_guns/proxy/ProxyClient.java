package com.spectral.spectral_guns.proxy;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.M.Id;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;
import com.spectral.spectral_guns.entity.projectile.EntitySmallFireball2;
import com.spectral.spectral_guns.event.HandlerClient;
import com.spectral.spectral_guns.event.HandlerClientFML;
import com.spectral.spectral_guns.render.entity.RenderFood;
import com.spectral.spectral_guns.render.entity.RenderNull;
import com.spectral.spectral_guns.render.entity.RenderShuriken;
import com.sun.xml.internal.bind.v2.model.core.ID;

public class ProxyClient extends ProxyCommon
{
	@Override
	public void preInit()
	{
		super.preInit();

		MinecraftForge.EVENT_BUS.register(new HandlerClient());
		FMLCommonHandler.instance().bus().register(new HandlerClientFML());
		HandlerClientFML.init();
	}

	@Override
	public void init()
	{
		super.init();

		RenderItem ri = Minecraft.getMinecraft().getRenderItem();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		registerItemModels(ri);
		entityRender(rm, ri);
	}

	private void entityRender(RenderManager rm, RenderItem ri)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmallFireball2.class, new RenderFireball(rm, 1.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityFood.class, new RenderFood(rm, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityShuriken.class, new RenderShuriken(rm, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityLaser.class, new RenderNull(rm));
	}

	private void registerItemModels(RenderItem ri)
	{
		Iterator<Id> ids = M.getIds();
		while(ids.hasNext())
		{
			Id id = ids.next();
			if(id != null)
			{
				Object item = M.getItem(id);
				if(item != null && item instanceof Block)
				{
					ri.getItemModelMesher().register(Item.getItemFromBlock((Block)item), 0, new ModelResourceLocation(id.mod.toLowerCase() + ":" + id.id.toLowerCase(), "inventory"));
				}
				else if(item != null && item instanceof Item)
				{
					ri.getItemModelMesher().register((Item)item, 0, new ModelResourceLocation(id.mod.toLowerCase() + ":" + id.id.toLowerCase(), "inventory"));
				}
			}
		}
	}
}