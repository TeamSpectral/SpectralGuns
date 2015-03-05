package com.spectral.spectral_guns.render.entity;

import java.util.Random;

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.MathWithMultiple;
import com.spectral.spectral_guns.entity.projectile.EntityFood;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderNull extends Render
{
	/**
	 * just to get rid of the annoying white boxes
	 * @param rm
	 */
	public RenderNull(RenderManager rm)
	{
		super(rm);
	}

	public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks)
	{
		
	}

	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
}