package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.IDAble;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.event.HandlerClientFML;

public class ItemGun extends ItemBase implements IDAble
{
	// nbt
	public static final String COMPONENTS = "Components";
	public static final String NAME = "Name";
	public static final String DELAYTIMER = "DelayTimer";
	public static final String RECOIL = "RecoilLast";
	public static final String FIRERATETIMER = "FireRateTimer";
	
	public ItemGun()
	{
		super("gun");
		this.setUnlocalizedName("gun");
		this.setCreativeTab(M.tabCore);
		this.setMaxDamage(200);
		this.maxStackSize = 1;
	}
	
	@Override
	public int getMaxDamage(ItemStack stack)
	{
		return durabilityMax(stack);
	}
	
	@Override
	public int getDamage(ItemStack stack)
	{
		return durabilityDamage(stack);
	}
	
	public static float fov = 0;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced)
	{
		if(!ComponentEvents.isGunValid(stack))
		{
			tooltip.add(ChatFormatting.RED + "ERROR!" + ChatFormatting.RESET);
		}
		int ammo = ammo(stack, player);
		int cap = capacity(stack, player);
		if(cap > 0)
		{
			String s = ChatFormatting.WHITE + "Ammo: " + ChatFormatting.GRAY + ammo + "/" + cap + ChatFormatting.RESET;
			if(ammo <= 0)
			{
				s += " (hit '" + Keyboard.getKeyName(HandlerClientFML.WeaponReload.getKeyCode()) + "' to reload)";
			}
			tooltip.add(s + ChatFormatting.RESET);
		}
		tooltip.add(ChatFormatting.WHITE + "Recoil: " + ChatFormatting.GRAY + Math.floor(recoil(stack, player) * 100) / 100 + new String(Character.toChars(0x00B0)) + ChatFormatting.RESET);
		tooltip.add(ChatFormatting.WHITE + "Instability: " + ChatFormatting.GRAY + Math.floor(instability(stack, player) * 100) + "%" + ChatFormatting.RESET);
		tooltip.add(ChatFormatting.WHITE + "Kickback: " + ChatFormatting.GRAY + Math.floor(kickback(stack, player) * 100) / 100 + " m/tick" + ChatFormatting.RESET);
		tooltip.add(ChatFormatting.WHITE + "Spread: " + ChatFormatting.GRAY + Math.floor(spread(stack, player) * 36000) / 100 + new String(Character.toChars(0x00B0)) + ChatFormatting.RESET);
		tooltip.add(ChatFormatting.WHITE + "Response Time: " + ChatFormatting.GRAY + delay(stack, player) + " ticks" + ChatFormatting.RESET);
		tooltip.add(ChatFormatting.WHITE + "Fire Rate: " + ChatFormatting.GRAY + fireRate(stack, player) + " ticks" + ChatFormatting.RESET);
		int zoom = (int)Math.floor(zoom(stack, player, 1) * 100);
		if(zoom > 100)
		{
			tooltip.add(ChatFormatting.WHITE + "Zoom: " + ChatFormatting.GRAY + zoom + "%" + ChatFormatting.RESET);
		}
		ArrayList<Component> c = getComponents(stack);
		if(c.size() > 0)
		{
			tooltip.add("");
			tooltip.add(ChatFormatting.WHITE + "Components used:" + ChatFormatting.GRAY + ChatFormatting.RESET);
			for(int i = 0; i < c.size(); ++i)
			{
				tooltip.add(" - " + I18n.format(c.get(i).item.getUnlocalizedName() + ".name") + ChatFormatting.RESET);
			}
		}
	}
	
	private ArrayList<ItemStack> guns = null;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(Config.showAllPossibleGunsInTab.get())
		{
			if(this.guns == null)
			{
				this.guns = new ArrayList<ItemStack>();
				for(Component misc1 : getComponentsOfType(Type.MISC, true))
				{
					for(Component misc2 : getComponentsOfType(Type.MISC, true))
					{
						for(Component misc3 : getComponentsOfType(Type.MISC, true))
						{
							for(Component misc4 : getComponentsOfType(Type.MISC, true))
							{
								for(Component barrel : getComponentsOfType(Type.BARREL, true))
								{
									for(Component magazine : getComponentsOfType(Type.MAGAZINE, true))
									{
										for(Component trigger : getComponentsOfType(Type.TRIGGER, true))
										{
											for(Component grip : getComponentsOfType(Type.GRIP, true))
											{
												for(Component stock : getComponentsOfType(Type.STOCK, true))
												{
													for(Component aim : getComponentsOfType(Type.AIM, true))
													{
														ItemStack gun = new ItemStack(M.gun);
														ItemGun.addComponent(gun, misc1);
														ItemGun.addComponent(gun, misc2);
														ItemGun.addComponent(gun, misc3);
														ItemGun.addComponent(gun, misc4);
														ItemGun.addComponent(gun, barrel);
														ItemGun.addComponent(gun, magazine);
														ItemGun.addComponent(gun, trigger);
														ItemGun.addComponent(gun, grip);
														ItemGun.addComponent(gun, stock);
														ItemGun.addComponent(gun, aim);
														if(ComponentEvents.isGunValid(gun))
														{
															this.guns.add(gun);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			subItems.addAll(this.guns);
		}
		else
		{
			this.guns = null;
			subItems.add(this.getSubItem(mc.thePlayer, item, tab));
		}
	}
	
	public ItemStack getSubItem(EntityPlayer player, Item item, CreativeTabs tab)
	{
		ArrayList<ItemStack> a = new ArrayList<ItemStack>();
		a.add(new ItemStack(item, 1, 0));
		for(int i = 0; i < a.size();)
		{
			compound(a.get(i));
			a.get(i).getTagCompound().setInteger(DELAYTIMER, -1);
			setComponents(a.get(0), getRandomComponents(Item.itemRand));
			ComponentEvents.setAmmo(capacity(a.get(i), player), a.get(i), player, getComponents(a.get(i)));
			return a.get(i);
		}
		return null;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		
		if(M.gun.canShoot(stack, player))
		{
			M.gun.setDelay(stack, player);
		}
		
		return stack;
	}
	
	public void resetDelay(ItemStack stack)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		compound.setInteger(DELAYTIMER, -1);
	}
	
	public void setDelay(ItemStack stack, EntityPlayer player)
	{
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		
		props.reloadDelay = props.maxReloadDelay;
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		int i = delay(stack, player);
		if(i < 0)
		{
			i = 0;
		}
		if(props.isRightClickHeldDownLast && automatic(stack, player))
		{
			i = 0;
		}
		compound.setInteger(DELAYTIMER, i);
	}
	
	public boolean canShoot(ItemStack stack, EntityPlayer player)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		if(EntityExtendedPlayer.get(player).reloadDelay > 0)
		{
			return false;
		}
		if(compound.getInteger(DELAYTIMER) >= 0 || compound.getInteger(FIRERATETIMER) > 0)
		{
			return false;
		}
		if(EntityExtendedPlayer.get(player).isRightClickHeldDownLast && !automatic(stack, player))
		{
			return false;
		}
		return true;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isSelected)
	{
		super.onUpdate(stack, world, entity, slot, isSelected);
		NBTTagCompound compound = stack.getTagCompound();
		ArrayList<Component> components = getComponents(stack);
		
		if(entity instanceof EntityPlayer)
		{
			components = ComponentEvents.updateComponents(stack, (EntityPlayer)entity, slot, isSelected, components);
		}
		if(compound.getFloat(RECOIL) > 0.01)
		{
			if(entity instanceof EntityPlayer)
			{
				this.recoilPerTick(stack, entity);
			}
		}
		if(compound.getInteger(FIRERATETIMER) > 0)
		{
			compound.setInteger(FIRERATETIMER, compound.getInteger(FIRERATETIMER) - 1);
		}
		
		if(entity instanceof EntityPlayer)
		{
			if(compound.getInteger(DELAYTIMER) == 0)
			{
				this.fire(stack, world, (EntityPlayer)entity);
			}
		}
		if(!(entity instanceof EntityPlayer) || this.canShoot(stack, (EntityPlayer)entity))
		{
			this.resetDelay(stack);
		}
		
		if(compound.getInteger(DELAYTIMER) > 0)
		{
			compound.setInteger(DELAYTIMER, compound.getInteger(DELAYTIMER) - 1);
		}
	}
	
	public void fire(ItemStack stack, World world, EntityPlayer player)
	{
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		
		props.reloadDelay = props.maxReloadDelay;
		
		compound(stack);
		ArrayList<Component> components = getComponents(stack);
		NBTTagCompound compound = stack.getTagCompound();
		
		ArrayList<Entity> e = ComponentEvents.fireComponents(stack, player, components);
		
		if(e == null || e.size() <= 0)
		{
			// click sound
		}
		else
		{
			if(!world.isRemote)
			{
				for(int i = 0; i < e.size(); ++i)
				{
					world.spawnEntityInWorld(e.get(i));
				}
			}
			this.kickBack(stack, player, e);
			this.applyRecoil(stack, player);
			compound.setInteger(FIRERATETIMER, fireRate(stack, player));
		}
		this.resetDelay(stack);
		
		setComponents(stack, components);
	}
	
	public void kickBack(ItemStack stack, EntityPlayer player, ArrayList<Entity> e)
	{
		double x = -Math.sin((player.rotationYaw / 180.0F - 180.0F) * Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI);
		double y = -Math.sin((player.rotationPitch / 180.0F - 180.0F) * Math.PI);
		double z = Math.cos((player.rotationYaw / 180.0F - 180.0F) * Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI);
		double k = kickback(stack, player);
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		if(props.isZoomHeldDown)
		{
			k *= 0.4;
		}
		if(!player.onGround || !player.isAirBorne)
		{
			k *= 0.6;
		}
		Vec3 m = Coordinates3D.stabilize(new Vec3(x, y, z), k);
		player.addVelocity(-m.xCoord, -m.yCoord, -m.zCoord);
		if(player.motionY >= 0)
		{
			player.fallDistance = 0;
		}
		else
		{
			float f = -(float)player.motionY * 3;
			if(f < player.fallDistance)
			{
				player.fallDistance = f;
			}
		}
	}
	
	public void recoilPerTick(ItemStack stack, Entity player)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		float r = compound.getFloat(RECOIL);
		int i = 3;
		player.rotationPitch += r - r * (i - 1) / i;
		
		compound.setFloat(RECOIL, r * (i - 1) / i);
	}
	
	public void applyRecoil(ItemStack stack, EntityPlayer player)
	{
		compound(stack);
		float r = recoil(stack, player);
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		NBTTagCompound compound = stack.getTagCompound();
		compound.setFloat(RECOIL, compound.getFloat(RECOIL) + r);
		player.rotationYaw += Randomization.r(Math.sqrt(r) * instability(stack, player) * 2);
		player.rotationPitch -= r;
	}
	
	public static void setComponents(ItemStack stack, Component[] cs)
	{
		setComponents(stack, ArraysAndSuch.arrayToArrayList(cs));
	}
	
	public static void addComponent(ItemStack stack, Component c)
	{
		if(c == null)
		{
			return;
		}
		ArrayList<Component> cs = getComponents(stack);
		
		cs.add(c);
		setComponents(stack, cs);
	}
	
	public static void setComponents(ItemStack stack, ArrayList<Component> cs)
	{
		ArrayList<String> ids = new ArrayList<String>();
		
		for(int i = 0; i < cs.size(); ++i)
		{
			Component c = cs.get(i);
			boolean b = true;
			for(int i2 = 0; i2 < ids.size(); ++i2)
			{
				if(c.getID() == ids.get(i2))
				{
					b = false;
					break;
				}
			}
			if(b)
			{
				ids.add(c.getID());
			}
		}
		setComponentIDs(stack, ids);
	}
	
	public static void setComponentIDs(ItemStack stack, ArrayList<String> components2)
	{
		ArrayList<String> components = Lists.newArrayList();
		ArrayList<String> allIds = Component.ComponentRegister.getIDs();
		for(String id : allIds)
		{
			if(components2.contains(id))
			{
				components.add(id);
			}
		}
		
		compound(stack);
		NBTTagCompound csOld = stack.getTagCompound().getCompoundTag(COMPONENTS);
		NBTTagCompound cs = new NBTTagCompound();
		for(int i = 0; i < components.size(); ++i)
		{
			if(csOld.hasKey(components.get(i)))
			{
				cs.setTag(components.get(i), csOld.getCompoundTag(components.get(i)));
			}
			else
			{
				cs.setTag(components.get(i), new NBTTagCompound());
			}
		}
		stack.getTagCompound().removeTag(COMPONENTS);
		stack.getTagCompound().setTag(COMPONENTS, cs);
	}
	
	public static ArrayList<Component> getComponents(ItemStack stack)
	{
		ArrayList<String> ids = getComponentIDs(stack);
		ArrayList<Component> cs = new ArrayList<Component>();
		for(int i = 0; i < ids.size(); ++i)
		{
			String id = ids.get(i);
			boolean b = true;
			for(int i2 = 0; i2 < cs.size(); ++i2)
			{
				if(cs.get(i2) == ComponentRegister.getComponent(id))
				{
					b = false;
					break;
				}
			}
			if(b && ComponentRegister.getComponent(id) != null)
			{
				cs.add(ComponentRegister.getComponent(id));
			}
		}
		return cs;
	}
	
	public static ArrayList<String> getComponentIDs(ItemStack stack)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		if(compound.hasKey(COMPONENTS))
		{
			return getStringsFromNBTList(compound.getCompoundTag(COMPONENTS));
		}
		return new ArrayList<String>();
	}
	
	protected static ArrayList<String> getStringsFromNBTList(NBTTagCompound c)
	{
		ArrayList<String> s = new ArrayList<String>();
		ArrayList<String> ids = ComponentRegister.getIDs();
		
		for(int i = 0; i < ids.size(); ++i)
		{
			if(c.hasKey(ids.get(i)))
			{
				s.add(ids.get(i));
			}
		}
		return s;
	}
	
	public static int delay(ItemStack stack, EntityPlayer player)
	{
		float delay = 0;
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).delay(0, stack, player.worldObj, player, components);
			a.add(f);
			delay += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).delay(delay, stack, player.worldObj, player, components);
			f -= a.get(i);
			delay = f;
		}
		return (int)Math.ceil(delay);
	}
	
	public static float recoil(ItemStack stack, EntityPlayer player)
	{
		float recoil = 0;
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).recoil(0, stack, player.worldObj, player, components);
			a.add(f);
			recoil += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).recoil(recoil, stack, player.worldObj, player, components);
			f -= a.get(i);
			recoil = f;
		}
		return recoil;
	}
	
	public static float instability(ItemStack stack, EntityPlayer player)
	{
		float instability = 1;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			instability = components.get(i).instability(instability, stack, player.worldObj, player, components);
		}
		return instability;
	}
	
	public static float spread(ItemStack stack, EntityPlayer player)
	{
		float spread = 180;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).spread(spread, stack, player.worldObj, player, components);
			if(f < spread)
			{
				spread = f;
			}
		}
		if(spread < 0)
		{
			spread = 0;
		}
		if(spread > 180)
		{
			spread = 180;
		}
		return spread / 360;
	}
	
	public static float speed(ItemStack stack, EntityPlayer player)
	{
		float speed = 0;
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).speed(0, stack, player.worldObj, player, components);
			a.add(f);
			speed += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).speed(speed, stack, player.worldObj, player, components);
			f -= a.get(i);
			speed = f;
		}
		float max = 5;
		if(speed > max)
		{
			speed = max;
		}
		return speed;
	}
	
	public static float kickback(ItemStack stack, EntityPlayer player)
	{
		float kickback = 0;
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).kickback(0, stack, player.worldObj, player, components);
			a.add(f);
			kickback += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).kickback(kickback, stack, player.worldObj, player, components);
			f -= a.get(i);
			kickback = f;
		}
		return kickback / 3;
	}
	
	public static float zoom(ItemStack stack, EntityPlayer player, float zoom)
	{
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).zoom(0, stack, player.worldObj, player, components);
			a.add(f);
			zoom += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).zoom(zoom, stack, player.worldObj, player, components);
			f -= a.get(i);
			zoom = f;
		}
		return zoom;
	}
	
	public static int fireRate(ItemStack stack, EntityPlayer player)
	{
		float fireRate = 0;
		ArrayList<Component> components = getComponents(stack);
		ArrayList<Float> a = new ArrayList<Float>();
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).fireRate(0, stack, player.worldObj, player, components);
			a.add(f);
			fireRate += f;
		}
		for(int i = 0; i < components.size(); ++i)
		{
			float f = components.get(i).fireRate(fireRate, stack, player.worldObj, player, components);
			f -= a.get(i);
			fireRate = f;
		}
		return (int)Math.ceil(fireRate);
	}
	
	public static boolean automatic(ItemStack stack, EntityPlayer player)
	{
		boolean b = false;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			if(components.get(i).automatic(stack, player.worldObj, player, components))
			{
				b = true;
			}
		}
		return b;
	}
	
	public static int ammo(ItemStack stack, EntityPlayer player)
	{
		int ammo = 0;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			ammo += components.get(i).ammo(stack, player.worldObj, player, components);
		}
		return ammo;
	}
	
	public static Item ejectableAmmo(ItemStack gun, EntityPlayer player)
	{
		ArrayList<Component> components = getComponents(gun);
		for(int i = 0; i < components.size(); ++i)
		{
			Item item = components.get(i).ejectableAmmo(gun, player.worldObj, player, components);
			if(item != null)
			{
				return item;
			}
		}
		return null;
	}
	
	public static boolean ammoItem(ItemStack gun, ItemStack stack, EntityPlayer player)
	{
		ArrayList<Component> components = getComponents(gun);
		for(int i = 0; i < components.size(); ++i)
		{
			if(components.get(i).isAmmoItem(stack, player.worldObj, player))
			{
				return true;
			}
		}
		return false;
	}
	
	public static int capacity(ItemStack stack, EntityPlayer player)
	{
		int capacity = 0;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			capacity += components.get(i).capacity(stack, player.worldObj, player, components);
		}
		return capacity;
	}
	
	public static int durability(ItemStack stack)
	{
		return durabilityMax(stack) - durabilityDamage(stack);
	}
	
	public static int durabilityDamage(ItemStack stack)
	{
		int capacity = 0;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			capacity += components.get(i).durabilityDamage(stack, components);
		}
		return capacity;
	}
	
	public static int durabilityMax(ItemStack stack)
	{
		int capacity = 0;
		ArrayList<Component> components = getComponents(stack);
		for(int i = 0; i < components.size(); ++i)
		{
			capacity += components.get(i).item.getMaxDamage(components.get(i).toItemStack(stack));
		}
		return capacity;
	}
	
	public static ArrayList<Component> getRandomComponents(Random rand)
	{
		ArrayList<Component> cs = new ArrayList<Component>();
		while(true)
		{
			for(int i = 0; i < 10 || !ComponentEvents.hasRequired(cs); ++i)
			{
				Component c = ComponentRegister.getRandomComponent(rand);
				ArrayList<Component> cs2 = (ArrayList<Component>)cs.clone();
				cs2.add(c);
				if(ComponentEvents.isValid(cs2, false))
				{
					cs.add(c);
				}
			}
			if(ComponentEvents.isValid(cs, true))
			{
				break;
			}
		}
		return cs;
	}
	
	public static ArrayList<Component> getComponentsOfType(Type type)
	{
		ArrayList<Component> a = new ArrayList<Component>();
		for(Component c : ComponentRegister.getAll())
		{
			if(c.type == type)
			{
				a.add(c);
			}
		}
		return a;
	}
	
	public static ArrayList<Component> getComponentsOfType(Type type, boolean withNull)
	{
		ArrayList<Component> a = getComponentsOfType(type);
		if(withNull)
		{
			a.add(null);
		}
		return a;
	}
	
	@Override
	public boolean isFull3D()
	{
		return true;
	}
	
	public static void dropAllComponents(EntityPlayer player, ItemStack stack)
	{
		for(Component c : getComponents(stack))
		{
			ItemStack drop = c.toItemStack(stack);
			player.dropItem(drop, true, false);
		}
		setComponents(stack, new ArrayList());
	}
	
}
