package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Playset extends RelativeLayout{
    public Item_Playset(Context context,int s) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_playset, this);
        switch (s){
            case 0: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item_ble1);break;
            case 1: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
            case 2: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item3);break;
            case 3: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
            case 4: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item3);break;
            case 5: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
        }
    }

}
