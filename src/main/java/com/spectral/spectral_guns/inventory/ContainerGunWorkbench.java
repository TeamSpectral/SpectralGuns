package com.spectral.spectral_guns.inventory;

import java.util.HashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class ContainerGunWorkbench extends Container implements IContainerAddPlayerSlots
{
	private final TileEntityGunWorkbench inventory;
	private int playersUsing;
	
	public ContainerGunWorkbench(InventoryPlayer playerInventory, TileEntityGunWorkbench containerInventory)
	{
		this.inventory = containerInventory;
		this.inventory.openInventory(playerInventory.player);
		this.addSlotToContainer(new SlotGun(this, containerInventory, 0, 18, 22));
		this.addSlotToContainer(new SlotWrench(containerInventory, 1, 18, 51));
		
		this.addSlotToContainer(new SlotComponent(containerInventory, 2, 70, 40, Type.BARREL));
		this.addSlotToContainer(new SlotComponent(containerInventory, 3, 88, 40, Type.MAGAZINE));
		this.addSlotToContainer(new SlotComponent(containerInventory, 4, 106, 40, Type.TRIGGER));
		this.addSlotToContainer(new SlotComponent(containerInventory, 5, 97, 58, Type.GRIP));
		this.addSlotToContainer(new SlotComponent(containerInventory, 6, 115, 58, Type.STOCK));
		this.addSlotToContainer(new SlotComponent(containerInventory, 7, 79, 22, Type.AIM));
		
		for(int i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new SlotComponent(containerInventory, 8 + i, 150, i * 18 + 7, Type.MISC));
		}
		
		Stuff.GuiStuff.addPlayerInventorySlots(this, playerInventory, 0, 0);
		this.inventory.markDirty();
	}
	
	/**
	 * Add the given Listener to the list of Listeners. Method name is for
	 * legacy.
	 */
	@Override
	public void addCraftingToCrafters(ICrafting listener)
	{
		super.addCraftingToCrafters(listener);
		listener.func_175173_a(this, this.inventory);
		// TODO
	}
	
	/**
	 * Looks for changes made in the container, sends them to every listener.
	 */
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for(int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if(this.playersUsing != this.inventory.getField(0))
			{
				icrafting.sendProgressBarUpdate(this, 0, this.inventory.getField(0));
			}
		}
		
		this.playersUsing = this.inventory.getField(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.inventory.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.inventory.isUseableByPlayer(playerIn);
	}
	
	/**
	 * Take a stack from the specified inventory slot.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot)this.inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(index < this.inventory.getSizeInventory())
			{
				if(!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), 36 + this.inventory.getSizeInventory(), true))
				{
					return null;
				}
			}
			else if(!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory() - 1, false))
			{
				return null;
			}
			
			if(itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack)null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if(itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(playerIn, itemstack1);
		}
		
		return itemstack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		this.inventory.closeInventory(player);
	}
	
	public void insertComponents(ItemStack gun)
	{
		this.inventory.clearComponentStacks(true, 64);
		HashMap<Integer, Component> cs = ItemGun.getComponents(gun);
		for(Integer slot : cs.keySet())
		{
			Component c = cs.get(slot);
			ItemStack stack = c.toItemStack(slot, gun);
			if(!this.mergeItemStack(stack, this.inventory.getSizeInventory() - this.inventory.getComponentSlots() + slot, this.inventory.getSizeInventory() - this.inventory.getComponentSlots() + slot + 1, true) || !this.mergeItemStack(stack, 0, this.inventory.getSizeInventory(), false))
			{
				EntityItem entity = new EntityItem(this.inventory.getWorld(), this.inventory.getPos().getX() + 0.5, this.inventory.getPos().getY() + 0.5, this.inventory.getPos().getZ() + 0.5, stack);
				entity.setNoPickupDelay();
				if(!this.inventory.getWorld().isRemote)
				{
					this.inventory.getWorld().spawnEntityInWorld(entity);
				}
			}
		}
	}
	
	@Override
	public Slot addSlotToContainer2(Slot slotIn)
	{
		return super.addSlotToContainer(slotIn);
	}
}