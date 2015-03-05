package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.audio.AudioHandler;
import com.spectral.spectral_guns.audio.MovingSoundPublic;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentBarrel;
import com.spectral.spectral_guns.components.ComponentBarrel.ComponentBarrelThin;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.String2;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentMagazineLaser extends ComponentGeneric
{
	public final int capacity;
	public final LaserColor color;

	public final static String FIRING = "IsFiring";
	public final static String CHARGE = "Charge";
	public final static String AMMO = "Ammo";
	public final static String TIMER = "Timer";
	public final static int ammoMultiplier = 50;
	public final int battery;

	public ComponentMagazineLaser(Component[] required, Component[] incapatible, ComponentMaterial material, LaserColor color, int capacity, int battery)
	{
		super(new String2("magazine_laser", "_" + color.toString().toLowerCase()), new String2("magazine.laser", "." + color.toString().toLowerCase()), required, incapatible, Type.MAGAZINE, material);
		this.color = color;
		this.capacity = capacity;
		this.battery = battery;
	}

	public int battery()
	{
		return 25;
	}

	@Override
	public void update(ItemStack gun, World world, Entity entity, int slot, boolean isSelected, ArrayList<Component> components)
	{
		NBTTagCompound compound = getTagCompound(gun);
		if(entity instanceof EntityPlayer && isSelected)
		{
			EntityExtendedPlayer props = EntityExtendedPlayer.get((EntityPlayer)entity);
			if(props.isRightClickHeldDown && props.reloadDelay <= 0)
			{
				compound.setBoolean(FIRING, true);
			}
		}
		if(!isSelected || !(entity instanceof EntityPlayer) || !EntityExtendedPlayer.get((EntityPlayer)entity).isRightClickHeldDown || compound.getInteger(AMMO)-1 < 0)
		{
			compound.setBoolean(FIRING, false);
			compound.setInteger(TIMER, 0);
		}
		if(compound.getBoolean(FIRING) && entity instanceof EntityPlayer)
		{
			float incline = 1.13F/ItemGun.delay(gun, (EntityPlayer)entity);
			compound.setFloat(CHARGE, compound.getFloat(CHARGE) + incline);
		}
		else
		{
			compound.setFloat(CHARGE, compound.getFloat(CHARGE)*0.95F - 0.1F);
		}
		compound.setInteger(TIMER, 1+compound.getInteger(TIMER));
		compound.setFloat(CHARGE, Math.max(0, Math.min(12, compound.getFloat(CHARGE))));
		if(compound.getFloat(CHARGE) > 0.5 && entity instanceof EntityPlayer)
		{
			EntityLaser e = new EntityLaser(world, (EntityPlayer)entity, compound.getFloat(CHARGE), color, 0);
			world.spawnEntityInWorld(e);

			int t = (int)(12/compound.getFloat(CHARGE));
			if(t <= 0 || compound.getInteger(TIMER) % t == t-1)
			{
				setAmmo(-1, gun, world, (EntityPlayer)entity, components);
			}
		}
		if(world.isRemote)
		{
			if(compound.getFloat(CHARGE) > 1)
			{
				if(AudioHandler.getSound(entity, "fire.fire") == null || !AudioHandler.isPlaying(entity, "fire.fire"))
				{
					AudioHandler.createMovingEntitySound(entity, "fire.fire", 1.8F + world.rand.nextFloat(), world.rand.nextFloat() * 0.5F + 0.1F, false);
				}
			}
			if(compound.getFloat(CHARGE) <= 0)
			{
				AudioHandler.stopSound(entity, "fire.fire");
			}
			if(AudioHandler.getSound(entity, "fire.fire") != null && AudioHandler.getSound(entity, "fire.fire") instanceof MovingSoundPublic)
			{
				((MovingSoundPublic)AudioHandler.getSound(entity, "fire.fire")).setVolume(compound.getFloat(CHARGE)/2);
			}
		}
		capAmmo(compound);
	}

	@Override
	public int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(stack);
		if(!compound.hasKey(AMMO))
		{
			compound.setInteger(AMMO, 0);
		}
		while(ammo > 0 && compound.getInteger(AMMO)+1 <= capacity*ammoMultiplier)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO)+1);
			--ammo;
		}
		while(ammo < 0 && compound.getInteger(AMMO)-1 >= 0)
		{
			compound.setInteger(AMMO, compound.getInteger(AMMO)-1);
			++ammo;
		}

		capAmmo(compound);
		return ammo;
	}

	@Override
	public int capacity(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return capacity*ammoMultiplier;
	}

	@Override
	public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
	{
		return delay + battery()/2 + capacity/32;
	}

	public boolean isValid(ArrayList<Component> ecs)
	{
		int count = 0;
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).type == Type.MAGAZINE)
			{
				++count;
			}
			if(ecs.get(i).type == Type.BARREL)
			{
				if(ecs.get(i).material != ComponentMaterial.DIAMOND || !(ecs.get(i) instanceof ComponentBarrelThin))
				{
					return false;
				}
			}
		}
		if(count > 2)
		{
			return false;
		}
		return super.isValid(ecs);
	}

	@Override
	public int ammo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		NBTTagCompound compound = this.getTagCompound(stack);
		capAmmo(compound);
		return compound.getInteger(AMMO);
	}

	@Override
	public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player)
	{
		return stack.getItem() == Items.redstone;
	}

	@Override
	public Item ejectableAmmo(ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return Items.redstone;
	}

	@Override
	public float spread(float spread, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public float kickback(float kickback, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public float recoil(float recoil, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public float speed(float speed, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 299792458/20;
	}

	public float fireRate(float rate, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return 0;
	}

	@Override
	public ArrayList<Entity> fire(ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		this.getTagCompound(stack).setBoolean(FIRING, props.isRightClickHeldDown);
		e.clear();
		return e;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item diode = M.laser_diode_green_strong;
		switch(material)
		{
		case IRON: 
		{
			switch(color)
			{
			case CYAN:
				break;
			case GREEN: diode = M.laser_diode_green_strong;
			break;
			case RED: diode = M.laser_diode_red_strong;
			break;
			case VIOLET:
				break;
			}
			break;
		}
		case GOLD:
		{
			switch(color)
			{
			case CYAN:
				break;
			case GREEN: diode = M.magazine_laser_iron_green.item;
			break;
			case RED: diode = M.magazine_laser_iron_red.item;
			break;
			case VIOLET:
				break;
			}
			break;
		}
		case DIAMOND:
		{
			switch(color)
			{
			case CYAN:
				break;
			case GREEN: diode = M.magazine_laser_gold_green.item;
			break;
			case RED: diode = M.magazine_laser_gold_red.item;
			break;
			case VIOLET:
				break;
			}
			break;
		}
		}
		switch(material)
		{
		case IRON: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"iir", "lbd", "iir", 'i', Items.iron_ingot, 'r', Items.redstone, 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode});
		break;
		case GOLD: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"iir", "lbd", "iir", 'i', Items.gold_ingot, 'r', Items.redstone, 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode});
		break;
		case DIAMOND: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"iir", "lbd", "iir", 'i', Items.diamond, 'r', Items.redstone, 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode});
		break;
		}
	}

	@Override
	public void renderModel(double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{

	}

	protected void capAmmo(NBTTagCompound c)
	{
		int a = c.getInteger(AMMO);
		if(c.getInteger(AMMO) > capacity*ammoMultiplier)
		{
			c.setInteger(AMMO, capacity*ammoMultiplier);
		}
		if(c.getInteger(AMMO) < 0)
		{
			c.setInteger(AMMO, 0);
		}
	}
}