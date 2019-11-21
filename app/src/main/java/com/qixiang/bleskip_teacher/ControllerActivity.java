package com.qixiang.bleskip_teacher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.MyListener;
import com.qixiang.bleskip_teacher.BLE.SendBle;
import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class ControllerActivity extends Activity implements View.OnClickListener{

    private MyTimerTask mt12,mt34;
    private Timer timer12,timer34;
    //指令参数
    byte[] commandTwoBytes = new byte[2];
    public SeekBar theSeek;
    public boolean reveiveFlag = false;
    private Intent intent = new Intent("CONTROLLERDATA");
    String data = "";
    private SendBle mSendBle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.layout_control);
        reveiveFlag = false;

        initView();

        theSeek.setProgressDrawable(null);
        theSeek.setProgress(50);
        theSeek.setOnSeekBarChangeListener(sChange);

        reveiveFlag = false;
        mSendBle = new SendBle(this);
    }
    void initView(){
        findViewById(R.id.btn_more_control).setOnClickListener(this);
        findViewById(R.id.ib_control_back).setOnClickListener(this);

        findViewById(R.id.btn_right_control).setOnTouchListener(MyTai);
        findViewById(R.id.btn_left_control).setOnTouchListener(MyTai);

        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams((int)(ControlMainAct.height*1.77),ControlMainAct.height);
        ll.gravity = Gravity.CENTER_HORIZONTAL;
        findViewById(R.id.ll_control_center).setLayoutParams(ll);
        theSeek = (SeekBar) findViewById(R.id.seekBar3);
    }
    void setFullScreen(){
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得当前窗体对象
        Window window = ControllerActivity.this.getWindow();
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    private SeekBar.OnSeekBarChangeListener sChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress<47){//前进
                //commandTwoBytes[0] = 0x1A;
                //将seekbar的波动范围映射成0x10-0x1F 十六个档位
                commandTwoBytes[0] = (byte)(0x10 | Utils.intToButeArray(Math.abs(46-progress)/3)[1]);
                if(timer12 == null){
                    timer12=new Timer();
                    mt12 = new MyTimerTask();
                    mt12.MyFlag = 1;
                    timer12.schedule(mt12,0,200);
                }
            }else if(progress>54){//后退
                //commandTwoBytes[0] = 0x2A;
                //将seekbar的波动范围映射成0x20-0x2F 十六个档位
                commandTwoBytes[0] = (byte)(0x20 | Utils.intToButeArray(Math.abs(55-progress)/3)[1]);
                if(timer12 == null){
                    timer12=new Timer();
                    mt12 = new MyTimerTask();
                    mt12.MyFlag = 2;
                    timer12.schedule(mt12,0,200);
                }
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Utils.LogE("progress:  onStartTrackingTouch");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Utils.LogE("progress:  onStopTrackingTouch");
            theSeek.setProgress(50);
            if(timer12 != null){
                commandTwoBytes[0] = 0;
                intent.putExtra("data",commandTwoBytes);
                sendBroadcast(intent);
                timer12.cancel();
                //stopSendData();
                mSendBle.stopSend();
                timer12 = null;
            }

        }
    };
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }


    public MyListener listener = new MyListener() {
        @Override
        public void readData(byte[] var1, String var2) {}

        @Override
        public void onSendStatus(Boolean var1) {
            Tools.setLog("log1", "..........................onSendStatus......");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more_control:
                reveiveFlag = true;
                //maxSendData("0000810000000000",(byte)0xFF);
                break;
            case R.id.btn_left_control:
                //maxSendData("00003A0000000000",(byte)0x03);
                break;
            case R.id.btn_right_control:
                //maxSendData("00004A0000000000",(byte)0x04);
                break;
            case R.id.ib_control_back:
                this.finish();
                break;

        }
    }
    class MyTimerTask extends TimerTask {
        public int MyFlag = 0;
        @Override
        public void run() {
            Utils.LogE("...........................................................................................");
            //myHandler2.sendEmptyMessage(4600);
            intent.putExtra("data",commandTwoBytes);
            sendBroadcast(intent);
        }
    }

    private View.OnTouchListener MyTai = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP){
                Toast.makeText(ControllerActivity.this, "UP", Toast.LENGTH_SHORT).show();
                if(v.getId() == R.id.btn_left_control || v.getId() == R.id.btn_right_control){
                    commandTwoBytes[1] = 0;
                    if(timer34 != null){
                        timer34.cancel();
                        //stopSendData();
                        mSendBle.stopSend();
                    }
                }
            } else  if(event.getAction()==MotionEvent.ACTION_DOWN){
                Toast.makeText(ControllerActivity.this, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                switch (v.getId()){
                    case R.id.btn_left_control:
                        commandTwoBytes[1] = 0x3C;
                        timer34=new Timer();
                        mt34 = new MyTimerTask();
                        timer34.schedule(mt34,0,200);
                        break;
                    case R.id.btn_right_control:
                        commandTwoBytes[1] = 0x4C;
                        timer34=new Timer();
                        mt34 = new MyTimerTask();
                        timer34.schedule(mt34,0,200);
                        break;
                }
            }
            return false;
        }
    };


}

