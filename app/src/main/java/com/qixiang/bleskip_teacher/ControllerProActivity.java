package com.qixiang.bleskip_teacher;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.MyListener;
import com.qixiang.bleskip_teacher.BLE.SendBle;
import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.MyView.MyRockerView;
import com.qixiang.bleskip_teacher.Util.DpUtils;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerProActivity extends Activity implements View.OnClickListener, MyRockerView.OnShakeListener {

    private final static int REQUEST_ENABLE_BT=2001;
    private BluetoothDevice theDevice;
    private List<String> fdArrayList = new ArrayList<String>();
    private boolean connected_flag;
    private boolean exit_activity = false;
    public String tmp,hex;
    public boolean reveiveFlag = false;

    public  byte theRandowData = 0;
    public  byte[] theTwoByte = new byte[]{0x00,0x00};

    //保存下位机设备ID
    public byte theOneByte=0;

    String data = "",theReceiveData;

    int ppCount = 0;
    String remainString = "";

    //代表在第几个“发收周期”，初始为"1"（一发一收代表一个周期）
    int recycleCount=1;

    Animation myAnimation;
    private SendBle mSendBle;
    private MyRockerView myRockerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.layout_controlpro);
        //checkBluetoothPermission();
        reveiveFlag = false;
        initView();

        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams((int)(ControlMainAct.height*1.77),ControlMainAct.height);
        ll.gravity = Gravity.CENTER_HORIZONTAL;

        reveiveFlag = false;
        mSendBle = new SendBle(this);
    }
    void initView(){
        findViewById(R.id.ib_control_pro_back).setOnClickListener(this);

        findViewById(R.id.btn_left_down).setOnClickListener(this);
        findViewById(R.id.btn_left_up).setOnClickListener(this);
        findViewById(R.id.btn_right_down).setOnClickListener(this);
        findViewById(R.id.btn_right_up).setOnClickListener(this);

        findViewById(R.id.btn_left_pro).setOnClickListener(this);
        findViewById(R.id.btn_right_pro).setOnClickListener(this);

        myRockerView = (MyRockerView) findViewById(R.id.act_myrockview);
        myRockerView.setOnShakeListener(MyRockerView.DirectionMode.DIRECTION_8,this);

    }
    void setFullScreen(){
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        // 定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        // 获得当前窗体对象
        Window window = ControllerProActivity.this.getWindow();
        // 设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    Intent intent = new Intent("CONTROLLERDATA");

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_left_pro:
                //maxSendData("00003A0000000000",(byte)0x03);
                break;
            case R.id.btn_right_pro:
                //maxSendData("00004A0000000000",(byte)0x04);
                break;
            case R.id.btn_left_up:
                //maxSendData("00003A0000000000",(byte)0x03);
                break;
            case R.id.btn_right_up:
                Tools.setLog("ControllerPro","gggggggggggggggggggggggggg");
                //maxSendData("00004A0000000000",(byte)0x04);
                break;
            case R.id.btn_left_down:
                //maxSendData("00003A0000000000",(byte)0x03);
                break;
            case R.id.btn_right_down:
                //maxSendData("00004A0000000000",(byte)0x04);
                break;
            case R.id.ib_control_pro_back:
                this.finish();
                break;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void direction(MyRockerView.Direction direction,float distance) {
        Tools.setLog("Rocker","Direction:::::"+direction +" distance:"+distance);
    }

    @Override
    public void onFinish() {

    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            intent.putExtra("data",dataTwoByte);
            sendBroadcast(intent);
        }
    }
    MyTimerTask mt34;
    Timer timer34;

    byte[] dataTwoByte = new byte[2];
    private View.OnTouchListener MyTai = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP){
                Toast.makeText(ControllerProActivity.this, "UP", Toast.LENGTH_SHORT).show();
                if(v.getId() == R.id.btn_left_pro|| v.getId() == R.id.btn_right_pro){
                    dataTwoByte[1] = 0;
                    if(timer34 != null){
                        timer34.cancel();
                        //stopSendData();
                        mSendBle.stopSend();
                    }
                }
            } else  if(event.getAction()==MotionEvent.ACTION_DOWN){
                Toast.makeText(ControllerProActivity.this, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                switch (v.getId()){
                    case R.id.btn_left_pro:
                        dataTwoByte[1] = 0x3C;
                        timer34=new Timer();
                        mt34 = new MyTimerTask();
                        timer34.schedule(mt34,0,200);
                        break;
                    case R.id.btn_right_pro:
                        dataTwoByte[1] = 0x4C;
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

