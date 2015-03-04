package tnt.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import tnt.client.models.ModelFoodGun;
import tnt.client.models.ModelSplash;

public class RenderGun implements IItemRenderer
{
	private int foodID;
    ModelFoodGun model = new ModelFoodGun();
    ModelSplash model2 = new ModelSplash();
    Minecraft mc = Minecraft.getMinecraft();
    
    public RenderGun(int i)
    {
    	foodID = i;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object ... data)
    {
        if (type.equals(ItemRenderType.EQUIPPED))
        {
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glPushMatrix();
            GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(80.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-75.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, -0.2F, 0.0F);
            if(foodID == 0)
            {
            this.mc.renderEngine.bindTexture(new ResourceLocation("tnt:textures/model/foodgun.png"));
            this.model.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            }
            else if(foodID == 1)
            {
            this.mc.renderEngine.bindTexture(new ResourceLocation("tnt:textures/model/splashgun.png"));
            this.model2.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            }
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
        {
     
                GL11.glEnable(GL11.GL_NORMALIZE);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glPushMatrix();
                GL11.glRotatef(-78.0F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(65.0F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-100.0F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.4F, -0.2F, 1.0F);
                if(foodID == 0)
                {
                this.mc.renderEngine.bindTexture(new ResourceLocation("tnt:textures/model/foodgun.png"));
                this.model.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                }
                else if(foodID == 1)
                {
                this.mc.renderEngine.bindTexture(new ResourceLocation("tnt:textures/model/splashgun.png"));
                this.model2.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
                }
                GL11.glPopMatrix();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        }
        else
        {
            GL11.glTranslatef(-1.0F, -0.5F, -0.1F);
        }
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type)
    {
        switch (RenderGun$1.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()])
        {
            case 1:
            case 2:
            case 3:
                return true;

            default:
                return false;
        }
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
    {
        return false;
    }
}
