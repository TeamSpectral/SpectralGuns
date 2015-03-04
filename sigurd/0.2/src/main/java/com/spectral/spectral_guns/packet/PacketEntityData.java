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

public class PacketEntityData implements IMessage
{
	protected NBTTagCompound compound;

	public PacketEntityData() {}

	public PacketEntityData(NBTTagCompound compound, UUID uuid, String id)
	{
		this.compound = new NBTTagCompound();
		this.compound.setTag("Props", compound);
		this.compound.setString("PropsID", id);
		this.compound.setLong("UUIDMost", uuid.getMostSignificantBits());
		this.compound.setLong("UUIDLeast", uuid.getLeastSignificantBits());
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

	public static class Handler implements IMessageHandler<PacketEntityData, IMessage>
	{
		@Override
		public IMessage onMessage(PacketEntityData message, MessageContext context)
		{
			if(context.side == Side.CLIENT)
			{
				stuffAndThings(message);
			}
			return null;
		}

		@SideOnly(Side.CLIENT)
		protected void stuffAndThings(PacketEntityData message)
		{
			Entity entity = null;
			World[] worlds = DimensionManager.getWorlds();
			UUID uuid = new UUID(message.compound.getLong("UUIDMost"), message.compound.getLong("UUIDLeast"));
			for(int i = 0; i < worlds.length; ++i)
			{
				List entitylist = worlds[i].playerEntities;
				for(int i2 = 0; i2 < entitylist.size() && entity == null; ++i2)
				{
					if(entitylist.get(i2) instanceof Entity && ((Entity)entitylist.get(i2)).getUniqueID().equals(uuid))
					{
						entity = (Entity)entitylist.get(i2);
						break;
					}
				}
				entitylist = worlds[i].loadedEntityList;
				for(int i2 = 0; i2 < entitylist.size() && entity == null; ++i2)
				{
					if(entitylist.get(i2) instanceof Entity && ((Entity)entitylist.get(i2)).getUniqueID().equals(uuid))
					{
						entity = (Entity)entitylist.get(i2);
						break;
					}
				}
			}
			
			if(entity == null)
			{
				entity = Minecraft.getMinecraft().thePlayer;
			}
		}
		
		protected void loadProps(PacketEntityData message, Entity entity)
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
