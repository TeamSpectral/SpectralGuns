package com.spectral.spectral_guns.components.magazine;

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
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.entity.projectile.EntityFireball2;
import com.spectral.spectral_guns.items.ItemGun;

public final class ComponentMagazineSmallFireball extends ComponentMagazineStandard
{
	public ComponentMagazineSmallFireball(ComponentMaterial material, int capacity, float kickback, float recoil, float speed, float fireRate, int projectileCount, float heating)
	{
		super("small_fireball", "fireballSmall", 0.2, 5 * 5 * 8, material, capacity, kickback, recoil, speed, fireRate * 3, projectileCount, 86.9F * heating);
	}
	
	@Override
	protected Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return new EntityFireball2(world, player, 0, 0, 0)
		{
			@Override
			public boolean isInRangeToRenderDist(double distance)
			{
				return super.isInRangeToRenderDist(distance / 16);
			}
		};
	}
	
	@Override
	public boolean doSpray(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public Item ammoItem()
	{
		return Items.fire_charge;
	}
	
	@Override
	protected void fireSound(int slot, Entity projectile, ItemStack stack, World world, EntityPlayer player)
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
		Object bar = Items.iron_ingot;
		switch(this.material)
		{
		case IRON:
			bar = "ingotIron";
			break;
		case GOLD:
			bar = "ingotGold";
			break;
		case DIAMOND:
			bar = "gemDiamond";
			break;
		}
		Object block = Item.getItemFromBlock(Blocks.iron_block);
		switch(this.material)
		{
		case IRON:
			block = "blockIron";
			break;
		case GOLD:
			block = "blockGold";
			break;
		case DIAMOND:
			block = "blockDiamond";
			break;
		}
		Item magazine = Items.iron_ingot;
		switch(this.material)
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
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"iBb", "MCF", "iBB", 'B', block, 'i', bar, 'M', magazine, 'C', Items.fire_charge, 'F', Item.getItemFromBlock(Blocks.furnace), 'b', Items.lava_bucket}));
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.capacity / 32 + 4;
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}