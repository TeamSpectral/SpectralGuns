package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.audio.AudioHandler;
import com.spectral.spectral_guns.audio.MovingSoundPublic;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentMagazineLaser extends ComponentMagazine
{
	public final LaserColor color;
	
	public final static String FIRING = "IsFiring";
	public final static String CHARGE = "Charge";
	public final static String TIMER = "Timer";
	public final static int ammoMultiplier = 25;
	public final int battery;
	
	public ComponentMagazineLaser(ComponentMaterial material, LaserColor color, int capacity, int battery)
	{
		super(new String2("laser", "_" + color.toString().toLowerCase()), new String2("laser", "." + color.toString().toLowerCase()), 0.4, 3 * 9 * 3, material, capacity, 4.6F);
		this.color = color;
		this.battery = battery;
		this.required = new Component[]{M.barrel_thin_diamond};
	}
	
	@Override
	public void update(int slot, ItemStack gun, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		NBTTagCompound compound = this.getTagCompound(slot, gun);
		if(isSelected)
		{
			ExtendedPlayer props = ExtendedPlayer.get(player);
			if(props.isRightClickHeldDown && props.reloadDelay <= 0)
			{
				compound.setBoolean(FIRING, true);
			}
		}
		if(!isSelected || !ExtendedPlayer.get(player).isRightClickHeldDown || this.ammo(slot, gun, world, player) - 1 < 0)
		{
			compound.setBoolean(FIRING, false);
			compound.setInteger(TIMER, 0);
		}
		if(compound.getBoolean(FIRING))
		{
			float incline = 1.13F / ItemGun.delay(gun, player);
			compound.setFloat(CHARGE, compound.getFloat(CHARGE) + incline);
			ComponentEvents.heatUp(gun, player, Math.max(1, compound.getFloat(CHARGE)) / 20);
		}
		else
		{
			compound.setFloat(CHARGE, compound.getFloat(CHARGE) * 0.95F - 0.1F);
		}
		compound.setInteger(TIMER, 1 + compound.getInteger(TIMER));
		compound.setFloat(CHARGE, Math.max(0, Math.min(12, compound.getFloat(CHARGE))));
		if(compound.getFloat(CHARGE) > 0.5)
		{
			EntityLaser e = new EntityLaser(world, player, compound.getFloat(CHARGE), this.color, 0);
			
			if(!world.isRemote || true) //this needs to be spawned clientside as well, but only for laser entities
			{
				world.spawnEntityInWorld(e);
			}
			
			int t = (int)(12 / compound.getFloat(CHARGE));
			if(t <= 0 || compound.getInteger(TIMER) % t == t - 1)
			{
				this.setAmmo(slot, -1, gun, world, player);
			}
		}
		if(world.isRemote)
		{
			if(compound.getFloat(CHARGE) > 1)
			{
				if(AudioHandler.getSound(player, "fire.fire") == null || !AudioHandler.isPlaying(player, "fire.fire"))
				{
					AudioHandler.createMovingEntitySound(player, "fire.fire", 1.8F + world.rand.nextFloat(), world.rand.nextFloat() * 0.5F + 0.1F, false);
				}
			}
			if(compound.getFloat(CHARGE) <= 0)
			{
				AudioHandler.stopSound(player, "fire.fire");
			}
			if(AudioHandler.getSound(player, "fire.fire") != null && AudioHandler.getSound(player, "fire.fire") instanceof MovingSoundPublic)
			{
				((MovingSoundPublic)AudioHandler.getSound(player, "fire.fire")).setVolume(compound.getFloat(CHARGE) / 2);
			}
		}
		this.capAmmo(slot, gun, world, player);
		super.update(slot, gun, world, player, invSlot, isSelected);
	}
	
	@Override
	public int capacity(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return super.capacity(slot, stack, world, player) * ammoMultiplier;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.battery / 2 + this.capacity(slot, stack, world, player) / 32;
	}
	
	@Override
	public float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed + 299792458 / 20; //the speed of light
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player)
	{
		ExtendedPlayer props = ExtendedPlayer.get(player);
		this.getTagCompound(slot, stack).setBoolean(FIRING, props.isRightClickHeldDown);
		e.clear();
		return e;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item diode = M.laser_diode_green_strong;
		switch(this.material)
		{
		case IRON:
		{
			switch(this.color)
			{
			case CYAN:
				break;
			case GREEN:
				diode = M.laser_diode_green_strong;
				break;
			case RED:
				diode = M.laser_diode_red_strong;
				break;
			case VIOLET:
				break;
			}
			break;
		}
		case GOLD:
		{
			switch(this.color)
			{
			case CYAN:
				break;
			case GREEN:
				diode = M.magazine_laser_iron_green.item;
				break;
			case RED:
				diode = M.magazine_laser_iron_red.item;
				break;
			case VIOLET:
				break;
			}
			break;
		}
		case DIAMOND:
		{
			switch(this.color)
			{
			case CYAN:
				break;
			case GREEN:
				diode = M.magazine_laser_gold_green.item;
				break;
			case RED:
				diode = M.magazine_laser_gold_red.item;
				break;
			case VIOLET:
				break;
			}
			break;
		}
		}
		switch(this.material)
		{
		case IRON:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"iir", "lbd", "iir", 'i', "ingotIron", 'r', "dustRedstone", 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode}));
			break;
		case GOLD:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"iir", "lbd", "iir", 'i', "ingotGold", 'r', "dustRedstone", 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode}));
			break;
		case DIAMOND:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"iir", "lbd", "iir", 'i', "gemDiamond", 'r', "dustRedstone", 'l', M.eyepiece, 'b', M.barrel_thin_diamond.item, 'd', diode}));
			break;
		}
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
	
	@Override
	public Item ammoItem()
	{
		return Items.redstone;
	}
	
	@Override
	public int projectileCount(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return 1;
	}
}