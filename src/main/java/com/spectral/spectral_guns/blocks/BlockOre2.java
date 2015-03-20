package com.spectral.spectral_guns.blocks;

import java.util.Random;

import com.spectral.spectral_guns.M;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOre2 extends BlockOre
{
	public final Item drop;
	protected final int amount;
	protected final int amountRandom;
	protected final int xp;
	protected final int xpRandom;
	
	public BlockOre2(Item drop, int xp, int xpRandom, int amount, int amountRandom)
	{
		super();
		this.drop = drop;
		this.amount = amount;
		this.amountRandom = amountRandom;
		this.xp = xp;
		this.xpRandom = xpRandom;
	}
	
	public BlockOre2(Item drop, int xp, int xpRandom, int amount)
	{
		this(drop, xp, xpRandom, amount, 0);
	}
	
	public BlockOre2(Item drop, int xp, int xpRandom)
	{
		this(drop, xp, xpRandom, 1);
	}
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return drop;
	}
	
	public int quantityDropped(Random random)
	{
		if(amountRandom > 0)
		{
			return amount + random.nextInt(amountRandom);
		}
		else
		{
			return amount;
		}
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		if(this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
		{
			if(amountRandom > 0)
			{
				return amount + rand.nextInt(amountRandom);
			}
			else
			{
				return amount;
			}
		}
		return 0;
	}
}
