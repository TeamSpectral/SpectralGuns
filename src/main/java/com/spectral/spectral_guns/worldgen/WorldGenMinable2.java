package com.spectral.spectral_guns.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.google.common.base.Predicate;
import com.spectral.spectral_guns.M;

public class WorldGenMinable2 extends WorldGenMinable implements IWorldGenerator
{
	public final IBlockState oreBlock2;
	
	public WorldGenMinable2(IBlockState blockstate, int perChunk)
	{
		super(blockstate, perChunk);
		this.oreBlock2 = blockstate;
	}
	
	public WorldGenMinable2(IBlockState blockstate, int perChunk, Predicate predicate)
	{
		super(blockstate, perChunk, predicate);
		this.oreBlock2 = blockstate;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BlockPos chunkpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
		this.generate(world, rand, chunkpos);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
	{
		if(!M.visible(this.oreBlock2.getBlock()))
		{
			return false;
		}
		return super.generate(world, rand, pos);
	}
}
