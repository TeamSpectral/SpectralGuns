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
		register(id);
	}
	
	protected void register(String id)
	{
		M.registerItem(id, this);
	}

	/**
	 * Base for all items
	 */
	public ItemBase(String id, CreativeTabs[] tabs)
	{
		this(id);
		this.setCreativeTabs(tabs);
	}

	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		if(M.creativeTabs.containsKey(this) && M.creativeTabs.get(this) != null)
		{
			return M.creativeTabs.get(this);
		}
		else
		{
			return super.getCreativeTabs();
		}
	}

	public ItemBase setCreativeTabs(CreativeTabs[] tabs)
	{
		M.creativeTabs.put(this, tabs);
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
