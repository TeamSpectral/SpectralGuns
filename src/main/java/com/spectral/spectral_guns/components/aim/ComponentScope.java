package com.spectral.spectral_guns.components.aim;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;

public class ComponentScope extends ComponentGeneric
{
	public final float zoom;
	
	public ComponentScope(ComponentMaterial material, float zoom)
	{
		this(new String2("", ""), new String2("", ""), material, zoom);
	}
	
	protected ComponentScope(String2 id, String2 name, ComponentMaterial material, float zoom)
	{
		super(new String2("scope", "").add(id), new String2("scope", "").add(name), 0.3, 6 * 2 * 2, 0.9F, Type.AIM, material);
		this.requiredTypes = new Type[]{Type.TRIGGER};
		this.zoom = zoom;
	}
	
	@Override
	public float zoom(int slot, float zoom, ItemStack stack, World world, EntityPlayer player)
	{
		return zoom * ((float)Math.sqrt(this.zoom - 1) * 2.6F + 1);
	}
	
	@Override
	public void registerRecipe()
	{
		switch(this.material)
		{
		case WOOD:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.lens_convex, 'B', M.barrel_thin_wood.item, 'G', "gearWood"}));
			break;
		case IRON:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_wood.item, 'B', M.barrel_thin_iron.item, 'G', "gearIron"}));
			break;
		case GOLD:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_iron.item, 'B', M.barrel_thin_gold.item, 'G', "gearGold"}));
			break;
		case DIAMOND:
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this.item), new Object[]{" PE", "LPB", " G ", 'P', M.prism, 'E', M.eyepiece, 'L', M.scope_gold.item, 'B', M.barrel_thin_diamond.item, 'G', "gearDiamond"}));
			break;
		}
	}
	
	@Override
	public void renderModel(int slot, double x, double y, double z, float rx, float ry, float rz, Comparable... flags)
	{
		
	}
}
