package com.spectral.spectral_guns.itemtags;

public abstract class ItemTagEnumMap<W, T extends ItemTagBase<W, ?>, K extends Enum> extends ItemTagMap<W, T, K>
{
	public ItemTagEnumMap(String key, T defaultEntry, boolean noWobble)
	{
		super(key, defaultEntry, noWobble);
	}
	
	@Override
	protected String getKey(K key)
	{
		return key.name().toLowerCase();
	}
}