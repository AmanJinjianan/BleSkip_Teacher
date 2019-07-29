package com.qixiang.bleskip_teacher.MyView;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.qixiang.bleskip_teacher.R;
import com.qixiang.bleskip_teacher.Util.Utils;

/**
 * Created by Administrator on 2018/8/8.
 */

public class CreateMissionDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;
    private Button btn_cancel,btn_confirm;

    public EditText theValue;
    public TextView tv_mission_type;

    private View.OnClickListener mClickListener;
    public String theType;
    //private AdapterView.OnItemSelectedListener onItemSelect;

    public CreateMissionDialog(Activity context) {
        super(context);
        this.context = context;
    }
    public CreateMissionDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
        //this.onItemSelect = onItemSelect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.create_mission_dialog);

        theValue = (EditText) findViewById(R.id.et_missiondialog_bjrs);

        tv_mission_type = (TextView)findViewById(R.id.tv_mission_type);

        tv_mission_type.setText(theType);
        //sp_mission_type.setOnItemSelectedListener(onItemSelect);

        btn_cancel = (Button) findViewById(R.id.btn_missiondialog_cancle);
        btn_confirm = (Button) findViewById(R.id.btn_missiondialog_confirm);
        btn_cancel.setOnClickListener(mClickListener);
        btn_confirm.setOnClickListener(mClickListener);


        Utils.LogE("selectedflag:5555555555511111111111111111");
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.6); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        //dialogWindow.setBackgroundDrawableResource(R.drawable.success);

        this.setCancelable(true);
    }
    public void SetType(){

        tv_mission_type.setText(theType);
    }
}

