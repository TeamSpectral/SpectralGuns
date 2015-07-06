package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.IComponentHeatOnFire;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentTriggerMechanismAuto extends ComponentTriggerMechanism implements IComponentHeatOnFire
{
	protected ComponentTriggerMechanismAuto(ComponentMaterial material, float delay)
	{
		super(new String2("", "_auto"), new String2("", ".automatic"), material, delay);
	}
	
	public ComponentTriggerMechanismAuto(ComponentTriggerMechanism c)
	{
		this(c.material, c.delay);
	}
	
	@Override
	public boolean automatic(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil * 3;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.delay * 1.3F;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate / 4;
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
	
	@Override
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		this.addHeat(slot, 30 * modifier, stack);
		HashMap<Integer, Component> cs = ItemGun.getComponents(stack);
		for(Integer slot2 : cs.keySet())
		{
			Component c = cs.get(slot2);
			if(c.type == Type.MAGAZINE)
			{
				c.addHeat(slot2, 200, stack);
			}
		}
	}
}
