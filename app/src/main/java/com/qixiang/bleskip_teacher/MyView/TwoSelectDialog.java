package com.qixiang.bleskip_teacher.MyView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2018/8/22.
 */

public class TwoSelectDialog extends Dialog {

    Activity context;
    int num;
    public Button btn_up,btn_down;

    private TextView tv_Num;
    public TwoSelectDialog(Activity context,int num) {
        super(context);
        this.context = context;
        this.num = num;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_twobutton);

        btn_up = (Button)findViewById(R.id.btn_upselect);
        btn_down = (Button)findViewById(R.id.btn_downselect);
//        tv_Num = findViewById(R.id.tv_countdownnum);
//        tv_Num.setText(String.valueOf(num));
//        tv_Num.setTextSize(60);
          /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getWidth() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.4); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        //dialogWindow.setBackgroundDrawableResource(R.drawable.success);

        this.setCancelable(true);
    }
}
