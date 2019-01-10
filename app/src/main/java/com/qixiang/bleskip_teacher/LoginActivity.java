package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.DB.StuDBHelper;
import com.qixiang.bleskip_teacher.Model.StuTrainGoingInfo;
import com.qixiang.bleskip_teacher.Util.HttpUtil;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/3.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText phoneNum,phoneCode;
    private Button getCode;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Toast.makeText(LoginActivity.this,"验证成功",Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(LoginActivity.this,MainActivity.class);

                    handler.postDelayed(runnable, 2000);//每两秒执行一次runnable.
                    startActivity(it);
                    //finish();
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this,"验证失败",Toast.LENGTH_SHORT).show();break;
                case 10:
                    Toast.makeText(LoginActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();break;
                case 11:
                    Toast.makeText(LoginActivity.this,"验证码发送失败",Toast.LENGTH_SHORT).show();break;
                case 100:
                    CreateAlertDialog();
            }
        }
    };
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            Utils.LogE("postDelayed.................");
            if(Utils.WXFlag && Utils.openID != null)//每隔十分钟
            {
                handler.postDelayed(this, 600000);
                LoginByWechat2();
            }
        }
    };

    private void CalculateData(){
//        List<StuTrainGoingInfo> modelList = new LinkedList<StuTrainGoingInfo>();
//        StuTrainGoingInfo su1 = new StuTrainGoingInfo(1,(byte)0x02,new byte[]{0x02,0x02},new byte[]{0x02,0x02});
//        modelList.add(su1);
//        StuTrainGoingInfo su2 = new StuTrainGoingInfo(1,(byte)0x21,new byte[]{0x02,0x02},new byte[]{0x05,0x02});
//        modelList.add(su2);
//        StuTrainGoingInfo su3 = new StuTrainGoingInfo(21,(byte)0x22,new byte[]{0x02,0x02},new byte[]{(byte)0xA2,0x02});
//        modelList.add(su3);
//
//        int starttime = 0;
//        for(int i=0;i<modelList.size();i++){
//            if(i ==0){
//               // modelList.get(i).getTimeState()
//                continue;
//            }
//            Utils.LogE("data0000000:"+modelList.get(i).getTimeState());
//
//        }
    }

    private void TimeCha(byte[] data1,byte[] data2){
            int cha = (((int)data1[1]&0xFF)*256+((int)data1[0]&0xFF))-(((int)data2[1]&0xFF)*256+((int)data2[0]&0xFF));
    }
    List<byte[]> hahaha = new LinkedList<>();
    private void Trainning(byte[] dataaray){
        byte[] chaArray;
        if(dataaray[0] == 9) {
            chaArray = null;
        }else {
            chaArray = new byte[dataaray[0]-9];
            for(int i=0;i<chaArray.length;i++){
                chaArray[i] = dataaray[9+i];
            }
        }

        hahaha.add(chaArray);
    }
    private SimpleDateFormat simpleDateFormat;
    private String GetTableName(String s){
        StringBuilder result=new StringBuilder();
        result.append(s.substring(0,4)).append(s.substring(5,7)).append(s.substring(8,10)).append(s.substring(12,14)).append(s.substring(15,17)).append(s.substring(18));
        return result.toString();
    }

    private void CreateAlertDialog(){
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("网络异常").
                setMessage("是否启用“脱网模式”？").
                setIcon(R.drawable.loading).
                setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // TODO Auto-generated method stub
                        HttpUtil.NetState = true;
                        mHandler.sendEmptyMessage(0);
                    }
                }).
                setNegativeButton("不", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                }).
                create();
        alertDialog.show();
    }
    //进行微信登录
    private void LoginByWechat(){
        if(Utils.openID.equals("")) return;
        Map<String,String> map=new HashMap<>();
        map.put("openid",Utils.openID);
        HttpUtil.load(LoginActivity.this,"http://usweb.wangjiayin.cn/us-web-app/login/ls/wxlogin", map, new HttpUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Log.e("falter","onSuccess Login:"+response);
                if(RecoginzeJson(response) == 0)
                    mHandler.sendEmptyMessage(0);
                else
                    mHandler.sendEmptyMessage(1);
            }
            @Override
            public void onError(String error) {
                Utils.LogE("onError.................");
                mHandler.sendEmptyMessage(100);
            }
        });
    } //定时器登录进行验证
    private void LoginByWechat2(){
        Map<String,String> map=new HashMap<>();
        map.put("openid",Utils.openID);
        HttpUtil.load(LoginActivity.this,"http://usweb.wangjiayin.cn/us-web-app/login/ls/wxlogin", map, new HttpUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Log.e("falter","onSuccess LoginByWechat2:"+response);
                RecoginzeJson(response);
            }
            @Override
            public void onError(String error) {
                Utils.LogE("onError.................");
            }
        });
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneNum = (EditText) findViewById(R.id.et_login_phonenum);
        phoneCode = (EditText) findViewById(R.id.et_login_phonereturnconde);

        findViewById(R.id.btn_sendcode).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_weixinlogin).setOnClickListener(this);
        findViewById(R.id.btn_loginout).setOnClickListener(this);

        byte[] ddf = Utils.intToButeArray(23);


        /*StuDBHelper dbHelper = new StuDBHelper(LoginActivity.this,Utils.SDLName,null,1);
        SQLiteDatabase dbQuery = dbHelper.getReadableDatabase();
        dbHelper.deleteDatabase(LoginActivity.this);*/

