package com.spectral.spectral_guns.render.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.MathWithMultiple;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderShuriken extends Render
{
	private final RenderItem ri;
	
	public RenderShuriken(RenderManager rm, RenderItem ri)
	{
		super(rm);
		this.ri = ri;
	}
	
	public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks)
	{
		if(entity instanceof EntityShuriken)
		{
			doRender((EntityShuriken)entity, x, y, z, f, partialTicks);
		}
	}
	
	public void doRender(EntityShuriken entity, double x, double y, double z, float f, float partialTicks)
	{
		Random rand = entity.worldObj.rand;
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		
		rand.setSeed(entity.getUniqueID().getMostSignificantBits());
		float roll = Randomization.r(1, rand);
		roll *= roll;
		roll *= 30;
		GlStateManager.rotate(90 + roll, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + entity.spin, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 1.0F, 0.0F);
		
		byte b0 = 0;
		float f2 = 0.0F;
		float f3 = 0.5F;
		float f4 = (float)(0 + b0 * 10) / 32.0F;
		float f5 = (float)(5 + b0 * 10) / 32.0F;
		float f6 = 0.0F;
		float f7 = 0.15625F;
		float f8 = (float)(5 + b0 * 10) / 32.0F;
		float f9 = (float)(10 + b0 * 10) / 32.0F;
		float f10 = 0.05625F;
		GlStateManager.enableRescaleNormal();
		float f11 = (float)entity.arrowShake - partialTicks;
		
		if(f11 > 0.0F)
		{
			float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
			GlStateManager.rotate(f12, 0.0F, 0.0F, 1.0F);
		}
		
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		GL11.glNormal3f(f10, 0.0F, 0.0F);
		float scale = 10F;
		GlStateManager.scale(scale, scale, scale);
		
		this.bindTexture(TextureMap.locationBlocksTexture);
		ri.renderItemModel(new ItemStack(M.shuriken));
		
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, f, partialTicks);
	}
	
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return TextureMap.locationBlocksTexture;
	}
}