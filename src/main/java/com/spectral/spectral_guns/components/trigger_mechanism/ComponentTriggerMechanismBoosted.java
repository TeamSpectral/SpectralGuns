package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.IComponentHeatOnFire;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentTriggerMechanismBoosted extends ComponentTriggerMechanism implements IComponentHeatOnFire
{
	protected ComponentTriggerMechanismBoosted(ComponentMaterial material, float delay)
	{
		super(new String2("", "_boosted"), new String2("", ".boosted"), material, delay);
	}
	
	public ComponentTriggerMechanismBoosted(ComponentTriggerMechanism c)
	{
		this(c.material, c.delay);
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil * 4;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay / 2 + this.delay;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate / 3;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed * 2;
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item trigger = M.trigger_iron.item;
		switch(this.material)
		{
		case WOOD:
			trigger = M.trigger_wood.item;
			break;
		case IRON:
			trigger = M.trigger_iron.item;
			break;
		case GOLD:
			trigger = M.trigger_gold.item;
			break;
		}
		GameRegistry.addShapelessRecipe(new ItemStack(this.item), new Object[]{Items.redstone, trigger});
	}
	
	@Override
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		this.addHeat(slot, 60 * modifier, stack);
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
