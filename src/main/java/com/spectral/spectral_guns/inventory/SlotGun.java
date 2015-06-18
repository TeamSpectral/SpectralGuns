package com.spectral.spectral_guns.inventory;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.components.ComponentEvents.ConsumerComponentEventVec3;
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
			TileEntityGunWorkbench tileEntity = (TileEntityGunWorkbench)this.inventory;
			Vec3 vec = Stuff.Coordinates3D.middle(tileEntity.getPos());
			EntityPlayer player = tileEntity.lastUsing;
			if(player == null)
			{
				player = tileEntity.getWorld().getClosestPlayer(vec.xCoord, vec.yCoord, vec.zCoord, 100);
			}
			if(player != null)
			{
				ArrayList<Component> cs = ItemGun.getComponents(stack);
				for(int i = 0; i < 1000 && ItemGun.ammo(stack, player) > 0; ++i)
				{
					vec.add(new Vec3(0, 1.5, 0));
					ConsumerComponentEventVec3 cce = new ConsumerComponentEventVec3(vec)
					{
						@Override
						public void action(ItemStack gun, EntityPlayer player, ArrayList<Component> components)
						{
							Item item = ItemGun.ejectableAmmo(gun, player);
							ItemStack drop = new ItemStack(item, 1, 0);
							if(drop.stackSize != 0 && drop.getItem() != null)
							{
								EntityItem e = new EntityItem(player.worldObj, this.vec.xCoord, this.vec.yCoord, this.vec.zCoord, drop);
								if(!player.worldObj.isRemote)
								{
									player.worldObj.spawnEntityInWorld(e);
								}
								
								if(e != null)
								{
									e.setDefaultPickupDelay();
									e.setPosition(this.vec.xCoord, this.vec.yCoord, this.vec.zCoord);
									e.motionX = e.motionY = e.motionZ = 0;
									e.setOwner(player.getName());
									player.worldObj.playSoundAtEntity(player, "random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 1.4F);
								}
							}
						}
					};
					ComponentEvents.eject(stack, player, cs, cce);
				}
				this.container.insertComponents(stack);
			}
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
