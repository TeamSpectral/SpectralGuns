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
	protected final String achievementTex;
	
	public LegendaryBase(String name, EnumChatFormatting color, int xpReward, String achievementTex)
	{
		super(name);
		this.color = color;
		this.xpReward = xpReward;
		this.achievementTex = achievementTex;
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
	public String getAchievementTexture()
	{
		return null;
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
