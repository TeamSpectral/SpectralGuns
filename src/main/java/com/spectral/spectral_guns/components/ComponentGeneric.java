package com.spectral.spectral_guns.components;

import java.util.ArrayList;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.spectral.spectral_guns.Stuff;
import com.spectral.spectral_guns.Stuff.ArraysAndSuch;
import com.spectral.spectral_guns.audio.AudioHandler;
import com.spectral.spectral_guns.components.Component.ComponentRegister.Type;
import com.spectral.spectral_guns.items.ItemComponent;
import com.spectral.spectral_guns.items.ItemGun;

public abstract class ComponentGeneric extends Component
{
	final protected String name;
	private Component[] required;
	private Component[] incapatible;
	private final Type connected;
	private Type[] requiredTypes;
	private Type[] incapatibleTypes;
	private ComponentMaterial[] requiredMats;
	private ComponentMaterial[] incapatibleMats;
	final protected double heatLoss;
	final protected float heatThreshold;
	final protected float maxDurability;
	
	public ComponentGeneric(String2 id, String2 name, Type connected, double heatLoss, float heatThreshold, float maxDurability, Type type, ComponentMaterial material)
	{
		super(id, type, material);
		this.name = name.s1 + (type != Type.MISC ? "." + material.getUnlocalizedName(type, this) : "") + name.s2;
		this.heatLoss = heatLoss;
		this.heatThreshold = heatThreshold;
		this.maxDurability = maxDurability;
		this.connected = connected;
	}
	
	@Override
	public String getFancyName()
	{
		return this.name;
	}
	
	public void addRequiredComponents(ArrayList<Component> cs)
	{
		Stuff.ArraysAndSuch.mixArrays(Component.class, this.required, Stuff.ArraysAndSuch.arrayListToArray(Component.class, cs));
	}
	
	public void addRequiredComponent(Component c)
	{
		Stuff.ArraysAndSuch.addToArray(Component.class, this.required, c);
	}
	
	@Override
	public ArrayList<Component> getRequired()
	{
		return ArraysAndSuch.arrayToArrayList(this.required);
	}
	
	public void addIncapatibleComponents(Component[] cs)
	{
		Stuff.ArraysAndSuch.mixArrays(Component.class, this.incapatible, cs);
	}
	
	public void addIncapatibleComponent(Component c)
	{
		Stuff.ArraysAndSuch.addToArray(Component.class, this.incapatible, c);
	}
	
