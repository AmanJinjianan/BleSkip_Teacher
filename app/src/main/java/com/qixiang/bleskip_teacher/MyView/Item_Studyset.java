package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Studyset extends RelativeLayout{
    public Item_Studyset(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_studyset, this);
    }

}
