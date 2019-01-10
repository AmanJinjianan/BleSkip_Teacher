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

import com.qixiang.bleskip_teacher.R;

/**
 * Created by Administrator on 2018/8/8.
 */

public class CreateClassDialog extends Dialog {
    /**
     * 上下文对象 *
     */
    Activity context;
    private Button btn_cancel,btn_confirm;

    public EditText className,classCount;
    public Spinner classYear;

    private View.OnClickListener mClickListener;
    private AdapterView.OnItemSelectedListener onItemSelect;

    public CreateClassDialog(Activity context) {
        super(context);
        this.context = context;
    }
    public CreateClassDialog(Activity context, int theme, View.OnClickListener clickListener, AdapterView.OnItemSelectedListener onItemSelect) {
        super(context, theme);
        this.context = context;
        this.mClickListener = clickListener;
        this.onItemSelect = onItemSelect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.create_class_dialog);

        className = (EditText) findViewById(R.id.et_dialog_bjmc);
        classCount = (EditText) findViewById(R.id.et_dialog_bjrs);

        classYear = (Spinner)findViewById(R.id.sp_dialog_year);
        classYear.setOnItemSelectedListener(onItemSelect);

        btn_cancel = (Button) findViewById(R.id.btn_dialog_cancle);
        btn_confirm = (Button) findViewById(R.id.btn_dialog_confirm);
        btn_cancel.setOnClickListener(mClickListener);
        btn_confirm.setOnClickListener(mClickListener);

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
}

