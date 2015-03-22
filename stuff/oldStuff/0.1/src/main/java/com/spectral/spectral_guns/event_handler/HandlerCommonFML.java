package com.spectral.spectral_guns.event_handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.items.ItemBase;

public class HandlerCommonFML extends HandlerBase
{
	//fml events for both sides here!

	@EventHandler
	public void FMLInitializationEvent(FMLInitializationEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		RenderItem ri = mc.getRenderItem();
		ItemModelMesher imm = ri.getItemModelMesher();
		for(int i = 0; i < ItemBase.itemsToBeRegistered.size(); ++i)
		{
			ItemBase item = ItemBase.itemsToBeRegistered.get(i);
			String id = References.MODID + ":" + item.id;
			imm.register(item, 0, new ModelResourceLocation(id, "inventory"));
			ModelBakery.addVariantName(item, new String[]{id});
		}
	}
}
