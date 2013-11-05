package com.darfoo.launcher.ConfigManager.Plugin;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wsmlby on 13-8-19.
 */
public class PluginHelper {

    private final Object mObj;

    Method createView,initView,getData;

    public PluginHelper(Object o, Class<?> mClass) {
        mObj = o;
        try {
            createView = mClass.getMethod("createView", Activity.class);
            createView.setAccessible(true);
            initView=mClass.getMethod("initView",String.class);
            initView.setAccessible(true);
            getData=mClass.getMethod("getData");
            getData.setAccessible(true);


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public View createView(Activity context) {
        try {
            return (View) createView.invoke(mObj, context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void initView(String data){
        if(initView==null)return;
        try {
            initView.invoke(mObj,data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public String getData(){
        if(getData==null)return null;
        try {
            return (String) getData.invoke(mObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
