package io.github.droppinganvil.easypersistence.Configuration;

import io.github.droppinganvil.easypersistence.PersistenceObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Config extends PersistenceObject {
    public Config() {
        /*
        There should only ever be one instance of Config so we don't need to worry about giving it a unique object ID
         */
        super("Configurations", "Config", User.getInstance(), 100, 0);
        setObject(this);
    }
    public static Integer port = 30;
    public static Long wait = 10L;
    public static Boolean addressWhitelist = false;
    public static List<String> addressList = new ArrayList<String>(Collections.singleton("127.0.0.1"));
    public static String key = "Please change me! " + new Random().nextInt(1000000);
    public static Boolean safeEdit = true;
    public static Integer cycleInterval = 20;

}
