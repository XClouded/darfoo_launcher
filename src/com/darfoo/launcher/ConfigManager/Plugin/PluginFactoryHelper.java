package com.darfoo.launcher.ConfigManager.Plugin;

/**
 * Created by wsmlby on 13-8-19.
 */
public class PluginFactoryHelper {
    public static PluginHelper newInstance(Class<?> mClass) {
        try {
            PluginHelper o = new PluginHelper(mClass.newInstance(), mClass);

            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
