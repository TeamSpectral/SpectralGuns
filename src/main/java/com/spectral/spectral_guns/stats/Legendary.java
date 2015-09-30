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
import com.spectral.spectral_guns.stats.AchievementHandler.Achievements;
import com.spectral.spectral_guns.stats.AchievementPageHandler.AchievementPages;

public abstract class Legendary
{
	public static final HashMap<String, Legendary> legendaries = new HashMap();
	private static int nextNumber = 0;
	private final String name;
	public final int number;
	
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
	
	public static Legendary getLegendaryFromNum(int number)
	{
		for(String s : legendaries.keySet())
		{
			Legendary l = legendaries.get(s);
			if(l != null)
			{
				if(l.number == number)
				{
					return l;
				}
			}
		}
		return null;
	}
	
	public static final void addAchievements()
	{
		int lenght = (int)Math.ceil(Math.sqrt(nextNumber));
		int maxY = AchievementPages.mod.maxY;
		for(int i = 0; i < nextNumber; ++i)
		{
			Legendary l = getLegendaryFromNum(i);
			if(l != null)
			{
				int y = (int)Math.floor((float)i / (float)lenght);
				int x = i - y * lenght;
				AchievementLegendaryGunName ach = new AchievementLegendaryGunName(l, x - Math.round((float)lenght / 2) + 2, y + maxY + 2);
				Achievements.legendaries.add(ach);
				AchievementHandler.registerAchievement(ach, AchievementPages.mod);
			}
		}
	}
	
	public Legendary(String name)
	{
		if(legendaries.containsKey(name))
		{
			throw new IllegalArgumentException("Legendary name " + '"' + name + '"' + " already taken! So sad... :,(");
		}
		legendaries.put(name, this);
		this.name = name;
		this.number = nextNumber;
		++nextNumber;
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
	
	public abstract ItemStack getAchievementIcon();
	
	public abstract String getAchievementDescription();
	
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
	
	public void triggerAchievement(ItemStack stack, World world, EntityPlayer player)
	{
		for(AchievementLegendaryGunName ach : Achievements.legendaries)
		{
			if(ach.legendary == this)
			{
				player.triggerAchievement(ach);
			}
		}
	}
}
