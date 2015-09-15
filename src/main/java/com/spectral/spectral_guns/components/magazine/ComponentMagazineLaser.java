package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.audio.AudioHandler;
import com.spectral.spectral_guns.audio.MovingSoundPublic;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.itemtags.ItemTagBoolean;
import com.spectral.spectral_guns.itemtags.ItemTagFloat;
import com.spectral.spectral_guns.itemtags.ItemTagInteger;
import com.spectral.spectral_guns.stats.Legendaries;
import com.spectral.spectral_guns.stats.Legendary;

public class ComponentMagazineLaser extends ComponentMagazine
{
	public final LaserColor color;
	
	public final static ItemTagBoolean FIRING = new ItemTagBoolean("IsFiring", false, true);
	public final static ItemTagFloat CHARGE = new ItemTagFloat("Charge", 0F, 0F, 12F, true);
	public final static ItemTagInteger TIMER = new ItemTagInteger("Timer", 0, 0, Integer.MAX_VALUE, true);
	public final static int ammoMultiplier = 25;
	public final int battery;
	public final float kickback;
	public final static float speedOfLight = 299792458 / 20;
	
	public ComponentMagazineLaser(ComponentMaterial material, LaserColor color, int capacity, int battery, float kickback)
	{
		super(new String2("laser", "_" + color.toString().toLowerCase()), new String2("laser", "." + color.toString().toLowerCase()), 0.4, 3 * 9 * 3, material, capacity, 4.6F);
		this.color = color;
		this.battery = battery;
		this.kickback = kickback;
		this.addRequiredComponent(M.barrel_thin_diamond);
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("Capacity:", this.capacity(-1, new ItemStack(M.gun), world, player) + "/" + this.battery));
		tooltip.add(new String2("Color:", this.color.formatting + Stuff.Strings.capitalize(this.color.formatting.name().toLowerCase())));
		tooltip.add(new String2("Recoil:", "" + 0));
		tooltip.add(new String2("Kickback:", "" + this.kickback));
		tooltip.add(new String2("Speed:", "" + speedOfLight));
		tooltip.add(new String2("ProjectileCount:", "" + 1));
		tooltip.add(new String2("FireRate:", "" + 0));
		tooltip.add(new String2("Delay:", this.ADDS(this.delay(-1, 0, new ItemStack(M.gun), world, player))));
	}
	
	@Override
	public String projectileName()
	{
		return new EntityLaser(M.proxy.world(0)).getName();
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
				FIRING.set(gun, compound, true);
			}
		}
		if(!isSelected || !ExtendedPlayer.get(player).isRightClickHeldDown || this.ammo(slot, gun, world, player) - 1 < 0)
		{
			FIRING.set(gun, compound, false);
			TIMER.set(gun, compound, 0);
		}
		if(FIRING.get(compound, true))
		{
			float incline = 1.13F / ItemGun.delay(gun, player);
			CHARGE.wobbleCheck(gun);
			CHARGE.add(compound, incline);
			double d = Math.max(1, CHARGE.get(compound, true)) / 20;
			ComponentEvents.heatUp(gun, player, d);
		}
		else
		{
			CHARGE.set(compound, CHARGE.get(compound, true) * 0.95F - 0.1F);
		}
		TIMER.add(compound, 1);
		if(CHARGE.get(compound, true) > 0)
		{
			EntityLaser e = new EntityLaser(world, player, CHARGE.get(compound, true), this.color, 0);
			
			if(Legendary.getLegendaryForGun(gun) == Legendaries.sigurds_funky_lasergun)
			{
				int i = (int)Math.floor((float)player.ticksExisted / Legendaries.sigurds_funky_lasergun_timing % LaserColor.values().length);
				e.color = LaserColor.values()[i];
			}
			
			if(CHARGE.get(compound, true) > 0.2)
			{
				if(!world.isRemote || true) //this needs to be spawned clientside as well, but only for laser entities
				{
					world.spawnEntityInWorld(e);
				}
				
				int t = (int)(12 / CHARGE.get(compound, true));
				if(t <= 0 || TIMER.get(compound, true) % t == t - 1)
				{
					this.setAmmo(slot, -1, gun, world, player);
				}
			}
			
			ArrayList<Entity> es = new ArrayList();
			es.add(e);
			Vec3 mot1 = Stuff.Coordinates3D.velocity(player);
			((ItemGun)gun.getItem()).kickBack(gun, player, es);
			Vec3 mot2 = Stuff.Coordinates3D.velocity(player);
			int x = 30;
			Vec3 mot = Stuff.Coordinates3D.divide(Stuff.Coordinates3D.add(Stuff.Coordinates3D.multiply(mot1, x), mot2), x + 1);
			player.motionX = mot.xCoord;
			player.motionY = mot.yCoord;
			player.motionZ = mot.zCoord;
			if(FIRING.get(compound, true))
			{
				double d = Math.max(1, CHARGE.get(compound, true)) / 20;
				ComponentEvents.onFireLegendary(e, gun, player, d);
			}
		}
		if(world.isRemote)
		{
			if(CHARGE.get(compound, true) > 1)
			{
				if(AudioHandler.getSound(player, "fire.fire") == null || !AudioHandler.isPlaying(player, "fire.fire"))
				{
					AudioHandler.createMovingEntitySound(player, "fire.fire", 1.8F + world.rand.nextFloat(), world.rand.nextFloat() * 0.5F + 0.1F, false);
				}
			}
			if(CHARGE.get(compound, true) <= 0)
			{
				AudioHandler.stopSound(player, "fire.fire");
			}
			if(AudioHandler.getSound(player, "fire.fire") != null && AudioHandler.getSound(player, "fire.fire") instanceof MovingSoundPublic)
			{
				((MovingSoundPublic)AudioHandler.getSound(player, "fire.fire")).setVolume(CHARGE.get(compound, true) / 2);
			}
		}
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
		return kickback + this.kickback * CHARGE.get(this.getTagCompound(slot, stack), true);
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed + Float.MAX_VALUE; //the speed of light
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
		FIRING.set(stack, this.getTagCompound(slot, stack), props.isRightClickHeldDown);
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