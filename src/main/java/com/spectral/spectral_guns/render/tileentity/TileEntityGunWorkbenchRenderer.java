package com.spectral.spectral_guns.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

@SideOnly(Side.CLIENT)
public class TileEntityGunWorkbenchRenderer extends TileEntitySpecialRenderer
{
	public void doRender(TileEntityGunWorkbench tileEntity, double x, double y, double z, float partialTick, int i)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x + 0.5F, (float)y + 1F, (float)z + 0.5F);
		GlStateManager.rotate(Stuff.Randomization.randSeed(M.proxy.world(0).getSeed(), 3523423524l, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()).nextFloat() * 360 - 180, 0.0F, 1.0F, 0.0F);
		if(tileEntity.getStackInSlot(0) != null)
		{
			this.renderStackOnTable(tileEntity.getStackInSlot(0), 0.15F, 0.0F, -20F + (40F * Stuff.Randomization.randSeed(M.proxy.world(0).getSeed(), 64359438525l, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()).nextFloat() - 20F));
		}
		if(tileEntity.getStackInSlot(1) != null)
		{
			this.renderStackOnTable(tileEntity.getStackInSlot(1), -0.26F, 0.0F, -70F + (40F * Stuff.Randomization.randSeed(M.proxy.world(0).getSeed(), 3928590302l, tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ()).nextFloat() - 20F));
		}
		GlStateManager.popMatrix();
	}
	
	private void renderStackOnTable(ItemStack stack, float x, float z, float angle)
	{
		GlStateManager.pushMatrix();
		float scale = 0.6F;
		
		GlStateManager.translate(x, 0.025, z);
		
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(angle, 0.0F, 0.0F, 1.0F);
		
		this.bindTexture(TextureMap.locationBlocksTexture);
		Minecraft.getMinecraft().getRenderItem().renderItemModel(stack);
		
		GlStateManager.scale(1 / scale, 1 / scale, 1 / scale);
		GlStateManager.disableRescaleNormal();
		
		GlStateManager.translate(-x, 0, -z);
		GlStateManager.popMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
	{
		this.doRender((TileEntityGunWorkbench)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
	}
}