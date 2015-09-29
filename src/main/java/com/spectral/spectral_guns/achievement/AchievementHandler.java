package com.spectral.spectral_guns.achievement;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.achievement.AchievementPageHandler.AchievementPageDefinition;
import com.spectral.spectral_guns.achievement.AchievementPageHandler.AchievementPages;

public final class AchievementHandler
{
	public static final class Achievements
	{
		//x -> the the left, y -> down
		public static final Achievement buildAssemblyStation = registerAchievement(new Achievement("achievement.buildAssemblyStation", "buildAssemblyStation", 1, -2, M.wrench, null), AchievementPages.mod);
		public static final Achievement buildGun = registerAchievement(new Achievement("achievement.buildGun", "buildGun", 1, 0, M.gun, Achievements.buildAssemblyStation), AchievementPages.mod);
		
		public Achievements()
		{
			
		}
	}
	
	private static final ArrayList<Achievement> achieves1 = new ArrayList();
	private static final HashMap<AchievementPageDefinition, ArrayList<Achievement>> achieves2 = new HashMap();
	
	public static final Achievement registerAchievement(Achievement achievement, AchievementPageDefinition page)
	{
		if(achievement.parentAchievement == null)
		{
			achievement = achievement.setIndependent();
		}
		if(page != null)
		{
			if(achieves2.get(page) == null)
			{
				achieves2.put(page, new ArrayList());
			}
			if(!achieves2.get(page).contains(achievement))
			{
				achieves2.get(page).add(achievement);
				page.updateBoundries(achievement);
				return achievement;
			}
			else
			{
				throw new IllegalArgumentException("Achievement id " + '"' + achievement.statId + '"' + " is already taken in page " + '"' + page.id + '"' + "!");
			}
		}
		else
		{
			if(!achieves1.contains(achievement) && !AchievementList.achievementList.contains(achievement))
			{
				achieves1.add(achievement);
				return achievement;
			}
			else
			{
				throw new IllegalArgumentException("Achievement id " + '"' + achievement.statId + '"' + " is already taken in the vanilla page!");
			}
		}
	}
	
	public static final void registerAchievements()
	{
		for(Achievement achieve : achieves1)
		{
			achieve.registerStat();
		}
		for(AchievementPageDefinition page : achieves2.keySet())
		{
			for(Achievement achieve : achieves2.get(page))
			{
				//nothing here for now...
			}
		}
	}
	
	public static final ArrayList<Achievement> getAchievementsForPage(AchievementPageDefinition def)
	{
		if(achieves2.get(def) == null)
		{
			return new ArrayList();
		}
		return achieves2.get(def);
	}
}
