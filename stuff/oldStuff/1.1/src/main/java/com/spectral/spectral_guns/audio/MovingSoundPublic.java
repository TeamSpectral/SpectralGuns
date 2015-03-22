package com.spectral.spectral_guns.audio;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class MovingSoundPublic extends MovingSound
{
	protected MovingSoundPublic(ResourceLocation resource)
	{
		super(resource);
	}

	public Entity entity;
    
    public void setVolume(float volume)
    {
    	this.volume = volume;
    }
    
    public void setPitch(float pitch)
    {
    	this.pitch = pitch;
    }
    
    public void setRepeat(boolean repeat)
    {
    	this.repeat = repeat;
    }
    
    public void done()
    {
    	this.donePlaying = true;
    }
}
