package com.spectral.spectral_guns.event;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.packet.PacketPlayerData;

public class HandlerCommonFML extends HandlerBase
{
	// fml events for both sides here!
	
	public static HashMap<EntityPlayer, NBTTagCompound> playerDeathData = new HashMap<EntityPlayer, NBTTagCompound>();
	
	@SubscribeEvent
	public void playerUpdateEvent(PlayerTickEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);
		
		if(event.side == Side.SERVER)
		{
			int i = 10;
			if(event.player instanceof EntityPlayerMP && event.player.worldObj.getWorldTime() % i == i - 1)
			{
				M.network.sendTo(new PacketPlayerData(event.player), (EntityPlayerMP)event.player);
			}
			
			/** makes it so that players keep certain data upon death **/
			if(playerDeathData.get(event.player) != null && event.player.getHealth() > 0)
			{
				props.loadNBTData(playerDeathData.get(event.player), false);
				playerDeathData.remove(event.player);
			}
			if(playerDeathData.get(event.player) == null && event.player.getHealth() <= 0 || event.player.deathTime > 0)
			{
				NBTTagCompound playerData = new NBTTagCompound();
				props.saveNBTData(playerData, false);
				playerDeathData.put(event.player, playerData);
			}
		}
		
		props.update();
	}
	
	@SubscribeEvent
	public void OnConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.modID.equals(References.MODID))
		{
			for(int i = 0; i < Config.entries.size(); ++i)
			{
				Config.entries.get(i).set(Config.config);
			}
			if(Config.config.hasChanged())
			{
				Config.config.save();
			}
		}
	}
}
