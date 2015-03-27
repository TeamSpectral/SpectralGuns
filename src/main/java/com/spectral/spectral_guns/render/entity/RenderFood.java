package com.spectral.spectral_guns.render.entity;

import java.util.Random;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff.MathWithMultiple;
import com.spectral.spectral_guns.entity.projectile.EntityFood;

@SideOnly(Side.CLIENT)
public class RenderFood extends Render
{
	private final RenderItem ri;
	
	public RenderFood(RenderManager rm, RenderItem ri)
	{
		super(rm);
		this.ri = ri;
	}
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks)
	{
		if(entity instanceof EntityFood)
		{
			this.doRender((EntityFood)entity, x, y, z, f, partialTicks);
		}
	}
	
	public void doRender(EntityFood entity, double x, double y, double z, float f, float partialTicks)
	{
		Random rand = entity.worldObj.rand;
		GlStateManager.pushMatrix();
		
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		float f2 = (float)MathWithMultiple.distance(entity.height, entity.width);
		GlStateManager.scale(f2, f2, f2);
		GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		
		rand.setSeed(entity.getUniqueID().getMostSignificantBits() / 3);
		if(rand.nextBoolean())
		{
			GlStateManager.scale(-1, 1, 1);
		}
		rand.setSeed(entity.getUniqueID().getMostSignificantBits() * 3);
		if(rand.nextBoolean())
		{
			GlStateManager.scale(1, -1, 1);
		}
		rand.setSeed(entity.getUniqueID().getMostSignificantBits() / 4);
		if(rand.nextBoolean())
		{
			GlStateManager.scale(1, 1, -1);
		}
		
		rand.setSeed(entity.getUniqueID().getMostSignificantBits());
		GlStateManager.rotate(-180 + 90 * rand.nextInt(3), 0, 0, 1);
		rand.setSeed(entity.getUniqueID().getMostSignificantBits() / 2);
		GlStateManager.rotate(180 * rand.nextInt(1), 0, 1, 0);
		rand.setSeed(entity.getUniqueID().getMostSignificantBits() * 2);
		GlStateManager.rotate(180 * rand.nextInt(1), 1, 0, 0);
		
		this.bindTexture(TextureMap.locationBlocksTexture);
		this.ri.renderItemModel(entity.getItemStack());
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, f, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
}