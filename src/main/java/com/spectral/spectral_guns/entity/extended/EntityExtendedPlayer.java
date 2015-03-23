package com.spectral.spectral_guns.entity.extended;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.spectral.spectral_guns.References;

public class EntityExtendedPlayer implements IExtendedEntityProperties, IEntityAdditionalSpawnData
{
	public float snow = 0;
	public boolean isRightClickHeldDown = false;
	
	public void setRightClick(boolean b)
	{
		this.isRightClickHeldDownLast = this.isRightClickHeldDown;
		this.isRightClickHeldDown = b;
	}
	
	public boolean isRightClickHeldDownLast = false;
	public boolean isZoomHeldDown = false;
	public int reloadDelay = 0;
	
	public final static String PROP = References.MODID;
	public final static String SNOW = "Snow";
	public final static String RIGHTCLICK = "RightClick";
	public final static String RIGHTCLICKLAST = "RightClickLast";
	public final static String ZOOM = "Zoom";
	public final static int maxReloadDelay = 32;
	
	private final EntityPlayer player;
	
	public EntityExtendedPlayer(EntityPlayer player)
	{
		this.player = player;
	}
	
	/**
	 * Returns ExtendedPlayer properties for player
	 * This method is for convenience only; it will make your code look nicer
	 */
	public static final EntityExtendedPlayer get(EntityPlayer player)
	{
		return (EntityExtendedPlayer)player.getExtendedProperties(PROP);
	}
	
	/**
	 * Used to register these extended properties for the player during
	 * EntityConstructing event
	 * This method is for convenience only; it makes my code look nicer.
	 */
	public static final void register(EntityPlayer player)
	{
		player.registerExtendedProperties(EntityExtendedPlayer.PROP, new EntityExtendedPlayer(player));
	}
	
	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		this.loadNBTData(compound, true);
	}
	
	public void loadNBTData(NBTTagCompound compound, boolean b)
	{
		NBTTagCompound properties = (NBTTagCompound)compound.getTag(PROP);
		if(properties == null)
		{
			return;
		}
		this.isRightClickHeldDown = properties.getBoolean(RIGHTCLICK);
		this.isRightClickHeldDownLast = properties.getBoolean(RIGHTCLICKLAST);
		this.isZoomHeldDown = properties.getBoolean(ZOOM);
		
		if(b)
		{
			this.snow = properties.getFloat(SNOW);
		}
		this.capSnow();
	}
	
	protected float capSnow()
	{
		if(this.snow < 0)
		{
			this.snow = 0;
		}
		if(this.snow > 7)
		{
			this.snow = 7;
		}
		return this.snow;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		this.saveNBTData(compound, true);
	}
	
	public void saveNBTData(NBTTagCompound compound, boolean b)
	{
		NBTTagCompound properties = new NBTTagCompound();
		compound.setTag(PROP, properties);
		
		properties.setBoolean(ZOOM, this.isZoomHeldDown);
		properties.setBoolean(RIGHTCLICK, this.isRightClickHeldDown);
		properties.setBoolean(RIGHTCLICKLAST, this.isRightClickHeldDownLast);
		
		if(b)
		{
			properties.setFloat(SNOW, this.snow);
		}
	}
	
	public void update()
	{
		this.updateSnow();
		if(this.isZoomHeldDown)
		{
			float slow = 0.8F;
			this.player.motionX *= slow;
			this.player.motionY *= slow;
			this.player.motionZ *= slow;
		}
		this.reloadDelay = Math.min(maxReloadDelay, Math.max(0, this.reloadDelay - 1));
	}
	
	public void updateSnow()
	{
		if(this.snow > 0)
		{
			this.snow -= 0.01F;
			if(this.player.worldObj.isRemote)
			{
				this.snow += 0;
			}
		}
		this.capSnow();
	}
	
	public void snowball()
	{
		this.snow += 1.4F + this.player.worldObj.rand.nextFloat();
	}
	
	@Override
	public void init(Entity entity, World world)
	{
	}
	
	/**
	 * Called by the server when constructing the spawn packet.
	 * Data should be added to the provided stream.
	 *
	 * @param buffer
	 *            The packet data stream
	 */
	@Override
	public void writeSpawnData(ByteBuf buf)
	{
		buf.writeFloat(this.snow);
		buf.writeBoolean(this.isRightClickHeldDown);
		buf.writeBoolean(this.isRightClickHeldDownLast);
		buf.writeBoolean(this.isZoomHeldDown);
		buf.writeInt(this.reloadDelay);
	}
	
	/**
	 * Called by the client when it receives a Entity spawn packet.
	 * Data should be read out of the stream in the same way as it was written.
	 *
	 * @param data
	 *            The packet data stream
	 */
	@Override
	public void readSpawnData(ByteBuf buf)
	{
		this.snow = buf.readFloat();
		this.isRightClickHeldDown = buf.readBoolean();
		this.isRightClickHeldDownLast = buf.readBoolean();
		this.isZoomHeldDown = buf.readBoolean();
		this.reloadDelay = buf.readInt();
	}
}