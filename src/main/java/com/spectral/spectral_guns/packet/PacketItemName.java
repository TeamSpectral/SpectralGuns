package com.spectral.spectral_guns.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.apache.commons.lang3.StringUtils;

import com.spectral.spectral_guns.inventory.IContainerItemName;

public class PacketItemName implements IMessage
{
	private String name;
	private Class<? extends Container> clazz;
	
	public PacketItemName()
	{
	}
	
	public PacketItemName(String name, Class<? extends Container> clazz)
	{
		this.name = name;
		this.clazz = null;
		if(clazz != null)
		{
			String s = clazz.getName();
			try
			{
				this.clazz = (Class<? extends Container>)Class.forName(s);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		this.name = compound.getString("name");
		this.clazz = null;
		if(compound.hasKey("class", new NBTTagString().getId()))
		{
			String s = compound.getString("class");
			try
			{
				this.clazz = (Class<? extends Container>)Class.forName(s);
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("name", this.name);
		if(this.clazz != null)
		{
			compound.setString("class", this.clazz.getName());
		}
		ByteBufUtils.writeTag(buf, compound);
	}
	
	public static class Handler implements IMessageHandler<PacketItemName, IMessage>
	{
		public Handler()
		{
		}
		
		@Override
		public IMessage onMessage(PacketItemName message, MessageContext context)
		{
			EntityPlayer player = context.getServerHandler().playerEntity;
			if(player.openContainer instanceof IContainerItemName)
			{
				if(message.clazz == null || message.clazz.isInstance(player.openContainer))
				{
					if(!StringUtils.isBlank(message.name))
					{
						if(message.name.length() <= 30)
						{
							((IContainerItemName)player.openContainer).updateItemName(message.name);
						}
					}
					else
					{
						((IContainerItemName)player.openContainer).updateItemName("");
					}
				}
			}
			return null;
		}
	}
}
