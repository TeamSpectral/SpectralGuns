package com.spectral.spectral_guns.stats;

import net.minecraft.client.Minecraft;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;

import com.spectral.spectral_guns.M;

public class AchievementLegendaryGunName extends Achievement
{
	public final Legendary legendary;
	
	public AchievementLegendaryGunName(Legendary legendary, int column, int row)
	{
		super("achievement.legendaryGunName." + legendary.getRequiredName(), legendary.getRequiredName(), column, row, legendary.getAchievementIcon() == null ? new ItemStack(M.gun) : legendary.getAchievementIcon(), null);
		this.legendary = legendary;
		this.setSpecial();
	}
	
	@Override
	public String getDescription()
	{
		return this.legendary.getAchievementDescription();
	}
	
	@Override
	public IChatComponent getStatName()
	{
		String s = "Legendary #" + this.legendary.number;
		if(M.proxy.side() == Side.CLIENT)
		{
			if(Minecraft.getMinecraft().thePlayer.getStatFileWriter().hasAchievementUnlocked(this))
			{
				s += ": " + this.legendary.getRequiredName();
			}
		}
		IChatComponent c = new ChatComponentTranslation(s);
		c.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ACHIEVEMENT, new ChatComponentText(this.statId)));
		c.getChatStyle().setColor(this.getSpecial() ? EnumChatFormatting.DARK_PURPLE : EnumChatFormatting.GREEN);
		return c;
	}
}
