package com.spectral.spectral_guns.components.magazine;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;

public class ComponentMagazineShuriken extends ComponentMagazineStandard
{
	public ComponentMagazineShuriken(ComponentMaterial material, int capacity, float kickback, float recoil, float speed, float fireRate, int projectileCount, float heating)
	{
		super("shuriken", "shuriken", 0.2, 3 * 10 * 3 / 6, material, capacity, kickback, recoil, speed, fireRate, projectileCount, 0.1F * heating);
	}
	
	@Override
	protected Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		EntityShuriken e = new EntityShuriken(world, player, 2)
		{
			@Override
			public boolean isInRangeToRenderDist(double distance)
			{
				return super.isInRangeToRenderDist(distance / 16);
			}
		};
		e.spinVelocity *= 4;
		return e;
	}
	
	@Override
	public Item ammoItem()
	{
		return M.shuriken;
	}
	
	@Override
	protected void fireSound(int slot, Entity projectile, ItemStack stack, World world, EntityPlayer player)
	{
		world.playSoundAtEntity(player, "tile.piston.out", 0.2F, world.rand.nextFloat() * 0.25F + 0.6F);
		//world.playSoundAtEntity(player, "random.bow", 0.1F, 1 + 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
		world.playSoundAtEntity(player, "tile.piston.in", 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
	}
	
	@Override
	public void registerRecipe()
	{
		Object bar = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			bar = "plankWood";
			break;
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
		Item magazine = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			magazine = Item.getItemFromBlock(Blocks.dropper);
			break;
		case IRON:
			magazine = M.magazine_shuriken_wood.item;
			break;
		case GOLD:
			magazine = M.magazine_shuriken_iron.item;
			break;
		case DIAMOND:
			magazine = M.magazine_shuriken_gold.item;
			break;
		}
		Item barrel = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			magazine = M.barrel_normal_wood.item;
			break;
		case IRON:
			magazine = M.barrel_normal_iron.item;
			break;
		case GOLD:
			magazine = M.barrel_normal_gold.item;
			break;
		case DIAMOND:
			magazine = M.barrel_normal_diamond.item;
			break;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"bMb", "BSP", "bJb", 'b', bar, 'M', magazine, 'B', barrel, 'S', Items.stick, 'P', Item.getItemFromBlock(Blocks.piston), 'J', Item.getItemFromBlock(Blocks.jukebox)}));
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}