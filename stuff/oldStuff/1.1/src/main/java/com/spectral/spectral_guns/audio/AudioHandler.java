package com.spectral.spectral_guns.audio;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AudioHandler
{
	@SideOnly(Side.CLIENT)
	public static HashMap<Entity, HashMap<String, ArrayList<ISound>>> sounds = new HashMap<Entity, HashMap<String, ArrayList<ISound>>>();

	public static ISound getSound(Entity entity, String string)
	{
		if(sounds.containsKey(entity) && sounds.get(entity) != null)
		{
			for(int i = 0; i < sounds.get(entity).get(string).size(); ++i)
			{
				ISound sound = sounds.get(entity).get(string).get(i);
				if(sounds.get(entity).get(string).get(i) != null)
				{
					if(!(sound instanceof MovingSound) || !((MovingSound)sound).isDonePlaying())
					{
						return sound;
					}
				}
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public static void createMovingEntitySound(Entity entity, String string, float volume, float pitch, boolean repeat)
	{
		ISound sound = new MovingSoundEntityGeneric(entity, string, volume, pitch, repeat);

		createSound(entity, sound);
	}

	@SideOnly(Side.CLIENT)
	public static void createSound(Entity entity, ISound sound)
	{
		String string = sound.getSoundLocation().getResourcePath();
		if(sounds.get(entity) == null)
		{
			sounds.put(entity, new HashMap<String, ArrayList<ISound>>());
		}
		if(sounds.get(entity).get(string) == null)
		{
			sounds.get(entity).put(string, new ArrayList<ISound>());
		}
		sounds.get(entity).get(string).add(sound);
		Minecraft.getMinecraft().getSoundHandler().playSound(sound);
	}

	@SideOnly(Side.CLIENT)
	public static void stopSound(Entity entity, String string)
	{
		ISound sound = getSound(entity, string);
		if(sound != null)
		{
			Minecraft.getMinecraft().getSoundHandler().stopSound(sound);
			if(sound instanceof MovingSoundPublic)
			{
				((MovingSoundPublic)sound).done();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static boolean isPlaying(Entity entity, String string)
	{
		ISound sound = getSound(entity, string);
		if(sound == null)
		{
			return false;
		}
		if(sound instanceof MovingSound && ((MovingSound)sound).isDonePlaying())
		{
			return false;
		}
		return true;
	}

	@SideOnly(Side.CLIENT)
	public static void onUpdate()
	{
		for(int i = 0; i < sounds.keySet().size(); ++i)
		{
			Object k1 = sounds.keySet().toArray()[i];
			HashMap<String, ArrayList<ISound>> sounds2 = sounds.get(k1);
			for(int i1 = 0; i1 < sounds2.keySet().size(); ++i1)
			{
				Object k2 = sounds2.keySet().toArray()[i];
				ArrayList<ISound> array = sounds2.get(k2);
				for(int i2 = 0; i2 < array.size(); ++i2)
				{
					if(array.get(i2) instanceof MovingSoundPublic)
					{
						MovingSoundPublic sound = (MovingSoundPublic)array.get(i2);
						if(sound.isDonePlaying())
						{
							sound.done();
							array.remove(i2);
						}
					}
				}
				if(array.size() <= 0)
				{
					sounds2.remove(k2);
				}
			}
			if(sounds2.size() <= 0)
			{
				sounds.remove(k1);
			}
		}
	}
}
