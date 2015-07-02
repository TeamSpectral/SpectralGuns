package com.spectral.spectral_guns.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemFood2 extends ItemFood
{
	public final int itemUseDuration;
	public final EnumAction action;
	
	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(int amount, float saturation, boolean isWolfFood, int duration, EnumAction action)
	{
		super(amount, saturation, isWolfFood);
		this.itemUseDuration = duration;
		this.action = action;
	}
	
	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(int amount, float saturation, boolean isWolfFood, int duration)
	{
		this(amount, saturation, isWolfFood, duration, EnumAction.EAT);
	}
	
	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(int amount, float saturation, boolean isWolfFood, int duration, CreativeTabs[] tabs)
	{
		this(amount, saturation, isWolfFood, duration);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		--stack.stackSize;
		playerIn.getFoodStats().addStats(this.getHealAmount(stack), this.getSaturationModifier(stack));
		worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		this.onFoodEaten(stack, worldIn, playerIn);
		playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
		return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return this.itemUseDuration;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return this.action;
	}
}
