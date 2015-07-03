package com.spectral.spectral_guns.event;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.components.aim.ComponentScope;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.items.ItemGun;
import com.spectral.spectral_guns.packet.PacketKey;
import com.spectral.spectral_guns.packet.PacketKey.Key;
import com.spectral.spectral_guns.particles.ParticleHandler;

@SideOnly(Side.CLIENT)
public class HandlerClientFML extends HandlerCommonFML
{
	// fml events for client only here!
	
	public static KeyBinding WeaponReload;
	public static KeyBinding WeaponZoom;
	public static KeyBinding WeaponEject;
	
	public static void init()
	{
		WeaponReload = new KeyBinding("key.WeaponReload", Keyboard.KEY_R, "key.categories.spectral.guns");
		WeaponZoom = new KeyBinding("key.WeaponZoom", Keyboard.KEY_Z, "key.categories.spectral.guns");
		WeaponEject = new KeyBinding("key.WeaponEject", Keyboard.KEY_B, "key.categories.spectral.guns");
		
		ClientRegistry.registerKeyBinding(WeaponReload);
		ClientRegistry.registerKeyBinding(WeaponZoom);
		ClientRegistry.registerKeyBinding(WeaponEject);
	}
	
	HashMap<KeyBinding, Integer> keyPressed = new HashMap<KeyBinding, Integer>();
	
	public boolean keypress(KeyBinding k)
	{
		if(!this.keyPressed.containsKey(k))
		{
			this.keyPressed.put(k, 0);
		}
		boolean b = true;
		if(!k.isKeyDown() || this.keyPressed.get(k) > 0)
		{
			b = false;
		}
		this.keyPressed.put(k, k.isKeyDown() ? this.keyPressed.get(k) + 1 : 0);
		return b;
	}
	
	public boolean keyhold(KeyBinding k, int rate)
	{
		if(!this.keyPressed.containsKey(k) || !k.isKeyDown())
		{
			this.keyPressed.put(k, 0);
			return false;
		}
		if(k.isKeyDown() && this.keyPressed.get(k) >= rate)
		{
			this.keyPressed.put(k, 0);
			return true;
		}
		this.keyPressed.put(k, k.isKeyDown() ? this.keyPressed.get(k) + 1 : 0);
		return false;
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event)
	{
		if(Minecraft.getMinecraft().thePlayer != null)
		{
			// use for keys to hit once
			if(this.keypress(WeaponReload) && !WeaponEject.isKeyDown())
			{
				M.network.sendToServer(new PacketKey(Key.RELOAD));
			}
			if(this.keypress(WeaponEject) && !WeaponReload.isKeyDown())
			{
				M.network.sendToServer(new PacketKey(Key.EJECT));
			}
		}
	}
	
	@SubscribeEvent
	public void clientTickEvent(ClientTickEvent event)
	{
		if(Minecraft.getMinecraft().thePlayer != null)
		{
			// use for keys to hold down
			if(Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown())
			{
				this.sendKey(Key.RIGHTCLICK);
				if(ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer) != null)
				{
					ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).setRightClick(true);
				}
			}
			else
			{
				this.sendKey(Key.NOTRIGHTCLICK);
				if(ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer) != null)
				{
					ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).setRightClick(false);
				}
			}
			if(this.keyhold(WeaponReload, 8) && !WeaponEject.isKeyDown())
			{
				this.sendKey(Key.RELOAD);
			}
			if(this.keyhold(WeaponEject, 8) && !WeaponReload.isKeyDown())
			{
				this.sendKey(Key.EJECT);
			}
			if(WeaponZoom.isKeyDown())
			{
				this.sendKey(Key.ZOOM);
				if(ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer) != null)
				{
					ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).isZoomHeldDown = true;
				}
			}
			else
			{
				this.sendKey(Key.NOTZOOM);
				if(ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer) != null)
				{
					ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).isZoomHeldDown = false;
				}
			}
			
			if(Minecraft.getMinecraft().thePlayer.getHeldItem() != null)
			{
				ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getHeldItem();
				if(itemstack.getItem() instanceof ItemGun)
				{
					ArrayList<ComponentScope> a = Stuff.ArraysAndSuch.allExtending(Stuff.ArraysAndSuch.hashMapToArrayList(ItemGun.getComponents(itemstack)), ComponentScope.class);
					Minecraft.getMinecraft().gameSettings.smoothCamera = Config.smoothZooming.get() && a.size() > 0 && EntityExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).isZoomHeldDown && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().gameSettings.thirdPersonView == 1);
				}
			}
		}
		ParticleHandler.update();
	}
	
	protected void sendKey(Key k)
	{
		PacketKey m = new PacketKey(k);
		M.network.sendToServer(m);
		PacketKey.Handler.onMessage(m, Minecraft.getMinecraft().thePlayer);
	}
}