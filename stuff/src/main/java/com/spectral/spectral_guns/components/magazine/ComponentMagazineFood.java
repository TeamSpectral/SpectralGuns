package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.items.ItemFood2;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ComponentMagazineFood extends ComponentMagazine
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
		return new EntityFood(world, player, getLastItem(stack));
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

		for(int i = 0; i < 64 && world.isRemote; ++i)
		{
			Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX+Randomization.r(spread), (float)projectile.motionY+Randomization.r(spread), (float)projectile.motionZ+Randomization.r(spread)), ItemGun.speed(stack, player)/4*world.rand.nextFloat());
			world.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[] {Item.getIdFromItem(ammoItem())});
		}

		world.playSoundAtEntity(player, "mob.slime.big", 2.0F, 1+world.rand.nextFloat());
		world.playSoundAtEntity(player, "mob.slime.small", 2.0F, 1+world.rand.nextFloat());
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);

	}

	@Override
	@SuppressWarnings("incomplete-switch")
	public void registerRecipe()
	{
		switch(material)
		{
		case WOOD: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"PGb", "DHC", "PPb", 'P', Item.getItemFromBlock(Blocks.wooden_pressure_plate), 'G', M.gear_wood, 'b', Item.getItemFromBlock(Blocks.planks), 'D', Item.getItemFromBlock(Blocks.dispenser), 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}); break;
		case IRON: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"PGb", "MHC", "PPb", 'P', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'G', M.gear_iron, 'b', Items.iron_ingot, 'M', M.magazine_food_wood.item, 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}); break;
		case GOLD: GameRegistry.addShapedRecipe(new ItemStack(this.item),  new Object[]{"PGb", "MHC", "PPb", 'P', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'G', M.gear_gold, 'b', Items.gold_ingot, 'M', M.magazine_food_iron.item, 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}); break;
		}
	}

	@Override
	public boolean isValid(ArrayList<Component> ecs)
	{
		for(int i = 0; i < ecs.size(); ++i)
		{
			if(ecs.get(i).material == ComponentMaterial.DIAMOND)
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
			ItemStack stack = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(items.tagCount()-1));
			removeLastItem(gun);
			return stack;
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

	protected void removeLastItem(ItemStack gun)
	{
		NBTTagList items = getItemsNBT(gun);
		if(items.tagCount() > 0)
		{
			items.removeTag(items.tagCount()-1);
		}
		this.getTagCompound(gun).setTag(ITEMS, items);
	}

	protected void addItem(ItemStack gun)
	{
		if(lastUsedStack == null)
		{
			lastUsedStack = new ItemStack(ammoItem());
		}
		if(lastUsedStack != null)
		{
			lastUsedStack.stackSize = 1;
			NBTTagList items = getItemsNBT(gun);
			NBTTagCompound stack = lastUsedStack.writeToNBT(new NBTTagCompound());
			if(stack != null)
			{
				items.appendTag(stack);
			}
			this.getTagCompound(gun).setTag(ITEMS, items);
		}

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
				lastUsedStack = stack.copy();
			}
			return true;
		}
		return false;
	}
}