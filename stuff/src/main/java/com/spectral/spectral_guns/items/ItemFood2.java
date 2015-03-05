package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.List;

import com.spectral.spectral_guns.IDAble;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.Stuff;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFood2 extends ItemFood implements IDAble
{
    public final int itemUseDuration;
    public final EnumAction action;
	private final String id; public final String getId(){return id;}
	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(String id, int amount, float saturation, boolean isWolfFood, int duration, EnumAction action)
	{
		super(amount, saturation, isWolfFood);
        this.itemUseDuration = duration;
        this.action = action;
        this.id = id;
	}
	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(String id, int amount, float saturation, boolean isWolfFood, int duration)
	{
		this(id, amount, saturation, isWolfFood, duration, EnumAction.EAT);
	}

	/**
	 * Food extending ItemBase
	 */
	public ItemFood2(String id, int amount, float saturation, boolean isWolfFood, int duration, CreativeTabs[] tabs)
	{
		this(id, amount, saturation, isWolfFood, duration);
		this.setCreativeTabs(tabs);
	}

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
    {
        --stack.stackSize;
        playerIn.getFoodStats().addStats(this.getHealAmount(stack), this.getSaturationModifier(stack));
        worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(stack, worldIn, playerIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack)
    {
        return itemUseDuration;
    }

    public EnumAction getItemUseAction(ItemStack stack)
    {
        return action;
    }
    
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		if(M.creativeTabs.containsKey(this) && M.creativeTabs.get(this) != null)
		{
			return M.creativeTabs.get(this);
		}
		else
		{
			return super.getCreativeTabs();
		}
	}

	public ItemFood2 setCreativeTabs(CreativeTabs[] tabs)
	{
		M.creativeTabs.put(this, tabs);
		return this;
	}
}
