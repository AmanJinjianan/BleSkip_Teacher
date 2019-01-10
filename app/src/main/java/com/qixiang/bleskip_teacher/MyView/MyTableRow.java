package com.qixiang.bleskip_teacher.MyView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.Util.Utils;

/**
 * Created by Administrator on 2018/7/26.
 */

public class MyTableRow extends TableRow{

    private String name;
    private int sex;//true :男，false :女
    private int mark;
    private int state;//true:待加入， false:已加入

    public MyTableRow(Context context,String name,int sex,int mark,int state){

        this(context,null,name,sex,mark,state);
    }

    public MyTableRow(Context context, AttributeSet attrs,String name,int sex,int mark,int state){
        super(context, attrs);

            //创建显示的内容,这里创建的是一列
            Button button2 = new Button(context);
            button2.setText(name);
            button2.setBackgroundColor(Color.WHITE);
            TableRow.LayoutParams klj2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1);
            klj2.setMargins(1,1,1,1);
            button2.setLayoutParams(klj2);
            super.addView(button2);

        //创建显示的内容,这里创建的是一列
        TextView textView3 = new TextView(context);
        if(sex == 1)
            textView3.setText("男");
        else
            textView3.setText("女");
        textView3.setBackgroundColor(Color.WHITE);
        TableRow.LayoutParams klj3 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1);
        klj3.setMargins(1,1,1,1);
        textView3.setLayoutParams(klj3);
        super.addView(textView3);

        //创建显示的内容,这里创建的是一列
        TextView textView4 = new TextView(context);
        textView4.setText(String.valueOf(mark));
        textView4.setBackgroundColor(Color.WHITE);
        TableRow.LayoutParams klj4 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1);
        klj4.setMargins(1,1,1,1);
        textView4.setLayoutParams(klj4);
        super.addView(textView4);

        Button button = new Button(context);
        if(state == 0)
            button.setText("待加入");
        else{
            button.setEnabled(false);
            button.setText("已加入");
        }
        TableRow.LayoutParams klj5 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1);
        klj5.setMargins(1,1,1,1);
        button.setLayoutParams(klj5);
        super.addView(button);

    }

}
