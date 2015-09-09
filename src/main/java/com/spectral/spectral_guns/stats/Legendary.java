package com.spectral.spectral_guns.stats;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.Stuff;

public abstract class Legendary
{
	public static final HashMap<String, Legendary> legendaries = new HashMap();
	private final String name;
	
	public static Legendary getLegendaryForGun(ItemStack gun)
	{
		String s = gun.getDisplayName();
		s = Stuff.Strings.removeFormatting(s);
		if(gun.hasDisplayName())
		{
			if(legendaries.containsKey(s) && legendaries.get(s).isValid(gun))
			{
				return legendaries.get(s);
			}
		}
		return null;
	}
	
	public static boolean gunHasLegendary(ItemStack gun)
	{
		return getLegendaryForGun(gun) != null;
	}
	
	public Legendary(String name)
	{
		if(legendaries.containsKey(name))
		{
			throw new IllegalArgumentException("Legendary name already taken! So sad... :,(");
		}
		legendaries.put(name, this);
		this.name = name;
	}
	
	public final String getRequiredName()
	{
		return this.name;
	}
	
	public String getPrefix(ItemStack stack, EntityPlayer player)
	{
		return "" + this.getColor(stack, player);
	}
	
	public abstract EnumChatFormatting getColor(ItemStack stack, EntityPlayer player);
	
	public abstract int getXpReward(EntityPlayer player);
	
	public abstract String getAchievementTexture();
	
	public String getTexture()
	{
		return null;
	}
	
	public EnumRarity getRarity(ItemStack stack, EntityPlayer player)
	{
		return EnumRarity.UNCOMMON;
	}
	
	public boolean isValid(ItemStack stack)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void onFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier);
	
	@SideOnly(Side.CLIENT)
	public abstract void onUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected);
}
