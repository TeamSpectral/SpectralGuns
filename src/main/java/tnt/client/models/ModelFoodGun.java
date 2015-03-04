package tnt.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFoodGun extends ModelBase
{
    ModelRenderer mainbody1;
    ModelRenderer mainbody2;
    ModelRenderer mainbody3;
    ModelRenderer shoulderpad;
    ModelRenderer handle1;
    ModelRenderer handle2;
    ModelRenderer barrelpiece1;
    ModelRenderer barrelpiece2;
    ModelRenderer barrelpiece3;
    ModelRenderer barrelpiece4;
    ModelRenderer hopper1;
    ModelRenderer hopper2;
    ModelRenderer hopper3;
  
  public ModelFoodGun()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      mainbody1 = new ModelRenderer(this, 38, 0);
      mainbody1.addBox(-1.5F, 0F, 0F, 3, 3, 10);
      mainbody1.setRotationPoint(6F, -3.5F, -15F);
      mainbody1.setTextureSize(64, 32);
      mainbody1.mirror = true;
      setRotation(mainbody1, 0F, 0F, 0F);
      mainbody2 = new ModelRenderer(this, 42, 13);
      mainbody2.addBox(-1F, 0F, 0F, 2, 2, 9);
      mainbody2.setRotationPoint(6F, -2.5F, -6F);
      mainbody2.setTextureSize(64, 32);
      mainbody2.mirror = true;
      setRotation(mainbody2, 0F, 0F, 0F);
      mainbody3 = new ModelRenderer(this, 48, 14);
      mainbody3.addBox(-0.5F, 0F, 0F, 1, 2, 7);
      mainbody3.setRotationPoint(6F, -3.5F, -6F);
      mainbody3.setTextureSize(64, 32);
      mainbody3.mirror = true;
      setRotation(mainbody3, 0F, 0F, 0F);
      shoulderpad = new ModelRenderer(this, 23, 23);
      shoulderpad.addBox(-2F, -3.3F, 0F, 4, 4, 5);
      shoulderpad.setRotationPoint(6F, -1F, -0.5F);
      shoulderpad.setTextureSize(64, 32);
      shoulderpad.mirror = true;
      setRotation(shoulderpad, -0.296706F, 0F, 0F);
      handle1 = new ModelRenderer(this, 0, 25);
      handle1.addBox(-0.5F, 0F, 0F, 1, 3, 1);
      handle1.setRotationPoint(6F, 0F, -11.5F);
      handle1.setTextureSize(64, 32);
      handle1.mirror = true;
      setRotation(handle1, 0.296706F, 0F, 0F);
      handle2 = new ModelRenderer(this, 0, 25);
      handle2.addBox(-1F, 0F, 0F, 2, 4, 2);
      handle2.setRotationPoint(6F, -1F, 3F);
      handle2.setTextureSize(64, 32);
      handle2.mirror = true;
      setRotation(handle2, -0.4886922F, 0F, 0F);
      barrelpiece1 = new ModelRenderer(this, 0, 0);
      barrelpiece1.addBox(-1F, -1F, -1F, 2, 4, 4);
      barrelpiece1.setRotationPoint(6F, -3F, -13.9F);
      barrelpiece1.setTextureSize(64, 32);
      barrelpiece1.mirror = true;
      setRotation(barrelpiece1, 0F, 0F, 0F);
      barrelpiece2 = new ModelRenderer(this, 0, 0);
      barrelpiece2.addBox(-1F, -2F, -1F, 2, 4, 4);
      barrelpiece2.setRotationPoint(6F, -2F, -13.9F);
      barrelpiece2.setTextureSize(64, 32);
      barrelpiece2.mirror = true;
      setRotation(barrelpiece2, 0F, 0F, 1.570796F);
      barrelpiece3 = new ModelRenderer(this, 0, 0);
      barrelpiece3.addBox(-0.5F, 0F, 0F, 1, 4, 4);
      barrelpiece3.setRotationPoint(6F, -4F, -11F);
      barrelpiece3.setTextureSize(64, 32);
      barrelpiece3.mirror = true;
      setRotation(barrelpiece3, 0F, 0F, 0F);
      barrelpiece4 = new ModelRenderer(this, 0, 0);
      barrelpiece4.addBox(-0.5F, -2F, 0F, 1, 4, 4);
      barrelpiece4.setRotationPoint(6F, -2F, -11F);
      barrelpiece4.setTextureSize(64, 32);
      barrelpiece4.mirror = true;
      setRotation(barrelpiece4, 0F, 0F, 1.570796F);
      hopper1 = new ModelRenderer(this, 0, 9);
      hopper1.addBox(-2.5F, 0F, -2.5F, 5, 1, 5);
      hopper1.setRotationPoint(11.5F, -5F, -2F);
      hopper1.setTextureSize(64, 32);
      hopper1.mirror = true;
      setRotation(hopper1, 0F, 0F, 0.1745329F);
      hopper2 = new ModelRenderer(this, 0, 16);
      hopper2.addBox(-2F, 0F, -1F, 2, 6, 2);
      hopper2.setRotationPoint(12.5F, -3F, -2F);
      hopper2.setTextureSize(64, 32);
      hopper2.mirror = true;
      setRotation(hopper2, 0F, 0F, 1.22173F);
      hopper3 = new ModelRenderer(this, 0, 16);
      hopper3.addBox(-2F, 1F, -2F, 4, 1, 4);
      hopper3.setRotationPoint(11.5F, -5F, -2F);
      hopper3.setTextureSize(64, 32);
      hopper3.mirror = true;
      setRotation(hopper3, 0F, 0F, 0.1745329F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    mainbody1.render(f5);
    mainbody2.render(f5);
    mainbody3.render(f5);
    shoulderpad.render(f5);
    handle1.render(f5);
    handle2.render(f5);
    barrelpiece1.render(f5);
    barrelpiece2.render(f5);
    barrelpiece3.render(f5);
    barrelpiece4.render(f5);
    hopper1.render(f5);
    hopper2.render(f5);
    hopper3.render(f5);
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
