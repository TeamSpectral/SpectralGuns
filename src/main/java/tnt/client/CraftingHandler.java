package tnt.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import tnt.TNTMain;
import cpw.mods.fml.common.ICraftingHandler;

public class CraftingHandler implements ICraftingHandler
{
    public int raw = 0;

    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
    {
        if (item.itemID == TNTMain.foodGun.itemID)
        {
            player.addStat(TNTMain.craftFood, 1);
        }
    }

    public void onSmelting(EntityPlayer player, ItemStack item)
    {

    }
}
