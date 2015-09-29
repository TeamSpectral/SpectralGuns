package com.spectral.spectral_guns.achievement;

import java.util.ArrayList;

import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

import com.spectral.spectral_guns.References;
import com.spectral.spectral_guns.Stuff;

public final class AchievementPageHandler
{
	public static final class AchievementPages
	{
		public static final AchievementPageDefinition mod = registerPage(References.NAME);
		
		public AchievementPages()
		{
			
		}
	}
	
	private static final ArrayList<AchievementPageDefinition> defs = new ArrayList();
	
	public static final AchievementPageDefinition registerPage(String id)
	{
		if(AchievementPage.getAchievementPage(id) == null && getDef(id) == null)
		{
			AchievementPageDefinition def = new AchievementPageDefinition(id);
			defs.add(def);
			return def;
		}
		else
		{
			throw new IllegalArgumentException("Achievement page id " + '"' + id + '"' + " is already used!");
		}
	}
	
	private static final AchievementPageDefinition getDef(String id)
	{
		for(AchievementPageDefinition def : defs)
		{
			if(def.id.equals(id))
			{
				return def;
			}
		}
		return null;
	}
	
	private static final AchievementPage get(AchievementPageDefinition def)
	{
		return AchievementPage.getAchievementPage(def.id);
	}
	
	public static final void registerAchievementPages()
	{
		for(AchievementPageDefinition def : defs)
		{
			ArrayList<Achievement> achieves = AchievementHandler.getAchievementsForPage(def);
			AchievementPage.registerAchievementPage(new AchievementPage(def.id, Stuff.ArraysAndSuch.arrayListToArray2(achieves, new Achievement[achieves.size()])));
		}
		defs.clear();
	}
	
	public static class AchievementPageDefinition
	{
		public final String id;
		
		private boolean b = false;
		public int minX = 0;
		public int maxX = 0;
		public int minY = 0;
		public int maxY = 0;
		
		private AchievementPageDefinition(String id)
		{
			this.id = id;
		}
		
		public AchievementPage get()
		{
			return AchievementPageHandler.get(this);
		}
		
		public void updateBoundries(Achievement a)
		{
			this.maxX = !this.b || this.maxX < a.displayColumn ? a.displayColumn : this.maxX;
			this.minX = !this.b || this.minX > a.displayColumn ? a.displayColumn : this.minX;
			
			this.maxY = !this.b || this.maxY < a.displayRow ? a.displayRow : this.maxY;
			this.minY = !this.b || this.minY > a.displayRow ? a.displayRow : this.minY;
			
			this.b = true;
		}
	}
}
