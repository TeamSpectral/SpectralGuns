package com.spectral.spectral_guns.components;

import java.util.ArrayList;
import java.util.Random;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component.ComponentMaterial;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.entity.projectile.EntitySmallFireballSpecial;
import com.spectral.spectral_guns.entity.projectile.EntitySnowballSpecial;
import com.spectral.spectral_guns.items.ItemFood2;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Components
{
	public static final class ComponentMagazineSnowball extends ComponentMagazine
	{
		public ComponentMagazineSnowball(Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
		{
			super("snowball", "snowball", required, incapatible, material, capacity, kickback, 60, fireRate, projectileCount);
		}

		@Override
		protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
		{
			EntitySnowball e = new EntitySnowballSpecial(world, player);

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

			for(int i = 0; i < 32; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX+Randomization.r(spread), (float)projectile.motionY+Randomization.r(spread), (float)projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.9F+0.1F));
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[] {Item.getIdFromItem(Items.snowball)});
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

	public static final class ComponentMagazineSmallFireball extends ComponentMagazine
	{
		public ComponentMagazineSmallFireball(Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
		{
			super("small_fireball", "fireballSmall", required, incapatible, material, capacity, kickback, 30, fireRate, projectileCount);
		}

		@Override
		protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
		{
			return new EntitySmallFireballSpecial(world);
		}

		@Override
		public Item ammoItem()
		{
			return Items.coal;
		}

		@Override
		protected void fireSound(Entity projectile, ItemStack stack, World world, EntityPlayer player)
		{
			float spread = ItemGun.spread(stack, player)+0.03F;

			for(int i = 0; i < 16; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX+Randomization.r(spread), projectile.motionY+Randomization.r(spread), projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.1F+0.3F));
				world.spawnParticle(EnumParticleTypes.FLAME, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
			}
			for(int i = 0; i < 32; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX+Randomization.r(spread), projectile.motionY+Randomization.r(spread), projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.7F+0.5F));
				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
			}
			for(int i = 0; i < 4; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3(projectile.motionX+Randomization.r(spread), projectile.motionY+Randomization.r(spread), projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*(world.rand.nextFloat()*0.3F+0.2F));
				world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[0]);
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
			case IRON: bar = Items.iron_ingot; break;
			case GOLD: bar = Items.gold_ingot; break;
			case DIAMOND: bar = Items.diamond; break;
			}
			Item block = Item.getItemFromBlock(Blocks.iron_block);
			switch(material)
			{
			case IRON: block = Item.getItemFromBlock(Blocks.iron_block); break;
			case GOLD: block = Item.getItemFromBlock(Blocks.gold_block); break;
			case DIAMOND: block = Item.getItemFromBlock(Blocks.diamond_block); break;
			}
			Item magazine = Items.iron_ingot;
			switch(material)
			{
			case IRON: magazine = Item.getItemFromBlock(Blocks.dispenser); break;
			case GOLD: magazine = M.magazine_small_fireball_iron.item; break;
			case DIAMOND: magazine = M.magazine_small_fireball_gold.item; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"bBB", "MCF", "bBB", 'B', block, 'b', bar, 'M', magazine, 'C', Items.fire_charge, 'F', Item.getItemFromBlock(Blocks.furnace)});
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
		public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
		{
			return delay + capacity/32 + 4;
		}
	}

	public static final class ComponentMagazineFood extends ComponentMagazine
	{
		protected ItemStack lastUsedStack = null;

		//nbt
		public static final String ITEMS = "Items";

		public ComponentMagazineFood(Component[] required, Component[] incapatible, ComponentMaterial material, int capacity, float kickback, float fireRate, int projectileCount)
		{
			super("food", "food", required, incapatible, material, capacity, kickback, 90, fireRate, projectileCount);
		}

		@Override
		protected Entity projectile(ItemStack stack, World world, EntityPlayer player)
		{
			return new EntityFood(world, getLastItem(stack));
		}

		@Override
		public Item ammoItem()
		{
			return M.food_mush;
		}

		@Override
		public boolean isAmmoItem(ItemStack stack, World world, EntityPlayer player)
		{
			if(this.isItemValid(stack, true))
			{
				lastUsedStack = stack;
				return true;
			}
			else
			{
				return false;
			}
		}

		@Override
		protected void fireSound(Entity projectile, ItemStack stack, World world, EntityPlayer player)
		{
			float spread = ItemGun.spread(stack, player)+0.03F;

			for(int i = 0; i < 64; ++i)
			{
				Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX+Randomization.r(spread), (float)projectile.motionY+Randomization.r(spread), (float)projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*world.rand.nextFloat());
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[] {Item.getIdFromItem(ammoItem())});
			}

			world.playSoundAtEntity(player, "mob.slime.big", 2.0F, 1+world.rand.nextFloat());
			world.playSoundAtEntity(player, "mob.slime.small", 2.0F, 1+world.rand.nextFloat());
		}

		@Override
		@SuppressWarnings("incomplete-switch")
		public void registerRecipe()
		{
			Item bar = Items.iron_ingot;
			switch(material)
			{
			case WOOD: bar = Item.getItemFromBlock(Blocks.planks); break;
			case IRON: bar = Items.iron_ingot; break;
			case GOLD: bar = Items.gold_ingot; break;
			}
			Item block = Item.getItemFromBlock(Blocks.iron_block);
			switch(material)
			{
			case WOOD: block = Item.getItemFromBlock(Blocks.log); break;
			case IRON: block = Item.getItemFromBlock(Blocks.iron_block); break;
			case GOLD: block = Item.getItemFromBlock(Blocks.gold_block); break;
			}
			Item magazine = Items.iron_ingot;
			switch(material)
			{
			case WOOD: magazine = Item.getItemFromBlock(Blocks.dispenser); break;
			case IRON: magazine = M.magazine_food_wood.item; break;
			case GOLD: magazine = M.magazine_food_iron.item; break;
			}
			GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"bBB", "MCF", "bBB", 'B', block, 'b', bar, 'M', magazine, 'C', Items.fire_charge, 'F', Item.getItemFromBlock(Blocks.furnace)});
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
		public int setAmmo(int ammo, ItemStack stack, World world, EntityPlayer player, ArrayList<Component> components)
		{
			int result =  super.setAmmo(ammo, stack, world, player, components);
			sortItems(stack);
			return result;
		}

		@Override
		public float delay(float delay, ItemStack stack, World world,EntityPlayer player, ArrayList<Component> components)
		{
			return delay + capacity/32 + 4;
		}

		protected NBTTagList getItemsNBT(ItemStack gun)
		{
			NBTTagCompound compound = this.getTagCompound(gun);

			NBTTagList items = compound.getTagList(ITEMS, new NBTTagCompound().getId());
			if(items == null)
			{
				items = new NBTTagList();
				compound.setTag(ITEMS, items);
			}
			for(int i = 0; i < items.tagCount(); ++i)
			{
				NBTTagCompound stackCompound = items.getCompoundTagAt(i);
				if(stackCompound == null)
				{
					items.removeTag(i);
					--i;
					continue;
				}
				else
				{
					ItemStack stack = ItemStack.loadItemStackFromNBT(stackCompound);
					if(stack == null || !this.isItemValid(stack, false))
					{
						items.removeTag(i);
						--i;
						continue;
					}
				}
			}
			this.getTagCompound(gun).setTag(ITEMS, items);
			return items;
		}

		protected ArrayList<ItemStack> getItems(ItemStack gun)
		{
			ArrayList<ItemStack> a = new ArrayList<ItemStack>();
			NBTTagList items = getItemsNBT(gun);

			for(int i = 0; i < items.tagCount(); ++i)
			{
				NBTTagCompound stackCompound = items.getCompoundTagAt(i);
				if(stackCompound != null)
				{
					ItemStack stack = ItemStack.loadItemStackFromNBT(stackCompound);
					if(stack != null && this.isItemValid(stack, false))
					{
						a.add(stack);
					}
					else
					{
						items.removeTag(i);
						--i;
					}
				}
				else
				{
					items.removeTag(i);
					--i;
				}
			}
			this.getTagCompound(gun).setTag(ITEMS, items);

			return a;
		}
		
		protected ItemStack getLastItem(ItemStack gun)
		{
			NBTTagList items = getItemsNBT(gun);
			if(items.tagCount() > 0)
			{
				return ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(items.tagCount()-1));
			}
			return new ItemStack(ammoItem());
		}

		protected void removeFirstItem(ItemStack gun)
		{
			NBTTagList items = getItemsNBT(gun);
			if(items.tagCount() > 0)
			{
				items.removeTag(0);
			}
			this.getTagCompound(gun).setTag(ITEMS, items);
		}

		protected void addItem(ItemStack gun)
		{
			if(lastUsedStack == null)
			{
				lastUsedStack = new ItemStack(ammoItem());
			}
			lastUsedStack.stackSize = 1;
			NBTTagList items = getItemsNBT(gun);
			NBTTagCompound stack = lastUsedStack.writeToNBT(new NBTTagCompound());
			items.appendTag(stack);
			this.getTagCompound(gun).setTag(ITEMS, items);
			
			lastUsedStack = null;
		}

		protected void sortItems(ItemStack gun)
		{
			int ammo = this.getTagCompound(gun).getInteger(AMMO);
			while(getItemsNBT(gun).tagCount() > ammo)
			{
				removeFirstItem(gun);
			}
			while(getItemsNBT(gun).tagCount() < ammo)
			{
				addItem(gun);
			}
		}

		public boolean isItemValid(ItemStack stack, boolean flag)
		{
			if(stack.getItem() == ammoItem())
			{
				if(flag)
				{
					lastUsedStack = null;
				}
				return true;
			}
			else if((stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemFood2) && stack.getItem().getRarity(stack) == EnumRarity.COMMON)
			{
				if(flag)
				{
					lastUsedStack = stack;
				}
				return true;
			}
			return false;
		}
	}
}
