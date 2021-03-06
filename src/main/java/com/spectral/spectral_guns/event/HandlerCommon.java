package com.spectral.spectral_guns.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntitySnowball2;
import com.spectral.spectral_guns.items.ItemGun;

public class HandlerCommon extends HandlerBase
{
	// minecraftforge events for both sides here!
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if(event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer)event.entity) == null)
		{
			ExtendedPlayer.register((EntityPlayer)event.entity);
		}
		
		if(event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(ExtendedPlayer.PROP) == null)
		{
			event.entity.registerExtendedProperties(ExtendedPlayer.PROP, new ExtendedPlayer((EntityPlayer)event.entity));
		}
	}
	
	@SubscribeEvent
	public void livingHurtEvent(LivingHurtEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			ExtendedPlayer propsP = ExtendedPlayer.get((EntityPlayer)event.entity);
			Entity e = event.source.getEntity();
			if(e instanceof EntitySnowball && !(e instanceof EntitySnowball2))
			{
				propsP.snowball();
			}
		}
	}
	
	@SubscribeEvent
	public void FOVUpdateEvent(FOVUpdateEvent event)
	{
		if(M.proxy.side() == Side.CLIENT)
		{
			if(event.entity.getHeldItem() != null)
			{
				ItemStack itemstack = event.entity.getHeldItem();
				ExtendedPlayer props = ExtendedPlayer.get(event.entity);
				if(itemstack.getItem() instanceof ItemGun && props.isZoomHeldDown && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().gameSettings.thirdPersonView == 1))
				{
					event.newfov = 1;
					event.newfov -= props.getZoom() * (ItemGun.zoom(itemstack, event.entity, 1) - 1) / 3 + 0.05;
				}
			}
		}
	}
}
