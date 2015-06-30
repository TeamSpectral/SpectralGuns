package com.spectral.spectral_guns.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.items.ItemGun;

public class PacketKey implements IMessage
{
	public static enum Key
	{
		RIGHTCLICK(0),
		
		NOTRIGHTCLICK(1),
		
		RELOAD(2),
		
		EJECT(3),
		
		ZOOM(4),
		
		NOTZOOM(5);
		
		public final int id;
		
		private Key(int id)
		{
			this.id = id;
		}
		
		public static Key get(int id)
		{
			for(int i = 0; i < Key.values().length; ++i)
			{
				if(Key.values()[i].id == id)
				{
					return Key.values()[i];
				}
			}
			return null;
		}
	}
	
	private int i;
	
	public PacketKey()
	{
	}
	
	public PacketKey(Key k)
	{
		this.i = k.id;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.i = buf.readInt();
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.i);
	}
	
	public static class Handler implements IMessageHandler<PacketKey, IMessage>
	{
		public Handler()
		{
		}
		
		@Override
		public IMessage onMessage(PacketKey message, MessageContext context)
		{
			EntityPlayer player = context.getServerHandler().playerEntity;
			return onMessage(message, player);
		}
		
		public static IMessage onMessage(PacketKey message, EntityPlayer player)
		{
			if(player == null)
			{
				return null;
			}
			EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
			Key k = Key.get(message.i);
			switch(k)
			{
			case RIGHTCLICK:
			{
				if(props != null)
				{
					props.setRightClick(true);
				}
				break;
			}
			case NOTRIGHTCLICK:
			{
				if(props != null)
				{
					props.setRightClick(false);
				}
				break;
			}
			case RELOAD:
			{
				if(props.reloadDelay <= props.maxReloadDelay)
				{
					if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemGun)
					{
						ItemStack stack = player.getHeldItem();
						if(ComponentEvents.reload(stack, player))
						{
							stack.getTagCompound().setInteger(ItemGun.FIRERATETIMER, 20);
							props.reloadDelay = props.maxReloadDelay;
						}
					}
				}
				break;
			}
			case EJECT:
			{
				if(props.reloadDelay <= props.maxReloadDelay)
				{
					if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemGun)
					{
						ItemStack stack = player.getHeldItem();
						if(ComponentEvents.eject(stack, player))
						{
							props.reloadDelay = props.maxReloadDelay;
						}
					}
				}
				break;
			}
			case ZOOM:
			{
				if(props != null)
				{
					props.isZoomHeldDown = true;
				}
			}
			case NOTZOOM:
			{
				if(props != null)
				{
					props.isZoomHeldDown = false;
				}
			}
			}
			return null;
		}
	}
}
