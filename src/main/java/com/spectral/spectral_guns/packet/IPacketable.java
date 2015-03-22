package com.spectral.spectral_guns.packet;

import io.netty.buffer.ByteBuf;

public interface IPacketable
{
	public void fromBytes(ByteBuf buf);
	
	public void toBytes(ByteBuf buf);
}
