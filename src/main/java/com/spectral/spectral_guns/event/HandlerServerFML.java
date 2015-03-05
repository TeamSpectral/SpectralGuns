package com.spectral.spectral_guns.event;

import java.util.HashMap;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class HandlerServerFML extends HandlerCommonFML
{
	//fml events for server only here!

	public static HashMap<EntityPlayer, NBTTagCompound> playerDeathData = new HashMap<EntityPlayer, NBTTagCompound>();

	@SubscribeEvent
	public void playerUpdateEvent(PlayerTickEvent event)
	{
		EntityExtendedPlayer props = EntityExtendedPlayer.get(event.player);

		/**makes it so that players keep certain data upon death**/
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
		
		/**give data to client**/
		if(event.player instanceof EntityPlayerMP)
		{
			NBTTagCompound compound = new NBTTagCompound();
			props.saveNBTData(compound);
			//M.network.sendTo(new PacketPlayerData(compound), (EntityPlayerMP)event.player);
		}
	}
}
