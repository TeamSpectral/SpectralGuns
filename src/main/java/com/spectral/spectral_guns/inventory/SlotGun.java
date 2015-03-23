package com.spectral.spectral_guns.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.items.ItemWrench;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class SlotGun extends Slot
{
	public final ContainerGunWorkbench container;
	
	public SlotGun(ContainerGunWorkbench container, TileEntityGunWorkbench inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.container = container;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		ItemStack stackWrench = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(1);
		if(stackWrench != null && stackWrench.getItem() instanceof ItemWrench && stackWrench.getItemDamage() < stackWrench.getMaxDamage())
		{
			if(stack == null || stack.getItem() instanceof ItemGun && ComponentEvents.isGunValid(stack))
			{
				return ((TileEntityGunWorkbench)this.inventory).getComponents().size() <= 0;
			}
		}
		return false;
	}
	
	/**
	 * Helper method to put a stack in the slot.
	 */
	@Override
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);
		if(stack != null)
		{
			this.container.insertComponents(ItemGun.getComponents(stack));
		}
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		ItemGun.setComponents(stack, ((TileEntityGunWorkbench)this.inventory).getComponents());
		((TileEntityGunWorkbench)this.inventory).clearComponentStacks(false);
	}
	
	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		ItemStack stackWrench = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(1);
		if(stackWrench != null && stackWrench.getItem() instanceof ItemWrench && stackWrench.getItemDamage() < stackWrench.getMaxDamage())
		{
			ItemStack stackGun = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(0);
			if(stackGun != null)
			{
				stackGun = stackGun.copy();
				ItemGun.setComponents(stackGun, ((TileEntityGunWorkbench)this.inventory).getComponents());
				if(ComponentEvents.isGunValid(stackGun))
				{
					return true;
				}
			}
		};
		return false;
	}
	
	/**
	 * Called when the stack in a Slot changes
	 */
	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
		ItemStack stack = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(1);
		if(stack != null && stack.getItem() instanceof ItemWrench)
		{
			if(((TileEntityGunWorkbench)this.inventory).lastUsing != null)
			{
				boolean b = ((TileEntityGunWorkbench)this.inventory).lastUsing.capabilities.isCreativeMode;
				if(b)
				{
					((TileEntityGunWorkbench)this.inventory).lastUsing.capabilities.isCreativeMode = false;
				}
				stack.damageItem(1, ((TileEntityGunWorkbench)this.inventory).lastUsing);
				if(b)
				{
					((TileEntityGunWorkbench)this.inventory).lastUsing.capabilities.isCreativeMode = true;
				}
			}
			else
			{
				stack.setItemDamage(stack.getItemDamage() + 1);
			}
			if(stack.getItemDamage() >= stack.getMaxDamage())
			{
				((TileEntityGunWorkbench)this.inventory).setStackInSlot(1, null);
			}
		}
	}
	
	@Override
	public ItemStack getStack()
	{
		ItemStack stackGun = super.getStack();
		if(stackGun == null)
		{
			stackGun = new ItemStack(M.gun);
		}
		if(stackGun != null && stackGun.getItem() instanceof ItemGun)
		{
			ItemGun.setComponents(stackGun, ((TileEntityGunWorkbench)this.inventory).getComponents());
		}
		if(stackGun != null && !ComponentEvents.isGunValid(stackGun))
		{
			stackGun = null;
		}
		this.inventory.setInventorySlotContents(this.getSlotIndex(), stackGun);
		return stackGun;
	}
}
