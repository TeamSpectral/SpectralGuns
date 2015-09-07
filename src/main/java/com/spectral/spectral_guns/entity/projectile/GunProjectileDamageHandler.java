package com.spectral.spectral_guns.entity.projectile;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class GunProjectileDamageHandler
{
	private double damage;
	private final DamageSource damageSource;
	private ArrayList<ConsumerDamageDealt> actions;
	
	private GunProjectileDamageHandler(DamageSource damageSource)
	{
		this.damageSource = damageSource;
	}
	
	public static final HashMap<Entity, GunProjectileDamageHandler> damages = new HashMap();
	
	public static boolean putDamage(Entity entity, DamageSource ds, double amount)
	{
		return putDamage(entity, ds, amount, null);
	}
	
	public static boolean putDamage(Entity entity, DamageSource ds, double amount, ConsumerDamageDealt action)
	{
		if(!damages.containsKey(entity))
		{
			damages.put(entity, new GunProjectileDamageHandler(ds));
		}
		if(damages.get(entity).damageSource.getDamageType().equals(ds.getDamageType()))
		{
			damages.get(entity).damage += amount;
			return true;
		}
		return false;
	}
	
	public static void applyDamages()
	{
		for(Entity entity : damages.keySet())
		{
			GunProjectileDamageHandler dh = damages.get(entity);
			if(entity.attackEntityFrom(dh.damageSource, (float)dh.damage))
			{
				if(dh.actions != null)
				{
					for(ConsumerDamageDealt action : dh.actions)
					{
						action.action(entity, dh.damageSource, dh.damage);
					}
				}
			}
		}
		damages.clear();
	}
	
	public abstract static class ConsumerDamageDealt
	{
		public ConsumerDamageDealt()
		{
			
		}
		
		public abstract void action(Entity entityHit, DamageSource ds, double amount);
	}
}
