package com.spectral.spectral_guns.components.trigger_mechanism;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;
import com.spectral.spectral_guns.components.IComponentHeatOnFire;
import com.spectral.spectral_guns.components.magazine.ComponentMagazine;
import com.spectral.spectral_guns.items.ItemGun;

public class ComponentTriggerMechanism extends ComponentGeneric implements IComponentHeatOnFire
{
	public final float delay;
	public final double heating;
	
	protected ComponentTriggerMechanism(String2 id, String2 name, ComponentMaterial material, float delay, double heating)
	{
		super(new String2("trigger", "").add(id), new String2("trigger", "").add(name), 0.4, 3 * 5 * 5 / 2, 3.2F, Type.TRIGGER, material);
		this.delay = delay;
		this.heating = heating;
		this.requiredTypes = new Type[]{Type.MAGAZINE};
	}
	
	public float magazineHeat = 0.25F;
	
	public ComponentTriggerMechanism(ComponentMaterial material, float delay, double heating)
	{
		this(new String2(), new String2(), material, delay, heating);
	}
	
	@SuppressWarnings("incomplete-switch")
	@Override
	public void registerRecipe()
	{
		Item bar = Items.iron_ingot;
		switch(this.material)
		{
		case WOOD:
			bar = Item.getItemFromBlock(Blocks.planks);
			break;
		case IRON:
			bar = Items.iron_ingot;
			break;
		case GOLD:
			bar = Items.gold_ingot;
			break;
		case DIAMOND:
			bar = Items.diamond;
			break;
		}
		Item nugget = M.iron_nugget;
		switch(this.material)
		{
		case WOOD:
			nugget = Items.stick;
			break;
		case IRON:
			nugget = M.iron_nugget;
			break;
		case GOLD:
			nugget = Items.gold_nugget;
			break;
		}
		Item gear = M.gear_iron;
		switch(this.material)
		{
		case WOOD:
			gear = M.gear_wood;
			break;
		case IRON:
			gear = M.gear_iron;
			break;
		case GOLD:
			gear = M.gear_gold;
			break;
		case DIAMOND:
			bar = M.gear_diamond;
			break;
		}
		GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{"NNB", "LGB", " HB", 'N', nugget, 'B', bar, 'L', Item.getItemFromBlock(Blocks.lever), 'G', gear, 'H', Item.getItemFromBlock(Blocks.tripwire_hook)});
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay + this.delay;
	}
	
	@Override
	public void heatUp(int slot, ItemStack stack, double modifier)
	{
		this.addHeat(slot, this.heating * modifier, stack);
		
		ArrayList<Component> cs = ItemGun.getComponentsOfType(Type.MAGAZINE);
		if(cs.size() > 0 && cs.get(0) instanceof ComponentMagazine)
		{
			this.addHeat(slot, modifier * ((ComponentMagazine)cs.get(0)).heating * this.magazineHeat, stack);
		}
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip)
	{
		super.getTooltip(tooltip);
		tooltip.add(new String2("Delay:", "" + this.delay));
		tooltip.add(new String2("Heating:", this.ADDS(this.heating) + this.ADDS(this.magazineHeat) + this.MULTIPLIES + "Magazine Heating"));
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
