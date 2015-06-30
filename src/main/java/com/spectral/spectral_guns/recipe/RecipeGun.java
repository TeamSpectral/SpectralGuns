package com.spectral.spectral_guns.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class RecipeGun implements IRecipe
{
	@Override
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
					Component c = ((ItemComponent)stack.getItem()).c;
					int slot2 = 0;
					while(ItemGun.getComponents(gun).containsKey(slot2) || !Stuff.ArraysAndSuch.has(Stuff.ArraysAndSuch.intArray(c.type.slots), slot2))
					{
						++slot2;
						if(slot2 >= new TileEntityGunWorkbench().getComponentSlots())
						{
							return null;
						}
					}
					ItemGun.addComponent(gun, slot2, c);
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
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return new ItemStack(M.gun);
	}
	
	@Override
	public int getRecipeSize()
	{
		return 1;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		return this.getCraftingResult(inv) != null;
	}
	
	@Override
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
