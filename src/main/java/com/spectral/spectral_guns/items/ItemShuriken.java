package com.spectral.spectral_guns.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.entity.projectile.EntityShuriken;

public class ItemShuriken extends Item
{
	public ItemShuriken()
	{
		super();
		this.maxStackSize = 32;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer player, int timeLeft)
	{
		int j = this.getMaxItemUseDuration(stack) - timeLeft;
		
		float f = j / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		
		if(f < 0.1D)
		{
			return;
		}
		
		if(f > 1.0F)
		{
			f = 1.0F;
		}
		
		double inaccuracy = 0.01 / f;
		if(inaccuracy > 0.3)
		{
			inaccuracy = 0.3;
		}
		EntityShuriken shuriken = new EntityShuriken(worldIn, player, f / 2.4F + 0.7F + Randomization.r((float)inaccuracy / 2));
		shuriken.motionX += Randomization.r(inaccuracy);
		shuriken.motionY += Randomization.r(inaccuracy);
		shuriken.motionZ += Randomization.r(inaccuracy);
		
		if(f >= 1.0F)
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
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		return stack;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 18000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		
		return itemStackIn;
	}
}
