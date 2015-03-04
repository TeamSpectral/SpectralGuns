package tnt.common;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemColorSplashGun extends Item
{
	private static ArrayList<ItemStack> dyeList = new ArrayList<ItemStack>();
	
    public ItemColorSplashGun(int par1)
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
    		
    		if(stack != null && (stack.getItem() instanceof ItemDye))
    		{
                dyeList.add(stack);  
    		}
    		continue;
    	}
    	
    	if(!dyeList.isEmpty())
    	{
    		if(dyeList.size() == 1)
    		{
    			ItemStack stack = (ItemStack)dyeList.get(0);
    			if(stack.stackSize >= 3)
    			{
        			fireSplashGun(stack.getItemDamage(), stack.getItemDamage(), stack.getItemDamage(), par2World, par3EntityPlayer);
    			}
    		}
    		else if(dyeList.size() == 2)
    		{
    			ItemStack stack = (ItemStack)dyeList.get(0);
    			ItemStack stack2 = (ItemStack)dyeList.get(1);
    			if(stack.stackSize + stack2.stackSize >= 3)
    			{
    				if(stack.stackSize > stack2.stackSize)
    				{
    	    			fireSplashGun(stack.getItemDamage(), stack2.getItemDamage(), stack.getItemDamage(), par2World, par3EntityPlayer);
    				}
    				else if(stack2.stackSize > stack.stackSize)
    				{
    	    			fireSplashGun(stack2.getItemDamage(), stack.getItemDamage(), stack2.getItemDamage(), par2World, par3EntityPlayer);
    				}
    				else
    				{
    					fireSplashGun(stack2.getItemDamage(), stack.getItemDamage(), stack2.getItemDamage(), par2World, par3EntityPlayer);
    				}
    			}
    		}
    		else
    		{
    			ItemStack stack = (ItemStack)dyeList.get(itemRand.nextInt(dyeList.size()));
    			ItemStack stack2 = (ItemStack)dyeList.get(itemRand.nextInt(dyeList.size()));
    			ItemStack stack3 = (ItemStack)dyeList.get(itemRand.nextInt(dyeList.size()));
    			fireSplashGun(stack.getItemDamage(), stack2.getItemDamage(), stack3.getItemDamage(), par2World, par3EntityPlayer);
    		}
    	}
    	
		return par1ItemStack;
    }

    public void fireSplashGun(int color1, int color2, int color3, World world, EntityPlayer player)
    {
    	if(!world.isRemote)
    	{
        world.spawnEntityInWorld(new EntityColorSplash(world, player, 1.0F, color1, color2, color3));
    	}
    	dyeList.clear();
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.itemIcon = par1IconRegister.registerIcon("tnt:" + this.getUnlocalizedName().substring(5));
    }
}
