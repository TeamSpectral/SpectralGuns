package com.spectral.spectral_guns.event;

import java.util.List;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntitySnowball2;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.FOVUpdateEvent;
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
				if(e instanceof EntitySnowball2)
				{
					i = (int)Math.floor(((EntitySnowball2)e).damage);
				}
				for(int i2 = 0; i2 < i; ++i2)
				{
					propsP.snowball();
				}
			}
		}
	}

	@SubscribeEvent
	public void FOVUpdateEvent(FOVUpdateEvent event)
	{
		if(event.entity instanceof EntityPlayer)
		{
			EntityPlayer player = event.entity;
			if(player.getHeldItem() != null)
			{
				ItemStack itemstack = player.getHeldItem();
				EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
				if(itemstack.getItem() instanceof ItemGun && props.isZoomHeldDown && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().gameSettings.thirdPersonView == 1))
				{
					event.newfov -= (ItemGun.zoom(itemstack, player, 1)-1)/3+0.05;
				}
			}
		}
	}
}
