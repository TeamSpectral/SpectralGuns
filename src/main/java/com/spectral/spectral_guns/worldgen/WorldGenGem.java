package com.spectral.spectral_guns.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenGem implements IWorldGenerator
{
	public final Block ore;
	public final Class<? extends BiomeGenBase>[] biomes;
	
	public WorldGenGem(Block ore2, Class<? extends BiomeGenBase>[] biomes)
	{
		this.ore = ore2;
		this.biomes = biomes;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		int i = 3 + rand.nextInt(6);
		int j;
		int k;
		int l;
		BlockPos chunkpos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
		
		for(j = 0; j < i; ++j)
		{
			k = rand.nextInt(16);
			l = rand.nextInt(28) + 4;
			int i1 = rand.nextInt(16);
			BlockPos blockpos1 = chunkpos.add(k, l, i1);
			
			for(Class<? extends BiomeGenBase> biomeclass : this.biomes)
			{
				if(biomeclass.isInstance(world.getBiomeGenForCoords(blockpos1)))
				{
					if(world.getBlockState(blockpos1).getBlock().isReplaceableOreGen(world, blockpos1, net.minecraft.block.state.pattern.BlockHelper.forBlock(Blocks.stone)))
					{
						world.setBlockState(blockpos1, this.ore.getDefaultState(), 2);
					}
					break;
				}
			}
		}
	}
}
