package tnt.client;

import net.minecraftforge.client.MinecraftForgeClient;
import tnt.TNTMain;
import tnt.common.CommonProxy;
import tnt.common.EntityColorSplash;
import tnt.common.EntityFoodBullet;
import tnt.common.EntityTNTBullet;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public void registerRenderThings()
    {
         RenderingRegistry.registerEntityRenderingHandler(EntityTNTBullet.class, new RenderTNTBullet());
         RenderingRegistry.registerEntityRenderingHandler(EntityFoodBullet.class, new RenderFoodBullet());
         RenderingRegistry.registerEntityRenderingHandler(EntityColorSplash.class, new RenderColorSplash());
        		 
        		 
        MinecraftForgeClient.registerItemRenderer(TNTMain.foodGun.itemID, new RenderGun(0));
        MinecraftForgeClient.registerItemRenderer(TNTMain.colorGun.itemID, new RenderGun(1));
    }
}
