package com.spectral.spectral_guns.packet;

import com.spectral.spectral_guns.items.ItemBase;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.M;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketKey implements IMessage
{
	private int i;

	public PacketKey() {}

	public PacketKey(int i)
	{
		this.i = i;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		i = ByteBufUtils.readVarInt(buf, 1);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeVarInt(buf, i, 1);
	}

	public static class Handler implements IMessageHandler<PacketKey, IMessage>
	{
		@Override
		public IMessage onMessage(PacketKey message, MessageContext context)
		{
			int i = message.i;
			EntityPlayer player = context.getServerHandler().playerEntity;
			//right click
			if(i == 0)
			{
				if(player.getHeldItem() != null && player.getHeldItem().getTagCompound() != null)
				{
					ItemStack stack = player.getHeldItem();
					stack.getTagCompound().setBoolean(ItemBase.RIGHTCLICKEDLAST, stack.getTagCompound().getBoolean(ItemBase.RIGHTCLICKED));
					if(stack.getItem() instanceof ItemGun)
					{
						//ItemGun.onItemRightClick2(stack, player.worldObj, player);
					}
					stack.getTagCompound().setBoolean(ItemBase.RIGHTCLICKED, true);
				}
			}
			//not right click
			else if(i == 1)
			{
				if(player.getHeldItem() != null && player.getHeldItem().getTagCompound() != null)
				{
					ItemStack stack = player.getHeldItem();
					stack.getTagCompound().setBoolean(ItemBase.RIGHTCLICKEDLAST, stack.getTagCompound().getBoolean(ItemBase.RIGHTCLICKED));
					
					stack.getTagCompound().setBoolean(ItemBase.RIGHTCLICKED, false);
				}
			}
			return null;
		}
	}
}
