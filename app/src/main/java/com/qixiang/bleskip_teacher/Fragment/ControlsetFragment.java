package com.qixiang.bleskip_teacher.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.qixiang.bleskip_teacher.ControlMainAct;
import com.qixiang.bleskip_teacher.MyView.Item_Controlset;
import com.qixiang.bleskip_teacher.MyView.Item_Playset;
import com.qixiang.bleskip_teacher.R;

import java.util.ArrayList;

//import android.app.Fragment;

/**
 * Created by Administrator on 2018/7/19.
 */

public class ControlsetFragment extends Fragment {
    View mView;

    private HorizontalScrollView horizontalScrollView;
    public LinearLayout container;
    public Activity context1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_controlset,null);
        }

        return mView;
    }

    @Override
    public void onAttach(Activity context) {
        context1 = context;
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        //Collections.addAll(data, cities);
        horizontalScrollView = (HorizontalScrollView) context1.findViewById(R.id.horizontalScrollViewcontrol);
        container = (LinearLayout) context1.findViewById(R.id.horizinercontrol);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((ControlMainAct.width-120)/3,ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(20, 10, 20, 10);

        for (int i = 0; i < 3; i++) {
            Item_Controlset ip = new Item_Controlset(context1,i,(ControlMainAct.width-120)/3);
            ip.setLayoutParams(layoutParams);
            container.addView(ip);
            container.invalidate();
        }
        super.onResume();
    }
}


