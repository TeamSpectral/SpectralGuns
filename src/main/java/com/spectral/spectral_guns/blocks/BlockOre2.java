package com.spectral.spectral_guns.blocks;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.spectral.spectral_guns.worldgen.WorldGenMinable2;

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
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return this.drop;
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		if(this.amountRandom > 0)
		{
			return this.amount + random.nextInt(this.amountRandom);
		}
		else
		{
			return this.amount;
		}
	}
	
	@Override
	public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune)
	{
		IBlockState state = world.getBlockState(pos);
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		if(this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this))
		{
			if(this.amountRandom > 0)
			{
				return this.amount + rand.nextInt(this.amountRandom);
			}
			else
			{
				return this.amount;
			}
		}
		return 0;
	}
	
	public IWorldGenerator worldGen()
	{
		return new WorldGenMinable2(this.getDefaultState(), 11);
	}
}
