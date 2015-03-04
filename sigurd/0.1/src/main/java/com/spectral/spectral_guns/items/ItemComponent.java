package com.spectral.spectral_guns.items;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemComponent extends ItemBase
{
	public final Component c;
	private final static CreativeTabs theTab = M.tabCore;
	/**
	 * Crafting components
	 */
	public ItemComponent(Component c)
	{
		super("component_" + c.getID());
		this.setCreativeTab(theTab);
		this.c = c;
		this.setUnlocalizedName(c.getFancyName());
	}

    public Item setUnlocalizedName(String unlocalizedName)
    {
        return super.setUnlocalizedName("component." + unlocalizedName);
    }
}
