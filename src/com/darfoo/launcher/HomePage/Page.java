package com.darfoo.launcher.HomePage;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.darfoo.launcher.Constant;
import com.darfoo.launcher.R;

/**
 * Created by wsmlby on 13-8-14.
 */
public class Page extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ViewGroup mRoot;
    int width;
    int height;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        Log.i("PAGE","CREATEDVIEW"+args.getInt(Constant.EXTRA_PAGE_ID));
        View rootView = inflater.inflate(R.layout.page, container, false);
        mRoot = (ViewGroup) rootView;


        mRoot.post(new Runnable() {
            @Override
            public void run() {
                width=mRoot.getWidth();
                height=mRoot.getHeight();

            }
        });
        return rootView;
    }
    int x=1,y=1;
    public void addWidget(View v, int left, int top, int w, int h) {
        mRoot.addView(v);

        RelativeLayout.LayoutParams lp=(LayoutParams) v.getLayoutParams();
        lp.height=h*width/5;
        lp.width=w*width/5;
        lp.leftMargin=left*width/5;
        lp.topMargin=top*width/5;
        v.setLayoutParams(lp);
        
    }
}
