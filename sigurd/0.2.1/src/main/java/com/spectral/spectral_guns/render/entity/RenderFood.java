package com.spectral.spectral_guns.render.entity;

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
public class RenderFood extends Render
{
    private final RenderItem ri;

    public RenderFood(RenderManager rm, RenderItem ri)
    {
        super(rm);
        this.ri = ri;
    }

    public void doRender(Entity entity, double x, double y, double z, float f, float partialTicks)
    {
    	if(entity instanceof EntityFood)
    	{
    		doRender((EntityFood)entity, x, y, z, f, partialTicks);
    	}
    }
    
    public void doRender(EntityFood entity, double x, double y, double z, float f, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        float f2 = (float)MathWithMultiple.distance(entity.height, entity.width);
        GlStateManager.scale(f2, f2, f2);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        this.bindTexture(TextureMap.locationBlocksTexture);
        ri.renderItemModel(entity.getItemStack());
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, f, partialTicks);
    }

    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}