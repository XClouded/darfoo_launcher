package com.darfoo.launcher.ConfigManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by wsmlby on 05/08/13.
 */
public class ApplicationManager extends MyManager {
    private final OnAppListChangedListener mListener;
    private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();
    boolean isLoadingApp = false;
    PackageManager manager;
    private ArrayList<ApplicationInfo> mApplications = new ArrayList<ApplicationInfo>();
    HashMap<String, ApplicationInfo> mApplicationMap = new HashMap<String, ApplicationInfo>();
    Collator collator = Collator.getInstance(Locale.CHINESE);


    @Override
    public void saveState() {
        SharedPreferences.Editor e = mPrefs.edit();
        e.commit();

    }


    @Override
    public void loadState() {


    }

    @Override
    public String saveStateToString() {
        return null;
    }

    @Override
    public void loadStateFromString(String string) {

    }



    public ArrayList<ApplicationInfo> getAppList() {
        return mApplications;
    }


    public interface OnAppListChangedListener {
        void onAppListChanged();

        void onFavListChanged();

        void onQuickListChanged();
    }

    float dpi;

    public ApplicationManager(Context context, OnAppListChangedListener listener) {
        super(context);
        mListener = listener;
        manager = mContext.getPackageManager();
        collator.setStrength(Collator.PRIMARY);
        loadState();
        Log.v("DPI", "" + mContext.getResources().getDisplayMetrics().densityDpi);
        dpi = (mContext.getResources().getDisplayMetrics().densityDpi / 160.0f);
        dpi = (float) (dpi * Math.sqrt(dpi) / 2);
        registerIntentReceivers();

    }


    private class ApplicationsIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApp();
        }
    }

    public void loadApp() {
        loadApplications();
        buildAppMap();

        mListener.onAppListChanged();
        mListener.onQuickListChanged();
        mListener.onFavListChanged();
    }


    String getPackageName(Intent i) {

        ResolveInfo mInfo = manager.resolveActivity(i, 0);
        if (mInfo == null) return "com.android";
        return mInfo.activityInfo.applicationInfo.packageName;
    }

    /**
     * Registers various intent receivers. The current implementation registers
     * only a wallpaper intent receiver to let other applications change the
     * wallpaper.
     */
    private void registerIntentReceivers() {
        IntentFilter filter;

        filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        mContext.registerReceiver(mApplicationsReceiver, filter);
    }


    void buildAppMap() {
        mApplicationMap.clear();
        for (int i = 0; i < mApplications.size(); i++) {
            ApplicationInfo info = mApplications.get(i);
            mApplicationMap.put(info.name, info);
        }
    }


    private void loadApplications() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);

        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        int width = 96;//(int) resources.getDimension(android.R.dimen.app_icon_size);
        int height = 96;//(int) resources.getDimension(android.R.dimen.app_icon_size);
        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        mApplications.clear();
        if (apps != null) {
            int count = apps.size();
            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
                application.title = info.loadLabel(manager);
                application.name = info.activityInfo.name;
                application.packageName = info.activityInfo.packageName;
                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = ApplicationInfo.resizeIcon(info.activityInfo.loadIcon(manager), (int) (width * dpi), (int) (height * dpi));
                mApplications.add(application);
            }
        }
        Collections.sort(mApplications, new Comparator<ApplicationInfo>() {
            @Override
            public int compare(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
                return collator.compare(applicationInfo.title, applicationInfo2.title);
            }
        });
    }


}
