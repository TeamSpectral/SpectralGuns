package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.Stuff.Coordinates3D.Coords3D;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.ComponentType;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ComponentEvents
{
	public static boolean isGunValid(ItemStack stack)
	{
		ArrayList<Component> cs = ItemGun.getComponents(stack);
		return isValid(cs);
	}

	public static boolean isValid(ArrayList<Component> cs)
	{
		boolean b = true;
		for(int i = 0; i < cs.size(); ++i)
		{
			Component c = cs.get(i);
			if(!c.isValid(cs))
			{
				b = false;
			}
		}
		if(!hasRequired(cs))
		{
			b = false;
		}
		return b;
	}

	public static boolean hasRequired(ArrayList<Component> cs)
	{
		ArrayList<ComponentType> a = new ArrayList<ComponentType>();
		ArrayList<ComponentType> rts = ComponentRegister.getComponentTypes(cs);
		for(int i = 0; i < cs.size(); ++i)
		{
			Component ec = cs.get(i);
			for(int i2 = 0; i2 < rts.size(); ++i2)
			{
				ComponentType rt = rts.get(i2);
				if(ec.type == rt)
				{
					a.add(rt);
				}
			}
		}
		if(a.size() < rts.size())
		{
			return false;
		}
		return true;
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
				ammo = component.setAmmo(ammo, stack, player.worldObj, player, components);
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
				e.motionZ = (double)(MathHelper.cos(e.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(e.rotationPitch / 180.0F * (float)Math.PI) * f);

				e.motionY = (double)(-MathHelper.sin((e.rotationPitch) / 180.0F * (float)Math.PI) * f);
			}
		}
		for(int i2 = 0; i2 < components.size(); ++i2)
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
				e.motionY += player.posY-player.lastTickPosY;
				e.motionZ += player.posZ-player.lastTickPosZ;

				if(e instanceof EntityThrowable)
				{
					((EntityThrowable)e).setThrowableHeading(e.motionX+Randomization.r(spread), e.motionY+Randomization.r(spread), e.motionZ+Randomization.r(spread), 1, 1);
				}

				Coords3D pos = Coordinates3D.stabilize(new Coords3D((float)e.motionX, (float)e.motionY, (float)e.motionZ), speed);

				e.motionX = pos.x;
				e.motionY = pos.y;
				e.motionZ = pos.z;
			}
		}

		return projectiles;
	}
}
