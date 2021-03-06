package com.spectral.spectral_guns.components.magazine;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.projectile.EntityFood;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.itemtags.ItemTagList;
import com.spectral.spectral_guns.stats.Legendaries;
import com.spectral.spectral_guns.stats.Legendary;

public final class ComponentMagazineFood extends ComponentMagazineStandard
{
	protected ItemStack lastUsedStack = null;
	protected ItemStack firedStack = null;
	protected long firedStackTick = 0;
	public final static int ammoMultiplier = 4;
	
	// nbt
	public static final ItemTagList ITEMS = new ItemTagList("Items", true);
	
	public ComponentMagazineFood(ComponentMaterial material, int capacity, float kickback, float recoil, float speed, float fireRate, int projectileCount, float heating)
	{
		super("food", "food", 0.4, 5 * 8 * 8, material, capacity, kickback, recoil, speed, fireRate, projectileCount, 5.2F * heating);
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("ProjectileCount:", "" + this.projectileCount(-1, new ItemStack(M.gun), world, player)));
	}
	
	@Override
	public int projectileCount(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		float f = super.projectileCount(slot, stack, world, player) * (1 + ItemGun.spread(stack, player) * 10);
		int i = (int)Math.floor(f);
		f = f - i;
		return i + (world.rand.nextFloat() <= f ? 1 : 0);
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> e, ItemStack stack, World world, EntityPlayer player)
	{
		e = super.fire(slot, e, stack, world, player);
		if(e.size() > 0)
		{
			HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
			for(Integer i : cs.keySet())
			{
				Component c = cs.get(i);
				if(c.hasMaterialTrait(ComponentTraits.SHINY))
				{
					c.addDurabilityDamage(i, shinyDamage, stack, player);
				}
			}
		}
		return e;
	}
	
	@Override
	protected Entity projectile(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		if(this.firedStack == null)
		{
			if(Legendary.getLegendaryForGun(stack) == Legendaries.baconslinger)
			{
				this.firedStack = new ItemStack(Items.cooked_porkchop);
			}
			else
			{
				this.firedStack = this.getLastItem(slot, stack);
			}
			this.firedStackTick = world.getTotalWorldTime();
		}
		return new EntityFood(world, player, this.firedStack)
		{
			@Override
			public boolean isInRangeToRenderDist(double distance)
			{
				return super.isInRangeToRenderDist(distance / 16);
			}
		};
	}
	
	@Override
	public Item ammoItem()
	{
		return M.food_mush;
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack)
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
	protected void fireSound(int slot, Entity projectile, ItemStack stack, World world, EntityPlayer player)
	{
		if(this.firedStack == null)
		{
			this.firedStack = new ItemStack(this.ammoItem());
		}
		float spread = ItemGun.spread(stack, player) + 0.03F;
		
		for(int i = 0; i < 64 && world.isRemote && projectile != null; ++i)
		{
			Vec3 m = Coordinates3D.stabilize(new Vec3((float)projectile.motionX + Randomization.r(spread), (float)projectile.motionY + Randomization.r(spread), (float)projectile.motionZ + Randomization.r(spread)), ItemGun.speed(stack, player) / 4 * world.rand.nextFloat());
			Item item2 = projectile instanceof EntityFood ? ((EntityFood)projectile).getItem() : this.firedStack.getItem();
			if(item2 != null)
			{
				world.spawnParticle(EnumParticleTypes.ITEM_CRACK, true, projectile.posX, projectile.posY, projectile.posZ, m.xCoord, m.yCoord, m.zCoord, new int[]{Item.getIdFromItem(world.rand.nextInt(3) == 0 ? item2 : this.ammoItem())});
			}
		}
		
		world.playSoundAtEntity(player, "mob.slime.big", 2.0F, 1 + world.rand.nextFloat());
		world.playSoundAtEntity(player, "mob.slime.small", 2.0F, 1 + world.rand.nextFloat());
		world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		this.firedStack = null;
		
	}
	
	@Override
	@SuppressWarnings("incomplete-switch")
	public void registerRecipe()
	{
		switch(this.material)
		{
		case WOOD:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"PGb", "DHC", "PPb", 'P', Item.getItemFromBlock(Blocks.wooden_pressure_plate), 'G', "gearWood", 'b', "plankWood", 'D', Item.getItemFromBlock(Blocks.dispenser), 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}));
			break;
		case IRON:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"PGb", "MHC", "PPb", 'P', Item.getItemFromBlock(Blocks.heavy_weighted_pressure_plate), 'G', "gearWood", 'b', "ingotIron", 'M', M.magazine_food_wood.item, 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}));
			break;
		case GOLD:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{"PGb", "MHC", "PPb", 'P', Item.getItemFromBlock(Blocks.light_weighted_pressure_plate), 'G', "gearWood", 'b', "ingotGold", 'M', M.magazine_food_iron.item, 'H', Item.getItemFromBlock(Blocks.hopper), 'C', M.container}));
			break;
		}
	}
	
	@Override
	public int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player)
	{
		int result = super.setAmmo(slot, ammo, stack, world, player);
		this.sortItems(slot, stack);
		return result;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.capacity / 32 + 4;
	}
	
	protected NBTTagList getItemsNBT(int slot, ItemStack gun)
	{
		NBTTagCompound compound = this.getTagCompound(slot, gun);
		
		NBTTagList items = ITEMS.get(compound, true);
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
		ITEMS.set(gun, compound, items);
		return items;
	}
	
	public ArrayList<ItemStack> getItems(int slot, ItemStack gun)
	{
		ArrayList<ItemStack> a = new ArrayList<ItemStack>();
		NBTTagList items = this.getItemsNBT(slot, gun);
		
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
		ITEMS.set(gun, this.getTagCompound(slot, gun), items);
		
		return a;
	}
	
	protected ItemStack getLastItem(int slot, ItemStack gun)
	{
		NBTTagList items = this.getItemsNBT(slot, gun);
		if(items.tagCount() > 0)
		{
			ItemStack stack = ItemStack.loadItemStackFromNBT(items.getCompoundTagAt(items.tagCount() - 1));
			this.removeLastItem(slot, gun);
			return stack;
		}
		return new ItemStack(this.ammoItem());
	}
	
	protected void removeFirstItem(int slot, ItemStack gun)
	{
		NBTTagList items = this.getItemsNBT(slot, gun);
		if(items.tagCount() > 0)
		{
			items.removeTag(0);
		}
		ITEMS.set(gun, this.getTagCompound(slot, gun), items);
	}
	
	protected void removeLastItem(int slot, ItemStack gun)
	{
		NBTTagList items = this.getItemsNBT(slot, gun);
		if(items.tagCount() > 0)
		{
			items.removeTag(items.tagCount() - 1);
		}
		ITEMS.set(gun, this.getTagCompound(slot, gun), items);
	}
	
	protected void addItem(int slot, ItemStack gun)
	{
		if(this.lastUsedStack == null)
		{
			this.lastUsedStack = new ItemStack(this.ammoItem());
		}
		int amount = ComponentEvents.amount(gun, this.lastUsedStack);
		for(int i = 0; i < amount; ++i)
		{
			if(this.lastUsedStack != null)
			{
				this.lastUsedStack.stackSize = 1;
				NBTTagList items = this.getItemsNBT(slot, gun);
				if(this.lastUsedStack != null)
				{
					NBTTagCompound stack = this.lastUsedStack.writeToNBT(new NBTTagCompound());
					if(stack != null)
					{
						items.appendTag(stack);
					}
					ITEMS.set(gun, this.getTagCompound(slot, gun), items);
				}
			}
		}
		this.lastUsedStack = null;
	}
	
	protected void sortItems(int slot, ItemStack gun)
	{
		int ammo = this.AMMO.get(this.getTagCompound(slot, gun), true);
		while(this.getItemsNBT(slot, gun).tagCount() > ammo)
		{
			this.removeFirstItem(slot, gun);
		}
		while(this.getItemsNBT(slot, gun).tagCount() < ammo)
		{
			this.addItem(slot, gun);
		}
	}
	
	public boolean isItemValid(ItemStack stack, boolean flag)
	{
		if(stack.getItem() == this.ammoItem())
		{
			if(flag)
			{
				this.lastUsedStack = null;
			}
			return true;
		}
		else if(stack.getItem() instanceof ItemFood)
		{
			if(flag)
			{
				this.lastUsedStack = stack.copy();
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}