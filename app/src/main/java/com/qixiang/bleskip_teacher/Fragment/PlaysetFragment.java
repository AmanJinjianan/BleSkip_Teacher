package com.qixiang.bleskip_teacher.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.qixiang.bleskip_teacher.ControlMainAct;
import com.qixiang.bleskip_teacher.MainActivity;
import com.qixiang.bleskip_teacher.MyView.Item_Playset;
import com.qixiang.bleskip_teacher.R;

import java.util.ArrayList;

//import android.app.Fragment;

/**
 * Created by Administrator on 2018/7/19.
 */

public class PlaysetFragment extends Fragment {
    View mView;

    private HorizontalScrollView horizontalScrollView;
    public LinearLayout container;
    public Activity context1;
    public Handler theHandler;

    public void setTheHandler(Handler handler){
        theHandler =handler;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_playset,null);
        }

        return mView;
    }

    @Override
    public void onAttach(Activity context) {
        context1 = context;
        super.onAttach(context);
    }

    private String cities[] = new String[]{"London", "Bangkok", "Paris", "Dubai", "Istanbul", "New York",
            "Singapore", "Kuala Lumpur", "Hong Kong", "Tokyo", "Barcelona",
            "Vienna", "Los Angeles", "Prague", "Rome", "Seoul", "Mumbai", "Jakarta",
            "Berlin", "Beijing", "Moscow", "Taipei", "Dublin", "Vancouver"};
    private ArrayList<String> data = new ArrayList<>();
    @Override
    public void onResume() {
        //Collections.addAll(data, cities);
        horizontalScrollView = (HorizontalScrollView) context1.findViewById(R.id.horizontalScrollView);
        container = (LinearLayout) context1.findViewById(R.id.horiziner334);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((ControlMainAct.width-240)/3,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(40, 10, 40, 10);

        for (int i = 0; i < 6; i++) {
            Item_Playset ip = new Item_Playset(context1,i,(ControlMainAct.width-240)/3,theHandler);
            ip.setLayoutParams(layoutParams);
            container.addView(ip);
            container.invalidate();
        }
        super.onResume();
    }
}


