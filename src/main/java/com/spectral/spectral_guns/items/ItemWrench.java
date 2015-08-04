package com.spectral.spectral_guns.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.M;

public class ItemWrench extends Item
{
	/**
	 * Wrench tool
	 */
	public ItemWrench()
	{
		super();
		this.setMaxDamage(64);
		this.setMaxStackSize(1);
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
	
	public ItemWrench setCreativeTabs(CreativeTabs[] tabs)
	{
		M.creativeTabs.put(this, tabs);
		return this;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return this.getUnlocalizedName();
	}
	
	/**
	 * Return whether this item is repairable in an anvil.
	 * 
	 * @param toRepair
	 *            The ItemStack to be repaired
	 * @param repair
	 *            The ItemStack that should repair this Item (leather for
	 *            leather armor, etc.)
	 */
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		ItemStack mat = new ItemStack(Items.iron_ingot);
		if(mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false))
		{
			return true;
		}
		return super.getIsRepairable(toRepair, repair);
	}
	
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
	
	/**
	 * Current implementations of this method in child classes do not use the
	 * entry argument beside ev. They just raise
	 * the damage on the stack.
	 * 
	 * @param target
	 *            The Entity being hit
	 * @param attacker
	 *            the attacking entity
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		stack.damageItem(2, attacker);
		return true;
	}
	
	/**
	 * Called when a Block is destroyed using this Item. Return true to trigger
	 * the "Use Item" statistic.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
	{
		if(blockIn.getBlockHardness(worldIn, pos) != 0.0D)
		{
			stack.damageItem(1, playerIn);
		}
		
		return true;
	}
}
