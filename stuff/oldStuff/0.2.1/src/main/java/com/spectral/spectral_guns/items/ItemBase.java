package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.List;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.Stuff;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item
{
	public static ArrayList<ItemBase> itemsToBeRegistered = new ArrayList<ItemBase>();

	public final String id;
	private CreativeTabs[] creativeTabs;

	public static final String RIGHTCLICKED = "RightClicked";
	public static final String RIGHTCLICKEDLAST = "RightClickedLast";
	public final boolean hasCompound = false;

	/**
	 * Base for all items
	 */
	public ItemBase(String id)
	{
		super();
		this.setMaxStackSize(64);
		this.id = id;
		GameRegistry.registerItem(this, id, References.MODID);
		itemsToBeRegistered.add(this);
	}

	/**
	 * Base for all items
	 */
	public ItemBase(String id, CreativeTabs[] tabs)
	{
		this(id);
		this.setCreativeTabs(tabs);
	}

	/**
	 * Gets a list of tabs that items belonging to this class can display on,
	 * combined properly with getSubItems allows for a single item to span
	 * many sub-items across many tabs.
	 *
	 * @return A list of all tabs that this item could possibly be on.
	 */
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		if(this.creativeTabs != null)
		{
			return  this.creativeTabs;
		}
		else
		{
			return super.getCreativeTabs();
		}
	}

	public ItemBase setCreativeTabs(CreativeTabs[] tabs)
	{
		this.creativeTabs = tabs;
		return this;
	}

    public String getUnlocalizedName(ItemStack stack)
    {
    	return getUnlocalizedName();
    }

	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected)
	{
		if(hasCompound)
		{
			stack = compound(stack);
			if(!isSelected)
			{
				stack.getTagCompound().setBoolean(ItemBase.RIGHTCLICKED, false);
			}
		}
	}

	protected static ItemStack compound(ItemStack stack)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack;
	}
}
