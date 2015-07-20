package com.spectral.spectral_guns.components;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineFood;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineLaser;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.items.ItemAmmo;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentEvents
{
	public static boolean isGunValid(ItemStack stack)
	{
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		return isValid(cs);
	}
	
	public static boolean isValid(HashMap<Integer, Component> cs)
	{
		return isValid(cs, true);
	}
	
	public static boolean isValid(HashMap<Integer, Component> cs, boolean b)
	{
		for(int i = 0; i < cs.size(); ++i)
		{
			Component c = cs.get(i);
			if(c != null)
			{
				if(!c.isValid(i, cs))
				{
					return false;
				}
			}
		}
		if(b && !hasRequired(cs))
		{
			return false;
		}
		return true;
	}
	
	public static boolean hasRequired(HashMap<Integer, Component> cs)
	{
		ArrayList<Type> ts = ComponentRegister.getComponentTypes(cs);
		ArrayList<Type> rts = (ArrayList<Type>)ComponentRegister.getRequiredTypes().clone();
		for(int i = 0; i < ts.size(); ++i)
		{
			rts.remove(ts.get(i));
		}
		return rts.size() <= 0;
	}
	
	public static void updateComponents(ItemStack stack, EntityPlayer player, int slot, boolean isSelected)
	{
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		if(!isGunValid(stack))
		{
			ItemGun.dropAllComponents(player, stack);
			stack.stackSize = 0;
			return;
		}
		for(Integer i : cs.keySet())
		{
			Component c = cs.get(i);
			if(c != null)
			{
				c.update(i, stack, player.worldObj, player, slot, isSelected);
			}
		}
		double h = 0;
		int a = 0;
		for(Integer i : cs.keySet())
		{
			Component c = cs.get(i);
			if(c != null)
			{
				double cond = c.heatConductiveness(i, stack) / 2;
				double heat = c.heat(i, stack);
				h += heat * cond / c.heatThreshold(slot, stack);
				++a;
				c.setHeat(i, heat * (1 - cond), stack);
			}
		}
		if(h != 0 && a > 0)
		{
			for(Integer i : cs.keySet())
			{
				Component c = cs.get(i);
				if(c != null)
				{
					c.addHeat(i, h / a * c.heatThreshold(slot, stack), stack);
				}
			}
		}
		for(Integer i : cs.keySet())
		{
			Component c = cs.get(i);
			if(c != null)
			{
				if(!c.isValid(i, cs))
				{
					cs.remove(i);
					--i;
				}
			}
		}
		for(Integer i : cs.keySet())
		{
			Component c = cs.get(i);
			if(c != null)
			{
				if(c.durabilityDamage(i, stack) > c.durabilityMax(i, stack))
				{
					cs.remove(i);
					--i;
					ItemGun.setComponents(stack, cs);
					ItemGun.dropAllComponents(player, stack);
					stack.stackSize = 0;
					return;
				}
			}
		}
		if(!isGunValid(stack))
		{
			ItemGun.setComponents(stack, cs);
			ItemGun.dropAllComponents(player, stack);
			stack.stackSize = 0;
		}
		return;
	}
	
	public static boolean addAmmo(int a, ItemStack stack, EntityPlayer player)
	{
		int ammo = ItemGun.ammo(stack, player);
		if(!isGunValid(stack) || ammo + a < 0 || ammo + a > ItemGun.capacity(stack, player))
		{
			return false;
		}
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			if(slot != null)
			{
				Component component = cs.get(slot);
				if(component != null)
				{
					ammo = component.setAmmo(slot, a, stack, player.worldObj, player);
				}
			}
		}
		return true;
	}
	
	public static void setAmmo(int ammo, ItemStack stack, EntityPlayer player)
	{
		if(!isGunValid(stack))
		{
			return;
		}
		
		addAmmo(ammo - ItemGun.ammo(stack, player), stack, player);
	}
	
	public static ArrayList<Entity> fireComponents(ItemStack stack, EntityPlayer player)
	{
		if(!addAmmo(-1, stack, player) || !isGunValid(stack))
		{
			return null;
		}
		
		ExtendedPlayer props = ExtendedPlayer.get(player);
		
		ArrayList<Entity> projectiles = new ArrayList<Entity>();
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			Component component = cs.get(slot);
			if(component != null)
			{
				projectiles = component.fire(slot, projectiles, stack, player.worldObj, player);
			}
		}
		
		float spread = ItemGun.spread(stack, player);
		float speed = ItemGun.speed(stack, player);
		
		for(int i = 0; i < projectiles.size(); ++i)
		{
			Entity e = projectiles.get(i);
			if(e != null)
			{
				e.setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
				e.posX -= MathHelper.cos(e.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
				e.posY -= 0.10000000149011612D;
				e.posZ -= MathHelper.sin(e.rotationYaw / 180.0F * (float)Math.PI) * 0.16F;
				e.setPosition(e.posX, e.posY, e.posZ);
				float f = 0.4F;
				e.motionX = -MathHelper.sin(e.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(e.rotationPitch / 180.0F * (float)Math.PI) * f;
				e.motionY = -MathHelper.sin(e.rotationPitch / 180.0F * (float)Math.PI) * f;
				e.motionZ = MathHelper.cos(e.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(e.rotationPitch / 180.0F * (float)Math.PI) * f;
			}
		}
		for(Integer slot : cs.keySet())
		{
			Component component = cs.get(slot);
			if(component != null && projectiles.size() > 0)
			{
				component.fireSound(slot, projectiles.get(0), stack, player.worldObj, player);
			}
		}
		
		float oldRotPitch = player.rotationPitch;
		float oldRotYaw = player.rotationYaw;
		float inst = (float)(props.recoilPitch + props.recoilYaw);
		if(inst > 60)
		{
			inst = 60;
		}
		player.rotationYaw += Randomization.r(inst * 5);
		player.rotationPitch += Randomization.r(inst * 5);
		for(int i = 0; i < projectiles.size(); ++i)
		{
			Entity e = projectiles.get(i);
			if(e != null)
			{
				e.motionX += player.posX - player.lastTickPosX;
				if(player.posY - player.lastTickPosY > 0 || !player.onGround)
				{
					e.motionY += player.posY - player.lastTickPosY;
				}
				e.motionZ += player.posZ - player.lastTickPosZ;
				
				if(e instanceof EntityThrowable)
				{
					Vec3 v = new Vec3(player.worldObj.rand.nextGaussian() * 2 - 1, player.worldObj.rand.nextGaussian() * 2 - 1, player.worldObj.rand.nextGaussian() * 2 - 1);
					v = Coordinates3D.stabilize(v, spread);
					((EntityThrowable)e).setThrowableHeading(e.motionX + v.xCoord, e.motionY + v.yCoord, e.motionZ + v.zCoord, 1, 1);
				}
				player.worldObj.rand.nextFloat();
				Vec3 pos = Coordinates3D.stabilize(new Vec3(e.motionX, e.motionY, e.motionZ), speed);
				
				e.motionX = pos.xCoord;
				e.motionY = pos.yCoord;
				e.motionZ = pos.zCoord;
			}
		}
		player.rotationPitch = oldRotPitch;
		player.rotationYaw = oldRotYaw;
		
		return projectiles;
	}
	
	public static void heatUp(ItemStack stack, EntityPlayer player, double modifier)
	{
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			Component c = cs.get(slot);
			if(c instanceof IComponentHeatOnFire)
			{
				((IComponentHeatOnFire)c).heatUp(slot, stack, modifier);
			}
		}
	}
	
	public static boolean reload(ItemStack stack, EntityPlayer player)
	{
		int amount = 1;
		
		if(!isGunValid(stack) || ItemGun.ammo(stack, player) + amount * amount(stack, new ItemStack(ItemGun.ejectableAmmo(stack, player))) > ItemGun.capacity(stack, player))
		{
			return false;
		}
		
		InventoryPlayer inv = player.inventory;
		if(!player.capabilities.isCreativeMode || !Config.canReloadWithoutAmmoInCreativeMode.get())
		{
			int slot = -1;
			ItemStack stack2 = null;
			for(int i = 0; i < inv.getSizeInventory(); ++i)
			{
				ItemStack s = inv.getStackInSlot(i);
				if(s != null)
				{
					if(s.getItem() instanceof ItemAmmo)
					{
						ItemAmmo item = (ItemAmmo)s.getItem();
						s.setItem(item.ammo());
						if(ItemGun.ammoItem(stack, s, player) && ItemGun.ammo(stack, player) + item.multiplier * amount <= ItemGun.capacity(stack, player))
						{
							if(stack2 == null || !(stack2.getItem() instanceof ItemAmmo) || item.multiplier > ((ItemAmmo)stack2.getItem()).multiplier)
							{
								stack2 = s;
								slot = i;
							}
						}
						s.setItem(item);
					}
					else
					{
						if(stack2 == null && ItemGun.ammoItem(stack, s, player))
						{
							if(!ItemAmmo.itemHasItemAmmo(s.getItem()))
							{
								stack2 = s;
								slot = i;
							}
						}
					}
				}
			}
			if(stack2 != null)
			{
				boolean flag = false;
				if(stack2.getItem() instanceof ItemAmmo)
				{
					ItemAmmo i = (ItemAmmo)stack2.getItem();
					stack2.setItem(i.ammo());
					flag = ItemGun.ammoItem(stack, stack2, player);
					stack2.setItem(i);
				}
				else
				{
					flag = ItemGun.ammoItem(stack, stack2, player);
				}
				amount *= amount(stack, stack2);
				if(flag && addAmmo(amount, stack, player))
				{
					--stack2.stackSize;
					if(stack2.stackSize <= 0)
					{
						inv.setInventorySlotContents(slot, null);
					}
					player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
					return true;
				}
			}
		}
		else
		{
			if(addAmmo(amount, stack, player))
			{
				player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
				return true;
			}
		}
		return false;
	}
	
	public static boolean eject(ItemStack stack, EntityPlayer player)
	{
		return eject(stack, player, new ConsumerComponentEvent()
		{
			@Override
			public void action(ItemStack gun, EntityPlayer player)
			{
				if(!player.capabilities.isCreativeMode || !Config.canReloadWithoutAmmoInCreativeMode.get())
				{
					Item item = ItemGun.ejectableAmmo(gun, player);
					EntityItem e = player.dropItem(item, 1);
					if(e != null)
					{
						e.setNoPickupDelay();
						e.setOwner(player.getName());
						player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 1.4F);
					}
				}
			}
		});
	}
	
	public static boolean eject(ItemStack stack, EntityPlayer player, ConsumerComponentEvent func)
	{
		int amount = 1;
		Item item = ItemGun.ejectableAmmo(stack, player);
		amount *= amount(stack, new ItemStack(item));
		
		if(!isGunValid(stack) || ItemGun.ammo(stack, player) - amount < 0)
		{
			return false;
		}
		
		if(item != null)
		{
			InventoryPlayer inv = player.inventory;
			if(addAmmo(-amount, stack, player))
			{
				if(!player.worldObj.isRemote)
				{
					func.action(stack, player);
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
	
	public static int amount(ItemStack gun, ItemStack ammo)
	{
		int amount = 1;
		
		ItemAmmo itemAmmo = null;
		
		if(ammo.getItem() instanceof ItemAmmo)
		{
			itemAmmo = (ItemAmmo)ammo.getItem();
			amount *= itemAmmo.multiplier;
			ammo.setItem(itemAmmo.ammo());
		}
		if(ammo.getItem() == Items.redstone)
		{
			amount *= ComponentMagazineLaser.ammoMultiplier;
		}
		if(ammo.getItem() instanceof ItemFood)
		{
			amount *= ComponentMagazineFood.ammoMultiplier;
		}
		
		if(amount < 1)
		{
			amount = 1;
		}
		
		if(itemAmmo != null)
		{
			ammo.setItem(itemAmmo);
		}
		
		return amount;
	}
	
	public abstract static class ConsumerComponentEvent
	{
		public ConsumerComponentEvent()
		{
			
		}
		
		public abstract void action(ItemStack gun, EntityPlayer player);
	}
	
	public abstract static class ConsumerComponentEventVec3 extends ConsumerComponentEvent
	{
		public final Vec3 vec;
		
		public ConsumerComponentEventVec3(Vec3 vec)
		{
			this.vec = vec;
		}
	}
}
