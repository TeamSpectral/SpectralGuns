package com.spectral.spectral_guns.components.misc;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.IComponentHeatOnFire;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentOverclocker extends ComponentAddon implements IComponentHeatOnFire
{
	public ComponentOverclocker()
	{
		super("overclocker", "overclocker", Type.MAGAZINE, 0.2, 2 * 3 * 2, 1.2F, ComponentMaterial.GOLD, 5);
	}
	
	public float fireRate1 = 0.7F;
	public float fireRate2 = 0.9F;
	public double heating = 100;
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("Fire Rate:", "Is gun fully automatic?: " + this.MULTIPLIES + this.fireRate2 + ", if not: " + this.MULTIPLIES + this.fireRate1));
		tooltip.add(new String2("Heating:", this.ADDS(this.heating)));
		tooltip.add(new String2("Recoil:", this.MULTIPLIES + this.recoil(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Instability:", this.MULTIPLIES + this.instability(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Kickback:", this.MULTIPLIES + this.kickback(-1, 1, new ItemStack(M.gun), world, player)));
	}
	
	@Override
	public void registerRecipe()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item, 1), new Object[]{"HRL", "RCR", "rGc", 'H', Blocks.hopper, 'R', "blockRedstone", 'L', Blocks.lever, 'G', M.gear_diamond, 'r', Items.repeater, 'c', Items.comparator, 'C', Items.clock}));
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		if(!ItemGun.automatic(stack, player))
		{
			return rate * this.fireRate1;
		}
		else
		{
			return rate * this.fireRate2;
		}
	}
	
	@Override
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		this.addHeat(slot, this.heating * modifier, stack);
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil * 1.2F;
	}
	
	@Override
	public float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability * 1.3F;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback * 1.1F;
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
