package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;

public class ComponentGrip extends ComponentGeneric
{
	public final float instabilityMultiplier;
	public final float recoilMultiplier;
	public final float kickbackMultiplier;
	
	public ComponentGrip(ComponentMaterial material, float instabilityMultiplier, float recoilMultiplier, float kickbackMultiplier)
	{
		super(new String2("grip", ""), new String2("grip", ""), Type.TRIGGER, 0.34, 3 * 9 * 1, 4.2F, Type.GRIP, material);
		this.instabilityMultiplier = instabilityMultiplier;
		this.recoilMultiplier = recoilMultiplier;
		this.kickbackMultiplier = kickbackMultiplier;
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("Instability:", this.MULTIPLIES + this.instability(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Recoil:", this.MULTIPLIES + this.recoil(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Kickback:", this.MULTIPLIES + this.kickback(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("", EnumChatFormatting.DARK_RED + "(high temperatures will hurt user)"));
	}
	
	@Override
	public void registerRecipe()
	{
		Object resource = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			resource = "plankWood";
			break;
		case IRON:
			resource = "ingotIron";
			break;
		case GOLD:
			resource = "ingotGold";
			break;
		case DIAMOND:
			resource = "gemDiamond";
			break;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item, 1), new Object[]{"# ", "# ", "##", '#', resource}));
	}
	
	@Override
	public float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability * this.instabilityMultiplier;
	}
	
	@Override
	public float recoil(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability * this.recoilMultiplier;
	}
	
	@Override
	public float kickback(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability * this.kickbackMultiplier;
	}
	
	@Override
	public void update(int slot, ItemStack gun, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		super.update(slot, gun, world, player, invSlot, isSelected);
		if(this.heat(slot, gun) > this.heatThreshold(slot, gun) || this.heat(slot, gun) > this.heatThreshold * ComponentMaterial.IRON.heatThresholdMax / 2)
		{
			if(player.ticksExisted % 20 == 1 && player.getHeldItem() == gun)
			{
				player.attackEntityFrom(DamageSource.inFire, 1F);
				this.addHeat(slot, -1, gun);
			}
			if(this.heat(slot, gun) * this.heatConductiveness(slot, gun) > 200 || this.heat(slot, gun) >= this.heatThreshold * ComponentMaterial.IRON.heatThresholdMax / 2)
			{
				if(world.rand.nextFloat() > 5 / 7)
				{
					player.setFire(1);
					this.addHeat(slot, -1, gun);
				}
			}
		}
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
