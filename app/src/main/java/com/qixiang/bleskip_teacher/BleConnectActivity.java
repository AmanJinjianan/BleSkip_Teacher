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
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.BLEService;
import com.qixiang.bleskip_teacher.BLE.SendBle;
import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/28.
 */

public class BleConnectActivity extends Activity implements View.OnClickListener{

    private final static int REQUEST_ENABLE_BT=2001;
    private BluetoothDevice theDevice;
    private List<String> fdArrayList = new ArrayList<String>();
    private boolean connected_flag;
    private boolean exit_activity = false;
    public String tmp,hex;

        private BLEService.OnWriteOverCallback mOnWriteOverCallback = new BLEService.OnWriteOverCallback(){

        @Override
        public void OnWriteOver(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int statue) {

            if(statue == BluetoothGatt.GATT_SUCCESS){
                Log.e(Utils.TAG,"OnWriteOver..............");
                if(fdArrayList.size()!= 0){
                    fdArrayList.remove(0);
//                    if(fdArrayList.size()!= 0){
//                        spliteArray =fdArrayList.get(0).split(",");
//                        if (spliteArray[0].equals("#")) {//常规指令
//                            //SendDataString(spliteArray[1]);
//                            Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                        }else {//速度指令
//                            //SendControllerSpeed(spliteArray[1]);
//                            Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                        }
//                    }
                }
            }else if(statue == BluetoothGatt.GATT_FAILURE){
                myHandler.sendEmptyMessage(8);
//                if (fdArrayList.size()!= 0){
//                    spliteArray =fdArrayList.get(0).split(",");
//                    if (spliteArray[0].equals("#")) {//常规指令
//                        //SendDataString(spliteArray[1]);
//                        Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                    }else {//速度指令
//                        //SendControllerSpeed(spliteArray[1]);
//                        Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//                        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//                    }
//                }
            }
        }

    };
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);
        return str;
    }
    public  String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    String data = "";

    int ppCount = 0;
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            if(null == device.getName())
            {
                //Tools.setLog("log1", device.getName()+"LeScanCallback..........LLL:");
                device.getUuids();
                Tools.setLog("log1", device.getName()+"LeScanCallback..........ppp:\n"+ device.getUuids());
                //myHandler.sendEmptyMessage(1314);
                if(!byteArrayToHexStr(scanRecord).substring(0,18).equals(data))
                {
                    //data = byteArrayToHexStr(scanRecord).substring(0,18);
                    //Tools.setLog("log1", device.getName()+"LeScanCallback..........ppp:"+ byteArrayToHexStr(scanRecord).length());
                    //myHandler.sendEmptyMessage(1313);
                }
            }else{
                Tools.setLog("log1", device.getName()+"被发现..........LLL:");
            }

            /*if(null != device && null != device.getName()){
                if (theDevice == null) {
                    if (device.getName().equals("TSnet")) {
                        theDevice = device;

                        myHandler.sendEmptyMessage(13);
                    }
                }
            }*/
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

        int numcount=0;
        private Handler myHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            //Tools.setLog("log1", "myHandler.........."+msg.what);
            switch (msg.what) {
                case 111:
                    //Toast.makeText(MainActivity.this, "startActivityForResult..", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                    //startActivityForResult(intent, 12);
                    break;
                case 110:
                    Toast.makeText(BleConnectActivity.this, "110............", Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(BleConnectActivity.this, "112.............", Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    bindService(new Intent(BleConnectActivity.this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
                    break;
                case 11:
                    if(Tools.mBleService != null)
                        Tools.mBleService.scanBle(mLeScanCallback);
                    break;
                case 1313:
                    numcount++;
                    thetr.setText(data+"  共"+numcount+"次");
                    Toast.makeText(BleConnectActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1314:
                    ppCount++;
                    thetr.setText("sleep100ms 共:"+ppCount+"次");
                    //Toast.makeText(BleConnectActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 1315:
                    ppCount++;
                    thetr.setText("Setting 100ms 共:"+ppCount+"次");
                    //Toast.makeText(BleConnectActivity.this, "连接成功:"+data+" ", Toast.LENGTH_LONG).show();
                    break;
                case 12:
                    if(Tools.mBleService != null){
                        Tools.mBleService.stopscanBle(mLeScanCallback);
                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
                    }
                    Toast.makeText(BleConnectActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent("com.qixiang.blesuccess");
                    sendBroadcast(intent2);

                    Intent intent = new Intent(BleConnectActivity.this,MainActivity.class);
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
                    Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                    break;
                default:
                    break;
            }
        };
    };
    Animation myAnimation;
    private SendBle mSendBle;
    TextView thetr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bleconnect);


        thetr = (TextView) findViewById(R.id.thrtittle);

        checkBluetoothPermission();

        Log.e(Utils.TAG,"Bleconnectedactivity oncreate..............");

        bindService(new Intent(this,BLEService.class), connection, Context.BIND_AUTO_CREATE);

        findViewById(R.id.btn_ble_jump).setOnClickListener(this);
        //findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.btn_startAd).setOnClickListener(this);

        myAnimation = AnimationUtils.loadAnimation(this, R.anim.rotation1);
        findViewById(R.id.imageView111).startAnimation(myAnimation);

        mSendBle = new SendBle(this);
    }

public void backtomain(View view){
        Intent intent = new Intent(BleConnectActivity.this,MainActivity.class);
        startActivity(intent);
        //BleConnectActivity.this.finish();
}

private void InitBle(){
    BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
    BluetoothAdapter bAdapter = bManager.getAdapter();
    if(bAdapter == null){
        Toast.makeText(BleConnectActivity.this, "not support", Toast.LENGTH_SHORT).show();
        finish();
    }
    if (bAdapter.isEnabled()) {
        bindService(new Intent(this,BleConnectActivity.class), connection, Context.BIND_AUTO_CREATE);
    }else {
        bAdapter.enable();
    }
    setBroadcastReveiver();
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
                    if (ContextCompat.checkSelfPermission(BleConnectActivity.this, mPermission[i]) != PackageManager.PERMISSION_GRANTED) {
                        unPermissionList.add(mPermission[i]);
                    }
                }
                for (int i = 0; i < mPermission.length; i++) {
                    if (ContextCompat.checkSelfPermission(BleConnectActivity.this, "android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                        if (isLocationEnabled()) {

                        }else {
                            Toast.makeText(BleConnectActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(BleConnectActivity.this, "Exception0:"+e.toString(), Toast.LENGTH_LONG).show();
            }

        }else {
            InitBle();
            Toast.makeText(BleConnectActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BleConnectActivity.this, "Exception11:"+e.toString(), Toast.LENGTH_LONG).show();
            }
        }

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
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                    }else {
                        InitBle();
                        Toast.makeText(BleConnectActivity.this, "Init.............:", Toast.LENGTH_LONG).show();
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
                Toast.makeText(BleConnectActivity.this, "Please open the location switch.", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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
        Log.e("sunlei","FirstActivity---onPause");
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


    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){

                Log.e(Utils.TAG,"心跳。。。。。。。。。。。。。。");
                ((Button)findViewById(R.id.btn_ble_jump)).setText("hahhaha");
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
                        bindService(new Intent(BleConnectActivity.this,BleConnectActivity.class), connection, Context.BIND_AUTO_CREATE);
                        break;
                }

            }
        }
    }

        @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        Log.e(Utils.TAG,"onDestroy..............");
        exit_activity = true;
        unbindService(connection);
    }

     public byte[] hexToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789ABCDEF";
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            int h = hexDigits.indexOf(hexChars[pos]) << 4;
            int l = hexDigits.indexOf(hexChars[pos + 1]);
            if (h == -1 || l == -1) {
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
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

    private String sendstr="";
    private String sss1="";
    private byte[] command_ = new byte[16];
    int data1 = 0;
    int data2 = 0;
    int data3 = 0;
    int data4 = 0;
    int data5 = 0;
    int data6 = 0;
    int data7 = 0;
    int data8 = 0;
    int data9 = 0;
    int data10 = 0;
    int data11 = 0;
    int data12 = 0;
    int data13 = 0;
    int data14 = 0;
    int data15 = 0;
    int data16 = 0;
    /**
     * 初始化操作指令
     */
    private void initData() {
        command_[0] = (byte) data16;
        command_[1] = (byte) data15;
        command_[2] = (byte) data14;
        command_[3] = (byte) data13;
        command_[4] = (byte) data12;
        command_[5] = (byte) data11;
        command_[6] = (byte) data10;
        command_[7] = (byte) data9;
        command_[8] = (byte) data8;
        command_[9] = (byte) data7;
        command_[10] = (byte) data6;
        command_[11] = (byte) data5;
        command_[12] = (byte) data4;
        command_[13] = (byte) data3;
        command_[14] = (byte) data2;
        command_[15] = (byte) data1;
        sss1 = bytesToHexString(command_);//需要发送的字节需反向拼接
        sendstr= sss1.substring(0,8)+"-"+sss1.substring(8,12)+"-"+sss1.substring(12,16)+"-"+sss1.substring(16,20)+"-"+sss1.substring(20,32);//"01020304-0506-0708-1112-131415161718"
    }
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ble_jump:
                Tools.mBleService.characterWrite1.setValue(hexToBytes("06241112131410"));
                Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
                break;
            /*case R.id.btn_reset:
                ppCount=0;
                numcount = 0;
                thetr.setText("0");
                break;*/
            case R.id.btn_startAd:
                initData();
                //boolean flag = mSendBle.d(sendstr, 3,myHandler);
                break;
        }
    }

}
