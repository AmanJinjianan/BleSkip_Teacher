package com.qixiang.bleskip_teacher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.qixiang.bleskip_teacher.BLE.BLEService;
import com.qixiang.bleskip_teacher.BLE.MyListener;
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

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(!reveiveFlag)
                return;

            if(identifySysID(scanRecord)){
                String ddString = Utils.toHexString(scanRecord);
                if (remainString.equals(ddString)){
                    return;
                }
                remainString  = ddString;
                Tools.setLog("log1", "系统值匹配成功..........LLL:"+recycleCount+"\n       ddString:"+ddString+"   remainString:"+remainString);
                theReamainDataString = "系统值匹配成功..........LLL:"+recycleCount;
                myHandler.sendEmptyMessage(131415);

                if(identifyRanData(scanRecord)){//匹配校验发送的随机数
                    Tools.setLog("log1", "随机数匹配成功..........:"+recycleCount);
                    theReamainDataString = "随机数匹配成功..........:"+recycleCount;
                    myHandler.sendEmptyMessage(131415);
                    if(recycleCount ==1){//在第一个“发收周期”
                        theOneByte =scanRecord[13];
                        theReceiveData = bytesToHexFun3(scanRecord);
                        myHandler.sendEmptyMessage(13141);
                    }
                }
            }
            if(null == device.getName())
            {
            }else{
                Message m = new Message();
                Bundle b = new Bundle();
                m.what = 1234321;
                b.putString("name",device.getName());
                m.setData(b);
                myHandler.sendMessage(m);
                Tools.setLog("log1", device.getName()+"被发现..........");
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            // TODO Auto-generated method stub
            BLEService.LocalBinder binder = (BLEService.LocalBinder)service;
            Tools.mBleService = binder.getService();
            if (Tools.mBleService.initBle()) {
                if(!Tools.mBleService.mBluetoothAdapter.isEnabled()){
                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }else {
                    myHandler.sendEmptyMessage(11);
                    //scanBle();
                }
            }
        }
    };
    private Handler myHandler2 = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Utils.LogE("myHandler2 dataTwoByte:   "+Utils.toHexString(dataTwoByte));
            maxSendData("0000"+Utils.toHexString(dataTwoByte)+"00000000",(byte)0x04);
        }};
    //代表在第几个“发收周期”，初始为"1"（一发一收代表一个周期）
    int recycleCount=1;
    String theReamainDataString = "";
    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //Tools.setLog("log1", "myHandler.........."+msg.what);
            switch (msg.what) {
                case 13141:
                    Tools.setLog("log1", "..........................................................................13141");
                    //进行周期递增
                    recycleCount++;
                    if(recycleCount >= 2){
                        //scrollToBottom("连接完成！" + recycleCount+" "+Utils.toHexString(new byte[]{theOneByte}));
                        reveiveFlag = false;
                        Toast.makeText(ControlMainAct.this, "连上了..."+Utils.toHexString(new byte[]{theOneByte}), Toast.LENGTH_LONG).show();
                        maxSendData("0000000000000000",(byte)0x01);
                        myHandler.sendEmptyMessage(122);
                        setFourBtnEnable(true);
                    }else {
                        //scrollToBottom("第"+recycleCount+"个周期");
                        maxSendData("0000000000000000",(byte)0xFF);
                    }
                    break;
                case 131415:
                    //scrollToBottom(theReamainDataString);
                    break;
                case 1234321:
                    Bundle d =msg.getData();
                    //scrollToBottom(d.get("name").toString());
                    break;
                case 110:
                    Toast.makeText(ControlMainAct.this, "广播成功（一次）............", Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(ControlMainAct.this, "广播失败............", Toast.LENGTH_SHORT).show();
                    break;
                case 1110:
                    Toast.makeText(ControlMainAct.this, "持续广播成功............", Toast.LENGTH_SHORT).show();
                    break;
                case 11120:
                    Toast.makeText(ControlMainAct.this, "停止广播成功.............", Toast.LENGTH_SHORT).show();
                    break;
                case 11121:
                    Toast.makeText(ControlMainAct.this, "停止广播失败.............", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    bindService(new Intent(ControlMainAct.this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
                    break;
                case 11:
                    if(Tools.mBleService != null)
                        Tools.mBleService.scanBle(mLeScanCallback);
                    //Toast.makeText(ControlMainAct.this, "0000810000000000", Toast.LENGTH_LONG).show();
                    //maxSendData("0000810000000000",(byte)0xFF);
                    break;
                case 1313:
                    Toast.makeText(ControlMainAct.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1314:
                    ppCount++;
                    break;
                case 1315:
                    ppCount++;
                    break;
                case 12:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
                    }
                    Toast.makeText(ControlMainAct.this, "连接成功", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent("com.qixiang.blesuccess");
                    sendBroadcast(intent2);

                    Intent intent = new Intent(ControlMainAct.this,ControlMainAct.class);
                    startActivity(intent);
                    break;
                case 122:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                    }
                    break;
                case 13:
                    new MyConnectedThread().start();
                    break;
                case 14:
                    Bundle bundle=msg.getData();
                    String string =(String) bundle.get("Key");
                    Tools.setLog("log1", "msg.what 14:"+string);
                    Tools.mBleService.characterWrite1.setValue(Utils.hexToBytes(string));
                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    break;
                case 456:
                    maxSendData("00001A0000000000",(byte)0x01);
                    break;
                case 457:
                    maxSendData("00002A0000000000",(byte)0x02);
                    break;
                case 458:
                    maxSendData("00003A0000000000",(byte)0x03);
                    break;
                case 459:
                    maxSendData("00004A0000000000",(byte)0x04);
                    break;
                case 4600:
                    Utils.LogE("dataTwoByte:   "+Utils.toHexString(dataTwoByte));
                    //maxSendData("0000"+Utils.toHexString(dataTwoByte)+"00000000",(byte)0x04);
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control_main);

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

    ArrayList<String> unPermissionList;
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            String[] mPermission = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            };
            unPermissionList = new ArrayList<String>();
            try {
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(ControlMainAct.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        unPermissionList.add(mPermission[i]);
                    }
                }
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(ControlMainAct.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                        if (isLocationEnabled()) {

                        }else {
                            Toast.makeText(ControlMainAct.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(ControlMainAct.this, "Exception0:"+e.toString(), Toast.LENGTH_LONG).show();
            }

        }else {
            InitBle();
            Toast.makeText(ControlMainAct.this, "Init.............:", Toast.LENGTH_LONG).show();
            return;
        }
        if (unPermissionList.isEmpty()) {
            if (isLocationEnabled())
                InitBle();
            //Toast.makeText(BleConnectActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
            //都授权了。。。
        }else {
            try {
                String[] permissionStrings = unPermissionList.toArray(new String[unPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissionStrings, 1);
            } catch (Exception e) {
                Toast.makeText(ControlMainAct.this, "Exception11:"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    //校验系统ID
    public boolean identifySysID(byte[] data){
        byte[] byteData = Utils.toBytes("0201050319C1030716");

        for(int i=0;i<byteData.length;i++){
            if(!(byteData[i] == data[i]))
                return false;
        }
        return true;
    }

    public boolean identifyRanData(byte[] data){

        if(data.length<15){
            Toast.makeText(ControlMainAct.this, "返回参数异常了", Toast.LENGTH_SHORT).show();
            return false;
        }


        int randomValue = Integer.valueOf(data[10] >> 2);
        Tools.setLog("log1", "randomValue..........:"+randomValue  +"  oRandowData:"+oRandowData);
        if(randomValue == oRandowData || (oRandowData-randomValue == 64))//如果相等或互补
            return true;
        else
            return false;

    }
    public static String bytesToHexFun3(byte[] bytes) {
        byte[] iData = new byte[6];
        for (int i= 0;i<6;i++){
            iData[i] = bytes[i+9];
        }

        StringBuilder buf = new StringBuilder(iData.length * 2);
        for(byte b : iData) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", new Integer(b & 0xff)));
        }

        return buf.toString();
    }

    private void InitBle(){
        BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bAdapter = bManager.getAdapter();
        if(bAdapter == null){
            Toast.makeText(ControlMainAct.this, "not support", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (bAdapter.isEnabled()) {
            setBroadcastReveiver();
            myHandler.sendEmptyMessage(10);
            //bindService(new Intent(this,ControlMainAct.class), connection, Context.BIND_AUTO_CREATE);
        }else {
            bAdapter.enable();
        }
        //setBroadcastReveiver();
    }




    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Toast.makeText(TestAct.this, "requestCode:"+requestCode, Toast.LENGTH_LONG).show();
        for (int i = 0; i < grantResults.length; i++) {
            //Toast.makeText(ScanActivity.this, permissions[i]+"     "+grantResults[i], Toast.LENGTH_LONG).show();

            if (permissions[i].equals("android.permission.ACCESS_COARSE_LOCATION")) {
                if ((grantResults[i] ==0)) {
                    if(!isLocationEnabled()){
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }else {
                        InitBle();
                        Toast.makeText(ControlMainAct.this, "Init.............:", Toast.LENGTH_LONG).show();
                    }
                }else {
                    finish();
                }

            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
            if (isLocationEnabled()) {
                checkBluetoothPermission();
            }else {
                Toast.makeText(ControlMainAct.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                }, 1000);
            }
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_WRITE_DESCRIPTOR_OVER);
        //intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }



    private BluetoothReceiver bluetoothReceiver = null;




    public MyListener listener = new MyListener() {
        @Override
        public void readData(byte[] var1, String var2) {

        }

        @Override
        public void onSendStatus(Boolean var1) {
            Tools.setLog("log1", "..........................onSendStatus......");
       /* if (var1)
            Toast.makeText(ControlMainAct.this, "...........成功...............", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(ControlMainAct.this, "...........失败...............", Toast.LENGTH_SHORT).show();*/
        }
    };


    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){
                //tmp_byte = characteristic.getValue();
                byte[] tmp_byte = intent.getByteArrayExtra("value");
                tmp = "";
                for (int i = 0; i < tmp_byte.length; i++) {
                    hex = Integer.toHexString(tmp_byte[i] & 0xFF);
                    if (hex.length() == 1) {
                        hex = '0' + hex;
                    }
                    tmp = tmp + hex;
                }
            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){

            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
                connected_flag = false;
                myHandler.sendEmptyMessage(11);
            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
                connected_flag = true;
                theDevice = null;
                myHandler.sendEmptyMessage(12);
            }else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch(blueState){
                    case BluetoothAdapter.STATE_ON:
                        //开始扫描
                        bindService(new Intent(ControlMainAct.this,ControlMainAct.class), connection, Context.BIND_AUTO_CREATE);
                        break;
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        exit_activity = true;

        this.unregisterReceiver(bluetoothReceiver);
        //unbindService(connection);
    }



    public class MyConnectedThread extends Thread{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();

            try {
                while (true) {
                    connected_flag = false;
                    if (exit_activity) return;
                    Tools.setLog("log1", "connectBle..........");
                    Tools.mBleService.connectBle(theDevice);

                    for(int j=0;j<50;j++){

                        if (connected_flag) {
                            break;
                        }
                        sleep(100);
                    }

                    if(connected_flag)
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
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

    private void stopSendData() {
        //Toast.makeText(ControlMainAct.this, "停22", Toast.LENGTH_SHORT).show();
//        findViewById(R.id.btn_stop).setEnabled(false);
//        findViewById(R.id.btn_more).setEnabled(true);
//        mSendBle.stopSend();
    }
    public void maxSendData(String realData,byte theDirection){
        if(recycleCount != 1)
            stopSendData();
        String sendData = getIDValue(realData,theDirection);
        if("".equals(sendData)){
            Toast.makeText(ControlMainAct.this, "发送数据异常", Toast.LENGTH_SHORT).show();
        }else {
            mSendBle.startSendMore(sendData, 3,myHandler);
            mSendBle.setListener(listener);
        }
    }

    //得到校验结果（存在输入风险 未严格筛选非法字符）
    private String getIDValue(String data,byte derectionFlag) {
        //非法字符校验
        String theData = validataData(data);
        if("".equals(theData)){
            return "";
        }
        byte[] theByte = Utils.toBytes(theData);

        Tools.setLog("log1","recycleCount:::::::::::::::::::::::::::::："+recycleCount+ "  data:"+data);
        switch (recycleCount)
        {
            case 1://代表第一发送
                //得到随机数
                theByte[0] = getSecondData();
                break;
            case 2:
                //第二次发送
                theByte[0] = getSecondData();
                theByte[1] = theOneByte;
                recycleCount++;
                //theByte[2] = 0x1A;
                break;
            default://非配对阶段
                //theByte = getDirectionData(derectionFlag);
                theByte[0] = getSecondData();
                theByte[1] = theOneByte;
                break;
        }
        //将随机数整合到数组中，并显示在view中
        //String theString = byteArrayToHexStr(theByte);

       /* //Tools.setLog("log1","最终出口数据："+theString);
        SpannableStringBuilder style=new SpannableStringBuilder(theString);
        //str代表要显示的全部字符串
        style.setSpan(new ForegroundColorSpan(Color.RED),6,8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //３代表从第几个字符开始变颜色，注意第一个字符序号是０．
        //８代表变色到第几个字符．
        et_data.setText(style);*/

        //颠倒数组顺序
        for (int i=0;i<theByte.length/2;i++){
            byte temp = theByte[i];
            theByte[i] = theByte[7-i];
            theByte[7-i] = temp;
        }
        byte theResult = 0;
        //依次"亦或"九个byte
        for (int i=0;i<theByte.length;i++){
            theResult = (byte)(theByte[i]^theResult);
        }
        theResult = (byte)(theResult^0x5A);
        Utils.LogE(("校验值：\n"+theResult));
        //组织新的数据 附加校验位
        byte[] newByte = new byte[theByte.length+1];
        for (int i=0;i<newByte.length;i++){
            if(i == 0)
                newByte[i] = theResult;
            else
                newByte[i] = theByte[i-1];
        }

        String stringData = Utils.bytesToHexString(newByte);
        return formData(stringData);
    }

    private String validataData(String data) {
        String regex="^[A-Fa-f0-9]+$";
        if(!data.matches(regex) || (data.length()%2 !=0)){
            Toast.makeText(ControlMainAct.this, "字符串不是十六进制字符串", Toast.LENGTH_SHORT).show();
            return "";
        }

        if(data.length() != 16){
            Toast.makeText(ControlMainAct.this, "数据长度异常..", Toast.LENGTH_SHORT).show();
            return "";
        }
        return data;
    }

    boolean mgFlag= true;
    byte[] df;
    byte windowNum =0;
    int oRandowData=0;

    int formdata = 0;
    //6位随机数+2位窗口号
    public byte getSecondData(){
        if(mgFlag){
            mgFlag = false;
            oRandowData = (int)(1+Math.random()*(62));
            df=Utils.intToButeArray(oRandowData);
        }
        if(df != null){
            //Utils.LogE("formdataaaaaaaaaaaaaaaaaaaaaaaaa:"+formdata++);
            windowNum++;
            if(windowNum>3)
                windowNum=0;
            return (byte)(df[1]*4 + windowNum);
        }else {
            Toast.makeText(ControlMainAct.this, "窗口数产生异常了", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }
    public String formData(String stringData){
        if(stringData.length() != 18)
            Toast.makeText(ControlMainAct.this, "数据长度异常2..", Toast.LENGTH_SHORT).show();

        return stringData.substring(0,8)+"-"+stringData.substring(8,12)+"-"+stringData.substring(12,16)+"-"+stringData.substring(16);
    }

}
