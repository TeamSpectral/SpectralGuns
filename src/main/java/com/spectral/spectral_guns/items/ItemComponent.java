package com.spectral.spectral_guns.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.IDAble;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;

public class ItemComponent extends ItemBase implements IDAble
{
	public final Component c;
	private final static CreativeTabs theTab = M.tabCore;
	
	public final static String HEAT = "Heat";
	public final static String ITEMDAMAGE = "ItemDamage";
	
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
	
	@Override
	public Item setUnlocalizedName(String unlocalizedName)
	{
		return super.setUnlocalizedName("component." + unlocalizedName);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		String s = "component." + this.c.getFancyName();
		super.setUnlocalizedName(s);
		return super.getUnlocalizedName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced)
	{
		EnumChatFormatting mat = EnumChatFormatting.DARK_GRAY;
		switch(this.c.material)
		{
		case DIAMOND:
			mat = EnumChatFormatting.AQUA;
			break;
		case GOLD:
			mat = EnumChatFormatting.YELLOW;
			break;
		case IRON:
			mat = EnumChatFormatting.GRAY;
			break;
		case WOOD:
			mat = EnumChatFormatting.GOLD;
			break;
		}
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Type: " + EnumChatFormatting.WHITE + this.c.type.name().toLowerCase() + EnumChatFormatting.DARK_GRAY + ", Material: " + mat + this.c.material.getDisplayName(this.c.type, this.c) + EnumChatFormatting.RESET);
	}
}
