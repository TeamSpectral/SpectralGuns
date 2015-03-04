package com.spectral.spectral_guns.event_handler;

import com.spectral.spectral_guns.gui.GuiSpectralGunsHud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandlerClient extends HandlerCommon
{
	//minecraftforge events for client only here!

	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent.Pre event)
	{
		if(event.type == ElementType.ALL)
		{
			GuiSpectralGunsHud hud = new GuiSpectralGunsHud(Minecraft.getMinecraft());
			hud.renderGameOverlay(Minecraft.getMinecraft().thePlayer);
		}
	}
}
