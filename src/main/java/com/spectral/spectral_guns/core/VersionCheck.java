package com.spectral.spectral_guns.core;

import com.google.common.collect.Lists;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class VersionCheck {
    public static HashMap<String, String> messages = new HashMap<String, String>();
    public static List<String> urls = Lists.newArrayList();

    public static boolean doesModHaveLatest(Object mod, int latest) {
        return Mods.isMod(mod) && Mods.doesModFollowVersioningRules(mod) >= latest;
    }

    public static String downloadFile(String url) throws IOException {
        URL url2 = new URL(url);
        URLConnection urlConnection = url2.openConnection();
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        BufferedReader breader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = breader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    public static void verCheck() throws IOException {
        for (String name : Mods.modList.keySet()) {
            JSONObject jsonObject = new JSONObject(downloadFile("https://raw.githubusercontent.com/TeamSpectral/static/master/ver" + name.toLowerCase()));  //For example verspectralguns

            if (!doesModHaveLatest(Mods.modList.get(name), jsonObject.getInt("version"))) {
                messages.put(jsonObject.getString("mcmessage"), name);
                urls.add(jsonObject.getString("url"));
            } else {
                System.out.println(name + " is up to date!");
            }
        }
    }
}
