package com.spectral.spectral_guns.event;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class HandlerServer extends HandlerCommon
{
	//minecraftforge events for server only here!

	@SubscribeEvent
	public void livingUpdateEvent(LivingUpdateEvent event)
	{
		if(event.entity != null && event.entity instanceof EntityPlayerMP)
		{
			EntityExtendedPlayer propsP = EntityExtendedPlayer.get((EntityPlayerMP)event.entity);
			NBTTagCompound compound = new NBTTagCompound();
			propsP.loadNBTData(compound);
			//M.network.sendTo(new PacketPlayerData(compound), (EntityPlayerMP)event.entity);
		}
	}
}
