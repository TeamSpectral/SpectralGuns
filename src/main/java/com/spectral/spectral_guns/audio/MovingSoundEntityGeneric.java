package com.spectral.spectral_guns.audio;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public class MovingSoundEntityGeneric extends MovingSoundPublic
{
    public MovingSoundEntityGeneric(Entity entity, String sound, float volume, float pitch, boolean repeat)
    {
        super(new ResourceLocation(sound));
        this.entity = entity;
        this.volume = volume;
        this.repeat = repeat;
        this.pitch = pitch;
    }

    /**
     * Updates the JList with a new model.
     */
    @Override
	public void update()
    {
        if (this.entity.isDead)
        {
            this.donePlaying = true;
        }
        else
        {
            this.xPosF = (float)this.entity.posX;
            this.yPosF = (float)this.entity.posY;
            this.zPosF = (float)this.entity.posZ;
        }
    }
}