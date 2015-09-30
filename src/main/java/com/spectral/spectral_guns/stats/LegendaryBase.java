package com.spectral.spectral_guns.stats;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class LegendaryBase extends Legendary
{
	protected final EnumChatFormatting color;
	protected final int xpReward;
	protected final ItemStack achievementIcon;
	protected final String desc;
	
	public LegendaryBase(String name, EnumChatFormatting color, int xpReward, ItemStack achievementIcon, String desc)
	{
		super(name);
		this.color = color;
		this.xpReward = xpReward;
		this.achievementIcon = achievementIcon;
		this.desc = desc;
	}
	
	@Override
	public EnumChatFormatting getColor(ItemStack stack, EntityPlayer player)
	{
		return this.color;
	}
	
	@Override
	public int getXpReward(EntityPlayer player)
	{
		return this.xpReward;
	}
	
	@Override
	public ItemStack getAchievementIcon()
	{
		return this.achievementIcon;
	}
	
	@Override
	public String getAchievementDescription()
	{
		return this.desc;
	}
	
	@Override
	public void onFire(Entity e, ItemStack stack, World world, EntityPlayer player, double modifier)
	{
		
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity player, int invSlot, boolean isSelected)
	{
		
	}
}
