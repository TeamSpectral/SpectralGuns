package com.spectral.spectral_guns.tabs;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TabGeneric extends CreativeTabs
{
	public TabGeneric(String tabLabel)
	{
		super("spectralGuns." + tabLabel);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public abstract Item getTabIconItem();
}