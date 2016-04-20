package com.spectral.spectral_guns.core;

import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

public class Mods {

    public static HashMap<String, Object> modList = new HashMap<String, Object>();

    public static boolean isMod(Object object) {
        return object.getClass().isAnnotationPresent(Mod.class);
    }

    public static boolean registerMod(Object mod, String name) {
        if (isMod(mod)) {
            modList.put(name, mod);
            return true;
        } else {
            throw new Error(mod + " is not a mod!");
        }

    }

    public static Mod getAnnotation(Object o) {
        if (isMod(o)) {
            return o.getClass().getAnnotation(Mod.class);
        } else {
            throw new Error(o + " is not a mod!");
        }
    }

    public static int doesModFollowVersioningRules(Object mod) {
        if (isMod(mod)) {
            try {
                return Integer.parseInt(getAnnotation(mod).version());
            } catch (Exception e) {
                throw new Error(mod + " does not follow versioning rules!" + getAnnotation(mod).version());
            }
        } else {
            throw new Error(mod + " is not a mod!");
        }
    }
}
