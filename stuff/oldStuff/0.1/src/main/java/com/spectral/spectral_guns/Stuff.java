package com.spectral.spectral_guns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.sun.javafx.geom.Vec3f;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Stuff
{
	public static Random rand = new Random();

	/**Two direction random things. I use it. Don't judge.**/ //- sigurd4
	public static class Randomization
	{
		public static double r(double i)
		{
			return (rand.nextDouble()*i*2)-i;
		}
		public static double r(double i, Random rand)
		{
			Stuff.rand = rand;
			return r(i);
		}
		public static float r(float i, Random rand)
		{
			Stuff.rand = rand;
			return r(i);
		}
		public static float r(float i)
		{
			return (rand.nextFloat()*i*2)-i;
		}
		public static Object getRandom(List es)
		{
			if(es.size() > 0)
			{
				return es.get(es.size() > 1 ? rand.nextInt(es.size()-1) : 0);
			}
			return null;
		}
	}
	
	/**3D coordinates etc.*/ //- sigurd4
	public static class Coordinates3D
	{
		public static Coords3D mix(ArrayList<Coords3D> a)
		{
			Coords3D pos = new Coords3D(0, 0, 0);
			for(int i = 0; i < a.size(); ++i)
			{
				pos = add(pos, a.get(i));
			}
			//pos = divide(pos, a.size());
			return pos;
		}
		
		public static Coords3D stabilize(Coords3D pos, double w)
		{
			double f = w/distance(pos);
			return new Coords3D(pos.x*f, pos.y*f, pos.z*f);
		}
		
		public static double distance(Coords3D pos1, Coords3D pos2)
		{
			return distance(subtract(pos1, pos2));
		}
		
		public static Coords3D subtract(Coords3D pos1, Coords3D pos2)
		{
			return new Coords3D(pos1.x-pos2.x, pos1.z-pos2.z, pos1.y-pos2.y);
		}
		
		public static Coords3D add(Coords3D pos1, Coords3D pos2)
		{
			return new Coords3D(pos1.x+pos2.x, pos1.z+pos2.z, pos1.y+pos2.y);
		}
		
		public static Coords3D divide(Coords3D pos, double f)
		{
			return multiply(pos, 1/f);
		}
		
		public static Coords3D multiply(Coords3D pos, double f)
		{
			return new Coords3D(pos.x*f, pos.z*f, pos.y*f);
		}
		
		public static double distance(Coords3D pos)
		{
			return Math.sqrt(pos.x*pos.x + pos.y*pos.y + pos.z*pos.z);
		}
		
		public static class Coords3D
		{
			public double x;
			public double y;
			public double z;
			
			public Coords3D(double x, double y, double z)
			{
				this.x = x;
				this.y = y;
				this.z = z;
			}
			
			public Coords3D(float x, float y, float z)
			{
				this((double)x, (double)y, (double)z);
			}
		}
		
		public static void throwThing(Entity source, Entity object)
		{
			object.setLocationAndAngles(source.posX, source.posY + (double)source.getEyeHeight(), source.posZ, source.rotationYaw, source.rotationPitch);
			object.posX -= (double)(MathHelper.cos(object.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			object.posY -= 0.10000000149011612D;
			object.posZ -= (double)(MathHelper.sin(object.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
			object.setPosition(object.posX, object.posY, object.posZ);
			float f = 0.4F;
			object.motionX = (double)(-MathHelper.sin(object.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(object.rotationPitch / 180.0F * (float)Math.PI) * f);
			object.motionZ = (double)(MathHelper.cos(object.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(object.rotationPitch / 180.0F * (float)Math.PI) * f);

			object.motionY = (double)(-MathHelper.sin((object.rotationPitch) / 180.0F * (float)Math.PI) * f);
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
	}

	/**Get all entities within the area**/ //- sigurd4
	public static class EntitiesInArea
	{
		public static List<Entity> getEntitiesWithinRadius(Entity e, float r)
		{
			return getEntitiesWithinRadius(e, r, true);
		}
		public static List<Entity> getEntitiesWithinRadius(Entity e, float r, boolean exclude)
		{
			if(e != null)
			{
				List<Entity> es = getEntitiesWithinRadius(e.worldObj, e.posX, e.posY, e.posZ, r);
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
		public static Entity getRandomEntityWithinRadius(Entity e, float r, Random rand)
		{
			List<Entity> es = getEntitiesWithinRadius(e, r);
			Entity e2 = (Entity)Randomization.getRandom(es);
			return e2;
		}
		public static Entity getRandomEntityWithinCube(Entity e, float m, Random rand)
		{
			List<Entity> es = getEntitiesWithinCube(e, m);
			Entity e2 = (Entity)Randomization.getRandom(es);
			return e2;
		}
		public static List<Entity> getEntitiesWithinCube(Entity e, float m)
		{
			return getEntitiesWithinCube(e, m, true);
		}
		public static List<Entity> getEntitiesWithinCube(Entity e, float m, boolean exclude)
		{
			if(e != null)
			{
				List<Entity> es = getEntitiesWithinCube(e.worldObj, e.posX, e.posY, e.posZ, m);
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
		public static List<Entity> getEntitiesWithinRadius(World w, double x, double y, double z, float r)
		{
			List<Entity> es = getEntitiesWithinCube(w, x, y, z, r);
			for(int i = 0; i > es.size(); ++i)
			{
				Entity e2 = es.get(i);
				if(e2.getDistance(x, y, z) > r)
				{
					es.remove(i);
					--i;
				}
			}
			return es;
		}
		public static List<Entity> getEntitiesWithinCube(World w, double x, double y, double z, float m)
		{
			List<Entity> es = w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x-m, y-m, z-m, x+m, y+m, z+m));
			return es;
		}
	}
}
