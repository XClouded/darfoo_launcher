package com.darfoo.launcher.ConfigManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.darfoo.launcher.ConfigManager.Plugin.CustomModel;
import com.darfoo.launcher.Constant;
import com.darfoo.launcher.HomePage.Page;
import com.darfoo.launcher.Launcher;
import com.darfoo.launcher.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wsmlby on 9/4/13.
 */
public class WorkSpace extends MyManager{
    Page[] pages;
    Launcher mLauncher;
    public WorkSpace(Launcher mContext,FragmentManager supportFragmentManager, ViewPager mPages) {
        super(mContext);
        mLauncher=mContext;
        mPages.setAdapter(new MyPageAdapter(supportFragmentManager));
        mPages.setCurrentItem(0);
    }

    List<CustomModel> mPluginList=new ArrayList<CustomModel>();
    public void addWidget(View v,CustomModel model) {
        mPluginList.add(model);
        pages[model.page].addWidget(v, model.leftMargin, model.topMargin, model.width, model.height);
    }

    @Override
    public void saveState() {

    }

    @Override
    public void loadState() {

    }

    @Override
    public String saveStateToString() {
        JSONArray json=new JSONArray();
        Iterator<CustomModel> i = mPluginList.iterator();
        while(i.hasNext()){
            json.put(i.next().toJsonObj());
        }
        return  json.toString();
    }

    @Override
    public void loadStateFromString(final String string) {
        new AsyncTask<Void, Void, List<CustomModel>>() {
            @Override
            protected List<CustomModel> doInBackground(Void... voids) {
                try {
                    JSONArray json = new JSONArray(string);
                    List<CustomModel> list = new ArrayList<CustomModel>(json.length());
                    for (int i = 0; i < json.length(); i++) {
                        CustomModel cm = new CustomModel();
                        JSONObject jo = json.getJSONObject(i);
                        cm=CustomModel.fromJsonObj(jo);
                        if(cm!=null)
                        list.add(cm);
                    }
                    return  list;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<CustomModel> customModels) {
                super.onPostExecute(customModels);
                Iterator<CustomModel> i = customModels.iterator();
                while(i.hasNext()){
                   CustomModel c=i.next();
                    mLauncher.mPluginManager.loadPlugin(c.packageName,c,mLauncher);
                }

            }
        }.execute();



    }
    int page_t=0,col_t=0;
    public CustomModel findPostion() {
        if(col_t<4)
            col_t+=1;
        else
        {
            col_t=0;
            page_t+=1;
        }
        CustomModel c = new CustomModel();
        c.height=4;
        c.topMargin=col_t;
        c.page=page_t;
        return c;
    }

    class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            pages = new Page[Launcher.pagenumber];
            for (int i = 0; i < pages.length; i++) {
                Page fragment = new Page();
                Bundle args = new Bundle();
                args.putInt(Constant.EXTRA_PAGE_ID, (i));
                fragment.setArguments(args);
                pages[i] = fragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "Page " + (position);
        }

        @Override
        public Fragment getItem(int i) {
            return pages[i];
        }

        @Override
        public int getCount() {
            return pages.length;
        }
    }
}
