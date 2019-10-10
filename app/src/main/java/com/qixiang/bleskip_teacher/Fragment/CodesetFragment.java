package com.qixiang.bleskip_teacher.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.CodingActivity;
import com.qixiang.bleskip_teacher.ControlMainAct;
import com.qixiang.bleskip_teacher.MyView.Item_Studyset;
import com.qixiang.bleskip_teacher.R;

//import android.app.Fragment;

/**
 * Created by Administrator on 2018/7/19.
 */

public class CodesetFragment extends Fragment {
    View mView;

    private HorizontalScrollView horizontalScrollView;
    public LinearLayout container;
    public Activity context1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_codeset,null);
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
//        horizontalScrollView = (HorizontalScrollView) context1.findViewById(R.id.horizontalScrollViewstudy);
//        container = (LinearLayout) context1.findViewById(R.id.horizinercstudy);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((ControlMainAct.width-120)/3,ViewGroup.LayoutParams.MATCH_PARENT);
//        layoutParams.gravity = Gravity.CENTER;
//        layoutParams.setMargins(20, 10, 20, 10);
//
//
//        for (int i = 0; i < 3; i++) {
//            Item_Studyset ip = new Item_Studyset(context1);
//            ip.setLayoutParams(layoutParams);
//            container.addView(ip);
//            container.invalidate();
//        }

        context1.findViewById(R.id.ib_code_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context1,"hahah",Toast.LENGTH_SHORT).show();
                Intent intent   = new Intent(context1, CodingActivity.class);
                startActivity(intent);
            }
        });
        super.onResume();
    }
}


