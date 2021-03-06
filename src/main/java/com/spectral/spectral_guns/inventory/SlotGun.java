package com.spectral.spectral_guns.inventory;

import java.util.HashMap;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

import org.apache.commons.lang3.StringUtils;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References.ReferencesGunErrors;
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
			if(stack == null || stack.getItem() instanceof ItemGun)
			{
				if(ComponentEvents.isGunValid(stack))
				{
					if(this.inventory().getComponents().size() <= 0)
					{
						this.damageWrench = true;
						return true;
					}
					this.inventory().errorMessage(ReferencesGunErrors.COMPONENT_SLOTS_OCCUPIED);
					return false;
				}
				this.inventory().errorMessage(ReferencesGunErrors.INVALID_GUN);
				return false;
			}
			this.inventory().errorMessage(ReferencesGunErrors.WRONG_SLOT("gun", "guns", false));
			return false;
		}
		this.inventory().errorMessage(ReferencesGunErrors.NO_WRENCH);
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
			this.container.setGunName(stack.getDisplayName());
			this.damageWrench();
			TileEntityGunWorkbench tileEntity = this.inventory();
			Vec3 vec = Stuff.Coordinates3D.middle(tileEntity.getPos());
			EntityPlayer player = tileEntity.lastUsing;
			if(player == null)
			{
				player = tileEntity.getWorld().getClosestPlayer(vec.xCoord, vec.yCoord, vec.zCoord, 100);
			}
			if(player != null)
			{
				HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
				for(int i = 0; i < 1000 && ItemGun.ammo(stack, player) > 0; ++i)
				{
					vec.add(new Vec3(0, 1.5, 0));
					ConsumerComponentEventVec3 cce = new ConsumerComponentEventVec3(vec)
					{
						@Override
						public void action(ItemStack gun, EntityPlayer player)
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
					ComponentEvents.eject(stack, player, cce);
				}
				this.container.insertComponents(stack);
			}
		}
		else
		{
			this.container.setGunName("");
		}
	}
	
	@Override
	public void onPickupFromSlot(EntityPlayer player, ItemStack stack)
	{
		super.onPickupFromSlot(player, stack);
		this.inventory().clearComponentStacks(false, 1);
		this.damageWrench();
		stack.onCrafting(player.worldObj, player, 1);
		//this.container.setGunName("");
	}
	
	public static void gunFromComponents(TileEntityGunWorkbench inventory, ContainerGunWorkbench container, EntityPlayer player, ItemStack stack)
	{
		ItemGun.setComponents(stack, inventory.getComponents());
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			Component c = cs.get(slot);
			ItemStack cStack = inventory.getStackInSlot(inventory.getSizeInventory() - inventory.getComponentSlots() + slot);
			if(cStack != null)
			{
				if(cStack.getTagCompound() != null)
				{
					ItemGun.COMPONENT_COMPOUND.set(stack, c.getComponentCompound(slot, stack), cStack.getTagCompound());
				}
				c.setDurabilityDamage(slot, cStack.getItemDamage(), stack, player);
			}
		}
		if(stack != null && (ItemGun.getComponents(stack).size() <= 0 || !ComponentEvents.isGunValid(stack)))
		{
			stack = null;
		}
		if(stack != null)
		{
			if(StringUtils.isBlank(container.getGunName()))
			{
				if(stack.hasDisplayName() && container.gunNameHasBeenSet)
				{
					stack.clearCustomName();
				}
			}
			else if(!container.getGunName().equals(stack.getItem().getItemStackDisplayName(stack)))
			{
				stack.setStackDisplayName(container.getGunNameFormatting());
			}
		}
		inventory.setInventorySlotContents(0, stack);
	}
	
	/**
	 * Return whether this slot's stack can be taken from this slot.
	 */
	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		this.damageWrench = true;
		ItemStack stackWrench = this.inventory().getStackInSlot(1);
		if(stackWrench != null && stackWrench.getItem() instanceof ItemWrench && stackWrench.getItemDamage() < stackWrench.getMaxDamage())
		{
			ItemStack stackGun = this.inventory().getStackInSlot(0);
			if(stackGun != null)
			{
				stackGun = stackGun.copy();
				gunFromComponents(this.inventory(), this.container, playerIn, stackGun);
				if(ComponentEvents.isGunValid(stackGun))
				{
					return true;
				}
				this.inventory().errorMessage(ReferencesGunErrors.INVALID_GUN);
				return false;
			}
			return false;
		}
		this.inventory().errorMessage(ReferencesGunErrors.NO_WRENCH);
		return false;
	}
	
	public boolean damageWrench = true;
	
	public void damageWrench()
	{
		if(this.damageWrench)
		{
			ItemStack stack = this.inventory().getStackInSlot(1);
			if(stack != null && stack.getItem() instanceof ItemWrench)
			{
				if(this.inventory().lastUsing != null)
				{
					boolean b = this.inventory().lastUsing.capabilities.isCreativeMode;
					if(b)
					{
						this.inventory().lastUsing.capabilities.isCreativeMode = false;
					}
					stack.damageItem(1, this.inventory().lastUsing);
					if(b)
					{
						this.inventory().lastUsing.capabilities.isCreativeMode = true;
					}
				}
				else
				{
					stack.setItemDamage(stack.getItemDamage() + 1);
				}
				if(stack.getItemDamage() >= stack.getMaxDamage())
				{
					this.inventory().setStackInSlot(1, null);
				}
			}
		}
		this.damageWrench = false;
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
			gunFromComponents(this.inventory(), this.container, null, stackGun);
			stackGun = super.getStack();
		}
		if(stackGun != null && stackGun.stackSize < 1 && !ComponentEvents.isGunValid(stackGun))
		{
			stackGun = null;
		}
		this.inventory().setInventorySlotContents(this.getSlotIndex(), stackGun);
		return stackGun;
	}
}
