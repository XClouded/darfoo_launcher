package com.darfoo.launcher.ServerAPI;

import com.darfoo.launcher.Util;
import com.darfoo.launcher.ConfigManager.Plugin.CustomModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wsmlby on 9/3/13.
 */
public class SyncAPI {
    public static void uploadSetting(String settings) {
        HashMap<String, String> p=new HashMap<String,String>();
        p.put("settings",settings);
        String result = Util.httpPost("depot/postsettings/",p);
        //System.out.println("post result ===>" + result);
    }

    public static String getSettings() {
        return Util.httpGet("depot/getsettings/");
    }
	public static List<CustomModel> getSSettings() {
		try {
			JSONArray json = new JSONArray(Util.httpGet("depot/customlist/"));
			List<CustomModel> list = new ArrayList<CustomModel>(json.length());
			for (int i = 0; i < json.length(); i++) {
				CustomModel cm = new CustomModel();
				JSONObject jo = json.getJSONObject(i);
				cm.packageName = jo.getString("packageName");
				cm.width = Integer.parseInt(jo.getString("width"));
				cm.height = Integer.parseInt(jo.getString("height"));
				cm.leftMargin = Integer.parseInt(jo.getString("leftMargin"));
				cm.topMargin = Integer.parseInt(jo.getString("topMargin"));
				list.add(cm);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
