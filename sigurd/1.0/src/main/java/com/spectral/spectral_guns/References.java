package com.spectral.spectral_guns;

import com.spectral.spectral_guns.proxy.ProxyCommon;

public class References extends ProxyCommon
{
	public static final String MODID = "spectralguns";
	public static final String VERSION = "0.2";
	public static final String Client = "com.spectral.spectral_guns.proxy.ProxyClient";
	public static final String Server = "com.spectral.spectral_guns.proxy.ProxyServer";
	
	/*
	 * CHANGELOG:
	 * 0.1 - sigurd4:
	 * *changed loads of stuff with the components. cleared stuff up. now uses material enum and more cool stuff.
	 * +added fireball projectile.
	 * +added food projectile
	 * +added additional food class to make custom food. currently used for the goopy food mush ammo for the food gun.
	 * +added various tooltip stuff for maximum user friendliness.
	 * +added snowy hud(not quite working)
	 * *i probably changed a bunch of other stuff that i can't remember. not to self: do keep track of changes next time.
	 * 
	 * 0.1.1 - sigurd4
	 * *fixed some bugs and crashes
	 * *minor adjustments
	 * 
	 * 1.0 (public release) - sigurd4
	 * *fixed textures!!! :DDDDDD
	 * +added some crafting recipes
	 * *fixed random gun generation from creative tab
	 * *fixed the check for making sure the gun component combination is valid and that all required component types are used
	 * +added throwable shurikens (not yet possible to shoot with a gun yet, cause i'm lazy)
	 * *changes to the food entity rendering code
	 * +added some crafting items, mostly related to optics which will be used for lasers and scopes
	 * +added scopes and zooming ability
	 * *changed a lot of the item registration code to make it tidier and allow textures
	 * +added automatic trigger mechanisms
	 * +added rubies, ruby ore and ruby blocks. i need help with world gen at some point. TODO lover of beards, i summon you!! ...or anyone else who knows how to do this stuff!
	 * *bugfixes
	 */
}