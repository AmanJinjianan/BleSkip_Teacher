package com.qixiang.bleskip_teacher;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import  com.qixiang.bleskip_teacher.BLE.BLEService;
import  com.qixiang.bleskip_teacher.BLE.MyListener;
import  com.qixiang.bleskip_teacher.BLE.SendBle;
import  com.qixiang.bleskip_teacher.BLE.Tools;
import  com.qixiang.bleskip_teacher.Util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ControllerActivity extends Activity implements View.OnClickListener{

    public SeekBar theSeek;
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

    private Handler myHandler2 = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Utils.LogE("myHandler2 dataTwoByte:   "+Utils.toHexString(dataTwoByte));
            maxSendData("0000"+Utils.toHexString(dataTwoByte)+"00000000",(byte)0x04);
        }};
    //代表在第几个“发收周期”，初始为"1"（一发一收代表一个周期）
    int recycleCount=1;

    Animation myAnimation;
    private SendBle mSendBle;
    TextView tv_id,mTextStatus;
    EditText et_SysID,et_SysID2,et_data;
    ScrollView mScrollView;
    Button btn_stop,btn_up,btn_down,btn_left,btn_right;
    ImageButton btn_more;
    ImageButton btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.layout_control);
        checkBluetoothPermission();
        reveiveFlag = false;
        btn_more = (ImageButton)findViewById(R.id.btn_more2598);
        btn_more.setOnClickListener(this);
        btn_left = (Button) findViewById(R.id.btn_left2598);
        btn_right = (Button)findViewById(R.id.btn_right2598);
        btn_back = (ImageButton)findViewById(R.id.ib_control_back);
        btn_back.setOnClickListener(this);

        btn_right.setOnTouchListener(MyTai);
        btn_left.setOnTouchListener(MyTai);

        FrameLayout.LayoutParams ll = new FrameLayout.LayoutParams((int)(ControlMainAct.height*1.77),ControlMainAct.height);
        ll.gravity = Gravity.CENTER_HORIZONTAL;
        findViewById(R.id.ll_control_center).setLayoutParams(ll);

        theSeek = (SeekBar) findViewById(R.id.seekBar3);
        theSeek.setProgressDrawable(null);
        theSeek.setProgress(50);
        theSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress<47){//前进
                    //dataTwoByte[0] = 0x1A;
                    //将seekbar的波动范围映射成0x10-0x1F 十六个档位
                    dataTwoByte[0] = (byte)(0x10 | Utils.intToButeArray(Math.abs(46-progress)/3)[1]);
                    if(timer12 == null){
                        timer12=new Timer();
                        mt12 = new MyTimerTask();
                        mt12.MyFlag = 1;
                        timer12.schedule(mt12,0,200);
                    }
                }else if(progress>54){//后退
                    //dataTwoByte[0] = 0x2A;
                    //将seekbar的波动范围映射成0x20-0x2F 十六个档位
                    dataTwoByte[0] = (byte)(0x20 | Utils.intToButeArray(Math.abs(55-progress)/3)[1]);
                    if(timer12 == null){
                        timer12=new Timer();
                        mt12 = new MyTimerTask();
                        mt12.MyFlag = 2;
                        timer12.schedule(mt12,0,200);
                    }
                }
                Utils.LogE("progress:::::::::::::  :::::::::::::::"+Utils.toHexString(dataTwoByte));

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
                    dataTwoByte[0] = 0;
                    timer12.cancel();
                    stopSendData();
                    mSendBle.stopSend();
                    timer12 = null;
                }

            }
        });

        reveiveFlag = false;
        mSendBle = new SendBle(this);
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

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    private BluetoothReceiver bluetoothReceiver = null;

    public MyListener listener = new MyListener() {
        @Override
        public void readData(byte[] var1, String var2) {}

        @Override
        public void onSendStatus(Boolean var1) {
            Tools.setLog("log1", "..........................onSendStatus......");
        }
    };


    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

//            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){
//                //tmp_byte = characteristic.getValue();
//                byte[] tmp_byte = intent.getByteArrayExtra("value");
//                tmp = "";
//                for (int i = 0; i < tmp_byte.length; i++) {
//                    hex = Integer.toHexString(tmp_byte[i] & 0xFF);
//                    if (hex.length() == 1) {
//                        hex = '0' + hex;
//                    }
//                    tmp = tmp + hex;
//                }
//            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){
//
//            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
//                connected_flag = false;
//                myHandler.sendEmptyMessage(11);
//            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
//                connected_flag = true;
//                theDevice = null;
//                myHandler.sendEmptyMessage(12);
//            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
//                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
//                switch(blueState){
//                    case BluetoothAdapter.STATE_ON:
//                        //开始扫描
//                        bindService(new Intent(ControllerActivity.this,ControllerActivity.class), connection, Context.BIND_AUTO_CREATE);
//                        break;
//                }
//
//            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        exit_activity = true;

        //this.unregisterReceiver(bluetoothReceiver);
        //unbindService(connection);
    }

    private String sss1="";
    private byte[] command_ = new byte[16];


    StringBuilder sb=  new StringBuilder();
    int switchFlag=0;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_more2598:
                reveiveFlag = true;
                maxSendData("0000810000000000",(byte)0xFF);
                break;
            case R.id.btn_left2598:
                maxSendData("00003A0000000000",(byte)0x03);
                break;
            case R.id.btn_right2598:
                maxSendData("00004A0000000000",(byte)0x04);
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
            myHandler2.sendEmptyMessage(4600);
        }
    }
    MyTimerTask mt12,mt34;
    Timer timer12,timer34;

    byte[] dataTwoByte = new byte[2];
    private View.OnTouchListener MyTai = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_CANCEL || event.getAction()==MotionEvent.ACTION_UP){
                Toast.makeText(ControllerActivity.this, "UP", Toast.LENGTH_SHORT).show();
                if(v.getId() == R.id.btn_left2598 || v.getId() == R.id.btn_right2598){
                    dataTwoByte[1] = 0;
                    if(timer34 != null){
                        timer34.cancel();
                        stopSendData();
                        mSendBle.stopSend();
                    }
                }
            } else  if(event.getAction()==MotionEvent.ACTION_DOWN){
                Toast.makeText(ControllerActivity.this, "ACTION_DOWN", Toast.LENGTH_SHORT).show();
                switch (v.getId()){
                    case R.id.btn_left2598:
                        dataTwoByte[1] = 0x3C;
                        timer34=new Timer();
                        mt34 = new MyTimerTask();
                        timer34.schedule(mt34,0,200);
                        break;
                    case R.id.btn_right2598:
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

