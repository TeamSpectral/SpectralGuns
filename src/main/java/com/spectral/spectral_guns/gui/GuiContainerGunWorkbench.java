package com.spectral.spectral_guns.gui;

import java.awt.Color;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.inventory.ContainerGunWorkbench;
import com.spectral.spectral_guns.inventory.SlotComponent;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class GuiContainerGunWorkbench extends GuiContainer
{
	private static final ResourceLocation guiTexturesTab = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/container/gun_table/tinkering.png");
	
	private TileEntityGunWorkbench tileEntity;
	
	public GuiContainerGunWorkbench(InventoryPlayer playerInventory, TileEntityGunWorkbench tileEntity)
	{
		super(new ContainerGunWorkbench(playerInventory, tileEntity));
		this.tileEntity = tileEntity;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items). Args : mouseX, mouseY
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		GlStateManager.pushMatrix();
		this.fontRendererObj.drawString(this.tileEntity.hasCustomName() ? this.tileEntity.getName() : I18n.format(this.tileEntity.getName() + ".tinkering", new Object[0]), 8, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		float fade = this.tileEntity.getErrorFade();
		String message = this.tileEntity.getErrorMessage();
		if(fade > 0 && message != null)
		{
			GlStateManager.scale(2D / 3, 2D / 3, 2D / 3);
			GlStateManager.enableBlend();
			Color c = new Color(1, 0, 0, fade);
			this.fontRendererObj.drawString(message, 8, -10, c.getRGB());
		}
		GlStateManager.popMatrix();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.mc.getTextureManager().bindTexture(guiTexturesTab);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		for(int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i)
		{
			if(this.inventorySlots.inventorySlots.get(i) instanceof SlotComponent)
			{
				SlotComponent slot = (SlotComponent)this.inventorySlots.inventorySlots.get(i);
				this.drawTexturedModalRect(k + slot.xDisplayPosition - 1, l + slot.yDisplayPosition - 1, 176, 0, 18, 18);
				if(!slot.getHasStack())
				{
					this.drawTexturedModalRect(k + slot.xDisplayPosition, l + slot.yDisplayPosition, 176, 18 + 16 * slot.type.ordinal(), 16, 16);
				}
			}
		}
	}
}
