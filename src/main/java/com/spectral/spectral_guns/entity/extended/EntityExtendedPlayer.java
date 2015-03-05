package com.spectral.spectral_guns.entity.extended;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.spectral.spectral_guns.References;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.brewing.PotionBrewedEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityExtendedPlayer implements IExtendedEntityProperties, IEntityAdditionalSpawnData
{
	public float snow = 0;
	public boolean isRightClickHeldDown = false;
	public void setRightClick(boolean b)
	{
		isRightClickHeldDownLast = isRightClickHeldDown;
		isRightClickHeldDown = b;
	}
	public boolean isRightClickHeldDownLast = false;
	public boolean isZoomHeldDown = false;
	public int reloadDelay = 0;

	public final static String PROP = References.MODID;
	public final static String SNOW = "Snow";
	public final static String RIGHTCLICK = "RightClick";
	public final static String RIGHTCLICKLAST = "RightClickLast";
	public final static String ZOOM = "Zoom";
	public final static int maxReloadDelay = 8;

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
	 * Used to register these extended properties for the player during EntityConstructing event
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
		isRightClickHeldDown = properties.getBoolean(RIGHTCLICK);
		isRightClickHeldDownLast = properties.getBoolean(RIGHTCLICKLAST);
		isZoomHeldDown = properties.getBoolean(ZOOM);

		if(b)
		{
			snow = properties.getFloat(SNOW);
		}
		capSnow();
	}

	protected float capSnow()
	{
		if(snow < 0)
		{
			snow = 0;
		}
		if(snow > 7)
		{
			snow = 7;
		}
		return snow;
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

		properties.setBoolean(ZOOM, isZoomHeldDown);
		properties.setBoolean(RIGHTCLICK, isRightClickHeldDown);
		properties.setBoolean(RIGHTCLICKLAST, isRightClickHeldDownLast);

		if(b)
		{
			properties.setFloat(SNOW, snow);
		}
	}

	public void update()
	{
		updateSnow();
		if(isZoomHeldDown)
		{
			float slow = 0.8F;
			player.motionX *= slow;
			player.motionY *= slow;
			player.motionZ *= slow;
		}
		reloadDelay = Math.min(maxReloadDelay, Math.max(0, reloadDelay-1));
	}

	public void updateSnow()
	{
		if(snow > 0)
		{
			snow -= 0.01F;
			if(player.worldObj.isRemote)
			{
				snow += 0;
			}
		}
		capSnow();
	}

	public void snowball()
	{
		snow += 1.4F + player.worldObj.rand.nextFloat();
	}

	@Override
	public void init(Entity entity, World world) {}

	/**
	 * Called by the server when constructing the spawn packet.
	 * Data should be added to the provided stream.
	 *
	 * @param buffer The packet data stream
	 */
	public void writeSpawnData(ByteBuf buf)
	{
		NBTTagCompound compound = new NBTTagCompound();
		saveNBTData(compound);
		ByteBufUtils.writeTag(buf, compound);
	}

	/**
	 * Called by the client when it receives a Entity spawn packet.
	 * Data should be read out of the stream in the same way as it was written.
	 *
	 * @param data The packet data stream
	 */
	public void readSpawnData(ByteBuf buf)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(buf);
		if(compound != null)
		{
			loadNBTData(compound);
		}
	}
}