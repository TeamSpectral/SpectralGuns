package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;

public class ComponentTriggerMechanismAuto extends ComponentTriggerMechanism
{
	protected ComponentTriggerMechanismAuto(ComponentMaterial material, float delay, double heating)
	{
		super(new String2("", "_auto"), new String2("", ".automatic"), material, delay * 1.1F, heating + 15);
	}
	
	public ComponentTriggerMechanismAuto(ComponentTriggerMechanism c)
	{
		this(c.material, c.delay, c.heating);
	}
	
	@Override
	public boolean automatic(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return true;
	}
	
	public float recoil = 1.1F;
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return super.recoil(slot, recoil, stack, world, player) * this.recoil;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return super.delay(slot, delay, stack, world, player) * this.delay;
	}
	
	public float fireRate = 0.25F;
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return super.fireRate(slot, rate, stack, world, player) * this.fireRate;
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip)
	{
		super.getTooltip(tooltip);
		tooltip.add(new String2("Delay:", "" + this.delay));
		tooltip.add(new String2("Fire Rate:", this.MULTIPLIES + this.fireRate));
		tooltip.add(new String2("Recoil:", this.MULTIPLIES + this.recoil));
		tooltip.add(new String2("Automatic", "" + true));
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		switch(this.material)
		{
		case WOOD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" G ", " TR", " G ", 'G', M.gear_wood, 'T', M.trigger_wood.item, 'R', Items.redstone});
			break;
		case IRON:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" G ", " TR", " G ", 'G', M.gear_iron, 'T', M.trigger_iron.item, 'R', Items.redstone});
			break;
		case GOLD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{" G ", " TR", " G ", 'G', M.gear_gold, 'T', M.trigger_gold.item, 'R', Items.redstone});
			break;
		// case DIAMOND: GameRegistry.addShapedRecipe(new ItemStack(this.item),
		// new Object[]{" G ", " TR", " G ", 'G', M.gear_diamond, 'T',
		// M.trigger_diamond.item, 'R', Items.redstone}); break;
		}
	}
}
