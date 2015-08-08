package com.spectral.spectral_guns;

import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class VersionChecker
{
	//see 'http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2091981-version-checker-auto-update-mods-and-clean'
	
	public static void addUpdate()
	{
		FMLInterModComms.sendRuntimeMessage(References.MODID, "VersionChecker", "addVersionCheck", References.VERSION_CHECK_URL);
		//FMLInterModComms.sendRuntimeMessage(References.MODID, "VersionChecker", "addUpdate", getUpdateData());
	}
	
	/*
	 * public static NBTTagCompound getUpdateData()
	 * {
	 * NBTTagCompound data = new NBTTagCompound();
	 * data.setString("modDisplayName", References.NAME);
	 * data.setString("oldVersion", References.VERSION_LAST);
	 * data.setString("newVersion", References.VERSION);
	 * data.setString("updateUrl", References.VERSION);
	 * data.setBoolean("isDirectLink", true);
	 * //data.setString("changeLog", ""); //might want to add this later
	 * data.setString("newFileName", References.MODID + "-" +
	 * References.VERSION);
	 * return data;
	 * }
	 */
}
