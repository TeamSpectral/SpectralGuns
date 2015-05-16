package com.spectral.spectral_guns.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.inventory.ContainerGunWorkbench;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class GuiHandler implements IGuiHandler
{
	public static enum GuiIDs
	{
		GUN_WORKBENCH
		{
			@Override
			@SideOnly(Side.SERVER)
			public Container server(EntityPlayer player, World world, TileEntity tileEntity)
			{
				if(tileEntity == null || !(tileEntity instanceof TileEntityGunWorkbench))
				{
					return null;
				}
				return new ContainerGunWorkbench(player.inventory, (TileEntityGunWorkbench)tileEntity);
			}
			
			@Override
			@SideOnly(Side.CLIENT)
			public Gui client(EntityPlayer player, World world, TileEntity tileEntity)
			{
				if(tileEntity == null || !(tileEntity instanceof TileEntityGunWorkbench))
				{
					return null;
				}
				return new GuiContainerGunWorkbench(player.inventory, (TileEntityGunWorkbench)tileEntity);
			}
		};
		
		public abstract Container server(EntityPlayer player, World world, TileEntity tileEntity);
		
		public abstract Gui client(EntityPlayer player, World world, TileEntity tileEntity);
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(GuiIDs.values()[ID] != null)
		{
			return GuiIDs.values()[ID].server(player, world, tileEntity);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(GuiIDs.values()[ID] != null)
		{
			return GuiIDs.values()[ID].client(player, world, tileEntity);
		}
		return null;
	}
}
