package com.spectral.spectral_guns.worldgen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import com.google.common.base.Predicate;

public class WorldGenMinable2 extends WorldGenMinable implements IWorldGenerator
{
	public WorldGenMinable2(IBlockState blockstate, int perChunk)
	{
		super(blockstate, perChunk);
	}
	
	public WorldGenMinable2(IBlockState blockstate, int perChunk, Predicate predicate)
	{
		super(blockstate, perChunk, predicate);
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		BlockPos chunkpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
		this.generate(world, rand, chunkpos);
	}
}
