package com.spectral.spectral_guns.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.inventory.ContainerGunWorkbench;
import com.spectral.spectral_guns.inventory.SlotComponent;
import com.spectral.spectral_guns.inventory.SlotGun;
import com.spectral.spectral_guns.packet.PacketItemName;
import com.spectral.spectral_guns.tileentity.TileEntityGunWorkbench;

public class GuiContainerGunWorkbench extends GuiContainer
{
	private static final ResourceLocation guiTexturesTab = new ResourceLocation(References.MODID.toLowerCase() + ":" + "textures/gui/container/gun_table/tinkering.png");
	
	private TileEntityGunWorkbench tileEntity;
	private ContainerGunWorkbench container;
	private GuiTextField nameField;
	
	public GuiContainerGunWorkbench(InventoryPlayer playerInventory, TileEntityGunWorkbench tileEntity)
	{
		super(new ContainerGunWorkbench(playerInventory, tileEntity));
		this.tileEntity = tileEntity;
		this.container = (ContainerGunWorkbench)this.inventorySlots;
		this.xSize = 176;
		this.ySize = 196;
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
		this.fontRendererObj.drawString(I18n.format(this.tileEntity.getName() + ".gunName", new Object[0]), 8, this.ySize - 123, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 94, 4210752);
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
		this.drawTexturedModalRect(this.nameField.xPosition - 3, this.nameField.yPosition - 4, 0, this.ySize + (this.container.getSlot(0).getHasStack() ? 0 : 16), 110, 16);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		Keyboard.enableRepeatEvents(true);
		this.nameField = new GuiTextField(0, this.fontRendererObj, k + 10, l + this.ySize - 109, 103, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnabled(true);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(40);
		if(this.container.getSlot(0).getStack() != null && (StringUtils.isBlank(this.container.getGunName()) || this.container.getSlot(0).getStack().hasDisplayName()))
		{
			this.setText(this.container.getSlot(0).getStack().getDisplayName());
		}
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(this.nameField.textboxKeyTyped(typedChar, keyCode))
		{
			this.renameItem();
		}
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	private void renameItem()
	{
		String s = this.nameField.getText();
		Slot slot = this.container.getSlot(0);
		
		if(slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(Stuff.Strings.removeFormatting(slot.getStack().getDisplayName())))
		{
			s = "";
		}
		
		this.container.updateItemName(s);
		M.network.sendToServer(new PacketItemName(s, this.container.getClass()));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.nameField.drawTextBox();
		GlStateManager.popMatrix();
	}
	
	public void sendContainerAndContentsToPlayer(Container containerToSend, List itemsList)
	{
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}
	
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	{
		if(slotInd == 0)
		{
			this.setText(stack == null ? "" : stack.getDisplayName());
			this.nameField.setEnabled(stack != null);
			
			if(stack != null)
			{
				this.renameItem();
			}
		}
	}
	
	boolean b = false;
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(this.container.gunNameIsBeingSet)
		{
			this.setText(this.container.getGunName());
			this.container.gunNameIsBeingSet = false;
		}
		if(this.container.getSlot(0).getStack() != null && !this.nameField.isFocused() && StringUtils.isBlank(this.nameField.getText()))
		{
			this.setText(this.container.getSlot(0).getStack().getDisplayName());
			this.renameItem();
		}
		boolean flag = true;
		for(Object slot : this.container.inventorySlots)
		{
			if(slot instanceof SlotGun || slot instanceof SlotComponent)
			{
				ItemStack stack = ((Slot)slot).getStack();
				if(stack != null)
				{
					flag = false;
				}
			}
		}
		if(flag)
		{
			if(this.b)
			{
				this.nameField.setFocused(false);
				this.setText("");
				this.renameItem();
			}
			else
			{
				this.b = true;
			}
		}
		else
		{
			this.b = false;
		}
	}
	
	private void setText(String s)
	{
		this.nameField.setText(s == null ? "" : Stuff.Strings.removeFormatting(s));
	}
}
