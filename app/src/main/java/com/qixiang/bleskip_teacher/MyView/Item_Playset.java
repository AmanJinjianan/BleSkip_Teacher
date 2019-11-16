package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qixiang.bleskip_teacher.R;

import android.os.Handler;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Playset extends RelativeLayout{
    public Handler myHandler;
    public Item_Playset(Context context, int s, int width, Handler theHandler) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_playset, this);
        myHandler = theHandler;
        ImageButton ib = (ImageButton)findViewById(R.id.iv_center_play);
        ImageView iv = (ImageView)findViewById(R.id.iv_control_center);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ib.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

//        linearParams.width = width-40;
//        linearParams.height = (int)(linearParams.width*1.04);
//        ib.setLayoutParams(linearParams);

        switch (s){
            case 0: ib.setBackgroundResource(R.drawable.pic_item_ble1);
                ib.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(myHandler != null){
                            myHandler.sendEmptyMessage(4601);
                        }
                    }
                });

                break;
            case 1: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
            case 2: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item3);break;
            case 3: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
            case 4: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item3);break;
            case 5: findViewById(R.id.iv_center_play).setBackgroundResource(R.drawable.pic_item2);break;
        }
    }

}