//        Cursor cursor = dbQuery.query("table20180505151526",null,null,null,null,null,null);
//        while (cursor.moveToNext()){
//            String se = cursor.getString(cursor.getColumnIndex("stuname"));
//
//            Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkoo333333333333stuname"+se);
//        }
//        dbQuery.close();

        int ff = (Integer.valueOf(0x00)&0xff)*256+(Integer.valueOf(0x02)&0xff);
        byte[] jj = {0x15};

        Utils.LogE("jjjjjjjjjjjjjj:"+HttpUtil.isNetworkAvailable(LoginActivity.this));

        byte[] gf =new byte[] {0x12,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02,0x02};
        hahaha.add(gf);
        gf = new byte[]{0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12,0x12};

        hahaha.add(gf);
        byte[] dd = new byte[]{(byte)0xB8,0x0B};
        int hh = 7879;

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd;HH:mm:ss");// HH:mm:ss
        String timeSting = simpleDateFormat.format(new Date(System.currentTimeMillis()));

        Log.e("falter","String.........oncreate....................");


        //Utils.writeFileData("data110","hahhajhhajh",LoginActivity.this);
      /*  HFHFHF.add(new StuTrainGoingInfo(1,(byte)(dataaray[0] - 8),
                        new byte[]{dataaray[5],dataaray[6]},
                        new byte[]{dataaray[7],dataaray[8]},chaArray));

        byte[] time1 = new byte[]{(byte)0xA6,0x00};
        byte[] chaArray = new byte[]{0x34,0x35};
        HFHFHF.add(new StuTrainGoingInfo(1,(byte)2,
                null,
                time1,chaArray));

        HFHFHF.add(new StuTrainGoingInfo(1,(byte)(dataaray[0] - 8),
                new byte[]{dataaray[5],dataaray[6]},
                new byte[]{dataaray[7],dataaray[8]},chaArray));*/
        //TimeCha(null,null);
    }
    LinkedList<StuTrainGoingInfo> HFHFHF = new LinkedList<>();
    int startTime=0,midTime = 0;
    private int CalculateLoseCount(LinkedList data){
        int lostCount = 0;
        byte[] pp = new byte[2];
        for(int i=0;i<data.size();i++){
            if(i==0){
                StuTrainGoingInfo stf = (StuTrainGoingInfo)(data.get(i));
                pp = stf.timeState;
                startTime = (Integer.valueOf(pp[1])&0xff)*256+(Integer.valueOf(pp[0])&0xff);
                for(int p=0;p<stf.thisPacketCount-1;p++){
                    startTime = startTime+(Integer.valueOf(stf.chaArray[p])&0xff) ;
                }
            }else {
                StuTrainGoingInfo stf = (StuTrainGoingInfo)(data.get(i));
                pp = stf.timeState;
                midTime = (Integer.valueOf(pp[1])&0xff)*256+(Integer.valueOf(pp[0])&0xff);
                if(midTime - startTime>100){
                    lostCount++;
                }
                for(int p=0;p<stf.thisPacketCount-1;p++){
                    startTime = midTime+(Integer.valueOf(stf.chaArray[p])&0xff) ;
                }
                if(stf.thisPacketCount==1)
                    startTime = midTime;
            }
        }
        int gggk =455;
        return lostCount;
    }
    String openId="";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("skiprope_demo", Context.MODE_PRIVATE);
        openId = sp.getString("openid", "");

        Utils.LogE("onResume.................:"+openId);
        //Utils.openID = "oeysJ49APOcCO1oyYmqkP1g9ATOE";
        if(Utils.WXFlag){
            Utils.openID = openId;
            LoginByWechat();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){
            Login2();
        }else if(v.getId() == R.id.btn_sendcode){
            SendCode();
            //Toast.makeText(LoginActivity.this,"未开发",Toast.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.btn_weixinlogin){
            //检查网络是否连通
            if(!HttpUtil.isNetworkAvailable(LoginActivity.this)){
                CreateAlertDialog();
                return;
            }
            if(!openId.equals("")){
                Utils.WXFlag = true;
                Utils.openID = openId;
                Utils.LogE("onResume.................:"+Utils.openID);
                LoginByWechat();
            }else {
                StartWX();
                Toast.makeText(LoginActivity.this,"正在打开小程序..",Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.btn_loginout){
            Toast.makeText(LoginActivity.this,"退出微信账号..",Toast.LENGTH_SHORT).show();
            try{
                SharedPreferences sp = getSharedPreferences("skiprope_demo", Context.MODE_PRIVATE);
                sp.edit().putString("openid", "").commit();
                openId = "";
                Utils.WXFlag = false;
                Utils.openID = "";
            }catch (Exception e){
            }
        }
    }

    private void StartWX(){
      /*  String appId = "wxe571406b7f0d80e3"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(LoginActivity.this, appId);
        api.registerApp(appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_7800b9880f49"; // 填小程序原始id
        req.path = "/pages/index/index?key=APP_login";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);*/

        String appId = "wxe571406b7f0d80e3"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(LoginActivity.this, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_7800b9880f49"; // 填小程序原始id
        req.path = "/pages/index/index?key=APP_login";                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }
    private void Login2(){

        Map<String,String> map=new HashMap<>();
        map.put("dhhm",phoneNum.getText().toString());
        map.put("yzm",phoneCode.getText().toString());

        Utils.LogE("phoneNum:  "+phoneNum.getText().toString());
        Utils.LogE("phoneCode:  "+phoneCode.getText().toString());

        HttpUtil.load(LoginActivity.this,"http://usweb.wangjiayin.cn/us-web-app/login/ls/dhhmlogin", map, new HttpUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Utils.LogE("手机登录返回结果:  "+response);
//                if(RecoginzeJson(response) == 0)
//                    mHandler.sendEmptyMessage(10);
//                else
//                    mHandler.sendEmptyMessage(11);
            }
            @Override
            public void onError(String error) {}
        });

//        String filePath = "/sdcard/Test/";
//        String fileName = "log.txt";
//        Utils.writeTxtToFile("txt content", filePath, fileName);
    }
