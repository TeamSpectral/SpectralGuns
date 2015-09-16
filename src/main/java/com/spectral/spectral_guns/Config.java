package com.spectral.spectral_guns;

import java.util.ArrayList;

import net.minecraftforge.common.config.Configuration;

import com.google.common.collect.Lists;

public class Config
{
	public static Configuration config = null;
	public static final ArrayList<ConfigEntry> entries = Lists.newArrayList();
	
	public enum ConfigEntryCategory
	{
		GENERAL
		{
			@Override
			public String toString()
			{
				return Configuration.CATEGORY_GENERAL;
			}
		},
		GUN, MOD_COMPATABILITY, CREATIVE_TABS;
		
		@Override
		public String toString()
		{
			return Stuff.Strings.UnderscoresToCamelSpaces(super.toString());
		}
	}
	
	public static final ConfigEntryBoolean showAllPossibleGunsInTab = new ConfigEntryBoolean(false, "showAllPossibleGunsInTab", ConfigEntryCategory.CREATIVE_TABS, "Enable if you want to see all possible gun combinations in the creative tab.");
	
	public static final ConfigEntryBoolean enableRubies = new ConfigEntryBoolean(true, "enableRubies", ConfigEntryCategory.MOD_COMPATABILITY, "Set to false to disable the ruby items in case another mod adds them. Prevents worldGen, recipes and everything. Restart required for changes to have effect.");
	public static final ConfigEntryBoolean enableIronNugget = new ConfigEntryBoolean(true, "enableIronNugget", ConfigEntryCategory.MOD_COMPATABILITY, "Set to false to disable the iron nugget item in case another mod adds it. Prevents recipes and everything. Restart required for changes to have effect.");
	
	public static final ConfigEntryBoolean canCraftGunInCraftingTable = new ConfigEntryBoolean(false, "canCraftGunInCraftingTable", ConfigEntryCategory.GUN, "Wether or not players should be able to craft guns in the crafting table as well as in the gun assembly table.");
	public static final ConfigEntryBoolean smoothZooming = new ConfigEntryBoolean(true, "smoothZooming", ConfigEntryCategory.GUN, "Enable or disable smooth camera movement while zooming/aiming with the guns.");
	public static final ConfigEntryBoolean canReloadWithoutAmmoInCreativeMode = new ConfigEntryBoolean(true, "canReloadWithoutAmmoInCreativeMode", ConfigEntryCategory.GUN, "Wether or not creative mode players should be able to reload their gun without having ammunition in their inventory.");
	
	public abstract static class ConfigEntry<T>
	{
		public final T defaultValue;
		public final String name;
		public final ConfigEntryCategory category;
		public final String description;
		
		protected T value;
		
		public ConfigEntry(T defaultValue, String name, ConfigEntryCategory category, String description)
		{
			this.defaultValue = this.value = defaultValue;
			this.name = name;
			this.category = category;
			this.description = description;
			entries.add(this);
		}
		
		public final void set(Configuration config)
		{
			T newValue = this.load(config);
			if(newValue != null)
			{
				this.value = newValue;
			}
		}
		
		public final T get()
		{
			return this.value != null ? this.valid(this.value) : this.valid(this.defaultValue);
		}
		
		protected abstract T load(Configuration config);
		
		protected abstract T valid(T value);
	}
	
	public static class ConfigEntryInt extends ConfigEntry<Integer>
	{
		public final int minValue;
		public final int maxValue;
		
		public ConfigEntryInt(int defaultValue, int minValue, int maxValue, String name, ConfigEntryCategory category, String description)
		{
			super(defaultValue, name, category, description);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		@Override
		protected Integer load(Configuration config)
		{
			return config.getInt(this.name, this.category.toString(), this.defaultValue, this.minValue, this.maxValue, this.description);
		}
		
		@Override
		protected Integer valid(Integer value)
		{
			if(value > this.maxValue)
			{
				value = this.maxValue;
			}
			if(value < this.minValue)
			{
				value = this.minValue;
			}
			return value;
		}
	}
	
	public static class ConfigEntryBoolean extends ConfigEntry<Boolean>
	{
		public ConfigEntryBoolean(boolean defaultValue, String name, ConfigEntryCategory category, String description)
		{
			super(defaultValue, name, category, description);
		}
		
		@Override
		protected Boolean load(Configuration config)
		{
			return config.getBoolean(this.name, this.category.toString(), this.defaultValue, this.description);
		}
		
		@Override
		protected Boolean valid(Boolean value)
		{
			return value;
		}
	}
	
	public static class ConfigEntryFloat extends ConfigEntry<Float>
	{
		public final float minValue;
		public final float maxValue;
		
		public ConfigEntryFloat(float defaultValue, float minValue, float maxValue, String name, ConfigEntryCategory category, String description)
		{
			super(defaultValue, name, category, description);
			this.minValue = minValue;
			this.maxValue = maxValue;
		}
		
		@Override
		protected Float load(Configuration config)
		{
			return config.getFloat(this.name, this.category.toString(), this.defaultValue, this.minValue, this.maxValue, this.description);
		}
		
		@Override
		protected Float valid(Float value)
		{
			if(value > this.maxValue)
			{
				value = this.maxValue;
			}
			if(value < this.minValue)
			{
				value = this.minValue;
			}
			return value;
		}
	}
}
