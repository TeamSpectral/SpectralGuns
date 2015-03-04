package com.spectral.spectral_guns.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.spectral.spectral_guns.M;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.Stuff.Coordinates3D;
import com.spectral.spectral_guns.Stuff.Randomization;
import com.spectral.spectral_guns.components.Component;
import com.spectral.spectral_guns.components.Component.ComponentRegister;
import com.spectral.spectral_guns.components.ComponentEvents;
import com.spectral.spectral_guns.entity.extended.EntityExtendedPlayer;
import com.spectral.spectral_guns.event.HandlerClientFML;

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

public class ItemGun extends ItemBase
{
	//nbt
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
		int ammo = ammo(stack, player);
		int cap = capacity(stack, player);
		if(cap > 0)
		{
			String s = "Ammo: " + ammo + "/" + cap;
			if(ammo <= 0)
			{
				if(HandlerClientFML.WeaponReload.getKeyCode() == HandlerClientFML.WeaponReload.getKeyCodeDefault())
				{
					s += " (hit 'R' to reload)";
				}
				else
				{
					s += " (hit reload key to reload)";
				}
			}
			tooltip.add(s);
		}
		tooltip.add("Recoil: " + Math.floor(recoil(stack, player)*100)/100 + "°");
		tooltip.add("Kickback: " + Math.floor(kickback(stack, player)*100)/100 + " m/tick");
		tooltip.add("Spread: " + Math.floor(spread(stack, player)*36000)/100 + "°");
		tooltip.add("Response Time: " + delay(stack, player) + " ticks");
		tooltip.add("Fire Rate: " + fireRate(stack, player) + " ticks");
		int zoom = (int)Math.floor((zoom(stack, player, 1))*100);
		if(zoom > 100)
		{
			tooltip.add("Zoom: " + zoom + "%");
		}
		ArrayList<Component> c = getComponents(stack);
		if(c.size() > 0)
		{
			tooltip.add("");
			tooltip.add("Components used:");
			for(int i = 0; i < c.size(); ++i)
			{
				tooltip.add(" - " + I18n.format(c.get(i).item.getUnlocalizedName()));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ArrayList<ItemStack> a = new ArrayList<ItemStack>();
		a.add(new ItemStack(item, 1, 0));
		for(int i = 0; i < a.size(); ++i)
		{
			compound(a.get(i));
			a.get(i).getTagCompound().setInteger(DELAYTIMER, -1);
			setComponents(a.get(0), getRandomComponents(Item.itemRand));
			ComponentEvents.setAmmo(capacity(a.get(i), mc.thePlayer), a.get(i), mc.thePlayer, getComponents(a.get(i)));
			subItems.add(a.get(i));
		}
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
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		int i = delay(stack, player);
		if(i < 0)
		{
			i = 0;
		}
		if(compound.getBoolean(RIGHTCLICKEDLAST) && automatic(stack, player))
		{
			i = 0;
		}
		compound.setInteger(DELAYTIMER, i);
	}

	public boolean canShoot(ItemStack stack, EntityPlayer player)
	{
		compound(stack);
		NBTTagCompound compound = stack.getTagCompound();
		if(compound.getInteger(DELAYTIMER) >= 0 || compound.getInteger(FIRERATETIMER) > 0)
		{
			return false;
		}
		if(compound.getBoolean(RIGHTCLICKEDLAST) && !automatic(stack, player))
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

		components = ComponentEvents.updateComponents(stack, entity, slot, isSelected, components);

		if(compound.getFloat(RECOIL) > 0.01)
		{
			if(entity instanceof EntityPlayer)
			{
				recoilPerTick(stack, entity);
			}
		}
		if(compound.getInteger(FIRERATETIMER) > 0)
		{
			compound.setInteger(FIRERATETIMER, compound.getInteger(FIRERATETIMER)-1);
		}

		if(entity instanceof EntityPlayer)
		{
			if(compound.getInteger(DELAYTIMER) == 0)
			{
				fire(stack, world, (EntityPlayer)entity);
			}
		}
		if(!(entity instanceof EntityPlayer) || canShoot(stack, (EntityPlayer)entity))
		{
			resetDelay(stack);
		}

		if(compound.getInteger(DELAYTIMER) > 0)
		{
			compound.setInteger(DELAYTIMER, compound.getInteger(DELAYTIMER)-1);
		}
	}

	public void fire(ItemStack stack, World world, EntityPlayer player)
	{
		compound(stack);
		ArrayList<Component> components = getComponents(stack);
		NBTTagCompound compound = stack.getTagCompound();

		ArrayList<Entity> e = ComponentEvents.fireComponents(stack, player, components);

		if(e == null || e.size() <= 0)
		{
			//click sound
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
			kickBack(stack, player, e);
			applyRecoil(stack, player);
			compound.setInteger(FIRERATETIMER, fireRate(stack, player));
		}
		resetDelay(stack);

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
			float f = -(float)player.motionY*3;
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
		player.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch + r - r*(i-1)/i);

		compound.setFloat(RECOIL, r*(i-1)/i);
	}

	public void applyRecoil(ItemStack stack, EntityPlayer player)
	{
		compound(stack);
		float r = recoil(stack, player);
		EntityExtendedPlayer props = EntityExtendedPlayer.get(player);
		NBTTagCompound compound = stack.getTagCompound();
		compound.setFloat(RECOIL, compound.getFloat(RECOIL) + r);
		player.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw + Randomization.r(r/8), player.rotationPitch - r);
	}

	public static void setComponents(ItemStack stack, Component[] cs)
	{
		setComponents(stack, ArraysAndSuch.arrayToArrayList(cs));
	}

	public static void addComponent(ItemStack stack, Component c)
	{
		ArrayList<Component> cs = getComponents(stack);
		if(!ArraysAndSuch.has(cs, c) || true)
		{
			cs.add(c);
			setComponents(stack, cs);
		}
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

	public static void setComponentIDs(ItemStack stack, ArrayList<String> components)
	{
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
		return spread/360;
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
		return kickback/3;
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

	public static Item ejectableAmmo(ItemStack gun, ItemStack stack, EntityPlayer player)
	{
		ArrayList<Component> components = getComponents(gun);
		for(int i = 0; i < components.size(); ++i)
		{
			Item item = components.get(i).ejectableAmmo(stack, player.worldObj, player, components);
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
		return durabilityMax(stack)-durabilityDamage(stack);
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
			capacity += components.get(i).material.durability;
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
	public boolean isFull3D()
	{
		return true;
	}
}
