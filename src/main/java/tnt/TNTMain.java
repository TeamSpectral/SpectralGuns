package tnt;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import tnt.client.CraftingHandler;
import tnt.common.CommonProxy;
import tnt.common.EntityColorSplash;
import tnt.common.EntityFoodBullet;
import tnt.common.EntityTNTBullet;
import tnt.common.ItemBasic;
import tnt.common.ItemColorSplashGun;
import tnt.common.ItemFoodGun;
import tnt.common.ItemTnTGun;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "tnt", name = "TNT Gun Mod", version = "Beta")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class TNTMain
{
    public static Item tntBullet;
    public static Item tntGun;
    public static Item foodGun;
    public static Item colorGun;
    
    public static Achievement craftFood;
    public static Achievement faceCake;
    public static AchievementPage tntPage;

    public static CraftingHandler craftHandler = new CraftingHandler();
    /**@Instance("tnt")
    public static TNTMain instance;**/
    
    @SidedProxy(clientSide = "tnt.client.ClientProxy", serverSide = "tnt.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        tntBullet = (new ItemBasic(8700)).setUnlocalizedName("tnt").setCreativeTab(CreativeTabs.tabCombat);
        LanguageRegistry.addName(tntBullet, "TNT Bullet");
        
        tntGun = (new ItemTnTGun(8701)).setUnlocalizedName("tntgun").setCreativeTab(CreativeTabs.tabCombat);
        LanguageRegistry.addName(tntGun, "TNT Gun");
        
        foodGun = (new ItemFoodGun(8702)).setUnlocalizedName("foodgun").setCreativeTab(CreativeTabs.tabCombat);
        LanguageRegistry.addName(foodGun, "Food Gun");
        
        colorGun = (new ItemColorSplashGun(8703)).setUnlocalizedName("colorgun").setCreativeTab(CreativeTabs.tabCombat);
        LanguageRegistry.addName(colorGun, "Color Splash Gun");
        
        craftFood = (new Achievement(5030, "food", 0, -2, foodGun, AchievementList.openInventory)).registerAchievement();
        faceCake = (new Achievement(5031, "cakeface", -1, -3, Item.cake, craftFood)).setSpecial().registerAchievement();
        
        tntPage = new AchievementPage("Spectral Guns Mod", new Achievement[] {craftFood, faceCake});
        AchievementPage.registerAchievementPage(tntPage);
        
        LanguageRegistry.instance().addStringLocalization("achievement.cakeface", "en_US", "Faceful of Frosting");
        LanguageRegistry.instance().addStringLocalization("achievement.cakeface.desc", "en_US", "Stuff a Cake in the Food Gun and blast a monster with it!");
        
        LanguageRegistry.instance().addStringLocalization("achievement.food", "en_US", "Food Gun");
        LanguageRegistry.instance().addStringLocalization("achievement.food.desc", "en_US", "Create a food gun to play with your food!");
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        proxy.registerRenderThings();
        proxy.registerSound();
        GameRegistry.registerCraftingHandler(craftHandler);
        
        EntityRegistry.registerModEntity(EntityTNTBullet.class, "tntbullet", 370, this, 64, 10, true);
        EntityRegistry.registerModEntity(EntityFoodBullet.class, "foodbullet", 371, this, 64, 10, true);
        EntityRegistry.registerModEntity(EntityColorSplash.class, "colorsplash", 372, this, 64, 10, true);
        
        GameRegistry.addRecipe(new ItemStack(tntGun), new Object[] {
        	"ZZX", 
        	"#YZ", 
        	'X', Block.blockIron, 'Z', Item.ingotIron, 'Y', Item.redstone});
        
        GameRegistry.addShapelessRecipe(new ItemStack(tntBullet, 15), new Object[] {new ItemStack(Block.tnt), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron), new ItemStack(Item.ingotIron)});
        GameRegistry.addShapelessRecipe(new ItemStack(tntBullet, 3), new Object[] {new ItemStack(Item.ingotIron), new ItemStack(Item.gunpowder), new ItemStack(Block.sand)});

    }
}
