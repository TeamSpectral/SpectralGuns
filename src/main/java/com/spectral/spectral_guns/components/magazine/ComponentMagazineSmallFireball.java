package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.entity.projectile.EntitySmallFireball2;
import com.spectral.spectral_guns.items.ItemGun;

public final class ComponentMagazineSmallFireball extends ComponentMagazine
{
	public ComponentMagazineSmallFireball(Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
	{
		super("small_fireball", "fireballSmall", required, incapatible, material, capacity, kickback, 30, fireRate, projectileCount);
	}
	
	@Override
	protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
	{
		return new EntitySmallFireball2(world, player, 0, 0, 0);
	}
	
	@Override
	public Item ammoItem()
	{
		return Items.coal;
	}
	
	@Override
	protected void fireSound(Entity projectile, ItemStack stack, World world, EntityPlayer player)
	{
		float spread = ItemGun.spread(stack, player) + 0.03F;
		
		if(world.isRemote)
		{
			for(int i = 0; i < 16; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX + Randomization.r(spread), projectile.motionY + Randomization.r(spread), projectile.motionZ + Randomization.r(spread)), ItemGun.speed(stack, player) / 4 * (world.rand.nextFloat() * 0.1F + 0.3F));
				world.spawnParticle(EnumParticleTypes.FLAME, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
			}
			for(int i = 0; i < 32; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX + Randomization.r(spread), projectile.motionY + Randomization.r(spread), projectile.motionZ + Randomization.r(spread)), ItemGun.speed(stack, player) / 4 * (world.rand.nextFloat() * 0.7F + 0.5F));
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
			}
			for(int i = 0; i < 4; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX + Randomization.r(spread), projectile.motionY + Randomization.r(spread), projectile.motionZ + Randomization.r(spread)), ItemGun.speed(stack, player) / 4 * (world.rand.nextFloat() * 0.3F + 0.2F));
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
			}
		}
		
		world.playSoundAtEntity(projectile, "mob.ghast.fireball", 2.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
		world.playSoundAtEntity(player, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
	}
	
	@Override
	@SuppressWarnings("incomplete-switch")
	public void registerRecipe()
	{
		Item bar = Items.iron_ingot;
		switch(material)
		{
		case IRON:
			bar = Items.iron_ingot;
			break;
		case GOLD:
			bar = Items.gold_ingot;
			break;
		case DIAMOND:
			bar = Items.diamond;
			break;
		}
		Item block = Item.getItemFromBlock(Blocks.iron_block);
		switch(material)
		{
		case IRON:
			block = Item.getItemFromBlock(Blocks.iron_block);
			break;
		case GOLD:
			block = Item.getItemFromBlock(Blocks.gold_block);
			break;
		case DIAMOND:
			block = Item.getItemFromBlock(Blocks.diamond_block);
			break;
		}
		Item magazine = Items.iron_ingot;
		switch(material)
		{
		case IRON:
			magazine = Item.getItemFromBlock(Blocks.dispenser);
			break;
		case GOLD:
			magazine = M.magazine_small_fireball_iron.item;
			break;
		case DIAMOND:
			magazine = M.magazine_small_fireball_gold.item;
			break;
		}
		GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]
			{
					"iBb",
					"MCF",
					"iBB",
					'B',
					block,
					'i',
					bar,
					'M',
					magazine,
					'C',
					Items.fire_charge,
					'F',
					Item.getItemFromBlock(Blocks.furnace),
					'b',
					Items.lava_bucket
			});
	}
	
	@Override
	public boolean isValid(ArrayList<Component> ecs)
	{
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).material == ComponentMaterial.WOOD)
			{
				return false;
			}
		}
		return super.isValid(ecs);
	}
	
	@Override
	public float delay(float delay, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
	{
		return delay + capacity / 32 + 4;
	}
}