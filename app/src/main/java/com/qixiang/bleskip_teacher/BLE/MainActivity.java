package com.qixiang.bleskip_teacher.BLE;

        import java.util.ArrayList;
        import java.util.List;
        import android.Manifest;
        import android.R.bool;
        import android.R.string;
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
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.Toast;


//public class MainActivity extends Activity {
//
//    public static boolean mimiFlag = false;
//    private final static int REQUEST_ENABLE_BT=2001;
//    private BluetoothDevice theDevice;
//
//    private boolean connected_flag;
//    private boolean exit_activity = false;
//
//    private List<String> fdArrayList = new ArrayList<String>();
//    private String[] spliteArray;
//    private ServiceConnection connection = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName arg0) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName arg0, IBinder service) {
//            // TODO Auto-generated method stub
//
//            Tools.setLog("log1", "onServiceConnected..........");
//
//            LocalBinder binder = (LocalBinder)service;
//            Tools.mBleService = binder.getService();
//            if (Tools.mBleService.initBle()) {
//                if(!Tools.mBleService.mBluetoothAdapter.isEnabled()){
//                    final Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                }else {
//                    myHandler.sendEmptyMessage(11);
//                    //scanBle();
//                }
//            }
//        }
//    };
//    private BLEService.OnWriteOverCallback mOnWriteOverCallback = new BLEService.OnWriteOverCallback(){
//
//        @Override
//        public void OnWriteOver(BluetoothGatt gatt,
//                                BluetoothGattCharacteristic characteristic,int statue) {
//
//            if(statue == BluetoothGatt.GATT_SUCCESS){
//                if(fdArrayList.size()!= 0){
//                    fdArrayList.remove(0);
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
//                }
//
//            }else if(statue == BluetoothGatt.GATT_FAILURE){
//                myHandler.sendEmptyMessage(8);
//                if (fdArrayList.size()!= 0){
//
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
//
//                }
//            }
////			if (characteristic == Tools.mBleService.characterWrite1) {
////				Tools.setLog("log1", "mOnWriteOverCallback.....11111111....."+statue);
////			}else if(characteristic == Tools.mBleService.characterWrite2){
////
////				Tools.setLog("log1", "mOnWriteOverCallback.....222222222....."+statue);
////			}
//        }
//
//    };
//    private Handler myHandler = new Handler(){
//        public void handleMessage(android.os.Message msg) {
//
//            Tools.setLog("log1", "myHandler.........."+msg.what);
//
//            switch (msg.what) {
//
//                case 111:
//                    //Toast.makeText(MainActivity.this, "startActivityForResult..", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(MainActivity.this,VideoActivity.class);
//                    startActivityForResult(intent, 12);
//                    break;
//                case 8:
//                    Toast.makeText(MainActivity.this, "write fail..", Toast.LENGTH_SHORT).show();
//                    break;
//                case 9:
//                    Toast.makeText(MainActivity.this, msg.getData().getString("daa"), Toast.LENGTH_SHORT).show();
//                    break;
//                case 10:
//                    bindService(new Intent(MainActivity.this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
//                    break;
//                case 11:
//                    if(Tools.mBleService != null)
//                        Tools.mBleService.scanBle(mLeScanCallback);
//                    break;
//
//                case 12:
//                    if(Tools.mBleService != null){
//                        Tools.mBleService.stopscanBle(mLeScanCallback);
//                        Tools.mBleService.setOnWriteOverCallback(mOnWriteOverCallback);
//                    }
//                    Toast.makeText(MainActivity.this, "连接成功。。。。。。。。。", Toast.LENGTH_LONG).show();
//                    SendFlagToUnity();
//                    break;
//                case 122:
//                    if(Tools.mBleService != null){
//                        Tools.mBleService.stopscanBle(mLeScanCallback);
//                    }
//                    break;
//                case 13:
//                    new MyConnectedThread().start();
//                    break;
//                case 14:
//                    Bundle bundle=msg.getData();
//                    String string =(String) bundle.get("Key");
//                    Tools.setLog("log1", "msg.what 14                 :"+string);
//                    Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
//                    Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//                    break;
//                default:
//                    break;
//            }
//        };
//    };
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == REQUEST_ENABLE_BT){
//
//            if(resultCode == RESULT_OK)	{
//                scanBle();
//            }else {
//                finish();
//            }
//        }
//    };
//
//    public void SendFlagToUnity() {
//        if (null == theDevice || null == theDevice.getName())
//            return;
//
//        int flag = 0;
//        if (theDevice.getName().equals("YinSuARC")) {
//            flag = 1;
//        }else if (theDevice.getName().equals("YinSuASR")) {
//            flag = 2;
//        }
//        UnityPlayer.UnitySendMessage("ScanUIPanel", "IdentifyFlag", String.valueOf(flag));
//    }
//    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
//
//        @Override
//        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//            Tools.setLog("log1", "LeScanCallback.........."+device.getName());
//            if(null != device && null != device.getName()){
//                if (theDevice == null) {
//                    if (device.getName().equals("YinSuARC") || device.getName().equals("YinSuASR")) {
//                        theDevice = device;
//                        myHandler.sendEmptyMessage(13);
//                        //Tools.mBleService.connectBle(theDevice);
//                    }
//                }
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////      requestWindowFeature(Window.FEATURE_NO_TITLE);
////      //全屏
////      getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
////              WindowManager.LayoutParams. FLAG_FULLSCREEN);
//
//        //setContentView(R.layout.activity_main);
//
//        BluetoothManager bManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        BluetoothAdapter bAdapter = bManager.getAdapter();
//        if(bAdapter == null){
//            Toast.makeText(MainActivity.this, "not support", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        bAdapter.enable();
//
//        mimiFlag = false;
//
//        getPermission();
//        setBroadcastReveiver();
//
//
//        //bindService(new Intent(this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
//
////		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
////			@Override
////			public void onClick(View arg0) {
////				// TODO Auto-generated method stub
////				byte[] pp = {(byte) 0xa8,0x02};
////				Tools.mBleService.characterWrite1.setValue(pp);
////				Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
////			}
//
//
////		});
//    }
//
//
//
//
//
//    private void getPermission() {
//        // TODO Auto-generated method stub
//        if (Build.VERSION.SDK_INT<23) {
//            return;
//        }
//
//        String[] mPermissionList = new String[]{
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.BLUETOOTH,
//                Manifest.permission.BLUETOOTH_ADMIN
//        };
//
//        ArrayList<String> unPermissionList = new ArrayList<String>();
//
//        for(int i=0;i<mPermissionList.length;i++){
//
//            if(ContextCompat.checkSelfPermission(MainActivity.this, mPermissionList[i]) != PackageManager.PERMISSION_GRANTED){
//                unPermissionList.add(mPermissionList[i]);
//            }
//        }
//        if (unPermissionList.isEmpty()) {
//            //都授权了
//        }else {
//            String[] ggStrings= unPermissionList.toArray(new String[unPermissionList.size()]);
//            ActivityCompat.requestPermissions(MainActivity.this, ggStrings, 12);
//        }
//    }
//
//    public void BeginToConnect(){
//        myHandler.sendEmptyMessage(111);
//    }
//    public void BeginToConnect11(){
//        bindService(new Intent(this,BLEService.class), connection, Context.BIND_AUTO_CREATE);
//    }
//    public void BeginToConnect2(){
//        myHandler.sendEmptyMessage(10);
//
//    }
//    //设置广播接收器
//    private void setBroadcastReveiver() {
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
//        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
//        intentFilter.addAction(BLEService.ACTION_WRITE_DESCRIPTOR_OVER);
//        intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);
//
//        bluetoothReceiver = new BluetoothReceiver();
//        registerReceiver(bluetoothReceiver, intentFilter);
//
//    }
//    public String tmp,hex;
//    private BluetoothReceiver bluetoothReceiver = null;
//    public class BluetoothReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context arg0, Intent intent) {
//            // TODO Auto-generated method stub
//            String action = intent.getAction();
//
//
//            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){
//
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
//
//
////			byte[] data = intent.getByteArrayExtra("value");
////			Tools.setLog("log1", "data.length:"+data.length);
////			Tools.setLog("log1", "data.length[0]:"+data[0]);
////			String kk = new String(data);
//                UnityPlayer.UnitySendMessage("ScanUIPanel", "UnityGet", tmp);
//            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){
//
//
//            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
//                connected_flag = false;
//                theDevice = null;
//                myHandler.sendEmptyMessage(11);
//
//            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
//                connected_flag = true;
//                myHandler.sendEmptyMessage(12);
//            }
//        }
//    }
//    public void SendDataString(String data) {//发送常规指令
//
//        fdArrayList.add("#,"+data);
//        SendRealData();
////	  if (fdArrayList.size()>=1) {
////
////		  SendRealData();
//////		  spliteArray =fdArrayList.get(0).split(",");
//////			if (spliteArray[0].equals("#")) {//常规指令
//////				SendDataString(spliteArray[1]);
//////				 Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//////				  Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//////			}else {//速度指令
//////				 Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//////				  Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//////			}
////
////		  //Tools.mBleService.characterWrite1.setValue(hexToBytes(fdArrayList.get(0).split(",")[1]));
////		  //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
////	}
//        //Tools.setLog("log1", "SendDataString.........."+data);
//
//        //Tools.mBleService.characterWrite1.setValue(hexToBytes(data));
//        //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//    }
//    public void SendControllerSpeed(String data) {//发送速度指令
//
//        fdArrayList.add("$,"+data);
//        SendRealData();
//
//        //Tools.setLog("log1", "SendControllerSpeed.........."+data);
//
//        //Tools.mBleService.characterWrite2.setValue(hexToBytes(data));
//        //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//    }
//    private void SendRealData() {
//        spliteArray =fdArrayList.get(0).split(",");
//        if (spliteArray[0].equals("#")) {//常规指令
//            //SendDataString(spliteArray[1]);
//            Tools.mBleService.characterWrite1.setValue(hexToBytes(spliteArray[1]));
//            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
//        }else {//速度指令
//            Tools.mBleService.characterWrite2.setValue(hexToBytes(spliteArray[1]));
//            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite2);
//        }
//    }
//    public void ShowDialog(String data){
//        Message msg = new Message();
//        Bundle b = new Bundle();
//        b.putString("daa", data);
//        msg.setData(b);
//        msg.what = 9;
//        myHandler.sendMessage(msg);
//    }
//    public void SendMessageTohandler(String data) {
//
//        Tools.setLog("log1", "SendMessageTohandler.........."+data);
//
//        Message msg = new Message();
//        msg.what = 14;
//        Bundle bundle = new Bundle();
//        bundle.putString("Key", data);
//        msg.setData(bundle);
//        myHandler.sendMessage(msg);
//    }
//    public void SendCallBack(String data) {
//
//    }
//    private void scanBle() {
//        // TODO Auto-generated method stub
//        Tools.mBleService.scanBle(mLeScanCallback);
//    }
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//
//        myHandler.sendEmptyMessage(122);
//    }
//
//    @Override
//    protected void onResume() {
//        // TODO Auto-generated method stub
//        super.onResume();
//        if(!connected_flag)
//            myHandler.sendEmptyMessage(11);
//
//        //Toast.makeText(MainActivity.this, "smimiFlag.."+mimiFlag, Toast.LENGTH_SHORT).show();
//
//        if(mimiFlag)
//            BeginToConnect11();
//    }
//    @Override
//    protected void onDestroy() {
//        // TODO Auto-generated method stub
//        super.onDestroy();
//        exit_activity = true;
//        unbindService(connection);
//    }
//
//    public byte[] hexToBytes(String hexString) {
//        if (hexString == null || hexString.equals("")) {
//            return null;
//        }
//        int length = hexString.length() / 2;
//        char[] hexChars = hexString.toCharArray();
//        byte[] bytes = new byte[length];
//        String hexDigits = "0123456789ABCDEF";
//        for (int i = 0; i < length; i++) {
//            int pos = i * 2;
//            int h = hexDigits.indexOf(hexChars[pos]) << 4;
//            int l = hexDigits.indexOf(hexChars[pos + 1]);
//            if (h == -1 || l == -1) {
//                return null;
//            }
//            bytes[i] = (byte) (h | l);
//        }
//        return bytes;
//    }
//
//    public class MyConnectedThread extends Thread{
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            super.run();
//
//            try {
//                while (true) {
//                    connected_flag = false;
//                    if (exit_activity) return;
//                    Tools.setLog("log1", "connectBle..........");
//                    Tools.mBleService.connectBle(theDevice);
//
//                    for(int j=0;j<50;j++){
//
//                        if (connected_flag) {
//                            break;
//                        }
//                        sleep(100);
//                    }
//
//                    if(connected_flag)
//                        break;
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        }
//    }



