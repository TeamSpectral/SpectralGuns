package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;

public class ComponentTriggerMechanismBoosted extends ComponentTriggerMechanism
{
	protected ComponentTriggerMechanismBoosted(ComponentMaterial material, float delay, double heating)
	{
		super(new String2("", "_boosted"), new String2("", ".boosted"), material, delay / 2, heating + 30);
	}
	
	public ComponentTriggerMechanismBoosted(ComponentTriggerMechanism c)
	{
		this(c.material, c.delay, c.heating);
	}
	
	public float recoilMod = 4;
	public float fireRateMod = 0.5F;
	public float speedMod = 1.2F;
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return super.recoil(slot, recoil, stack, world, player) * this.recoilMod;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return super.fireRate(slot, rate, stack, world, player) * this.fireRateMod;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return super.speed(slot, speed, stack, world, player) * this.speedMod;
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		super.getTooltip(tooltip, player, world);
		tooltip.add(new String2("Delay:", "" + this.delay(-1, 0, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Fire Rate:", this.MULTIPLIES + this.fireRate(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Recoil:", this.MULTIPLIES + this.recoil(-1, 1, new ItemStack(M.gun), world, player)));
		tooltip.add(new String2("Speed:", this.MULTIPLIES + this.speed(-1, 1, new ItemStack(M.gun), world, player)));
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
}
