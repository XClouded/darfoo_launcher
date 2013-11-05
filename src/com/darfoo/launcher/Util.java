package com.darfoo.launcher;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by wsmlby on 05/08/13.
 */
public class Util {
    public static String IntArrayToString(List<Integer> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i)).append(",");
        }
        return str.toString();
    }

    public static String StringArrayToString(List<String> list) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i)).append("|");
        }
        return str.toString();
    }

    public static int[] StringToIntArray(String string) {
        StringTokenizer st = new StringTokenizer(string, ",");
        int[] savedList = new int[st.countTokens()];
        for (int i = 0; i < savedList.length; i++) {
            savedList[i] = Integer.parseInt(st.nextToken());
        }

        return savedList;
    }

    public static String[] StringToStringArray(String string) {
        StringTokenizer st = new StringTokenizer(string, "|");
        String[] savedList = new String[st.countTokens()];
        for (int i = 0; i < savedList.length; i++) {
            savedList[i] = st.nextToken();
        }

        return savedList;
    }

    public static String httpGet(String url) {

        HttpGet get = new HttpGet(Constant.ServerBase + url);
        HttpClient c = new DefaultHttpClient();
        try {
            HttpResponse response = c.execute(get);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String data = "";

            while ((data = br.readLine()) != null) {
                sb.append(data);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String httpPost(String url, String datas) {
        try {
            return httpPost(url, new StringEntity(datas));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpPost(String url, HttpEntity entity) {
        HttpPost post = new HttpPost(Constant.ServerBase + url);
        HttpClient c = new DefaultHttpClient();

        try {

            post.setEntity(entity);
            HttpResponse response = c.execute(post);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String data = "";

            while ((data = br.readLine()) != null) {
                sb.append(data);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String httpPost(String url, Map<String, String> datas) {
        ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> i = datas.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry<String, String> p = i.next();
            parameters.add(new BasicNameValuePair(p.getKey(), p.getValue()));
        }
        try {
            return httpPost(url, new UrlEncodedFormEntity(parameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean downloadtoFile(Context mContext, String url,
                                         String filename) {
        InputStream inputStream = null;
        try {
            byte[] buffer = new byte[4 * 1024];
            OutputStream outStream = mContext.openFileOutput(filename,
                    Context.MODE_PRIVATE);
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(
                    url).openConnection();
            inputStream = httpURLConnection.getInputStream();
            int temp;
            while ((temp = (inputStream.read(buffer))) != -1) {
                outStream.write(buffer, 0, temp);
            }
            outStream.flush();
            outStream.close();
            inputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class DownloadTask extends AsyncTask<Void, Void, Boolean> {
        private final Context mContext;
        private String mUrl, mFilename;

        public DownloadTask(Context context, String url, String filename) {
            super();
            mUrl = url;
            mFilename = filename;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return downloadtoFile(mContext, mUrl, mFilename);
        }
    }

    ;

}
