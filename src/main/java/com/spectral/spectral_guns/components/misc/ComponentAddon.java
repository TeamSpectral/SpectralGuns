package com.spectral.spectral_guns.components.misc;

import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentGeneric;

public abstract class ComponentAddon extends ComponentGeneric
{
	public ComponentAddon(String id, String name, Type connected, double heatLoss, float heatThreshold, float maxDurability, ComponentMaterial material, int maxAmount)
	{
		super(new String2("addon", "_" + id), new String2("addon.", name), connected, heatLoss, heatThreshold, maxDurability, Type.MISC, material);
		this.maxAmount = maxAmount;
	}
}
