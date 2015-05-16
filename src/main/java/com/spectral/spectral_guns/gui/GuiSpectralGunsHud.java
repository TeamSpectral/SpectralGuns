package com.spectral.spectral_guns.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.event.HandlerClientFML;
import com.spectral.spectral_guns.items.ItemGun;

@SideOnly(Side.CLIENT)
public class GuiSpectralGunsHud extends Gui
{
	protected final Random rand = new Random();
	protected final Minecraft mc;
	/** Remaining ticks the item highlight should be visible */
	protected int remainingHighlightTicks;
	/** The ItemStack that is currently being highlighted */
	protected ItemStack highlightingItemStack;
	protected ResourceLocation[] snowOverlay = {new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_0.png"), new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_1.png"), new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_2.png"), new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_3.png"), new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_4.png"), new ResourceLocation(References.MODID + ":" + "textures/misc/overlay_snow_5.png")};
	
	public GuiSpectralGunsHud(Minecraft mc)
	{
		this.mc = mc;
	}
	
	public static enum RenderOrder
	{
		PRE, POST
	}
	
	/**
	 * Render ingame hud
	 */
	public void renderGameOverlay(EntityPlayer player, RenderOrder order, ElementType type)
	{
		GL11.glPushMatrix();
		
		ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
		int w = scaledresolution.getScaledWidth();
		int h = scaledresolution.getScaledHeight();
		FontRenderer fontrenderer = this.mc.fontRendererObj;
		
		this.mc.mcProfiler.startSection("snowOverlay");
		
		this.mc.entityRenderer.setupOverlayRendering();
		
		if(this.mc.gameSettings.thirdPersonView == 0 && order == RenderOrder.PRE)
		{
			GL11.glEnable(GL11.GL_BLEND);
			this.renderSnow(scaledresolution, player);
		}
		
		this.mc.mcProfiler.endSection();
		
		if(this.mc.inGameHasFocus && order == RenderOrder.POST && type == ElementType.EXPERIENCE)
		{
			if(player.getHeldItem() != null && player.getHeldItem().getItem() == M.gun)
			{
				int line = 1;
				ItemStack stack = player.getHeldItem();
				this.mc.mcProfiler.startSection("ammoStats");
				if(true)
				{
					int a = ItemGun.ammo(stack, player);
					String s = "Ammo: " + a + "/" + ItemGun.capacity(stack, player);
					boolean b = false;
					if(!player.capabilities.isCreativeMode)
					{
						
					}
					if((b || player.capabilities.isCreativeMode) && a <= 0)
					{
						s += " (hit '" + Keyboard.getKeyName(HandlerClientFML.WeaponReload.getKeyCode()) + "' to reload)";
					}
					fontrenderer.drawStringWithShadow(s, 2, h - 10 * line, 0xAF8A33);
					++line;
				}
				this.mc.mcProfiler.endSection();
				
				this.mc.mcProfiler.startSection("fireRateStats");
				if(stack.getTagCompound().getInteger(ItemGun.FIRERATETIMER) > 0)
				{
					String s = "" + (float)stack.getTagCompound().getInteger(ItemGun.FIRERATETIMER) / 20;
					
					fontrenderer.drawStringWithShadow(s, 2, h - 10 * line, 0xAF8A33);
					++line;
				}
				this.mc.mcProfiler.endSection();
			}
		}
		
		int line = 0;
		
		GL11.glColor4f(1, 1, 1, 1);
		
		GL11.glPopMatrix();
	}
	
	private void renderSnow(ScaledResolution sr, EntityPlayer player)
	{
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		int i = 5;
		float f = 4;
		if(props != null)
		{
			f = props.snow;
		}
		if(f < 1)
		{
			i = 0;
		}
		else if(f < 2)
		{
			i = 1;
		}
		else if(f < 3)
		{
			i = 2;
		}
		else if(f < 4)
		{
			i = 3;
		}
		else if(f < 5)
		{
			i = 4;
		}
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(1.0F, 1.0F, 1.0F, (float)Math.sqrt(f) / 7);
		if(i > 0)
		{
			this.mc.getTextureManager().bindTexture(this.snowOverlay[i - 1]);
			Tessellator tessellator = Tessellator.getInstance();
			WorldRenderer worldrenderer = tessellator.getWorldRenderer();
			worldrenderer.startDrawingQuads();
			worldrenderer.addVertexWithUV(0.0D, sr.getScaledHeight(), -90.0D, 0.0D, 1.0D);
			worldrenderer.addVertexWithUV(sr.getScaledWidth(), sr.getScaledHeight(), -90.0D, 1.0D, 1.0D);
			worldrenderer.addVertexWithUV(sr.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
			worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
			tessellator.draw();
		}
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	private int getInventoryItem(InventoryPlayer inventory, Item item)
	{
		int j = 0;
		int k;
		ItemStack itemstack;
		
		for(k = 0; k < inventory.mainInventory.length; ++k)
		{
			itemstack = inventory.mainInventory[k];
			if(itemstack != null && (item == null || itemstack.getItem() == item))
			{
				j += itemstack.stackSize;
			}
		}
		return j;
	}
}