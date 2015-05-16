package com.spectral.spectral_guns.tileentity;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Lists;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.blocks.BlockGunWorkbench;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.inventory.ContainerGunWorkbench;
import com.spectral.spectral_guns.inventory.SlotGun;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.items.ItemWrench;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;

public class TileEntityGunWorkbench extends TileEntity implements IInteractionObject, IUpdatePlayerListBox, ISidedInventory
{
	private String customName;
	private int playersUsing = 0;
	private ItemStack gunStack = null;
	private ItemStack wrenchStack = null;
	private final int offset = 2;
	private ItemStack[] componentStacks = new ItemStack[ComponentRegister.Type.values().length - 1];
	private ItemStack[] componentMiscStacks = new ItemStack[4];
	private boolean readFlag = false;
	public EntityPlayer lastUsing = null;
	
	public static final String GUN_STACK = "GunStack";
	public static final String WRENCH_STACK = "WrenchStack";
	public static final String COMPONENT_STACKS = "ComponentStacks";
	public static final String COMPONENT_MISC_STACKS = "ComponentMiscStacks";
	public static final String PLAYERS_USING = "PlayersUsing";
	
	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return this.offset + this.componentStacks.length + this.componentMiscStacks.length;
	}
	
	/**
	 * Returns the stack in slot i
	 */
	@Override
	public ItemStack getStackInSlot(int index)
	{
		switch(index)
		{
		case 0:
		{
			return this.gunStack;
		}
		case 1:
		{
			return this.wrenchStack;
		}
		default:
		{
			if(index >= this.offset && index < this.offset + this.componentStacks.length)
			{
				return this.componentStacks[index - this.offset];
			}
			if(index >= this.offset + this.componentStacks.length && index < this.offset + this.componentStacks.length + this.componentMiscStacks.length)
			{
				return this.componentMiscStacks[index - this.offset - this.componentStacks.length];
			}
			return null;
		}
		}
	}
	
	public void setStackInSlot(int index, ItemStack stack)
	{
		switch(index)
		{
		case 0:
		{
			this.gunStack = stack;
			break;
		}
		case 1:
		{
			this.wrenchStack = stack;
			break;
		}
		default:
		{
			if(index >= this.offset && index < this.offset + this.componentStacks.length)
			{
				this.componentStacks[index - this.offset] = stack;
			}
			if(index >= this.offset + this.componentStacks.length && index < this.offset + this.componentStacks.length + this.componentMiscStacks.length)
			{
				this.componentMiscStacks[index - this.offset - this.componentStacks.length] = stack;
			}
		}
		}
	}
	
	/**
	 * Removes from an inventory slot (first arg) up to a specified number
	 * (second arg) of items and returns them in a
	 * new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if(this.getStackInSlot(index) != null)
		{
			ItemStack itemstack;
			
			if(this.getStackInSlot(index).stackSize <= count)
			{
				itemstack = this.getStackInSlot(index);
				this.setStackInSlot(index, null);
				return itemstack;
			}
			else
			{
				itemstack = this.getStackInSlot(index).splitStack(count);
				
				if(this.getStackInSlot(index).stackSize == 0)
				{
					this.setStackInSlot(index, null);
				}
				
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * When some containers are closed they call this on each slot, then drop
	 * whatever it returns as an EntityItem -
	 * like when you close a workbench GUI.
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		if(this.getStackInSlot(index) != null)
		{
			ItemStack itemstack = this.getStackInSlot(index);
			this.setStackInSlot(index, null);
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		boolean flag = stack != null && stack.isItemEqual(this.getStackInSlot(index)) && ItemStack.areItemStackTagsEqual(stack, this.getStackInSlot(index));
		this.setStackInSlot(index, stack);
		
		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
		{
			stack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	/**
	 * Gets the name of this command sender (usually username, but possibly
	 * "Rcon")
	 */
	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.customName : "container.gunTable";
	}
	
	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName()
	{
		return this.customName != null && this.customName.length() > 0;
	}
	
	public void setCustomInventoryName(String p_145951_1_)
	{
		this.customName = p_145951_1_;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		
		if(compound.hasKey("CustomName", 8))
		{
			this.customName = compound.getString("CustomName");
		}
		
		if(this.readFlag)
		{
			this.playersUsing = compound.getInteger(PLAYERS_USING);
		}
		
		if(compound.hasKey(GUN_STACK, new NBTTagCompound().getId()))
		{
			this.gunStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(GUN_STACK));
		}
		else
		{
			this.gunStack = null;
		}
		
		if(compound.hasKey(WRENCH_STACK, new NBTTagCompound().getId()))
		{
			this.wrenchStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag(WRENCH_STACK));
		}
		else
		{
			this.wrenchStack = null;
		}
		
		if(compound.hasKey(COMPONENT_STACKS, new NBTTagList().getId()))
		{
			NBTTagList list = compound.getTagList(COMPONENT_STACKS, new NBTTagCompound().getId());
			for(int i = 0; i < this.componentStacks.length && i < list.tagCount(); ++i)
			{
				if(list.getCompoundTagAt(i).getKeySet().size() > 0)
				{
					ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
					if(stack != null)
					{
						this.componentStacks[i] = stack;
						continue;
					}
				}
				this.componentStacks[i] = null;
			}
		}
		
		if(compound.hasKey(COMPONENT_MISC_STACKS, new NBTTagList().getId()))
		{
			NBTTagList list = compound.getTagList(COMPONENT_MISC_STACKS, new NBTTagCompound().getId());
			for(int i = 0; i < this.componentMiscStacks.length && i < list.tagCount(); ++i)
			{
				if(list.getCompoundTagAt(i).getKeySet().size() > 0)
				{
					ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
					if(stack != null)
					{
						this.componentMiscStacks[i] = stack;
						continue;
					}
				}
				this.componentMiscStacks[i] = null;
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		
		if(this.hasCustomName())
		{
			compound.setString("CustomName", this.customName);
		}
		
		compound.setInteger(PLAYERS_USING, this.playersUsing);
		
		if(this.gunStack != null)
		{
			compound.setTag(GUN_STACK, this.gunStack.writeToNBT(new NBTTagCompound()));
		}
		
		if(this.wrenchStack != null)
		{
			compound.setTag(WRENCH_STACK, this.wrenchStack.writeToNBT(new NBTTagCompound()));
		}
		
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < this.componentStacks.length; ++i)
		{
			if(this.componentStacks[i] != null)
			{
				list.appendTag(this.componentStacks[i].writeToNBT(new NBTTagCompound()));
			}
			else
			{
				list.appendTag(new NBTTagCompound());
			}
		}
		compound.setTag(COMPONENT_STACKS, list);
		
		NBTTagList list2 = new NBTTagList();
		for(int i = 0; i < this.componentMiscStacks.length; ++i)
		{
			if(this.componentMiscStacks[i] != null)
			{
				list2.appendTag(this.componentMiscStacks[i].writeToNBT(new NBTTagCompound()));
			}
			else
			{
				list2.appendTag(new NBTTagCompound());
			}
		}
		compound.setTag(COMPONENT_MISC_STACKS, list2);
	}
	
	/**
	 * Returns the maximum stack size for a inventory slot. Seems to always be
	 * 64, possibly will be extended. *Isn't
	 * this more of a set than a get?*
	 */
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	/**
	 * Furnace isBurning
	 */
	public boolean isBurning()
	{
		return this.playersUsing > 0 || this.gunStack != null && this.canExtractItem(0, this.gunStack, EnumFacing.DOWN, false);
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory)
	{
		return inventory.getField(0) > 0;
	}
	
	/**
	 * Updates the JList with a new model.
	 */
	@Override
	public void update()
	{
		boolean flag1 = false;
		
		IBlockState bs = this.worldObj.getBlockState(this.pos);
		if(bs.getBlock() instanceof BlockGunWorkbench && bs.getProperties().containsKey(BlockGunWorkbench.ON))
		{
			boolean value = (Boolean)bs.getValue(BlockGunWorkbench.ON);
			boolean newValue = this.isBurning();
			if(value != newValue)
			{
				flag1 = true;
			}
			this.worldObj.setBlockState(this.pos, bs.withProperty(BlockGunWorkbench.ON, newValue));
			
			value = (Boolean)this.worldObj.getBlockState(this.pos).getValue(BlockGunWorkbench.ON);
		}
		
		if(this.getStackInSlot(1) != null && this.getStackInSlot(1).getItemDamage() >= this.getStackInSlot(1).getMaxDamage())
		{
			this.setInventorySlotContents(1, null);
		}
		
		if(flag1)
		{
			this.markDirty();
		}
	}
	
	/**
	 * Do not make give this method the name canInteractWith because it clashes
	 * with Container
	 */
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		this.lastUsing = player;
		++this.playersUsing;
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		--this.playersUsing;
	}
	
	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if(this.getStackInSlot(index) != null)
		{
			if(!(this.getStackInSlot(index).isItemEqual(stack) && this.getStackInSlot(index).isStackable() && this.getStackInSlot(index).stackSize + stack.stackSize <= stack.getMaxStackSize()))
			{
				return false;
			}
		}
		
		switch(index)
		{
		case 0:
		{
			if(stack != null && !(stack.getItem() instanceof ItemGun && ComponentEvents.isGunValid(stack)))
			{
				return false;
			}
			break;
		}
		case 1:
		{
			if(stack != null && !(stack.getItem() instanceof ItemWrench))
			{
				return false;
			}
			break;
		}
		default:
		{
			if(index >= this.offset && index < this.offset + this.componentStacks.length)
			{
				ComponentRegister.Type type = ComponentRegister.Type.values()[index - this.offset + 1];
				if(stack != null && !(stack.getItem() instanceof ItemComponent && ((ItemComponent)stack.getItem()).c.type == type))
				{
					return false;
				}
			}
			if(index >= this.offset + this.componentStacks.length && index < this.offset + this.componentStacks.length + this.componentMiscStacks.length)
			{
				ComponentRegister.Type type = ComponentRegister.Type.MISC;
				if(stack != null && !(stack.getItem() instanceof ItemComponent && ((ItemComponent)stack.getItem()).c.type == type))
				{
					return false;
				}
			}
			break;
		}
		}
		
		return true;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		IBlockState bs = this.worldObj.getBlockState(this.pos);
		switch(side)
		{
		case DOWN:
			return new int[]{0};
		case UP:
			IntegerArray slots = new IntegerArray();
			for(int i = 0; i < this.componentStacks.length + this.componentMiscStacks.length; ++i)
			{
				slots.add(i + this.offset);
			}
			return slots.toIntArray();
		default:
		{
			if(bs != null && bs.getProperties().containsKey(BlockGunWorkbench.FACING))
			{
				if(side == bs.getValue(BlockGunWorkbench.FACING))
				{
					return new int[]{0};
				}
				else
				{
					return new int[]{1};
				}
			}
			break;
		}
		}
		return new int[]{};
	}
	
	/**
	 * Returns true if automation can insert the given item in the given slot
	 * from the given side. Args: slot, item,
	 * side
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction)
	{
		if(this.isItemValidForSlot(index, stack))
		{
			if(index == 0)
			{
				if(stack != null)
				{
					((SlotGun)new ContainerGunWorkbench(new InventoryPlayer(null), this).inventorySlots.get(0)).putStack(stack);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if automation can extract the given item in the given slot
	 * from the given side. Args: slot, item,
	 * side
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return this.canExtractItem(index, stack, direction, true);
	}
	
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction, boolean flag)
	{
		if(index == 0)
		{
			if(this.getStackInSlot(1) != null && this.getStackInSlot(1).getItem() instanceof ItemWrench && this.getStackInSlot(1).getItemDamage() < this.getStackInSlot(1).getMaxDamage())
			{
				if(flag)
				{
					this.getStackInSlot(1).attemptDamageItem(1, Stuff.rand);
				}
			}
			else
			{
				return false;
			}
			for(int i = 0; i < EnumFacing.values().length; ++i)
			{
				if(this.worldObj.getRedstonePower(this.pos, EnumFacing.values()[i]) <= 0)
				{
					if(flag)
					{
						this.clearComponentStacks(false);
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String getGuiID()
	{
		return getID();
	}
	
	public static String getID()
	{
		return References.MODID.toLowerCase() + ":" + "gun_workbench";
	}
	
	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerGunWorkbench(playerInventory, this);
	}
	
	@Override
	public int getField(int id)
	{
		switch(id)
		{
		case 0:
			return this.playersUsing;
		default:
			return 0;
		}
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch(id)
		{
		case 0:
			this.playersUsing = value;
			break;
		}
	}
	
	@Override
	public int getFieldCount()
	{
		return 1;
	}
	
	@Override
	public void clear()
	{
		this.clear(false);
	}
	
	public void clear(boolean drop)
	{
		if(drop)
		{
			if(this.gunStack != null)
			{
				EntityItem entity = new EntityItem(this.worldObj, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, this.gunStack);
				entity.setNoPickupDelay();
				if(!this.getWorld().isRemote)
				{
					this.getWorld().spawnEntityInWorld(entity);
				}
			}
			if(this.wrenchStack != null)
			{
				EntityItem entity = new EntityItem(this.worldObj, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, this.wrenchStack);
				entity.setNoPickupDelay();
				if(!this.getWorld().isRemote)
				{
					this.getWorld().spawnEntityInWorld(entity);
				}
			}
		}
		this.gunStack = null;
		this.wrenchStack = null;
		this.clearComponentStacks(drop);
	}
	
	public void clearComponentStacks(boolean drop)
	{
		for(int i = 0; i < this.componentStacks.length; ++i)
		{
			if(drop && this.componentStacks[i] != null)
			{
				EntityItem entity = new EntityItem(this.getWorld(), this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, this.componentStacks[i]);
				entity.setNoPickupDelay();
				if(!this.getWorld().isRemote)
				{
					this.getWorld().spawnEntityInWorld(entity);
				}
			}
			this.componentStacks[i] = null;
		}
		for(int i = 0; i < this.componentMiscStacks.length; ++i)
		{
			if(drop && this.componentMiscStacks[i] != null)
			{
				EntityItem entity = new EntityItem(this.getWorld(), this.getPos().getX() + 0.5, this.getPos().getY() + 0.5, this.getPos().getZ() + 0.5, this.componentMiscStacks[i]);
				entity.setNoPickupDelay();
				if(!this.getWorld().isRemote)
				{
					this.getWorld().spawnEntityInWorld(entity);
				}
			}
			this.componentMiscStacks[i] = null;
		}
	}
	
	@Override
	public IChatComponent getDisplayName()
	{
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.pos, 1, nbtTag);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	{
		this.readFlag = true;
		this.readFromNBT(packet.getNbtCompound());
		this.readFlag = false;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return this.playersUsing <= 0 && oldState.getBlock() != newSate.getBlock();
	}
	
	public ArrayList<Component> getComponents()
	{
		ArrayList<Component> a = Lists.newArrayList();
		for(ItemStack stack : this.componentStacks)
		{
			if(stack != null && stack.getItem() instanceof ItemComponent)
			{
				a.add(((ItemComponent)stack.getItem()).c);
			}
		}
		return a;
	}
}