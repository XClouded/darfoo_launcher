package com.darfoo.launcher.ConfigManager.Plugin;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomModel {

    public static final String PACKAGE_NAME = "packageName";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String LEFT_MARGIN = "leftMargin";
    public static final String TOP_MARGIN = "topMargin";
    public static final String PAGE = "page";
    public static final String DATA = "data";
    public String packageName;
	public int width=3;
	public int height=4;
    public int page=1;
	public int leftMargin=1;
	public int topMargin=2;
    public PluginHelper helper;
    public String data;

    public JSONObject toJsonObj() {
        JSONObject jo = new JSONObject();
        try {
            jo.put(PACKAGE_NAME,packageName);
            jo.put(WIDTH,width);
            jo.put(HEIGHT,height);
            jo.put(TOP_MARGIN,topMargin);
            jo.put(LEFT_MARGIN,leftMargin);
            jo.put(PAGE,page);
            jo.put(DATA,helper.getData());
            return  jo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static CustomModel fromJsonObj(JSONObject jo){
         CustomModel cm=new CustomModel();

        try {
            cm.packageName = jo.getString(PACKAGE_NAME);
            cm.width = Integer.parseInt(jo.getString(WIDTH));
            cm.height = Integer.parseInt(jo.getString(HEIGHT));
            cm.leftMargin = Integer.parseInt(jo.getString(LEFT_MARGIN));
            cm.topMargin = Integer.parseInt(jo.getString(TOP_MARGIN));
            cm.page=Integer.parseInt(jo.getString(PAGE));
            cm.data=null;
            try {
                cm.data=jo.getString(DATA);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return cm;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}
