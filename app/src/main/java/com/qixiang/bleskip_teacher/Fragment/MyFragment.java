package com.qixiang.bleskip_teacher.Fragment;

import android.support.v4.app.Fragment;
//import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2018/7/24.
 */

public class MyFragment extends Fragment{

    private View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(mView == null){
            mView = inflater.inflate(R.layout.fragment_my,null);
        }
        return mView;
    }
}
