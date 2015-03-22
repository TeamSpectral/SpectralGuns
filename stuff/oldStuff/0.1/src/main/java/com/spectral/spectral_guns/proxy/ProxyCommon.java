package com.spectral.spectral_guns.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.event_handler.HandlerCommon;
import com.spectral.spectral_guns.event_handler.HandlerCommonFML;
import com.spectral.spectral_guns.event_handler.HandlerServer;
import com.spectral.spectral_guns.event_handler.HandlerServerFML;
import com.spectral.spectral_guns.packet.PacketEntityData;
import com.spectral.spectral_guns.packet.PacketKey;
import com.spectral.spectral_guns.packet.PacketPlayerData;

public class ProxyCommon
{
	public void init()
	{
		MinecraftForge.EVENT_BUS.register(new HandlerCommon());
		FMLCommonHandler.instance().bus().register(new HandlerCommonFML());
		
		M.network = NetworkRegistry.INSTANCE.newSimpleChannel(References.MODID + "Packets");
		M.network.registerMessage(PacketKey.Handler.class, PacketKey.class, 0, Side.SERVER);
		M.network.registerMessage(PacketEntityData.Handler.class, PacketEntityData.class, 1, Side.CLIENT);
		M.network.registerMessage(PacketPlayerData.Handler.class, PacketPlayerData.class, 2, Side.CLIENT);
	}
}
