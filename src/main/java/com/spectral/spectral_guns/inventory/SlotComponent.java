package com.spectral.spectral_guns.inventory;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.items.ItemWrench;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class SlotComponent extends Slot
{
	public final ComponentRegister.Type type;
	
	public SlotComponent(IInventory inventoryIn, int index, int xPosition, int yPosition, ComponentRegister.Type type)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.type = type;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		ItemStack stackWrench = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(1);
		if(stackWrench != null && stackWrench.getItem() instanceof ItemWrench && stackWrench.getItemDamage() < stackWrench.getMaxDamage())
		{
			return stack == null || stack.getItem() instanceof ItemComponent && ((ItemComponent)stack.getItem()).c.type == this.type;
		}
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);
		if(((TileEntityGunWorkbench)this.inventory).getStackInSlot(0) == null)
		{
			ItemStack stackGun = new ItemStack(M.gun);
			ItemGun.setComponents(stackGun, ((TileEntityGunWorkbench)this.inventory).getComponents());
			((TileEntityGunWorkbench)this.inventory).setInventorySlotContents(0, stackGun);
			
		}
		ItemStack stackGun = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof ItemComponent)
		{
			//ItemGun.addComponent(stackGun, ((ItemComponent)stack.getItem()).c);
		}
		if(ItemGun.getComponents(stackGun).size() <= 0 || !ComponentEvents.isGunValid(stackGun))
		{
			((TileEntityGunWorkbench)this.inventory).setInventorySlotContents(0, null);
		}
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		ItemStack stackGun = ((TileEntityGunWorkbench)this.inventory).getStackInSlot(0);
		if(stackGun != null)
		{
			if(stack != null && stack.getItem() instanceof ItemComponent)
			{
				HashMap<Integer, Component> cs = ItemGun.getComponents(stackGun);
				for(Integer slot : cs.keySet())
				{
					if(cs.get(slot) == ((ItemComponent)stack.getItem()).c)
					{
						cs.remove(slot);
						break;
					}
				}
				ItemGun.setComponents(stackGun, cs);
			}
			if(ItemGun.getComponents(stackGun).size() <= 0 || !ComponentEvents.isGunValid(stackGun))
			{
				((TileEntityGunWorkbench)this.inventory).setInventorySlotContents(0, null);
			}
		}
	}
}