private void Login(){
    Map<String,String> map=new HashMap<>();
    map.put("dhhm",phoneNum.getText().toString());
    map.put("yzm",phoneCode.getText().toString());
    HttpUtil.load(LoginActivity.this,"http://usweb.wangjiayin.cn/us-web-app/login/ls/dhhmlogin", map, new HttpUtil.OnResponseListner() {
        @Override
        public void onSucess(String response) {

            if(RecoginzeJson(response) == 0)
                mHandler.sendEmptyMessage(0);
            else
                mHandler.sendEmptyMessage(1);
        }
        @Override
        public void onError(String error) {}
    });
}
private void SendCode(){
    Map<String,String> map=new HashMap<>();
    map.put("dhhm",phoneNum.getText().toString());
    HttpUtil.load(LoginActivity.this,"http://usweb.wangjiayin.cn/us-web-app/login/sendYzm", map, new HttpUtil.OnResponseListner() {
        @Override
        public void onSucess(String response) {
            Utils.LogE("发验证码返回结果:  "+response);
            if(RecoginzeJson(response) == 0)
                mHandler.sendEmptyMessage(10);
            else
                mHandler.sendEmptyMessage(11);
        }
        @Override
        public void onError(String error) {}
    });
}


    private int RecoginzeJson(String JSON){
        Log.e("falter","JSONException:"+JSON);
        try {
            JSONTokener jsonParser = new JSONTokener(JSON);
            // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
            // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
            JSONObject person = (JSONObject) jsonParser.nextValue();
            if (person.getInt("resultCode") == 0){
                Utils.token  = person.getString("token");
                Utils.lsID = RecoginzeJson2(person.getString("data"));

                return 0;
            }
            return 1;
            //String tokent = person.getString("token");
        } catch (JSONException ex) {
            Log.e("falter","JSONException:"+ex);
            // 异常处理代码
            return 1;
        }
    }
    private String RecoginzeJson2(String JSON){
        try {
            JSONTokener jsonParser = new JSONTokener(JSON);
            JSONObject person = (JSONObject) jsonParser.nextValue();
            Utils.xyID =person.getString("xyid");
            return person.getString("xh");
            //String tokent = person.getString("token");
        } catch (JSONException ex) {
            Log.e("falter","JSONException:"+ex);
            // 异常处理代码
            return "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.LogE(" ...onActivityResult.........resultCode:"+resultCode+"   requestCode:"+requestCode);
        Utils.LogE(" ...onActivityResult.........data:"+data.getStringExtra("data"));
    }
}
