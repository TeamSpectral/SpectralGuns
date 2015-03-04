package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Coordinates3D.Coords3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.entity.projectile.EntitySnowballDamaging;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class Components
{
	public static float thin = 0.4F;
	public static float normal = 3.5F;
	public static float wide = 18F;
	public static float musket = 70F;
	
	public static final class ComponentMagazineSnowball extends ComponentMagazine
	{
		public ComponentMagazineSnowball(String id, String name, Component[] required, Component[] incapatible, int capacity, float kickback, float spread, float fireRate, int projectileCount)
		{
			super(id, name, required, incapatible, capacity, kickback, spread, fireRate, projectileCount);
		}

		@Override
		protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
		{
			EntitySnowball e = new EntitySnowballDamaging(world, player);
			
			return e;
		}

		@Override
		protected void fireSound(Entity projectile, ItemStack stack, World world, EntityPlayer player)
		{
			float spread = ItemGun.spread(stack, player)+0.01F;
			
			for(int i = 0; i < 32; ++i)
			{
				Coords3D m = Coordinates3D.stabilize(new Coords3D((float)projectile.motionX+Randomization.r(spread), (float)projectile.motionY+Randomization.r(spread), (float)projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.9F+0.1F));
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, projectile.posX, projectile.posY, projectile.posZ, m.x, m.y, m.z, new int[] {Item.getIdFromItem(Items.snowball)});
			}
			
			world.playSoundAtEntity(player, "random.bow", 0.1F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
			world.playSoundAtEntity(player, "dig.snow", 2.8F, 1.3F / (world.rand.nextFloat() * 0.4F + 0.8F));
		}
	}
}
