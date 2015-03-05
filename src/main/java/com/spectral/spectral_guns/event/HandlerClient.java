package com.spectral.spectral_guns.event;

import java.util.ArrayList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.magazine.ComponentMagazine;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.gui.GuiSpectralGunsHud;
import com.spectral.spectral_guns.gui.GuiSpectralGunsHud.RenderOrder;
import com.spectral.spectral_guns.items.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

@SideOnly(Side.CLIENT)
public class HandlerClient extends HandlerCommon
{
	//minecraftforge events for client only here!

	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		GuiSpectralGunsHud hud = new GuiSpectralGunsHud(Minecraft.getMinecraft());
		hud.renderGameOverlay(player, event instanceof RenderGameOverlayEvent.Pre ? RenderOrder.PRE : RenderOrder.POST, event.type);
		if(player.getHeldItem() != null)
		{
			ItemStack itemstack = player.getHeldItem();
			if(itemstack.getItem() instanceof ItemGun)
			{
				if(event.type == ElementType.CROSSHAIRS && event.isCancelable())
				{
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void ItemTooltipEvent(ItemTooltipEvent event)
	{
		ArrayList<ComponentMagazine> cs = ArraysAndSuch.allExtending(ComponentRegister.getAll(), ComponentMagazine.class);
		for(int i = 0; i < cs.size(); ++i)
		{
			if(!cs.get(i).isAmmoItem(event.itemStack, event.entityPlayer.worldObj, event.entityPlayer))
			{
				cs.remove(i);
				--i;
			}
		}
		if(cs.size() > 0)
		{
			event.toolTip.add("Ammo for:");
			for(int i = 0; i < cs.size(); ++i)
			{
				event.toolTip.add(" - " + I18n.format(cs.get(i).getFancyName()));
			}
		}
	}
}
