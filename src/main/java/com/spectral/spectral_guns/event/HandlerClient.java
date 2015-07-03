package com.spectral.spectral_guns.event;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.components.magazine.IComponentAmmoItem;
import com.spectral.spectral_guns.gui.GuiSpectralGunsHud;
import com.spectral.spectral_guns.gui.GuiSpectralGunsHud.RenderOrder;
import com.spectral.spectral_guns.items.ItemAmmo;
import com.spectral.spectral_guns.items.ItemGun;

@SideOnly(Side.CLIENT)
public class HandlerClient extends HandlerCommon
{
	// minecraftforge events for client only here!
	
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
		ArrayList<IComponentAmmoItem> cs = ArraysAndSuch.allExtending(ComponentRegister.getAll(), IComponentAmmoItem.class);
		for(int i = 0; i < cs.size(); ++i)
		{
			if(event.itemStack.getItem() instanceof ItemAmmo)
			{
				ItemAmmo item = (ItemAmmo)event.itemStack.getItem();
				event.itemStack.setItem(item.ammo);
				if(!cs.get(i).isAmmoItem(event.itemStack))
				{
					cs.remove(i);
					--i;
				}
				event.itemStack.setItem(item);
			}
			else
			{
				if(!cs.get(i).isAmmoItem(event.itemStack) || ItemAmmo.itemHasItemAmmo(event.itemStack.getItem()))
				{
					cs.remove(i);
					--i;
				}
			}
		}
		if(cs.size() > 0)
		{
			for(int i = 0; i < cs.size(); ++i)
			{
				if(cs.get(i) instanceof Component)
				{
					if(i == 0)
					{
						event.toolTip.add("Ammo(" + ComponentEvents.amount(new ItemStack(M.gun), event.itemStack) + ") for:");
					}
					event.toolTip.add(" - " + I18n.format(((Component)cs.get(i)).item.getUnlocalizedName() + ".name"));
				}
			}
		}
	}
}
