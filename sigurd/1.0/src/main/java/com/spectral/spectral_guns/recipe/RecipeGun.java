package com.spectral.spectral_guns.recipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesCrafting;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeGun implements IRecipe
{
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack gun = new ItemStack(M.gun);
		for(int slot = 0; slot < inv.getSizeInventory(); ++slot)
		{
			ItemStack stack = inv.getStackInSlot(slot);
			if(stack != null)
			{
				if(stack.getItem() instanceof ItemComponent)
				{
					ItemGun.addComponent(gun, ((ItemComponent)stack.getItem()).c);
				}
				else
				{
					return null;
				}
			}
		}
		if(ComponentEvents.isGunValid(gun))
		{
			return gun;
		}
		return null;
	}

	public ItemStack getRecipeOutput()
	{
		return new ItemStack(M.gun);
	}

	public int getRecipeSize()
	{
		return 1;
	}

	public boolean matches(InventoryCrafting inv, World world)
	{
		ItemStack gun = new ItemStack(M.gun);
		for(int slot = 0; slot < inv.getSizeInventory(); ++slot)
		{
			ItemStack stack = inv.getStackInSlot(slot);
			if(stack != null)
			{
				if(stack.getItem() instanceof ItemComponent)
				{
					ItemGun.addComponent(gun, ((ItemComponent)stack.getItem()).c);
				}
				else
				{
					return false;
				}
			}
		}
		return ComponentEvents.isGunValid(gun) && ItemGun.getComponents(gun).size() > 0;
	}

	public ItemStack[] getRemainingItems(InventoryCrafting inv)
	{
		ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

		for(int i = 0; i < aitemstack.length; ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
		}

		return aitemstack;
	}
}