	@Override
	public ArrayList<Component> getIncapatible()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatible);
	}
	
	public void addRequiredTypes(Type[] ts)
	{
		Stuff.ArraysAndSuch.mixArrays(Type.class, this.requiredTypes, ts);
	}
	
	public void addRequiredType(Type t)
	{
		Stuff.ArraysAndSuch.addToArray(Type.class, this.requiredTypes, t);
	}
	
	@Override
	public Type getTypeConnectedTo()
	{
		return this.connected;
	}
	
	@Override
	public ArrayList<Type> getRequiredTypes()
	{
		if(this.connected != null && this.requiredTypes != null)
		{
			return ArraysAndSuch.arrayToArrayList(Stuff.ArraysAndSuch.mixArrays(Type.class, new Type[]{this.connected}, this.requiredTypes));
		}
		return ArraysAndSuch.arrayToArrayList(this.requiredTypes);
	}
	
	public void addIncapatibleTypes(Type[] ts)
	{
		Stuff.ArraysAndSuch.mixArrays(Type.class, this.incapatibleTypes, ts);
	}
	
	public void addIncapatibleType(Type t)
	{
		Stuff.ArraysAndSuch.addToArray(Type.class, this.incapatibleTypes, t);
	}
	
	@Override
	public ArrayList<Type> getIncapatibleTypes()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatibleTypes);
	}
	
	public void addRequiredMats(ComponentMaterial[] ms)
	{
		Stuff.ArraysAndSuch.mixArrays(ComponentMaterial.class, this.requiredMats, ms);
	}
	
	public void addRequiredMat(ComponentMaterial m)
	{
		Stuff.ArraysAndSuch.addToArray(ComponentMaterial.class, this.requiredMats, m);
	}
	
	@Override
	public ArrayList<ComponentMaterial> getRequiredMats()
	{
		return ArraysAndSuch.arrayToArrayList(this.requiredMats);
	}
	
	public void addIncapatibleMats(ComponentMaterial[] ms)
	{
		Stuff.ArraysAndSuch.mixArrays(ComponentMaterial.class, this.incapatibleMats, ms);
	}
	
	public void addIncapatibleMat(ComponentMaterial m)
	{
		Stuff.ArraysAndSuch.addToArray(ComponentMaterial.class, this.incapatibleMats, m);
	}
	
	@Override
	public ArrayList<ComponentMaterial> getIncapatibleMats()
	{
		return ArraysAndSuch.arrayToArrayList(this.incapatibleMats);
	}
	
	public int setAmmo(ItemStack stack, World world, EntityPlayer player, int ammo)
	{
		return ammo;
	}
	
	@Override
	public ArrayList<Entity> fire(int slot, ArrayList<Entity> projectiles, ItemStack stack, World world, EntityPlayer player)
	{
		return projectiles;
	}
	
	@Override
	public float delay(int slot, float delay, ItemStack stack, World world, EntityPlayer player)
	{
		return delay;
	}
	
	@Override
	public float recoil(int slot, float recoil, ItemStack stack, World world, EntityPlayer player)
	{
		return recoil;
	}
	
	@Override
	public float instability(int slot, float instability, ItemStack stack, World world, EntityPlayer player)
	{
		return instability;
	}
	
	@Override
	public float kickback(int slot, float kickback, ItemStack stack, World world, EntityPlayer player)
	{
		return kickback;
	}
	
	@Override
	public float zoom(int slot, float zoom, ItemStack stack, World world, EntityPlayer player)
	{
		return zoom;
	}
	
	@Override
	public float fireRate(int slot, float rate, ItemStack stack, World world, EntityPlayer player)
	{
		return rate;
	}
	
	@Override
	public float spread(int slot, float spread, ItemStack stack, World world, EntityPlayer player)
	{
		return spread;
	}
	
	@Override
	public float speed(int slot, float speed, ItemStack stack, World world, EntityPlayer player)
	{
		return speed;
	}
	
	@Override
	public boolean doSpray(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	protected void fireSound(int slot, Entity e, ItemStack stack, World world, EntityPlayer player)
	{
	}
	
	@Override
	public int ammo(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public boolean isAmmoItem(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public Item ejectableAmmo(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public int setAmmo(int slot, int ammo, ItemStack stack, World world, EntityPlayer player)
	{
		return ammo;
	}
	
	@Override
	public int capacity(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return 0;
	}
	
	@Override
	public boolean automatic(int slot, ItemStack stack, World world, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	protected double heatConductiveness2(int slot, ItemStack stack)
	{
		return this.heatLoss;
	}
	
	@Override
	public float heatThreshold(int slot, ItemStack stack)
	{
		return (float)(this.heatThreshold * (this.heat(slot, stack) > 0 ? this.material.heatThresholdMax : this.material.heatThresholdMin));
	}
	
	@Override
	public int durabilityMax(int slot, ItemStack stack)
	{
		return (int)(this.material.durability * this.maxDurability);
	}
	
	@Override
	public void update(int slot, ItemStack stack, World world, EntityPlayer player, int invSlot, boolean isSelected)
	{
		if(this.hasMaterialTrait(ComponentTraits.BURNS))
		{
			if(this.heat(slot, stack) > this.heatThreshold(slot, stack))
			{
				if(this.heat(slot, stack) > this.heatThreshold(slot, stack) * 1.5)
				{
					ItemComponent.ON_FIRE.add(stack, 1);
				}
				else if(ItemComponent.ON_FIRE.get(stack) < ItemComponent.ON_FIRE.max)
				{
					ItemComponent.ON_FIRE.add(stack, -1);
				}
			}
			else
			{
				ItemComponent.ON_FIRE.add(stack, -1);
			}
		}
		if(this.heat(slot, stack) > this.heatThreshold(slot, stack) || this.heat(slot, stack) < -this.heatThreshold(slot, stack))
		{
			if(ItemComponent.ON_FIRE.get(stack) >= ItemComponent.ON_FIRE.max && this.hasMaterialTrait(ComponentTraits.BURNS) && this.heat(slot, stack) > this.heatThreshold(slot, stack) * 1.5 && Stuff.rand.nextFloat() <= 1 / 3)
			{
				double d = 0.1;
				world.spawnParticle(EnumParticleTypes.FLAME, player.posX + Stuff.Randomization.r(d), player.posY + player.getYOffset() + player.getEyeHeight() * 0.9 + Stuff.Randomization.r(d), player.posZ + Stuff.Randomization.r(d), player.motionX / 4, player.motionY / 4 + 0.1F, player.motionZ / 4, new int[]{});
			}
			if(player.ticksExisted % 10 == 1)
			{
				int damage = 1;
				if(ItemComponent.ON_FIRE.get(stack) >= ItemComponent.ON_FIRE.max && this.hasMaterialTrait(ComponentTraits.BURNS) && this.heat(slot, stack) > this.heatThreshold(slot, stack) * 1.5)
				{
					damage += burnDamage;
					if(world.rand.nextFloat() <= (float)burnIgniteChance1 / (float)burnIgniteChance2)
					{
						player.setFire(burnIgniteSeconds);
					}
					AudioHandler.createMovingEntitySound(player, "fire.fire", 1.8F + world.rand.nextFloat(), world.rand.nextFloat() * 0.2F + 0.7F, false);
				}
				this.addDurabilityDamage(slot, damage, stack, player);
			}
		}
		loop:
		for(Component c : ItemGun.getComponents(stack).values())
		{
			if(c != this)
			{
				Type type = this.getTypeConnectedTo();
				if(type == c.type)
				{
					this.heatMix(slot, stack, c);
					continue loop;
				}
			}
		}
		float h = 0.01F;
		float baseTemp = player.isInsideOfMaterial(Material.water) ? -20 : 0;
		float mult = 0.995F;
		if(this.heat(slot, stack) > baseTemp)
		{
			if(this.heat(slot, stack) > this.heatThreshold(slot, stack) / 20 + baseTemp)
			{
				if(!player.isInLava() && !player.isBurning())
				{
					this.addHeat(slot, -h, stack);
				}
				if(this.heat(slot, stack) < baseTemp)
				{
					this.setHeat(slot, baseTemp, stack);
				}
			}
			if(this.heat(slot, stack) > baseTemp)
			{
				this.setHeat(slot, (this.heat(slot, stack) - baseTemp) * mult + baseTemp, stack);
			}
		}
		else if(this.heat(slot, stack) < baseTemp)
		{
			if(this.heat(slot, stack) < -this.heatThreshold(slot, stack) / 20 + baseTemp)
			{
				this.addHeat(slot, h, stack);
				if(this.heat(slot, stack) > baseTemp)
				{
					this.setHeat(slot, baseTemp, stack);
				}
			}
			if(this.heat(slot, stack) < baseTemp)
			{
				this.setHeat(slot, (this.heat(slot, stack) - baseTemp) * mult + baseTemp, stack);
			}
		}
		if(player.isInsideOfMaterial(Material.water) || world.isRaining() && world.canLightningStrike(player.getPosition()))
		{
			if(this.hasMaterialTrait(ComponentTraits.RUSTS) && player.ticksExisted % 640 == 1)
			{
				this.addDurabilityDamage(slot, 1, stack, player);
			}
		}
	}
	
	@Override
	public void getTooltip(ArrayList<String2> tooltip, EntityPlayer player, World world)
	{
		
	}
}
