package tnt.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tnt.TNTMain;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTnTGun extends Item
{
    public ItemTnTGun(int par1)
    {
        super(par1);
        this.maxStackSize = 1;
        this.setMaxDamage(400);
    }

    public boolean shouldRotateAroundWhenRendering()
    {
    	return true;
    }
    
    public ItemStack onItemRightClick(ItemStack i, World w, EntityPlayer p)
    {
		if(p.inventory.hasItem(TNTMain.tntBullet.itemID) || p.capabilities.isCreativeMode)
		{
			w.playSoundAtEntity(p, "random.explode", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + 1.0F);
			
			if(!w.isRemote)
			{
			EntityTNTBullet b = new EntityTNTBullet(w, p, 1.0F);
			w.spawnEntityInWorld(b);
			}
			
			if(!p.capabilities.isCreativeMode)
			{
			p.inventory.consumeInventoryItem(TNTMain.tntBullet.itemID);
			
			}
			
		}
    	
    	return i;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("tnt:" + this.getUnlocalizedName().substring(5));
    }
}
