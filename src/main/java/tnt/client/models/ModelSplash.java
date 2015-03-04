package tnt.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSplash extends ModelBase
{
  //fields
    ModelRenderer mainBody1;
    ModelRenderer mainBody2;
    ModelRenderer shoulderPad;
    ModelRenderer handle1;
    ModelRenderer handle2;
    ModelRenderer nozzle;
    ModelRenderer barrel1;
    ModelRenderer barrel2;
    ModelRenderer barrel3;
    ModelRenderer barrel4;
    ModelRenderer barrel5;
    ModelRenderer barrel6;
    ModelRenderer barrel7;
    ModelRenderer ammoCase1;
    ModelRenderer ammoCase2;
    ModelRenderer ammoCase3;
    ModelRenderer ammoCase4;
    ModelRenderer ammoCase5;
  
  public ModelSplash()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      mainBody1 = new ModelRenderer(this, 40, 19);
      mainBody1.addBox(-1F, 0F, 0F, 2, 3, 10);
      mainBody1.setRotationPoint(6F, -3.2F, -8.5F);
      mainBody1.setTextureSize(64, 32);
      mainBody1.mirror = true;
      setRotation(mainBody1, 0F, 0F, 0F);
      mainBody2 = new ModelRenderer(this, 41, 9);
      mainBody2.addBox(-1.5F, 0F, 0F, 3, 3, 4);
      mainBody2.setRotationPoint(6F, -3.5F, -5F);
      mainBody2.setTextureSize(64, 32);
      mainBody2.mirror = true;
      setRotation(mainBody2, 0.1745329F, 0F, 0F);
      shoulderPad = new ModelRenderer(this, 50, 0);
      shoulderPad.addBox(-1.5F, -3F, 0F, 3, 3, 4);
      shoulderPad.setRotationPoint(6F, -1F, -0.5F);
      shoulderPad.setTextureSize(64, 32);
      shoulderPad.mirror = true;
      setRotation(shoulderPad, -0.3839724F, 0F, 0F);
      handle1 = new ModelRenderer(this, 56, 8);
      handle1.addBox(-1F, 0F, 0F, 2, 3, 2);
      handle1.setRotationPoint(6F, -1F, 3F);
      handle1.setTextureSize(64, 32);
      handle1.mirror = true;
      setRotation(handle1, -0.6108652F, 0F, 0F);
      handle2 = new ModelRenderer(this, 56, 8);
      handle2.addBox(-0.5F, 0F, 0F, 1, 3, 1);
      handle2.setRotationPoint(6F, -1F, -10.5F);
      handle2.setTextureSize(64, 32);
      handle2.mirror = true;
      setRotation(handle2, 0F, 0F, 0F);
      nozzle = new ModelRenderer(this, 0, 30);
      nozzle.addBox(-0.5F, 0F, 0F, 1, 1, 1);
      nozzle.setRotationPoint(6F, -2.5F, -11.8F);
      nozzle.setTextureSize(64, 32);
      nozzle.mirror = true;
      setRotation(nozzle, 0F, 0F, 0F);
      barrel1 = new ModelRenderer(this, 37, 0);
      barrel1.addBox(-1.5F, 0F, 0F, 3, 3, 3);
      barrel1.setRotationPoint(6F, -3.5F, -11F);
      barrel1.setTextureSize(64, 32);
      barrel1.mirror = true;
      setRotation(barrel1, 0F, 0F, 0F);
      barrel2 = new ModelRenderer(this, 37, 0);
      barrel2.addBox(-1F, 0F, 0F, 2, 4, 2);
      barrel2.setRotationPoint(6F, -4F, -10.5F);
      barrel2.setTextureSize(64, 32);
      barrel2.mirror = true;
      setRotation(barrel2, 0F, 0F, 0F);
      barrel3 = new ModelRenderer(this, 37, 0);
      barrel3.addBox(-1F, -2F, 0F, 2, 4, 2);
      barrel3.setRotationPoint(6F, -2F, -10.5F);
      barrel3.setTextureSize(64, 32);
      barrel3.mirror = true;
      setRotation(barrel3, 0F, 0F, 1.570796F);
      barrel4 = new ModelRenderer(this, 37, 0);
      barrel4.addBox(-1F, -1F, -1F, 2, 3, 2);
      barrel4.setRotationPoint(6F, -2F, -10.5F);
      barrel4.setTextureSize(64, 32);
      barrel4.mirror = true;
      setRotation(barrel4, 0F, 1.570796F, 1.570796F);
      barrel5 = new ModelRenderer(this, 37, 0);
      barrel5.addBox(-0.5F, 0F, 0F, 1, 1, 2);
      barrel5.setRotationPoint(6F, -3.8F, -8.5F);
      barrel5.setTextureSize(64, 32);
      barrel5.mirror = true;
      setRotation(barrel5, -0.0872665F, 0F, 0F);
      barrel6 = new ModelRenderer(this, 37, 0);
      barrel6.addBox(-0.5F, 0F, 0F, 1, 1, 2);
      barrel6.setRotationPoint(7.8F, -2F, -8.5F);
      barrel6.setTextureSize(64, 32);
      barrel6.mirror = true;
      setRotation(barrel6, -0.0872665F, 0F, 1.570796F);
      barrel7 = new ModelRenderer(this, 37, 0);
      barrel7.addBox(-0.5F, -1F, 0F, 1, 1, 2);
      barrel7.setRotationPoint(4.2F, -2F, -8.5F);
      barrel7.setTextureSize(64, 32);
      barrel7.mirror = true;
      setRotation(barrel7, 0.0872665F, 0F, 1.570796F);
      ammoCase1 = new ModelRenderer(this, 0, 0);
      ammoCase1.addBox(-2F, 0F, -2F, 4, 4, 4);
      ammoCase1.setRotationPoint(10F, -7.7F, -1.3F);
      ammoCase1.setTextureSize(64, 32);
      ammoCase1.mirror = true;
      setRotation(ammoCase1, 0F, 0F, 0F);
      ammoCase2 = new ModelRenderer(this, 0, 0);
      ammoCase2.addBox(-1.5F, -1.5F, -2.5F, 3, 3, 5);
      ammoCase2.setRotationPoint(10F, -5.7F, -1.3F);
      ammoCase2.setTextureSize(64, 32);
      ammoCase2.mirror = true;
      setRotation(ammoCase2, 0F, 0F, 0F);
      ammoCase3 = new ModelRenderer(this, 0, 0);
      ammoCase3.addBox(-1.5F, -1.5F, -2.5F, 3, 3, 5);
      ammoCase3.setRotationPoint(10F, -5.7F, -1.3F);
      ammoCase3.setTextureSize(64, 32);
      ammoCase3.mirror = true;
      setRotation(ammoCase3, 0F, 1.570796F, 0F);
      ammoCase4 = new ModelRenderer(this, 0, 0);
      ammoCase4.addBox(-1.5F, -1.5F, -2.5F, 3, 3, 5);
      ammoCase4.setRotationPoint(10F, -5.7F, -1.3F);
      ammoCase4.setTextureSize(64, 32);
      ammoCase4.mirror = true;
      setRotation(ammoCase4, 1.570796F, 1.570796F, 0F);
      ammoCase5 = new ModelRenderer(this, 0, 9);
      ammoCase5.addBox(-2F, 0F, -1F, 2, 5, 2);
      ammoCase5.setRotationPoint(11F, -3.7F, -1.3F);
      ammoCase5.setTextureSize(64, 32);
      ammoCase5.mirror = true;
      setRotation(ammoCase5, 0F, 0F, 1.012291F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    mainBody1.render(f5);
    mainBody2.render(f5);
    shoulderPad.render(f5);
    handle1.render(f5);
    handle2.render(f5);
    nozzle.render(f5);
    barrel1.render(f5);
    barrel2.render(f5);
    barrel3.render(f5);
    barrel4.render(f5);
    barrel5.render(f5);
    barrel6.render(f5);
    barrel7.render(f5);
    ammoCase1.render(f5);
    ammoCase2.render(f5);
    ammoCase3.render(f5);
    ammoCase4.render(f5);
    ammoCase5.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
  }

}
