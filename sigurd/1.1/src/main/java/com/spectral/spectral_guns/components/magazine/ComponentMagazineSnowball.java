package com.spectral.spectral_guns.components.magazine;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntitySnowball2;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ComponentMagazineSnowball extends ComponentMagazine
{
	public ComponentMagazineSnowball(Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
	{
		super("snowball", "snowball", required, incapatible, material, capacity, kickback, 60, fireRate, projectileCount);
	}
	public ComponentMagazineSnowball(String id, String name, Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
	{
		super("snowball" + id, "snowball" + name, required, incapatible, material, capacity, kickback, 60, fireRate, projectileCount);
	}

	@Override
	protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
	{
		EntitySnowball e = new EntitySnowball2(world, player);

		return e;
	}

	@Override
	public Item ammoItem()
	{
		return Items.snowball;
	}

	@Override
	protected void fireSound(Entity projectile, ItemStack stack, World world, EntityPlayer player)
	{
		float spread = ItemGun.spread(stack, player)+0.03F;

		for(int i = 0; i < 32 && world.isRemote; ++i)
		{
			Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX+Randomization.r(spread), (float)projectile.motionY+Randomization.r(spread), (float)projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.9F+0.1F));
			world.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[] {Item.getIdFromItem(Items.snowball)});
		}

		world.playSoundAtEntity(player, "random.bow", 0.3F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
		world.playSoundAtEntity(player, "dig.snow", 3.8F, 1.3F / (world.rand.nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public void registerRecipe()
	{
		Item bar = Items.iron_ingot;
		switch(material)
		{
		case WOOD: bar = Item.getItemFromBlock(Blocks.planks); break;
		case IRON: bar = Items.iron_ingot; break;
		case GOLD: bar = Items.gold_ingot; break;
		case DIAMOND: bar = Items.diamond; break;
		}
		Item block = Item.getItemFromBlock(Blocks.iron_block);
		switch(material)
		{
		case WOOD: block = Item.getItemFromBlock(Blocks.log); break;
		case IRON: block = Item.getItemFromBlock(Blocks.iron_block); break;
		case GOLD: block = Item.getItemFromBlock(Blocks.gold_block); break;
		case DIAMOND: block = Item.getItemFromBlock(Blocks.diamond_block); break;
		}
		Item magazine = Items.iron_ingot;
		switch(material)
		{
		case WOOD: magazine = Item.getItemFromBlock(Blocks.dispenser); break;
		case IRON: magazine = M.magazine_snowball_wood.item; break;
		case GOLD: magazine = M.magazine_snowball_iron.item; break;
		case DIAMOND: magazine = M.magazine_snowball_gold.item; break;
		}
		GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"BbC", "SMP", "Bb ", 'B', block, 'b', bar, 'M', magazine, 'S', Item.getItemFromBlock(Blocks.snow), 'P', Items.gunpowder, 'C', M.container});
	}
}