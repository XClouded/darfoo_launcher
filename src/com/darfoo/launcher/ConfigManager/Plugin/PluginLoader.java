package com.darfoo.launcher.ConfigManager.Plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;

/**
 * Created by wsmlby on 13-8-19.
 */
public class PluginLoader extends DexClassLoader {

    private final String mPath;
    private final Context mContext;

    public PluginLoader(Context context, String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
        mPath = dexPath;
        mContext = context;

    }

    public Resources getResource() {

        try {
            Resources res = mContext.getResources();
            AssetManager as = AssetManager.class.newInstance();
            as.getClass().getMethod("addAssetPath", String.class).invoke(as, mPath);
            return new Resources(as, res.getDisplayMetrics(), res.getConfiguration());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
