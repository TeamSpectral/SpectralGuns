package com.spectral.spectral_guns.entity.projectile;

public interface IEntityGunProjectile
{
	public void setMass(double mass);
	
	public void setSpeed(double velocity);
	
	//Stuff.Coordinates3D.velocity(this, Stuff.Coordinates3D.stabilize(Stuff.Coordinates3D.velocity(this), velocity));
	
	public void setForce(double force);
	
	//setSpeed(force/getMass());
	
	public double getMass();
	
	public double getSpeed();
	
	//return Stuff.Coordinates3D.distance(Stuff.Coordinates3D.velocity(this));
	
	public double getForce();
	
	//return getSpeed()*getMass();
	
}
