package com.spectral.spectral_guns.core;

import com.spectral.spectral_guns.core.chat.ChatMessageFancifier;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

public class SpectralCore {
    public static SpectralCore instance = new SpectralCore();

    public void init(FMLInitializationEvent event) {
        System.out.println("Init (SpectralCore)");
        FMLCommonHandler.instance().bus().register(this);
    }

    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("Postinit (SpectralCore)");
        try {
            VersionCheck.verCheck();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        try {
            if (VersionCheck.messages.size() > 0)
                for (String s : VersionCheck.messages.keySet()) {
                    System.out.println("Update avaliable - " + s);
                    VersionCheck.messages.remove(s);
                    event.player.addChatMessage(new ChatMessageFancifier().fancifyUpdateMessage(s));
                    // event.player.addChatMessage(new ChatMessageFancifier().makeURL(new ChatMessageFancifier().fancifyUpdateMessage("Update HERE!"), new URL(VersionCheck.modnametourl.get(VersionCheck.messages.get(s)))));
                }
            if (VersionCheck.urls.size() > 0)
                for (String s : VersionCheck.urls) {
                    VersionCheck.urls.remove(s);
                    event.player.addChatMessage(new ChatMessageFancifier().fancifyURL(s));
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
