package com.spectral.spectral_guns.entity.extended;

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityExtendedPlayer implements IExtendedEntityProperties
{
	public float snow = 0;
	public boolean isRightClickHeldDown = false;

	public final static String PROP = References.MODID;
	public final static String SNOW = "Snow";

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
		
		if(b)
		{
			snow = properties.getFloat(SNOW);
			snow = 7;
		}
		capSnow();
	}

	@SideOnly(Side.CLIENT)
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
		
		if(b)
		{
			properties.setFloat(SNOW, snow);
		}
	}

	public void update()
	{
		updateSnow();
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
}