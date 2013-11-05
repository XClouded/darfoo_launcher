package com.darfoo.launcher.ConfigManager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.darfoo.launcher.ConfigManager.Plugin.CustomModel;
import com.darfoo.launcher.ConfigManager.Plugin.OnPluginLoadedListener;
import com.darfoo.launcher.ConfigManager.Plugin.PluginLoader;
import com.darfoo.launcher.ConfigManager.Plugin.PluginModel;
import com.darfoo.launcher.Launcher;
import com.darfoo.launcher.ServerAPI.PluginAPI;
import com.darfoo.launcher.Util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wsmlby on 16/08/13.
 */
public class PluginManager extends MyManager {
    HashMap<String,PluginLoader> loaders;
    public PluginManager(Context context) {
        super(context);
        loaders=new HashMap<String, PluginLoader>();
    }


    public void loadPlugin(final PluginModel plugin,final CustomModel customModel, final OnPluginLoadedListener listener) {
        Log.i("AddWidget", "CheckLoaded");
        PluginLoader loader=loaders.get(plugin.packageName);
        if(loaders.get(plugin.packageName)!=null){
            listener.onPluginLoaded(loader, plugin,customModel);
            return;
        }
        String fn = plugin.packageName + ".apk";
        String fullPath = mContext.getFilesDir().getPath() + "/" + fn;
        File p = new File(fullPath);
        Log.i("AddWidget", "checkExisted");
        if (!p.isFile()) {
            new Util.DownloadTask(mContext, plugin.url, fn) {
                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                    loadPlugin(plugin, customModel,listener);
                }
            }.execute();
            return;
        }
        Log.i("AddWidget", "LoadingInTO");
        File dexOutputDir = mContext.getDir("dexed", 0);
        loader= new PluginLoader(mContext, fullPath, dexOutputDir.getAbsolutePath(), null, mContext.getClassLoader());
        listener.onPluginLoaded(loader, plugin,customModel);
    }


    @Override
    public void saveState() {

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


    public void loadPlugin(final String packageName,final CustomModel customModel,  final OnPluginLoadedListener listener) {
        Log.i("AddWidget", "checkPackageName");
        if(plugins==null)
        {
            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    loadPluginList();
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    loadPlugin(packageName, customModel, listener);
                }
            }.execute();
            return;
        }
        PluginModel p=plugins.get(packageName);
        if(p==null){
            Log.i("AddWidget", "PluginNoFound");
            Toast.makeText(mContext,"Plugin Not Found!",Toast.LENGTH_SHORT).show();
            return;
        }
        loadPlugin(p,customModel,listener);

    }
    List<PluginModel> pluginlist=null;
    Map<String,PluginModel> plugins;
    public void loadPluginList() {
        Log.i("AddWidget","getlist");
        pluginlist= PluginAPI.getPluginList();
        plugins=new HashMap<String, PluginModel>();
        Iterator<PluginModel> i = pluginlist.iterator();
        while(i.hasNext()){
            PluginModel p=i.next();
            plugins.put(p.packageName,p);
        }
        Log.i("AddWidget","listdone");
    }

    public List<PluginModel> getPluginList() {
        return pluginlist;
    }
}
