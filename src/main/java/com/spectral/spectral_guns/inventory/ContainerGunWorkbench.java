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

import org.apache.commons.lang3.StringUtils;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class ContainerGunWorkbench extends Container implements IContainerAddPlayerSlots, IContainerItemName
{
	private final TileEntityGunWorkbench inventory;
	private int playersUsing;
	private String gunName;
	public boolean gunNameIsBeingSet = false;
	public boolean gunNameHasBeenSet = false;
	
	public ContainerGunWorkbench(InventoryPlayer playerInventory, TileEntityGunWorkbench containerInventory)
	{
		this.inventory = containerInventory;
		this.inventory.openInventory(playerInventory.player);
		this.addSlotToContainer(new SlotGun(this, containerInventory, 0, 18, 22));
		this.addSlotToContainer(new SlotWrench(containerInventory, 1, 18, 51));
		
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 2, 70, 36, Type.BARREL));
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 3, 88, 36, Type.MAGAZINE));
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 4, 106, 36, Type.TRIGGER));
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 5, 97, 54, Type.GRIP));
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 6, 115, 54, Type.STOCK));
		this.addSlotToContainer(new SlotComponent(this, containerInventory, 7, 79, 18, Type.AIM));
		
		for(int i = 0; i < 4; ++i)
		{
			this.addSlotToContainer(new SlotComponent(this, containerInventory, 8 + i, 150, i * 18 + 7, Type.MISC));
		}
		
		Stuff.GuiStuff.addPlayerInventorySlots(this, playerInventory, 0, 30);
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
			else
			{
				boolean b = false;
				int[] slots = this.inventory.getSlotsForStack(itemstack1);
				loop:
				for(int tSlot : slots)
				{
					ItemStack stack1 = this.getSlot(tSlot).getStack();
					if(stack1 != null)
					{
						for(int tSlot2 : slots)
						{
							ItemStack stack2 = this.getSlot(tSlot2).getStack();
							if(tSlot != tSlot2)
							{
								if(stack2 == null && stack1.stackSize < stack1.stackSize)
								{
									break loop;
								}
							}
						}
					}
					if(this.mergeItemStack(itemstack1, tSlot, tSlot + 1, false))
					{
						b = true;
						break;
					}
				}
				if(!b)
				{
					return null;
				}
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
	
	public String getGunName()
	{
		return Stuff.Strings.removeFormatting(this.gunName);
	}
	
	public String getGunNameFormatting()
	{
		String s = this.getGunName();
		if(!StringUtils.isBlank(s))
		{
			return ChatFormatting.RESET + Stuff.Strings.removeFormatting(s);
		}
		return s;
	}
	
	public void setGunName(String newName)
	{
		this.gunName = Stuff.Strings.removeFormatting(newName);
		this.gunNameIsBeingSet = true;
		this.gunNameHasBeenSet = true;
	}
	
	@Override
	public void updateItemName(String newName)
	{
		this.setGunName(newName);
		
		if(this.getSlot(0).getHasStack())
		{
			ItemStack itemstack = this.getSlot(0).getStack();
			if(StringUtils.isBlank(this.getGunName()) || this.getGunName().equals(itemstack.getItem().getItemStackDisplayName(itemstack)))
			{
				this.setGunName(null);
			}
			
			if(StringUtils.isBlank(this.getGunName()))
			{
				itemstack.clearCustomName();
			}
			else
			{
				itemstack.setStackDisplayName(this.getGunNameFormatting());
			}
			SlotGun.gunFromComponents(this.inventory, this, this.inventory.lastUsing, itemstack);
		}
	}
}