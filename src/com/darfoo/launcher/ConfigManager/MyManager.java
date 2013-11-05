package com.darfoo.launcher.ConfigManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by wsmlby on 05/08/13.
 */
public abstract class MyManager {
    protected SharedPreferences mPrefs;
    Context mContext;

    public MyManager(Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

    }

    public abstract void saveState();

    public abstract void loadState();
    public abstract String saveStateToString();

    public abstract void loadStateFromString(String string);
}
