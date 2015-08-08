package com.spectral.spectral_guns.proxy;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.M.Id;
import com.spectral.spectral_guns.VersionChecker;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;
import com.spectral.spectral_guns.entity.projectile.EntityFireball2;
import com.spectral.spectral_guns.event.HandlerClient;
import com.spectral.spectral_guns.event.HandlerClientFML;
import com.spectral.spectral_guns.render.entity.RenderFood;
import com.spectral.spectral_guns.render.entity.RenderNull;
import com.spectral.spectral_guns.render.entity.RenderShuriken;
import com.spectral.spectral_guns.render.tileentity.TileEntityGunWorkbenchRenderer;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class ProxyClient extends ProxyCommon
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		
		MinecraftForge.EVENT_BUS.register(new HandlerClient());
		FMLCommonHandler.instance().bus().register(new HandlerClientFML());
		HandlerClientFML.init();
		VersionChecker.addUpdate();
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		this.registerItemModels(ri);
		this.tileEntityRender(rm, ri);
		this.entityRender(rm, ri);
	}
	
	@Override
	public World world(int dimension)
	{
		return Minecraft.getMinecraft().theWorld;
	}
	
	@Override
	public Side side()
	{
		return Side.CLIENT;
	}
	
	@Override
	public EntityPlayer player()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	
	private void tileEntityRender(RenderManager rm, RenderItem ri)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGunWorkbench.class, new TileEntityGunWorkbenchRenderer());
	}
	
	private void entityRender(RenderManager rm, RenderItem ri)
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityFireball2.class, new RenderFireball(rm, 0.3F));
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