package com.spectral.spectral_guns;

public class References
{
	public static final String MODID = "spectralguns";
	public static final String NAME = "Spectral Guns";
	public static final String VERSION = "131"; //As in build (For SpectralCore)
	public static final String VERSION_CHECK_URL = "https://github.com/TeamSpectral/SpectralGuns/blob/dev/versionChecker/versionData.txt";
	public static final String CLIENT_PROXY_CLASS = "com.spectral.spectral_guns.proxy.ProxyClient";
	public static final String SERVER_PROXY_CLASS = "com.spectral.spectral_guns.proxy.ProxyServer";
	public static final String GUI_FACTORY_CLASS = "com.spectral.spectral_guns.gui.GuiFactory";
	
	public static class ReferencesGunErrors
	{
		public static final String NO_WRENCH = "A wrench is required!";
		public static final String INVALID_GUN = "The gun is invalid!";
		public static final String COMPONENT_SLOTS_OCCUPIED = "Component slots are already occupied!";
		
		public static final String WRONG_SLOT(String singular, String plural, boolean slotPlural)
		{
			return "The " + singular + " slot" + (slotPlural ? "s" : "") + " " + (slotPlural ? "are" : "is") + " only for " + plural + "!";
		}
	}
	
	public static class ReferencesTemp
	{
		public static final int NORMAL = 20;
		public static final double MOD = 80;
	}
}
