package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.magazine.ComponentMagazine;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineLaser;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameData;

public class ComponentEvents
{
	public static boolean isGunValid(ItemStack stack)
	{
		ArrayList<Component> cs = ItemGun.getComponents(stack);
		return isValid(cs);
	}

	public static boolean isValid(ArrayList<Component> cs)
	{
		return isValid(cs, true);
	}

	public static boolean isValid(ArrayList<Component> cs, boolean b)
	{
		for(int i = 0; i < cs.size(); ++i)
		{
			Component c = cs.get(i);
			if(!c.isValid(cs))
			{
				return false;
			}
		}
		if(b && !hasRequired(cs))
		{
			return false;
		}
		return true;
	}

	public static boolean hasRequired(ArrayList<Component> cs)
	{
		ArrayList<Type> ts = ComponentRegister.getComponentTypes(cs);
		ArrayList<Type> rts = ComponentRegister.getRequiredTypes();
		for(int i = 0; i < ts.size(); ++i)
		{
			rts.remove(ts.get(i));
		}
		return rts.size() <= 0;
	}

	public static ArrayList<Component> updateComponents(ItemStack stack, Entity entity, int slot, boolean isSelected, ArrayList<Component> components)
	{
		if(!isGunValid(stack))
		{
			return components;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			Component component = components.get(i);
			component.update(stack, entity.worldObj, entity, slot, isSelected, components);
			if(!component.isValid(components))
			{
				components.remove(i);
				--i;
			}
		}
		return components;
	}

	public static boolean addAmmo(int a, ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		int ammo = ItemGun.ammo(stack, player);
		if(!isGunValid(stack) || ammo+a < 0 || ammo+a > ItemGun.capacity(stack, player))
		{
			return false;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			Component component = components.get(i);
			if(component != null)
			{
				ammo = component.setAmmo(a, stack, player.worldObj, player, components);
			}
		}
		return true;
	}

	public static void setAmmo(int ammo, ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		if(!isGunValid(stack))
		{
			return;
		}

		addAmmo(ammo-ItemGun.ammo(stack, player), stack, player, components);
	}

	public static ArrayList<Entity> fireComponents(ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		if(!addAmmo(-1, stack, player, components) || !isGunValid(stack))
		{
			return null;
		}

		ArrayList<Entity> projectiles = new ArrayList<Entity>();

		for(int i = 0; i < components.size(); ++i)
		{
			Component component = components.get(i);
			if(component != null && component instanceof ComponentMagazine)
			{
				projectiles = component.fire(projectiles, stack, player.worldObj, player, components);
			}
		}
		for(int i = 0; i < components.size(); ++i)
		{
			Component component = components.get(i);
			if(component != null && !(component instanceof ComponentMagazine))
			{
				projectiles = component.fire(projectiles, stack, player.worldObj, player, components);
			}
		}

		float spread = ItemGun.spread(stack, player);
		float speed = ItemGun.speed(stack, player);

		if(stack.getTagCompound().getInteger(ItemGun.RECOIL) > 0)
		{
			spread *= 1+stack.getTagCompound().getInteger(ItemGun.RECOIL);
		}

		for(int i = 0; i < projectiles.size(); ++i)
		{
			Entity e = projectiles.get(i);
			if(e != null)
			{
				e.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
				e.posX -= (double)(MathHelper.cos(e.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
				e.posY -= 0.10000000149011612D;
				e.posZ -= (double)(MathHelper.sin(e.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
				e.setPosition(e.posX, e.posY, e.posZ);
				float f = 0.4F;
				e.motionX = (double)(-MathHelper.sin(e.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(e.rotationPitch / 180.0F * (float)Math.PI) * f);
				e.motionY = (double)(-MathHelper.sin((e.rotationPitch) / 180.0F * (float)Math.PI) * f);
				e.motionZ = (double)(MathHelper.cos(e.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(e.rotationPitch / 180.0F * (float)Math.PI) * f);
			}
		}
		for(int i2 = 0; i2 < components.size() && projectiles.size() > 0; ++i2)
		{
			Component component = components.get(i2);
			if(component != null)
			{
				component.fireSound(projectiles.get(0), stack, player.worldObj, player);
			}
		}

		for(int i = 0; i < projectiles.size(); ++i)
		{
			Entity e = projectiles.get(i);
			if(e != null)
			{
				e.motionX += player.posX-player.lastTickPosX;
				if(player.posY-player.lastTickPosY > 0 || !player.onGround)
				{
					e.motionY += player.posY-player.lastTickPosY;
				}
				e.motionZ += player.posZ-player.lastTickPosZ;

				if(e instanceof EntityThrowable)
				{
					Vec3 v = new Vec3((player.worldObj.rand.nextGaussian()*2)-1, (player.worldObj.rand.nextGaussian()*2)-1, (player.worldObj.rand.nextGaussian()*2)-1);
					v = Coordinates3D.stabilize(v, spread);
					((EntityThrowable)e).setThrowableHeading(e.motionX+v.xCoord, e.motionY+v.yCoord, e.motionZ+v.zCoord, 1, 1);
				}
				player.worldObj.rand.nextFloat();
				Vec3 pos = Coordinates3D.stabilize(new Vec3(e.motionX, e.motionY, e.motionZ), speed);

				e.motionX = pos.xCoord;
				e.motionY = pos.yCoord;
				e.motionZ = pos.zCoord;
			}
		}

		return projectiles;
	}

	public static boolean reload(ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		if(!isGunValid(stack) || ItemGun.ammo(stack, player)+1 > ItemGun.capacity(stack, player) || (ArraysAndSuch.allExtending(ItemGun.getComponents(stack), ComponentMagazineLaser.class).size() > 0 && ItemGun.ammo(stack, player)+ComponentMagazineLaser.battery() > ItemGun.capacity(stack, player)))
		{
			return false;
		}

		InventoryPlayer inv = player.inventory;
		if(!player.capabilities.isCreativeMode)
		{
			for(int i = 0; i < inv.getSizeInventory(); ++i)
			{
				ItemStack s = inv.getStackInSlot(i);
				if(s != null && ItemGun.ammoItem(stack, s, player))
				{
					if(addAmmo(1, stack, player, components))
					{
						--s.stackSize;
						if(s.stackSize <= 0)
						{
							if(!player.capabilities.isCreativeMode)
							{
								inv.setInventorySlotContents(i, null);
							}
						}
						player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

						return true;
					}
				}
			}
		}
		else
		{
			if(addAmmo(1, stack, player, components))
			{
				player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

				return true;
			}
		}
		return false;
	}

	public static boolean eject(ItemStack stack, EntityPlayer player, ArrayList<Component> components)
	{
		if(!isGunValid(stack) || ItemGun.ammo(stack, player)-1 < 0 || ArraysAndSuch.allExtending(ItemGun.getComponents(stack), ComponentMagazineLaser.class).size() > 0)
		{
			return false;
		}

		Item item = ItemGun.ejectableAmmo(stack, stack, player);

		if(item != null)
		{
			InventoryPlayer inv = player.inventory;
			if(addAmmo(-1, stack, player, components))
			{
				if(!player.capabilities.isCreativeMode)
				{
					EntityItem e = player.dropItem(item, 1);
					if(e != null)
					{
						e.setNoPickupDelay();
						e.setOwner(player.getName());
						player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 1.4F);
					}
				}
				return true;
			}
		}
		return false;
	}

	public static void registerRecipes()
	{
		ArrayList<Component> components = ComponentRegister.getAll();
		for(int i = 0; i < components.size(); ++i)
		{
			components.get(i).registerRecipe();
		}
	}
}
