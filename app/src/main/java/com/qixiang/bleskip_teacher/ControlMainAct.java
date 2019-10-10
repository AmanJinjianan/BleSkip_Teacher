package com.qixiang.bleskip_teacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Fragment.ClassFragment;
import com.qixiang.bleskip_teacher.Fragment.CodesetFragment;
import com.qixiang.bleskip_teacher.Fragment.ControlsetFragment;
import com.qixiang.bleskip_teacher.Fragment.MyFragment;
import com.qixiang.bleskip_teacher.Fragment.PlaysetFragment;
import com.qixiang.bleskip_teacher.Fragment.StudysetFragment;
import com.qixiang.bleskip_teacher.Fragment.TeachFragment;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapter;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterMission;
import com.qixiang.bleskip_teacher.ViewAdapter.ViewPagerFragmentAdapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControlMainAct extends AppCompatActivity implements  View.OnClickListener {

    public WindowManager wm;
    public DisplayMetrics dm;
    public static int width;         // 屏幕宽度（像素）
    public static int height;       // 屏幕高度（像素）

    private HorizontalScrollView horizontalScrollView;
    public LinearLayout container;
    GradientDrawable firstLinearLayoutGD,secondLinearlayoutGD,threeLinearLayoutGD,fourLinearLayoutGD,fiveLinearLayoutGD;

    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    FragmentManager mFragmentManager;
    public LinearLayout firstLinearLayout,secondLinearlayout,threeLinearLayout,fourLinearLayout,fiveLinearLayout;

    ViewPager mViewpager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;

    TextView titleTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control_main);


//        Context mContext = this;
//        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
//        float density = mContext.getResources().getDisplayMetrics().density;
//        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
//        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
//        int width = mContext.getResources().getDisplayMetrics().widthPixels;
//        int height = mContext.getResources().getDisplayMetrics().heightPixels;
//        // 这样可以计算屏幕的物理尺寸
//        float width2 = width / xdpi;
//        float height2 = height / ydpi;
        //得到屏幕尺寸信息
        dm = new DisplayMetrics();
        wm = (WindowManager) ControlMainAct.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;



        mFragmentManager = getSupportFragmentManager();

        Fragment pf = new PlaysetFragment();
        Fragment cf = new ControlsetFragment();
        Fragment sf = new StudysetFragment();
        Fragment csf = new CodesetFragment();

        mFragmentList.add(pf);
        mFragmentList.add(cf);
        mFragmentList.add(sf);
        mFragmentList.add(csf);

        mViewPagerFragmentAdapter =   new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        initView();

        //firstLinearLayout.setSelected(true);
    }
    private void initView() {

        findViewById(R.id.btn_my).setOnClickListener(this);
        titleTextView = (TextView) findViewById(R.id.ViewTitle);
        mViewpager = (ViewPager) findViewById(R.id.ViewPagerLayout);
        firstLinearLayout = (LinearLayout)findViewById(R.id.firstLinearLayout);
        firstLinearLayout.setOnClickListener(this);
        secondLinearlayout = (LinearLayout)findViewById(R.id.secondLinearLayout);
        secondLinearlayout.setOnClickListener(this);
        threeLinearLayout = (LinearLayout)findViewById(R.id.threeLinearLayout);
        threeLinearLayout.setOnClickListener(this);
        fourLinearLayout = (LinearLayout)findViewById(R.id.fourthLinearLayout);
        fourLinearLayout.setOnClickListener(this);
        fiveLinearLayout = (LinearLayout)findViewById(R.id.fivthLinearLayout);
        fiveLinearLayout.setOnClickListener(this);

        firstLinearLayoutGD = (GradientDrawable) firstLinearLayout.getBackground();
        secondLinearlayoutGD = (GradientDrawable) secondLinearlayout.getBackground();
        threeLinearLayoutGD = (GradientDrawable) threeLinearLayout.getBackground();
        fourLinearLayoutGD = (GradientDrawable) fourLinearLayout.getBackground();
        fiveLinearLayoutGD = (GradientDrawable) firstLinearLayout.getBackground();

        mViewpager.addOnPageChangeListener(new ViewPagerOnPagerChangedLisenter());
        mViewpager.setAdapter(mViewPagerFragmentAdapter);
        mViewpager.setCurrentItem(0);
        firstLinearLayoutGD.setColor(Color.parseColor("#49BAC8"));
        updateBottomLinearLayoutSelect(true,false,false,false);
    }

    private void updateBottomLinearLayoutSelect(boolean f, boolean s, boolean t,boolean q) {
        firstLinearLayout.setSelected(f);
        secondLinearlayout.setSelected(s);
        threeLinearLayout.setSelected(t);
        fourLinearLayout.setSelected(q);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstLinearLayout:
                mViewpager.setCurrentItem(0);
                updateBottomLinearLayoutSelect(true,false,false,false);
                break;
            case R.id.secondLinearLayout:
                mViewpager.setCurrentItem(1);
                updateBottomLinearLayoutSelect(false,true,false,false);
                break;
            case R.id.threeLinearLayout:
                mViewpager.setCurrentItem(2);
                updateBottomLinearLayoutSelect(false,false,true,false);
                break;
            case R.id.fourthLinearLayout:
                mViewpager.setCurrentItem(3);
                updateBottomLinearLayoutSelect(false,false,false,true);
                break;
            case  R.id.btn_my:
                Toast.makeText(ControlMainAct.this,"未开发",Toast.LENGTH_SHORT).show();
                break;
            default:


                break;
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
    }

    private void SwitchColor(int position){
        firstLinearLayoutGD.setColor(Color.parseColor("#FC4E57"));
        secondLinearlayoutGD.setColor(Color.parseColor("#FC4E57"));
        threeLinearLayoutGD.setColor(Color.parseColor("#FC4E57"));
        fourLinearLayoutGD.setColor(Color.parseColor("#FC4E57"));
        fiveLinearLayoutGD.setColor(Color.parseColor("#FC4E57"));
        switch (position){
            case 0:firstLinearLayoutGD.setColor(Color.parseColor("#2E43C0"));break;
            case 1:secondLinearlayoutGD.setColor(Color.parseColor("#41349e"));break;
            case 2:threeLinearLayoutGD.setColor(Color.parseColor("#00bbc7"));break;
            case 3:fourLinearLayoutGD.setColor(Color.parseColor("#12b961"));break;
            case 4:fiveLinearLayoutGD.setColor(Color.parseColor("#49BAC8"));break;
        }
    }
    class ViewPagerOnPagerChangedLisenter implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            boolean[] state = new boolean[4];
            state[position] = true;
            //titleTextView.setText(titleName[position]);
            SwitchColor(position);
            updateBottomLinearLayoutSelect(state[0],state[1],state[2],state[3]);
//            Log.e(Utils.TAG,"position:"+position+" Tools.connectedFlag:"+ Tools.connectedFlag);
            if(position == 1)
            {

                //initPlaySet();
                if(Tools.connectedFlag){
                    //InitTeach();
                }else {

                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
