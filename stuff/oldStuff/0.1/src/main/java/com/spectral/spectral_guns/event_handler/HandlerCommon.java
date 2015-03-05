package com.spectral.spectral_guns.event_handler;

import java.util.List;

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntitySnowballDamaging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerCommon extends HandlerBase
{
	//minecraftforge events for both sides here!

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if(event.entity instanceof EntityPlayer && EntityExtendedPlayer.get((EntityPlayer) event.entity) == null)
		{
			EntityExtendedPlayer.register((EntityPlayer)event.entity);
		}

		if (event.entity instanceof EntityPlayer && event.entity.getExtendedProperties(EntityExtendedPlayer.PROP) == null)
		{
			event.entity.registerExtendedProperties(EntityExtendedPlayer.PROP, new EntityExtendedPlayer((EntityPlayer) event.entity));
		}
	}

	@SubscribeEvent
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityExtendedPlayer propsP = EntityExtendedPlayer.get((EntityPlayer)event.entity);
			propsP.update();
		}
	}

	@SubscribeEvent
	public void livingHurtEvent(LivingHurtEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityExtendedPlayer propsP = EntityExtendedPlayer.get((EntityPlayer)event.entity);
			Entity e = event.source.getEntity();
			if(e instanceof EntitySnowball || true)
			{
				int i = 1;
				if(e instanceof EntitySnowballDamaging)
				{
					i = (int)Math.floor(((EntitySnowballDamaging)e).damage);
				}
				for(int i2 = 0; i2 < i; ++i2)
				{
					propsP.snowball();
				}
			}
		}
	}
}
