package com.spectral.spectral_guns.stats;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.spectral.spectral_guns.components.Component.ComponentTraits;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineFood;
import com.spectral.spectral_guns.components.magazine.ComponentMagazineLaser;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;
import com.spectral.spectral_guns.items.ItemGun;

public final class Legendaries
{
	public static final int sigurds_funky_lasergun_timing = 10;
	
	public static final LegendaryBase spectral_taco = new LegendaryBase("Spectral Taco", EnumChatFormatting.GOLD, 30, null);
	public static final LegendaryBase sigurds_funky_lasergun = new LegendaryBase("Sigurd's Funky Lasergun", EnumChatFormatting.LIGHT_PURPLE, 10, null)
	{
		@Override
		public EnumChatFormatting getColor(ItemStack stack, EntityPlayer player)
		{
			int i = (int)Math.floor((float)player.ticksExisted / Legendaries.sigurds_funky_lasergun_timing % LaserColor.values().length);
			return LaserColor.values()[i].formatting;
		}
		
		@Override
		public boolean isValid(ItemStack stack)
		{
			return super.isValid(stack) && ItemGun.getComponentsOf(stack, ComponentMagazineLaser.class).size() > 0;
		}
	};
	public static final LegendaryBase baconslinger = new LegendaryBase("Baconslinger", EnumChatFormatting.RED, 10, null)
	{
		@Override
		public boolean isValid(ItemStack stack)
		{
			return super.isValid(stack) && ItemGun.getComponentsOf(stack, ComponentMagazineFood.class).size() > 0;
		}
	};
	/** kai i made your gun extra shiny. (oh gosh what have i done D:>) **/
	public static final LegendaryBase beardies_shiny_gun = new LegendaryParticles("Beardie's Shiny Gun", EnumChatFormatting.YELLOW, 10, null, 5, 0.1, 1)
	{
		@Override
		public boolean isValid(ItemStack stack)
		{
			return super.isValid(stack) && ItemGun.hasComponentTrait(stack, ComponentTraits.SHINY);
		}
		
		@Override
		public void particleOnFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier, double x, double y, double z)
		{
			world.spawnParticle(this.getParticleType(world.rand), x, y, z, 0, 0, 0, new int[0]);
		}
		
		@Override
		public void particleOnUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected, double x, double y, double z)
		{
			world.spawnParticle(this.getParticleType(world.rand), x, y, z, 0, 0, 0, new int[0]);
		}
		
		public EnumParticleTypes getParticleType(Random rand)
		{
			EnumParticleTypes[] particles = {EnumParticleTypes.CRIT_MAGIC, EnumParticleTypes.FIREWORKS_SPARK, EnumParticleTypes.VILLAGER_HAPPY};
			return particles[rand.nextInt(particles.length)];
		}
	};
	/**
	 * KH, please specify more stuff for your gun cuzzzzz i have no idea what to
	 * even arhurehhrghrhuhrhwhyyyyyyyyyyyyyy...
	 **/
	public static final LegendaryBase kh = new LegendaryBase("KH", EnumChatFormatting.AQUA, 10, null);
	
	public Legendaries()
	{
		
	}
}
