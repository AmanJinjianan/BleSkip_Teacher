package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.ControlMainAct;
import com.qixiang.bleskip_teacher.ControllerActivity;
import com.qixiang.bleskip_teacher.PKActivity;
import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2019/10/4.
 */

public class Item_Controlset extends RelativeLayout{
    private Context theContext;
    public Item_Controlset(Context context,int s,int width) {
        super(context);
        theContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_controlset, this);

        ImageButton ib = (ImageButton)findViewById(R.id.ib_control_center);
        ImageView iv = (ImageView)findViewById(R.id.iv_control_center);
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) ib.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

        linearParams.width = width-40;
        linearParams.height = (int)(linearParams.width*1.04);
        ib.setLayoutParams(linearParams);
        switch (s){
            case 0: ib.setBackgroundResource(R.drawable.btn_control1);
                    ib.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(theContext, ControllerActivity.class);
                            theContext.startActivity(intent);
                        }
                    });
                    iv.setBackgroundResource(R.drawable.pic_col_zi1);
                    break;
            case 1: ib.setBackgroundResource(R.drawable.btn_control2);
                ib.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(theContext, ControllerActivity.class);
                        //theContext.startActivity(intent);
                        Toast.makeText(theContext,"未开发",Toast.LENGTH_SHORT).show();
                    }
                });
                    iv.setBackgroundResource(R.drawable.pic_col_zi2);
                break;
            case 2: ib.setBackgroundResource(R.drawable.btn_control3);
                ib.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(theContext, PKActivity.class);
                        theContext.startActivity(intent);
                    }
                });
                    iv.setBackgroundResource(R.drawable.pic_col_zi3);
                break;
        }
    }

}
