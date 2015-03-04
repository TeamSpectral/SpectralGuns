package com.spectral.spectral_guns.packet;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketPlayerData implements IMessage
{
	protected NBTTagCompound compound;

	public PacketPlayerData() {}

	public PacketPlayerData(NBTTagCompound compound)
	{
		this.compound = new NBTTagCompound();
		this.compound.setTag("Props", compound);
		this.compound.setString("PropsID", EntityExtendedPlayer.PROP);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		compound = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, compound);
	}

	public static class Handler implements IMessageHandler<PacketPlayerData, IMessage>
	{
		@Override
		public IMessage onMessage(PacketPlayerData message, MessageContext context)
		{
			if(context.side == Side.CLIENT)
			{
				stuffAndThings(message);
			}
			return null;
		}
		
		@SideOnly(Side.CLIENT)
		protected void stuffAndThings(PacketPlayerData message)
		{
			Entity entity = Minecraft.getMinecraft().thePlayer;
			loadProps(message, entity);
		}
		
		protected void loadProps(PacketPlayerData message, Entity entity)
		{
			if(entity instanceof EntityPlayer)
			{
				IExtendedEntityProperties p = entity.getExtendedProperties(message.compound.getString("PropsID"));
				if(p != null)
				{
					p.loadNBTData(message.compound.getCompoundTag("Props"));
				}
			}
		}
	}
}
