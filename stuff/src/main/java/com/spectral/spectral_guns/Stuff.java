package com.spectral.spectral_guns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.sun.javafx.geom.Vec3f;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public class Stuff
{
	public static Random rand = new Random();

	/**Random stuff**/ //- sigurd4
	public static class Randomization
	{
		public static double r(double i)
		{
			return r(i, Stuff.rand);
		}
		public static double r(double i, Random rand)
		{
			return (rand.nextDouble()*i*2)-i;
		}
		public static float r(float i, Random rand)
		{
			return (rand.nextFloat()*i*2)-i;
		}
		public static float r(float i)
		{
			return r(i, Stuff.rand);
		}
		public static <T> T getRandom(List<T> es)
		{
			if(es.size() > 0)
			{
				return es.get(es.size() > 1 ? rand.nextInt(es.size()-1) : 0);
			}
			return null;
		}
		public static <T> T getRandom(T[] es)
		{
			return getRandom(ArraysAndSuch.arrayToArrayList(es));
		}
	}

	/**Stuff with coordinates in a 3D room.*/ //- sigurd4
	public static class Coordinates3D
	{
		public static Vec3 mix(ArrayList<Vec3> a)
		{
			Vec3 pos = new Vec3(0, 0, 0);
			for(int i = 0; i < a.size(); ++i)
			{
				pos = add(pos, a.get(i));
			}
			pos = divide(pos, a.size());
			return pos;
		}

		public static Vec3 stabilize(Vec3 pos, double w)
		{
			double d = w/distance(pos);
			return new Vec3(pos.xCoord*d, pos.yCoord*d, pos.zCoord*d);
		}

		public static double distance(Vec3 pos1, Vec3 pos2)
		{
			return distance(subtract(pos1, pos2));
		}

		public static Vec3 subtract(Vec3 pos1, Vec3 pos2)
		{
			return new Vec3(pos1.xCoord-pos2.xCoord, pos1.yCoord-pos2.yCoord, pos1.zCoord-pos2.zCoord);
		}

		public static Vec3 add(Vec3 pos1, Vec3 pos2)
		{
			return new Vec3(pos1.xCoord+pos2.xCoord, pos1.yCoord+pos2.yCoord, pos1.zCoord+pos2.zCoord);
		}

		public static Vec3 divide(Vec3 pos, double d)
		{
			return multiply(pos, 1/d);
		}

		public static Vec3 multiply(Vec3 pos, double d)
		{
			return new Vec3(pos.xCoord*d, pos.yCoord*d, pos.zCoord*d);
		}

		public static double distance(Vec3 pos)
		{
			return Math.sqrt(pos.xCoord*pos.xCoord + pos.yCoord*pos.yCoord + pos.zCoord*pos.zCoord);
		}

		public static void throwThing(Entity source, Entity object, double f)
		{
			object.setLocationAndAngles(source.posX, source.posY + (double)source.getEyeHeight(), source.posZ, source.rotationYaw, source.rotationPitch);
			object.posX -= (double)(MathHelper.cos(object.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			object.posY -= 0.10000000149011612D;
			object.posZ -= (double)(MathHelper.sin(object.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			object.setPosition(object.posX, object.posY, object.posZ);

			object.motionX = (double)(-MathHelper.sin(object.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(object.rotationPitch / 180.0F * (float)Math.PI) * f);
			object.motionZ = (double)(MathHelper.cos(object.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(object.rotationPitch / 180.0F * (float)Math.PI) * f);

			object.motionY = (double)(-MathHelper.sin((object.rotationPitch) / 180.0F * (float)Math.PI) * f);
		}

		public static Vec3 velocity(Entity entity)
		{
			if(entity == null)
			{
				return new Vec3(0, 0, 0);
			}
			return new Vec3(entity.motionX, entity.motionY, entity.motionZ);
		}

		public static void velocity(Entity entity, Vec3 vec)
		{
			if(entity == null || vec == null)
			{
				return;
			}
			entity.motionX = vec.xCoord;
			entity.motionY = vec.yCoord;
			entity.motionZ = vec.zCoord;
		}

		public static void bounce(Entity entity, EnumFacing sideHit, double bouncyness)
		{
			velocity(entity, bounce(velocity(entity), sideHit, bouncyness));
		}

		public static Vec3 bounce(Vec3 m, EnumFacing sideHit, double bouncyness)
		{
			if(sideHit == null)
			{
				return m;
			}
			double xCoord = m.xCoord;
			double yCoord = m.yCoord;
			double zCoord = m.zCoord;
			Axis a = sideHit.getAxis();
			switch(a)
			{
			case X:
			{
				xCoord *= -bouncyness;
				break;
			}
			case Y:
			{
				yCoord *= -bouncyness;
				break;
			}
			case Z:
			{
				zCoord *= -bouncyness;
				break;
			}
			}
			return new Vec3(xCoord, yCoord, zCoord);
		}
		
		public static Vec3i getVecFromAxis(Axis axis, AxisDirection direction)
		{
			int x = 0;
			int y = 0;
			int z = 0;

			switch(axis)
			{
			case X:
			{
				x = direction.getOffset();
				break;
			}
			case Y:
			{
				y = direction.getOffset();
				break;
			}
			case Z:
			{
				z = direction.getOffset();
				break;
			}
			}

			return new Vec3i(x, y, z);
		}
	}

	/**Compare entities.**/ //- sigurd4
	public static class EntityComparison
	{
		public static boolean isEntitySmaller(Entity e1, Entity e2, float offset)
		{
			return isEntityBigger(e2, e1, offset);
		}

		public static boolean isEntityBigger(Entity e1, Entity e2, float offset)
		{
			if(e1 != null && e2 != null)
			{
				float s1 = e1.height;
				if(e1.width > s1)
				{
					s1 = e1.width;
				}
				float s2 = e2.height;
				if(e2.width > s2)
				{
					s2 = e2.width;
				}
				return s1+offset > s2;
			}
			return false;
		}
	}

	/**Things with arrays and lists etc.**/ //- sigurd4
	public static class ArraysAndSuch
	{
		public static <T> ArrayList<T> hashMapToArrayList(HashMap<?, T> map)
		{
			ArrayList<T> a = new ArrayList<T>();
			Iterator<T> values = map.values().iterator();
			while(values.hasNext())
			{
				a.add(values.next());
			}
			return a;
		}

		public static <T> ArrayList<T> hashMapKeysToArrayList(HashMap<T, ?> map)
		{
			ArrayList<T> a = new ArrayList<T>();
			Iterator<T> keys = map.keySet().iterator();
			while(keys.hasNext())
			{
				a.add(keys.next());
			}
			return a;
		}

		public static <T> boolean has(T[] a, T o)
		{
			return has(arrayToArrayList(a), o);
		}

		public static <T> boolean has(ArrayList<T> a, T o)
		{
			for(int i = 0; i < a.size(); ++i)
			{
				if(a.get(i) == o)
				{
					return true;
				}
			}
			return false;
		}

		public static Object[] arrayListToArray(ArrayList<Object> al)
		{
			return arrayListToArray2(al, new Object[al.size()]);
		}

		public static <T> T[] arrayListToArray2(ArrayList<T> al, T[] a)
		{
			if(al.size() == a.length)
			{
				for(int i = 0; i < al.size(); ++i)
				{
					a[i] = al.get(i);
				}
			}
			return a;
		}

		public static <T> ArrayList<T> arrayToArrayList(T[] a)
		{
			ArrayList<T> al = new ArrayList<T>();
			for(int i = 0; i < a.length; ++i)
			{
				al.add(a[i]);
			}
			return al;
		}

		public static <T> T[] mixArrays(T[] a1, T[] a2)
		{
			ArrayList<T> al = new ArrayList<T>();
			al.addAll(arrayToArrayList(a1));
			al.addAll(arrayToArrayList(a2));
			return (T[])al.toArray();
		}

		public static <T> T[] addToArray(T[] a, T o)
		{
			ArrayList<T> al = new ArrayList<T>();
			al.addAll(arrayToArrayList(a));
			al.add(o);
			return (T[])al.toArray();
		}

		public static <T> boolean removeFromArrayList(ArrayList<T> a, T o)
		{
			for(int i = 0; i < a.size(); ++i)
			{
				if(a.get(i) == o)
				{
					a.remove(i);
					return true;
				}
			}
			return false;
		}

		public static <T> ArrayList<T> allExtending(ArrayList a, Class<T> c)
		{
			ArrayList<T> at = new ArrayList<T>();
			for(int i = 0; i < a.size(); ++i)
			{
				if(c.isInstance(a.get(i)))
				{
					at.add((T)a.get(i));
				}
			}
			return at;
		}
	}

	/**Get all entities within the area**/ //- sigurd4
	public static class EntitiesInArea
	{
		public static HashMap<Entity, Vec3> hit = new HashMap<Entity, Vec3>();

		public static List<Entity> getEntitiesWithinRadius(Entity e, double r)
		{
			return getEntitiesWithinRadius(e, r, true);
		}
		public static List<Entity> getEntitiesWithinRadius(Entity e, double r, boolean exclude)
		{
			if(e != null)
			{
				List<Entity> es = getEntitiesWithinRadius(e.worldObj, new Vec3(e.posX, e.posY, e.posZ), r);
				for(int i = 0; i > es.size(); ++i)
				{
					Entity e2 = es.get(i);
					if(e2 == e)
					{
						es.remove(i);
						--i;
					}
				}
				return es;
			}
			return null;
		}
		public static Entity getRandomEntityWithinRadius(Entity e, double r, Random rand)
		{
			List<Entity> es = getEntitiesWithinRadius(e, r);
			Entity e2 = (Entity)Randomization.getRandom(es);
			return e2;
		}
		public static Entity getRandomEntityWithinCube(Entity e, double m, Random rand)
		{
			List<Entity> es = getEntitiesWithinCube(e, m);
			Entity e2 = (Entity)Randomization.getRandom(es);
			return e2;
		}
		public static List<Entity> getEntitiesWithinCube(Entity e, double m)
		{
			return getEntitiesWithinCube(e, m, true);
		}
		public static List<Entity> getEntitiesWithinCube(Entity e, double m, boolean exclude)
		{
			if(e != null)
			{
				List<Entity> es = getEntitiesWithinCube(e.worldObj, new Vec3(e.posX, e.posY, e.posZ), m);
				for(int i = 0; i > es.size(); ++i)
				{
					Entity e2 = es.get(i);
					if(e2 == e && exclude)
					{
						es.remove(i);
						--i;
					}
				}
				return es;
			}
			return null;
		}
		public static List<Entity> getEntitiesWithinRadius(World w, Vec3 v, double r)
		{
			List<Entity> es = getEntitiesWithinCube(w, v, r);
			for(int i = 0; i > es.size(); ++i)
			{
				Entity e2 = es.get(i);
				if(e2.getDistance(v.xCoord, v.yCoord, v.zCoord) > r)
				{
					es.remove(i);
					--i;
				}
			}
			return es;
		}
		public static List<Entity> getEntitiesWithinCube(World w, Vec3 v, double m)
		{
			List<Entity> es = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(v.xCoord-m, v.yCoord-m, v.zCoord-m, v.xCoord+m, v.yCoord+m, v.zCoord+m));
			return es;
		}
		public static List<Entity> getEntitiesOnAxis(World w, Vec3 pos, Vec3 p2)
		{
			return getEntitiesOnAxis(w, pos, p2, 0.04);
		}
		public static List<Entity> getEntitiesOnAxis(World w, Vec3 pos, Vec3 p2, double r)
		{
			ArrayList<Entity> es = new ArrayList<Entity>();

			double h = Math.max(r/5, 3);
			double d = Coordinates3D.distance(pos, p2);
			int t = (int)Math.ceil(d/h);
			d = h/t;

			Vec3 axis = Coordinates3D.subtract(p2, pos);
			for(int i = 0; i < t; ++i)
			{
				List<Entity> es2 = getEntitiesWithinRadius(w, Coordinates3D.add(Coordinates3D.multiply(axis, d*i), pos), r);
				for(int i2 = 0; i2 < es2.size(); ++i2)
				{
					EntitiesInArea.hit.put(es2.get(i2), Coordinates3D.add(Coordinates3D.multiply(axis, d*i), pos));
					es.addAll(es2);
				}
			}

			return es;
		}
		public static Entity getClosestEntity(List<Entity> es, Vec3 pos)
		{
			Entity e = null;
			for(int i = 0; i < es.size(); ++i)
			{
				if(e == null || e.getDistance(pos.xCoord, pos.yCoord, pos.zCoord) > es.get(i).getDistance(pos.xCoord, pos.yCoord, pos.zCoord));
				{
					e = es.get(i);
				}
			}
			return e;
		}
	}

	/**Arrays of multiple numbers**/ //- sigurd4
	public static class MathWithMultiple
	{
		public static double distance(double... ds)
		{
			for(int i = 0; i < ds.length; ++i)
			{
				ds[i] *= ds[i];
			}
			return Math.sqrt(addAll(ds));
		}

		public static double addAll(double... ds)
		{
			double d = 0;
			for(int i = 0; i < ds.length; ++i)
			{
				d += ds[i];
			}
			return d;
		}
	}

	/**fun with hashmaps**/ //- sigurd4
	public static class HashMapStuff
	{
		public static <K, V> K getKeyFromValue(HashMap<K, V> map, V value)
		{
			Iterator<K> keys = map.keySet().iterator();
			while(keys.hasNext())
			{
				K key = keys.next();
				if(map.get(key) == value)
				{
					return key;
				}
			}
			return null;
		}
	}
}
