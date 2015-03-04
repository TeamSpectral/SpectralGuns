package tnt.client;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import tnt.common.EntityTNTBullet;

public class TNTDamageSource
{
    public static DamageSource causeBulletDamage(Entity entity, Entity par1Entity)
    {
        return (new EntityDamageSourceIndirect("arrow", entity, par1Entity)).setProjectile();
    }

}
