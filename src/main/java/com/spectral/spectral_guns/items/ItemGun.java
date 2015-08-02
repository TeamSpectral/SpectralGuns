package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.spectral.spectral_guns.Config;
import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.components.magazine.IComponentProjectileCount;
import com.spectral.spectral_guns.entity.extended.ExtendedPlayer;
import com.spectral.spectral_guns.event.HandlerClientFML;

public class ItemGun extends Item
{
	// nbt
	public static final String COMPONENTS = "ComponentsList";
	public static final String COMPONENTS_PRE_v1_3 = "Components";
	public static final String COMPONENT_SLOT = "SLOT";
	public static final String COMPONENT_ID = "ID";
	public static final String COMPONENT_COMPOUND = "COMPOUND";
	public static final String NAME = "Name";
	public static final String DELAYTIMER = "DelayTimer";
	public static final String FIRERATETIMER = "FireRateTimer";
	
	public ItemGun()
	{
		super();
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
		boolean hide = !Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode());
		if(hide)
		{
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
			tooltip.add(ChatFormatting.WHITE + "Velocity: " + ChatFormatting.GRAY + Math.floor(speed(stack, player) * 100) / 100 + " m/tick" + ChatFormatting.RESET);
			tooltip.add(ChatFormatting.WHITE + "Projectile Count: " + ChatFormatting.GRAY + amount(stack, player) + ChatFormatting.RESET);
		}
		HashMap<Integer, Component> c = getComponents(stack);
		if(c.size() > 0)
		{
			tooltip.add(ChatFormatting.WHITE + "Components used:" + ChatFormatting.GRAY + (hide ? " " + c.size() + " " + ChatFormatting.WHITE + "(Hold '" + Keyboard.getKeyName(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) + "')" : "") + ChatFormatting.RESET);
			if(!hide)
			{
				int max = this.getMaxComponentId(c);
				for(int i = 0; i <= max; ++i)
				{
					if(c.get(i) != null)
					{
						ItemStack componentStack = c.get(i).toItemStack(i, stack);
						tooltip.add(" - " + I18n.format(componentStack.getUnlocalizedName() + ".name") + ":" + ChatFormatting.RESET);
						tooltip.add("    D:" + (c.get(i).durabilityMax(i, stack) - c.get(i).durabilityDamage(i, stack)) + "/" + c.get(i).durabilityMax(i, stack) + ", T:" + (int)(c.get(i).heat(i, stack) * 100 / c.get(i).heatThreshold(i, stack)) + "%" + ChatFormatting.RESET);
					}
				}
			}
		}
	}
	
	private static ArrayList<HashMap<Integer, Component>> guns = null;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(Config.showAllPossibleGunsInTab.get())
		{
			this.generateGunCombinations();
			subItems.addAll(this.guns);
		}
		else
		{
			this.guns = null;
			subItems.add(this.getSubItem(mc.thePlayer, item, tab));
		}
	}
	
	public static void generateGunCombinations()
	{
		if(guns == null)
		{
			guns = new ArrayList();
			ArrayList<Component> misc = getComponentsOfType(Type.MISC, true);
			for(Component misc1 : misc)
			{
				for(Component misc2 : misc)
				{
					if(misc2 != null && misc2.maxAmount < 2)
					{
						continue;
					}
					for(Component misc3 : misc)
					{
						if(misc3 != null && misc3.maxAmount < 3)
						{
							continue;
						}
						for(Component misc4 : misc)
						{
							if(misc4 != null && misc4.maxAmount < 4)
							{
								continue;
							}
							for(Component barrel : getComponentsOfType(Type.BARREL, false))
							{
								for(Component magazine : getComponentsOfType(Type.MAGAZINE, false))
								{
									for(Component trigger : getComponentsOfType(Type.TRIGGER, false))
									{
										for(Component grip : getComponentsOfType(Type.GRIP, false))
										{
											for(Component stock : getComponentsOfType(Type.STOCK, true))
											{
												for(Component aim : getComponentsOfType(Type.AIM, true))
												{
													ItemStack gun = new ItemStack(M.gun);
													gun.setTagCompound(new NBTTagCompound());
													if(misc1 != null)
													{
														ItemGun.addComponent(gun, 6, misc1);
													}
													if(misc2 != null)
													{
														ItemGun.addComponent(gun, 7, misc2);
													}
													if(misc3 != null)
													{
														ItemGun.addComponent(gun, 8, misc3);
													}
													if(misc4 != null)
													{
														ItemGun.addComponent(gun, 9, misc4);
													}
													ItemGun.addComponent(gun, 0, barrel);
													ItemGun.addComponent(gun, 1, magazine);
													ItemGun.addComponent(gun, 2, trigger);
													ItemGun.addComponent(gun, 3, grip);
													if(stock != null)
													{
														ItemGun.addComponent(gun, 4, stock);
													}
													if(aim != null)
													{
														ItemGun.addComponent(gun, 5, aim);
													}
													if(ComponentEvents.isGunValid(gun))
													{
														guns.add(getComponents(gun));
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
	}
	
	public ItemStack getSubItem(EntityPlayer player, Item item, CreativeTabs tab)
	{
		ArrayList<ItemStack> a = new ArrayList<ItemStack>();
		a.add(new ItemStack(item, 1, 0));
		for(int i = 0; i < a.size();)
		{
			Stuff.ItemStacks.compound(a.get(i));
			a.get(i).getTagCompound().setInteger(DELAYTIMER, -1);
			setComponents(a.get(0), getRandomComponents(Item.itemRand));
			ComponentEvents.setAmmo(capacity(a.get(i), player), a.get(i), player);
			return a.get(i);
		}
		return null;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		Stuff.ItemStacks.compound(stack);
		ExtendedPlayer props = ExtendedPlayer.get(player);
		if(this.canShoot(stack, player) && !props.isRightClickHeldDownLast)
		{
			this.setDelay(stack, player);
		}
		return stack;
	}
	
	public void resetDelay(ItemStack stack)
	{
		NBTTagCompound compound = stack.getTagCompound();
		compound.setInteger(DELAYTIMER, -1);
	}
	
	public void setDelay(ItemStack stack, EntityPlayer player)
	{
		ExtendedPlayer props = ExtendedPlayer.get(player);
		
		props.reloadDelay = props.maxReloadDelay;
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
		NBTTagCompound compound = stack.getTagCompound();
		if(ExtendedPlayer.get(player).reloadDelay > 0)
		{
			return false;
		}
		if(compound.getInteger(DELAYTIMER) >= 0 || compound.getInteger(FIRERATETIMER) > 0)
		{
			return false;
		}
		if(ExtendedPlayer.get(player).isRightClickHeldDownLast && !automatic(stack, player))
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
		if(entity instanceof EntityPlayer)
		{
			ExtendedPlayer props = ExtendedPlayer.get((EntityPlayer)entity);
			ComponentEvents.updateComponents(stack, (EntityPlayer)entity, slot, isSelected);
			if(this.canShoot(stack, (EntityPlayer)entity) && isSelected && props.isRightClickHeldDownLast && props.isRightClickHeldDown)
			{
				this.setDelay(stack, (EntityPlayer)entity);
			}
		}
		if(compound.getInteger(FIRERATETIMER) > 0)
		{
			compound.setInteger(FIRERATETIMER, compound.getInteger(FIRERATETIMER) - 1);
		}
		
		if(entity instanceof EntityPlayer)
		{
			if(compound.getInteger(DELAYTIMER) == 0 && compound.getInteger(FIRERATETIMER) <= 0)
			{
				this.fire(stack, world, (EntityPlayer)entity);
			}
		}
		if(isSelected && !(entity instanceof EntityPlayer) || this.canShoot(stack, (EntityPlayer)entity))
		{
			this.resetDelay(stack);
		}
		
		if(compound.getInteger(DELAYTIMER) > 0)
		{
			compound.setInteger(DELAYTIMER, compound.getInteger(DELAYTIMER) - 1);
		}
		
		if(stack.stackSize == 0 && entity instanceof EntityPlayer)
		{
			((EntityPlayer)entity).inventory.setInventorySlotContents(slot, null);
			return;
		}
	}
	
	public void fire(ItemStack stack, World world, EntityPlayer player)
	{
		ExtendedPlayer props = ExtendedPlayer.get(player);
		
		props.reloadDelay = props.maxReloadDelay;
		
		NBTTagCompound compound = stack.getTagCompound();
		
		ArrayList<Entity> e = ComponentEvents.fireComponents(stack, player);
		
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
	}
	
	public void kickBack(ItemStack stack, EntityPlayer player, ArrayList<Entity> e)
	{
		double x = -Math.sin((player.rotationYaw / 180.0F - 180.0F) * Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI);
		double y = -Math.sin((player.rotationPitch / 180.0F - 180.0F) * Math.PI);
		double z = Math.cos((player.rotationYaw / 180.0F - 180.0F) * Math.PI) * MathHelper.cos(player.rotationPitch / 180.0F * (float)Math.PI);
		double k = kickback(stack, player);
		ExtendedPlayer props = ExtendedPlayer.get(player);
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
	
	public static void recoilPerTick(EntityPlayer player)
	{
		ExtendedPlayer props = ExtendedPlayer.get(player);
		double i = 20;
		i = (i - 1) / i;
		
		double oldPitch = props.recoilPitch;
		props.recoilPitch *= i;
		player.rotationPitch -= props.recoilPitch - oldPitch;
		
		double oldYaw = props.recoilYaw;
		props.recoilYaw *= i;
		player.rotationYaw -= props.recoilYaw - oldYaw;
	}
	
	public void applyRecoil(ItemStack stack, EntityPlayer player)
	{
		double oldYaw = player.rotationYaw;
		double oldPitch = player.rotationPitch;
		ExtendedPlayer props = ExtendedPlayer.get(player);
		float r = recoil(stack, player) * 2;
		float i = instability(stack, player);
		if(props.isZoomHeldDown)
		{
			float z = zoom(stack, player, 1);
			i /= z;
			i *= 2 / 3;
		}
		
		player.rotationYaw += Randomization.r(Math.sqrt(r) * i);
		player.rotationPitch -= r + Randomization.r(Math.sqrt(r) * i);
		float max = 90;
		if(player.rotationPitch < -max)
		{
			player.rotationPitch = -max;
		}
		if(player.rotationPitch > max)
		{
			player.rotationPitch = max;
		}
		props.recoilYaw += oldYaw - player.rotationYaw + Randomization.r(Math.sqrt(r) * i) / 3;
		props.recoilPitch += oldPitch - player.rotationPitch + Randomization.r(Math.sqrt(r) * i) / 3;
	}
	
	public static void addComponent(ItemStack stack, int slot, Component c)
	{
		if(c == null)
		{
			return;
		}
		HashMap<Integer, Component> cs = getComponents(stack);
		
		cs.put(slot, c);
		setComponents(stack, cs);
	}
	
	public static void setComponents(ItemStack stack, HashMap<Integer, Component> cs)
	{
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound compound = stack.getTagCompound();
		if(!compound.hasKey(COMPONENTS))
		{
			compound.setTag(COMPONENTS, new NBTTagCompound());
		}
		NBTTagList oldList = compound.getTagList(COMPONENTS, new NBTTagCompound().getId());
		NBTTagList list = new NBTTagList();
		int max = getMaxComponentId(cs);
		for(int i = 0; i <= max; ++i)
		{
			Component c = cs.get(i);
			if(c != null)
			{
				NBTTagCompound cCompound = new NBTTagCompound();
				for(int i2 = 0; i2 < oldList.tagCount(); ++i2)
				{
					NBTTagCompound cCompound2 = oldList.getCompoundTagAt(i);
					if(cCompound2 != null && cCompound2.hasKey(COMPONENT_SLOT, new NBTTagInt(0).getId()) && cCompound2.hasKey(COMPONENT_ID, new NBTTagString("").getId()))
					{
						int slot = cCompound2.getInteger(COMPONENT_SLOT);
						String id = cCompound2.getString(COMPONENT_ID);
						if(slot == i && id.equals(c.getID()))
						{
							cCompound = (NBTTagCompound)cCompound2.copy();
						}
					}
				}
				cCompound.setInteger(COMPONENT_SLOT, i);
				cCompound.setString(COMPONENT_ID, c.getID());
				if(!cCompound.hasKey(COMPONENT_COMPOUND, new NBTTagCompound().getId()))
				{
					cCompound.setTag(COMPONENT_COMPOUND, new NBTTagCompound());
				}
				list.appendTag(cCompound);
			}
		}
		compound.setTag(COMPONENTS, list);
	}
	
	public static HashMap<Integer, Component> getComponents(ItemStack stack)
	{
		HashMap<Integer, Component> cs = new HashMap();
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound compound = stack.getTagCompound();
		if(compound.hasKey(COMPONENTS))
		{
			NBTTagList list = compound.getTagList(COMPONENTS, new NBTTagCompound().getId());
			for(int i = 0; i < list.tagCount(); ++i)
			{
				if(list.getCompoundTagAt(i) != null)
				{
					NBTTagCompound componentCompound = list.getCompoundTagAt(i);
					if(componentCompound.hasKey(COMPONENT_ID) && componentCompound.hasKey(COMPONENT_ID))
					{
						int slot = componentCompound.getInteger(COMPONENT_SLOT);
						String id = componentCompound.getString(COMPONENT_ID);
						Component c = Component.ComponentRegister.getComponent(id);
						if(c != null)
						{
							cs.put(slot, c);
						}
					}
				}
			}
		}
		return cs;
	}
	
	public static <T extends Component> ArrayList<T> getComponentsOf(ItemStack gun, Class<T> clazz)
	{
		return Stuff.ArraysAndSuch.allExtending(Stuff.ArraysAndSuch.hashMapToArrayList(ItemGun.getComponents(gun)), clazz);
	}
	
	public static int getMaxComponentId(HashMap<Integer, Component> components)
	{
		Iterator<Integer> keyIter = components.keySet().iterator();
		int maxKey = 0;
		while(keyIter.hasNext())
		{
			Integer key = keyIter.next();
			if(key != null && key > maxKey)
			{
				maxKey = key;
			}
		}
		return maxKey;
	}
	
	public static int delay(ItemStack stack, EntityPlayer player)
	{
		float delay = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).delay(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			delay += f;
		};
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).delay(slot, delay, stack, player.worldObj, player);
			f -= a.get(slot);
			delay = f;
		}
		return (int)Math.ceil(delay);
	}
	
	public static float recoil(ItemStack stack, EntityPlayer player)
	{
		float recoil = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).recoil(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			recoil += f;
		};
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).recoil(slot, recoil, stack, player.worldObj, player);
			f -= a.get(slot);
			recoil = f;
		}
		return recoil;
	}
	
	public static float instability(ItemStack stack, EntityPlayer player)
	{
		float instability = 1;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			instability = cs.get(slot).instability(slot, instability, stack, player.worldObj, player);
		}
		return instability;
	}
	
	public static float spread(ItemStack stack, EntityPlayer player)
	{
		float spread = 180;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).spread(slot, spread, stack, player.worldObj, player);
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
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).speed(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			speed += f;
		}
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).speed(slot, speed, stack, player.worldObj, player);
			f -= a.get(slot);
			speed = f;
		}
		return speed;
	}
	
	public static float kickback(ItemStack stack, EntityPlayer player)
	{
		float kickback = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).kickback(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			kickback += f;
		}
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).kickback(slot, kickback, stack, player.worldObj, player);
			f -= a.get(slot);
			kickback = f;
		}
		return kickback / 3;
	}
	
	public static float zoom(ItemStack stack, EntityPlayer player, float zoom)
	{
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).zoom(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			zoom += f;
		}
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).zoom(slot, zoom, stack, player.worldObj, player);
			f -= a.get(slot);
			zoom = f;
		}
		return zoom;
	}
	
	public static int fireRate(ItemStack stack, EntityPlayer player)
	{
		float fireRate = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		HashMap<Integer, Float> a = new HashMap();
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).fireRate(slot, 0, stack, player.worldObj, player);
			a.put(slot, f);
			fireRate += f;
		}
		for(Integer slot : cs.keySet())
		{
			float f = cs.get(slot).fireRate(slot, fireRate, stack, player.worldObj, player);
			f -= a.get(slot);
			fireRate = f;
		}
		return Math.round(fireRate);
	}
	
	public static boolean automatic(ItemStack stack, EntityPlayer player)
	{
		boolean b = false;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			if(cs.get(slot).automatic(slot, stack, player.worldObj, player))
			{
				b = true;
			}
		}
		return b;
	}
	
	public static int amount(ItemStack stack, EntityPlayer player)
	{
		int amount = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			if(cs.get(slot) instanceof IComponentProjectileCount)
			{
				amount += ((IComponentProjectileCount)cs.get(slot)).projectileCount(slot, stack, player.worldObj, player);
			}
		}
		if(amount < 1)
		{
			amount = 1;
		}
		return amount;
	}
	
	public static int ammo(ItemStack stack, EntityPlayer player)
	{
		int ammo = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			ammo += cs.get(slot).ammo(slot, stack, player.worldObj, player);
		}
		return ammo;
	}
	
	public static Item ejectableAmmo(ItemStack gun, EntityPlayer player)
	{
		return ejectableAmmo(gun, player, true);
	}
	
	public static Item ejectableAmmo(ItemStack gun, EntityPlayer player, boolean itemAmmo)
	{
		HashMap<Integer, Component> cs = getComponents(gun);
		for(Integer slot : cs.keySet())
		{
			Item item = cs.get(slot).ejectableAmmo(slot, gun, player.worldObj, player);
			if(item != null)
			{
				if(itemAmmo)
				{
					ItemAmmo ammo = ItemAmmo.getItemAmmo(item);
					if(ammo != null)
					{
						return ammo;
					}
				}
				return item;
			}
		}
		return null;
	}
	
	public static boolean ammoItem(ItemStack gun, ItemStack stack, EntityPlayer player)
	{
		HashMap<Integer, Component> cs = getComponents(gun);
		for(Integer slot : cs.keySet())
		{
			if(cs.get(slot).isAmmoItem(stack))
			{
				return true;
			}
		}
		return false;
	}
	
	public static int capacity(ItemStack stack, EntityPlayer player)
	{
		int capacity = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			capacity += cs.get(slot).capacity(slot, stack, player.worldObj, player);
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
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			capacity += cs.get(slot).durabilityDamage(slot, stack);
		}
		return capacity;
	}
	
	public static int durabilityMax(ItemStack stack)
	{
		int capacity = 0;
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			capacity += ComponentRegister.getItem(cs.get(slot)).getMaxDamage(cs.get(slot).toItemStack(slot, stack));
		}
		return capacity;
	}
	
	public static HashMap<Integer, Component> getRandomComponents(Random rand)
	{
		if(guns != null)
		{
			ArrayList<HashMap<Integer, Component>> cs = guns;
			if(cs.size() > 0)
			{
				return cs.get(rand.nextInt(cs.size()));
			}
		}
		ArrayList<Component> misc = getComponentsOfType(Type.MISC, true);
		Component misc1 = misc.get(rand.nextInt(misc.size()));
		for(int i = rand.nextInt(misc.size());; i = rand.nextInt(misc.size()))
		{
			Component misc2 = misc.get(i);
			if(misc2 != null && misc2.maxAmount < 2)
			{
				continue;
			}
			for(i = rand.nextInt(misc.size());; i = rand.nextInt(misc.size()))
			{
				Component misc3 = misc.get(i);
				if(misc3 != null && misc3.maxAmount < 3)
				{
					continue;
				}
				for(i = rand.nextInt(misc.size());; i = rand.nextInt(misc.size()))
				{
					Component misc4 = misc.get(i);
					if(misc4 != null && misc4.maxAmount < 4)
					{
						continue;
					}
					ArrayList<Component> barrels = getComponentsOfType(Type.BARREL, false);
					Component barrel = barrels.get(rand.nextInt(barrels.size()));
					ArrayList<Component> magazines = getComponentsOfType(Type.MAGAZINE, false);
					Component magazine = magazines.get(rand.nextInt(magazines.size()));
					ArrayList<Component> triggers = getComponentsOfType(Type.TRIGGER, false);
					Component trigger = triggers.get(rand.nextInt(triggers.size()));
					ArrayList<Component> grips = getComponentsOfType(Type.GRIP, false);
					Component grip = grips.get(rand.nextInt(grips.size()));
					ArrayList<Component> stocks = getComponentsOfType(Type.STOCK, true);
					Component stock = stocks.get(rand.nextInt(stocks.size()));
					ArrayList<Component> aims = getComponentsOfType(Type.AIM, true);
					Component aim = aims.get(rand.nextInt(aims.size()));
					ItemStack gun = new ItemStack(M.gun);
					gun.setTagCompound(new NBTTagCompound());
					if(misc1 != null)
					{
						ItemGun.addComponent(gun, 6, misc1);
					}
					if(misc2 != null)
					{
						ItemGun.addComponent(gun, 7, misc2);
					}
					if(misc3 != null)
					{
						ItemGun.addComponent(gun, 8, misc3);
					}
					if(misc4 != null)
					{
						ItemGun.addComponent(gun, 9, misc4);
					}
					ItemGun.addComponent(gun, 0, barrel);
					ItemGun.addComponent(gun, 1, magazine);
					ItemGun.addComponent(gun, 2, trigger);
					ItemGun.addComponent(gun, 3, grip);
					if(stock != null)
					{
						ItemGun.addComponent(gun, 4, stock);
					}
					if(aim != null)
					{
						ItemGun.addComponent(gun, 5, aim);
					}
					if(ComponentEvents.isGunValid(gun))
					{
						return getComponents(gun);
					}
				}
			}
		}
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
		if(withNull || a.size() <= 0)
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
		HashMap<Integer, Component> cs = getComponents(stack);
		for(Integer slot : cs.keySet())
		{
			Component c = cs.get(slot);
			ItemStack drop = c.toItemStack(slot, stack);
			player.dropItem(drop, true, false);
		}
		setComponents(stack, new HashMap());
	}
	
}
