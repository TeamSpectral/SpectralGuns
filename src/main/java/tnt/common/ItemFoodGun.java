package tnt.common;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFoodGun extends Item
{
    public ItemFoodGun(int par1)
    {
        super(par1);
        this.maxStackSize = 1;
        this.setMaxDamage(0);
    }
    
    public boolean shouldRotateAroundWhenRendering()
    {
    	return true;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
	    
    	for(int i = 0; i < par3EntityPlayer.inventory.getSizeInventory(); i++)
    	{
    		ItemStack stack = par3EntityPlayer.inventory.getStackInSlot(i);
    		
    		if(stack != null && (stack.getItem() instanceof ItemFood || stack.getItem() == Item.cake))
    		{

           	 par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                 if (!par2World.isRemote)
                 {
                	 EntityFoodBullet food = new EntityFoodBullet(par2World, par3EntityPlayer, 1.0F);
                	 food.setFoodID(stack.getItem().itemID);
                     par2World.spawnEntityInWorld(food);
                    
                 }
                 
                 if (!par3EntityPlayer.capabilities.isCreativeMode)
                 {
                     par3EntityPlayer.inventory.consumeInventoryItem(stack.getItem().itemID);
                 }
     			
                 break;
    		}
    	
    	}
		return par1ItemStack;
    	
	/**	if(this.hasFood(p.inventory) || p.capabilities.isCreativeMode)
		{
			
			if(!w.isRemote)
			{
				EntityTNTBullet b = new EntityTNTBullet(w, p, 1.0F);
				w.spawnEntityInWorld(b);
			}
			
			if(!p.capabilities.isCreativeMode)
			{
				this.consumeFood(p.inventory);
			}
			
		}**/

    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("tnt:" + this.getUnlocalizedName().substring(5));
    }
}
