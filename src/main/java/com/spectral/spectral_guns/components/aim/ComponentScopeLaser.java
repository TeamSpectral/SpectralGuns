package com.spectral.spectral_guns.components.aim;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.entity.projectile.EntityLaser;
import com.spectral.spectral_guns.entity.projectile.EntityLaser.LaserColor;

public class ComponentScopeLaser extends ComponentScope
{
	public final LaserColor color;
	
	public ComponentScopeLaser(ComponentScope c, LaserColor color)
	{
		super(new String2("", "_laser_" + color.toString().toLowerCase()), new String2("", ".laser." + color.toString().toLowerCase()), c.material, c.zoom);
		this.color = color;
	}
	
	@Override
	public void update(int slot, ItemStack gun, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		boolean b = EntityExtendedPlayer.get(player).isZoomHeldDown;
		if(isSelected && b)
		{
			EntityLaser e = new EntityLaser(world, player, 0.1, this.color, 0.1);
			if(!world.isRemote || true) //this needs to be spawned clientside as well, but only for laser entities
			{
				world.spawnEntityInWorld(e);
			}
		}
	}
	
	@Override
	public void registerRecipe()
	{
		Item diode = M.laser_diode_green;
		switch(this.color)
		{
		case CYAN:
			break;
		case GREEN:
			diode = M.laser_diode_green;
			break;
		case RED:
			diode = M.laser_diode_red;
			break;
		case VIOLET:
			break;
		}
		switch(this.material)
		{
		case WOOD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{"D", "S", 'D', diode, 'S', M.scope_wood.item});
			break;
		case IRON:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{"D", "S", 'D', diode, 'S', M.scope_iron.item});
			break;
		case GOLD:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{"D", "S", 'D', diode, 'S', M.scope_gold.item});
			break;
		case DIAMOND:
			GameRegistry.addShapedRecipe(new ItemStack(this.item), new Object[]{"D", "S", 'D', diode, 'S', M.scope_diamond.item});
			break;
		}
	}
}