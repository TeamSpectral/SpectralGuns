package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.StringUtils;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentTraits;
import com.spectral.spectral_guns.components.Component.String2;
import com.spectral.spectral_guns.itemtags.ItemTagDouble;
import com.spectral.spectral_guns.itemtags.ItemTagInteger;

public class ItemComponent extends Item
{
	public final Component c;
	private final static CreativeTabs theTab = M.tabCore;
	
	public static final ItemTagDouble HEAT = new ItemTagDouble("Heat", 0D, -Double.MAX_VALUE, Double.MAX_VALUE, true);
	public static final ItemTagInteger ITEMDAMAGE = new ItemTagInteger("Damage", 0, 0, Integer.MAX_VALUE, true);
	
	/**
	 * Crafting components
	 */
	public ItemComponent(Component c)
	{
		super();
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
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName();
	}
	
	@Override
	public String getUnlocalizedName()
	{
		String s = this.c.getFancyName();
		return "item.component." + s;
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
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Type: " + EnumChatFormatting.WHITE + Stuff.Strings.capitalize(this.c.type.name().toLowerCase()) + EnumChatFormatting.DARK_GRAY);
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Material: " + mat + this.c.material.getDisplayName(this.c.type, this.c) + EnumChatFormatting.RESET);
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Durability: " + EnumChatFormatting.WHITE + (this.getMaxDamage() - this.getDamage(stack)) + "/" + this.getMaxDamage());
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Withstands Temperatures: " + EnumChatFormatting.WHITE + -this.c.material.heatThresholdMin + " to " + this.c.material.heatThresholdMax);
		tooltip.add(EnumChatFormatting.DARK_GRAY + "Temperature Conductiveness: " + EnumChatFormatting.WHITE + this.c.material.heatLoss);
		
		ArrayList<String2> tooltip2 = new ArrayList();
		this.c.getTooltip(tooltip2, player, player.worldObj);
		loop:
		for(int i1 = 0; i1 < tooltip2.size(); ++i1)
		{
			String2 s = tooltip2.get(i1);
			for(int i2 = i1 + 1; i2 < tooltip2.size(); ++i2)
			{
				String2 s2 = tooltip2.get(i2);
				if(s.s1.equals(s2.s1))
				{
					continue loop;
				}
			}
			tooltip.add(EnumChatFormatting.DARK_GRAY + s.s1 + EnumChatFormatting.RESET + (StringUtils.isBlank(s.s1) ? "" : " ") + EnumChatFormatting.WHITE + s.s2 + EnumChatFormatting.RESET);
		}
	}
	
	@Override
	public int getMaxDamage()
	{
		return this.c.durabilityMax(-1, new ItemStack(M.gun));
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack mat = null;
		switch(this.c.material)
		{
		case DIAMOND:
			mat = new ItemStack(Items.diamond);
			break;
		case GOLD:
			mat = new ItemStack(Items.gold_ingot);
			break;
		case IRON:
			mat = new ItemStack(Items.iron_ingot);
			break;
		case WOOD:
			mat = new ItemStack(Blocks.planks);
			break;
		}
		if(mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false))
		{
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	}
}
