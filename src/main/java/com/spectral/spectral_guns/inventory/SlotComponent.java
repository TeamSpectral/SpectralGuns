package com.spectral.spectral_guns.inventory;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References.ReferencesGunErrors;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.items.ItemWrench;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class SlotComponent extends Slot
{
	public final ContainerGunWorkbench container;
	public final ComponentRegister.Type type;
	
	public SlotComponent(ContainerGunWorkbench container, TileEntityGunWorkbench inventoryIn, int index, int xPosition, int yPosition, ComponentRegister.Type type)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.type = type;
		this.container = container;
	}
	
	protected TileEntityGunWorkbench inventory()
	{
		return (TileEntityGunWorkbench)this.inventory;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		ItemStack stackWrench = this.inventory().getStackInSlot(1);
		if(stackWrench != null && stackWrench.getItem() instanceof ItemWrench && stackWrench.getItemDamage() < stackWrench.getMaxDamage())
		{
			if(stack == null || stack.getItem() instanceof ItemComponent)
			{
				if(stack == null || ((ItemComponent)stack.getItem()).c.type == this.type)
				{
					return true;
				}
				Type type1 = null;
				loop:
				for(Type type : Type.values())
				{
					for(int slot : type.slots)
					{
						if(slot + this.inventory().getSizeInventory() - this.inventory().getComponentSlots() == this.slotNumber)
						{
							type1 = type;
							break loop;
						}
					}
				}
				switch(type1)
				{
				case AIM:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("aim", "aims", false));
					break;
				case BARREL:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("barrel", "barrels", false));
					break;
				case GRIP:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("grips", "grip", false));
					break;
				case MAGAZINE:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("magazine", "magazines", false));
					break;
				case MISC:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("addon", "addons", true));
					break;
				case STOCK:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("stock", "stocks", false));
					break;
				case TRIGGER:
					this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("trigger", "trigger mechanisms", false));
					break;
				}
				return false;
			}
			this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("component", "components", true));
			return false;
		}
		this.inventory().errorMessage(ReferencesGunErrors.NO_WRENCH);
		return false;
	}
	
	@Override
	public void putStack(ItemStack stack)
	{
		super.putStack(stack);
		if(this.inventory().getStackInSlot(0) == null)
		{
			ItemStack stackGun = new ItemStack(M.gun);
			SlotGun.gunFromComponents(this.inventory(), this.container, null, stackGun);
		}
		ItemStack stackGun = this.inventory().getStackInSlot(0);
		if(stack != null && stack.getItem() instanceof ItemComponent)
		{
			//ItemGun.addComponent(stackGun, ((ItemComponent)stack.getItem()).c);
		}
		if(stackGun != null && (ItemGun.getComponents(stackGun).size() <= 0 || !ComponentEvents.isGunValid(stackGun)))
		{
			this.inventory().setInventorySlotContents(0, null);
		}
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		ItemStack stackGun = this.inventory().getStackInSlot(0);
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
				SlotGun.gunFromComponents(this.inventory(), this.container, player, stackGun);
			}
			if(stackGun != null && (ItemGun.getComponents(stackGun).size() <= 0 || !ComponentEvents.isGunValid(stackGun)))
			{
				this.inventory().setInventorySlotContents(0, null);
			}
		}
	}
}
