package com.spectral.spectral_guns.items;

import com.spectral.spectral_guns.achievement.AchievementHandler.Achievements;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBlockGunWorkbench extends ItemBlock
{
	public ItemBlockGunWorkbench(Block block)
	{
		super(block);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		player.triggerAchievement(Achievements.buildAssemblyStation);
	}
}
