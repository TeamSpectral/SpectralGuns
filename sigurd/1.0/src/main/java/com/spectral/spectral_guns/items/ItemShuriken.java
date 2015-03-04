package com.spectral.spectral_guns.items;

import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemShuriken extends ItemBase
{
	public ItemShuriken(String id)
	{
		super(id);
		this.maxStackSize = 32;
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer player, int timeLeft)
	{
		int j = this.getMaxItemUseDuration(stack) - timeLeft;

		float f = (float)j / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;

		if ((double)f < 0.1D)
		{
			return;
		}

		if (f > 1.0F)
		{
			f = 1.0F;
		}

		double inaccuracy = 0.01/f;
		if(inaccuracy > 0.3)
		{
			inaccuracy = 0.3;
		}
		EntityShuriken shuriken = new EntityShuriken(worldIn, player, f/2 + 1F + Randomization.r((float)inaccuracy/2));
		shuriken.motionX += Randomization.r(inaccuracy);
		shuriken.motionY += Randomization.r(inaccuracy);
		shuriken.motionZ += Randomization.r(inaccuracy);
		
		
		if (f >= 1.0F)
		{
			shuriken.setIsCritical(true);
		}

        worldIn.playSoundAtEntity(player, "random.bow", 0.2F, 1.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		--stack.stackSize;

		if(!worldIn.isRemote)
		{
			worldIn.spawnEntityInWorld(shuriken);
		}
		
		if(stack.stackSize < 1)
		{
			player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
		}
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		return stack;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 18000;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));

		return itemStackIn;
	}
}
