package com.spectral.spectral_guns.event_handler;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.items.ItemBase;
import com.spectral.spectral_guns.packet.PacketKey;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SideOnly(Side.CLIENT)
public class HandlerClientFML extends HandlerCommonFML
{
	//fml events for client only here!
	
	public static KeyBinding WeaponReload;
	public static KeyBinding WeaponZoom;

	public static void init()
	{
		WeaponReload = new KeyBinding("key.WeaponReload", Keyboard.KEY_R, "key.categories.spectral.guns");
		WeaponZoom = new KeyBinding("key.WeaponZoom", Keyboard.KEY_Z, "key.categories.spectral.guns");

		ClientRegistry.registerKeyBinding(WeaponReload);
		ClientRegistry.registerKeyBinding(WeaponZoom);
	}

	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		//use for keys to hit once
	}

	@SubscribeEvent
	public void clientTickEvent(ClientTickEvent event)
	{
		//use for keys to hold down
		
		//check if right click is held down or not
		if(Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())
		{
			M.network.sendToServer(new PacketKey(0));
		}
		else
		{
			M.network.sendToServer(new PacketKey(1));
		}
	}

	/**registration of item models (does not work at the moment. not even sure if this is necessary) - sigurd4**/
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