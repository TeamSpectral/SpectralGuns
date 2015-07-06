package com.spectral.spectral_guns.event;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
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
public class HandlerClientFML extends HandlerBase
{
	// fml events for client only here!
	
	private float pitch = 0;
	private float yaw = 0;
	private float pitchMove = 0;
	private float yawMove = 0;
	private float lastPitchMove = 0;
	private float lastYawMove = 0;
	
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
	
	@SubscribeEvent
	public void playerUpdateEvent(PlayerTickEvent event)
	{
		ExtendedPlayer props = ExtendedPlayer.get(event.player);
		
		if(event.player.getHeldItem() != null && event.player.getHeldItem().getItem() instanceof ItemGun)
		{
			event.player.rotationPitch += this.pitchMove;
			event.player.rotationYaw += this.yawMove;
			this.pitch += this.pitchMove;
			this.yaw += this.yawMove;
			this.pitchMove *= 0.97;
			this.yawMove *= 0.97;
			
			float f = 0.0005F;
			if(event.player.getHeldItem() != null && event.player.getHeldItem().getItem() instanceof ItemGun)
			{
				f *= 2 * ItemGun.instability(event.player.getHeldItem(), event.player);
				if(props.isZoomHeldDown)
				{
					f *= ItemGun.zoom(event.player.getHeldItem(), event.player, 1);
				}
				if(!props.isZoomHeldDown || ItemGun.getComponentsOf(event.player.getHeldItem(), ComponentScope.class).size() <= 0)
				{
					f /= 30;
				}
			}
			else
			{
				f /= 30;
			}
			
			Vec3 vec = Stuff.Coordinates3D.stabilize(new Vec3(Stuff.Randomization.r(f), Stuff.Randomization.r(f), 0), Stuff.Randomization.r(f));
			float lastPitchMove = this.lastPitchMove;
			float lastYawMove = this.lastYawMove;
			this.lastPitchMove = ((float)vec.xCoord + lastPitchMove * 3) / 4;
			this.lastYawMove = ((float)vec.yCoord + lastYawMove * 3) / 4;
			
			float max = 2;
			float w = (float)Math.sqrt((this.pitch + this.pitchMove) * (this.pitch + this.pitchMove) + (this.yaw + this.yawMove) * (this.yaw + this.yawMove));
			for(int i = 0; i < 20 && (this.pitch > 0 == this.pitchMove > 0 || this.yaw > 0 == this.yawMove > 0) && w > max; ++i)
			{
				vec = Stuff.Coordinates3D.stabilize(new Vec3(Stuff.Randomization.r(f), Stuff.Randomization.r(f), 0), Stuff.Randomization.r(f));
				this.lastPitchMove = ((float)vec.xCoord + lastPitchMove * 3) / 4;
				this.lastYawMove = ((float)vec.yCoord + lastYawMove * 3) / 4;
				this.pitchMove = (this.lastPitchMove + this.pitchMove) / 2;
				this.yawMove = (this.lastYawMove + this.yawMove) / 2;
				w = (float)Math.sqrt((this.pitch + this.yawMove) * (this.pitch + this.yawMove) + (this.yaw + this.lastYawMove) * (this.yaw + this.lastYawMove));
			}
			max = 2;
			w = (float)Math.sqrt(this.pitchMove * this.pitchMove + this.yawMove * this.yawMove);
			if(w > max)
			{
				this.pitchMove /= w / max;
				this.yawMove /= w / max;
				vec = Stuff.Coordinates3D.stabilize(new Vec3(Stuff.Randomization.r(f), Stuff.Randomization.r(f), 0), Stuff.Randomization.r(f));
				
				this.lastPitchMove = ((float)vec.xCoord + lastPitchMove) / 2;
				this.lastYawMove = ((float)vec.yCoord + lastYawMove) / 2;
			}
			max = 1;
			w = (float)Math.sqrt(this.lastPitchMove * this.lastPitchMove + this.lastYawMove * this.lastYawMove);
			if(w > max)
			{
				this.lastPitchMove /= w / max;
				this.lastYawMove /= w / max;
			}
			this.pitchMove += this.lastPitchMove;
			this.yawMove += this.lastYawMove;
		}
		else
		{
			this.pitchMove = 0;
			this.yawMove = 0;
			event.player.rotationPitch -= this.pitch;
			event.player.rotationYaw -= this.yaw;
			this.pitch = 0;
			this.yaw = 0;
		}
		
		float f = 0.02F;
		event.player.rotationPitch -= this.pitch * f;
		event.player.rotationYaw -= this.yaw * f;
		this.pitch -= this.pitch * f;
		this.yaw -= this.yaw * f;
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
			if(this.keypress(WeaponReload) && !WeaponEject.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
			{
				M.network.sendToServer(new PacketKey(Key.RELOAD));
			}
			if(this.keypress(WeaponEject) && !WeaponReload.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
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
			if(Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
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
			if(this.keyhold(WeaponReload, 8) && !WeaponEject.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
			{
				this.sendKey(Key.RELOAD);
			}
			if(this.keyhold(WeaponEject, 8) && !WeaponReload.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
			{
				this.sendKey(Key.EJECT);
			}
			if(WeaponZoom.isKeyDown() && Minecraft.getMinecraft().inGameHasFocus)
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
					Minecraft.getMinecraft().gameSettings.smoothCamera = Config.smoothZooming.get() && a.size() > 0 && ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).isZoomHeldDown && (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 || Minecraft.getMinecraft().gameSettings.thirdPersonView == 1 && Minecraft.getMinecraft().inGameHasFocus);
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