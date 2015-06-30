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
import com.spectral.spectral_guns.entity.projectile.EntitySnowball2;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentMagazineSnowball extends ComponentMagazineStandard
{
	public ComponentMagazineSnowball(ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount, float heating)
	{
		super("snowball", "snowball", 0.3, 4 * 4 * 9, material, capacity, kickback, 60, fireRate, projectileCount, 4.2F * heating);
	}
	
	@Override
	protected Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return new EntitySnowball2(world, player);
	}
	
	@Override
	public Item ammoItem()
	{
		return Items.snowball;
	}
	
	@Override
	protected void fireSound(int slot, Entity projectile, ItemStack stack, World world, EntityPlayer player)
	{
		float spread = ItemGun.spread(stack, player) + 0.03F;
		
		for(int i = 0; i < 32 && world.isRemote; ++i)
		{
			Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX + Randomization.r(spread), (float)projectile.motionY + Randomization.r(spread), (float)projectile.motionZ + Randomization.r(spread)), ItemGun.speed(stack, player) / 4 * (world.rand.nextFloat() * 0.9F + 0.1F));
			world.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[]{Item.getIdFromItem(Items.snowball)});
		}
		
		world.playSoundAtEntity(player, "random.bow", 0.3F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
		world.playSoundAtEntity(player, "dig.snow", 3.8F, 1.3F / (world.rand.nextFloat() * 0.4F + 0.8F));
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
		Object block = Item.getItemFromBlock(Blocks.iron_block);
		switch(this.material)
		{
		case WOOD:
			block = "logWood";
			break;
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
		case WOOD:
			magazine = Item.getItemFromBlock(Blocks.dispenser);
			break;
		case IRON:
			magazine = M.magazine_snowball_wood.item;
			break;
		case GOLD:
			magazine = M.magazine_snowball_iron.item;
			break;
		case DIAMOND:
			magazine = M.magazine_snowball_gold.item;
			break;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"BbC", "SMP", "Bb ", 'B', block, 'b', bar, 'M', magazine, 'S', Item.getItemFromBlock(Blocks.snow), 'P', Items.gunpowder, 'C', M.container}));
	}
	
	@Override
	public void update(int slot, ItemStack stack, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		super.update(slot, stack, world, player, invSlot, isSelected);
		if(this.ammo(slot, stack, world, player) > 0 && this.heat(slot, stack) <= 0)
		{
			this.addHeat(slot, -5, stack);
		}
	}
}