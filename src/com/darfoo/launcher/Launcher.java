package com.darfoo.launcher;


import android.animation.Animator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.darfoo.launcher.ConfigManager.Plugin.CustomModel;
import com.darfoo.launcher.ConfigManager.Plugin.OnPluginLoadedListener;
import com.darfoo.launcher.ConfigManager.Plugin.PluginFactoryHelper;
import com.darfoo.launcher.ConfigManager.Plugin.PluginHelper;
import com.darfoo.launcher.ConfigManager.Plugin.PluginLoader;
import com.darfoo.launcher.ConfigManager.Plugin.PluginModel;
import com.darfoo.launcher.ConfigManager.PluginManager;
import com.darfoo.launcher.ConfigManager.WorkSpace;
import com.darfoo.launcher.ServerAPI.PluginAPI;
import com.darfoo.launcher.ServerAPI.SyncAPI;

import java.util.List;


public class Launcher extends FragmentActivity implements OnPluginLoadedListener, ViewPager.OnPageChangeListener {
    ViewPager mPages;
    public static int pagenumber=1;
    private boolean customResource = false;
    private Resources mRes;
    public PluginManager mPluginManager;
    private WorkSpace mWorkspace;
    private View mPositionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000ff")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000ff")));
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        setContentView(R.layout.launcher_main);
        mPages = (ViewPager) findViewById(R.id.pager);
        mWorkspace = new WorkSpace(this, getSupportFragmentManager(), mPages);
        mPluginManager = new PluginManager(this);
        mPages.setOnPageChangeListener(this);
        mPositionBar=findViewById(R.id.positionbar);

    }

    @Override
    public void onPluginLoaded(PluginLoader loader, PluginModel plugin, CustomModel cplugin) {
        try {

            PluginHelper mPlugin = PluginFactoryHelper.newInstance(loader.loadClass(plugin.className));
            setResource(loader.getResource());

            View v = mPlugin.createView(this);
            if (cplugin == null)
                cplugin = mWorkspace.findPostion();
            cplugin.packageName=plugin.packageName;

            cplugin.helper=mPlugin;
            mWorkspace.addWidget(v, cplugin);
            Log.i("AddWidget", "Add to workspace");
            mPlugin.initView(cplugin.data);

            resetResource();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }
    void refreshBar(){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mPositionBar.getLayoutParams();

        lp.width=mPages.getWidth()/pagenumber;

        lp.leftMargin=mPages.getCurrentItem()*lp.width;
        mPositionBar.setLayoutParams(lp);
        pos=mPages.getCurrentItem();
    }
    int pos;
    @Override
    public void onPageScrollStateChanged(int i) {
            switch (i){
                case ViewPager.SCROLL_STATE_DRAGGING:
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    refreshBar();/*
                    mPositionBar.setAnimation(new Animation() {

                    });
                    mPositionBar.animate().translationX((pos-mPages.getCurrentItem())*mPages.getWidth()/3).setDuration(1000).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });*/

                    break;
                case ViewPager.SCROLL_STATE_SETTLING:



                    break;
            }

    }

    class myLayoutInflater extends LayoutInflater {


        @Override
        public View inflate(int resource, ViewGroup root) {
            return super.inflate(resource, root);
        }

        protected myLayoutInflater(LayoutInflater original, Context newContext) {
            super(original, newContext);
        }

        @Override
        public LayoutInflater cloneInContext(Context context) {
            return null;
        }
    }

    void setResource(Resources res) {
        customResource = true;
        mRes = res;
    }

    void resetResource() {
        customResource = false;
    }

    @Override
    public Resources getResources() {
        if (customResource) return mRes;
        return super.getResources();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

       mPages.post(new Runnable() {
            @Override
            public void run() {
           /*     PluginModel plugin = new PluginModel();
                plugin.className = "com.darfoo.launcher.plugins.Plugins.MyPlugin1";
                plugin.packageName = "com.darfoo.launcher.plugins.Plugins.MyPlugin1";
                plugin.url = "http://192.168.0.109:8080/MainActivity.apk";
                mPluginManager.loadPlugin(plugin, Launcher.this);*/

                refreshBar();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                addWidgetStart();
                break;
            case R.id.action_upload:
                uploadSettings();
                break;
            case R.id.action_download:
                downloadSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addWidgetStart() {

        Log.i("AddWidget", "start");
        if (mPluginManager.getPluginList() == null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Getting List");
            dialog.show();

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    mPluginManager.loadPluginList();
                    return null;
                }

                @Override
                protected void onPostExecute(Void pluginModels) {
                    dialog.dismiss();

                    showWidgetList();

                }
            }.execute();
        } else {
        	//所以当插件已经加载过之后就不会再显示progressbar了
            showWidgetList();
        }
    }

    private void showWidgetList() {
        final List<PluginModel> pluginlist = mPluginManager.getPluginList();
        String[] strings = new String[pluginlist.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = pluginlist.get(i).className;
        }
        new AlertDialog.Builder(Launcher.this).setTitle("Choose Widget").setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.i("AddWidget","listclicked");
                mPluginManager.loadPlugin(pluginlist.get(i), null, Launcher.this);
            }
        }).create().show();
    }

    private void downloadSettings() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return SyncAPI.getSettings();
            }

            @Override
            protected void onPostExecute(String aVoid) {
                mWorkspace.loadStateFromString(aVoid);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void uploadSettings() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                SyncAPI.uploadSetting(mWorkspace.saveStateToString());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(Launcher.this, "Upload Completed", Toast.LENGTH_SHORT).show();
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}
