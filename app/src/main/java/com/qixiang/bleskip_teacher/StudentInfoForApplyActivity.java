package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.Util.HttpUtil;
import com.qixiang.bleskip_teacher.Util.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/27.
 */

public class StudentInfoForApplyActivity extends Activity implements View.OnClickListener{
    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    Toast.makeText(StudentInfoForApplyActivity.this,"Jsong Exception.",Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    Toast.makeText(StudentInfoForApplyActivity.this,"Http request error..",Toast.LENGTH_SHORT).show();
                    break;
                case 15:
                    Tv_name.setText("姓名： "+xsName);
                    Tv_gender.setText("性别： "+xsGender);
                    Tv_height.setText("身高： "+xssg);
                    Tv_weight.setText("体重： "+xstz);
                    Tv_area.setText("地区： "+xsArea);
                    Tv_school.setText("学校： "+xsSchool);
                    Tv_class.setText("班级： "+xsClass);
                    break;
                case 111:
                    Toast.makeText(StudentInfoForApplyActivity.this,"已通过审核。。.",Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(StudentInfoForApplyActivity.this,"已拒绝审核。。.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private String xsName = "",xsGender = "";
    private int xssg =0,xstz=0;
    private String xsArea = "",xsSchool = "",xsClass = "";
    private TextView Tv_name,Tv_gender,Tv_height,Tv_weight,Tv_area,Tv_school,Tv_class;
    private Button btn_agree,btn_refuse;
    private ImageButton btn_headPortrait;
    //private String[] stringList = {"姓名：","性别：","身高：","体重：","地区：","学校：","班级："};
    public String xsid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studetail);
        InitView();

        Intent intent = getIntent();
        xsid = intent.getStringExtra("com.qixiang.bleskip.xsid");
        Log.e(Utils.TAG,"intent String xsid:"+xsid);
        GetDetialInfo(xsid);
       // findViewById(R.id.button_backward).setOnClickListener();
    }

    public void backtostulist(View view){
        StudentInfoForApplyActivity.this.finish();
    }
    private void InitView(){
        btn_headPortrait = (ImageButton) findViewById(R.id.btn_headpor);
        btn_headPortrait.setOnClickListener(this);
        Tv_name = (TextView) findViewById(R.id.tv_stu_name);
        Tv_gender = (TextView) findViewById(R.id.tv_stu_gender);
        Tv_height = (TextView) findViewById(R.id.tv_stu_height);
        Tv_weight = (TextView) findViewById(R.id.tv_stu_weight);
        Tv_school = (TextView) findViewById(R.id.tv_stu_school);
        Tv_area = (TextView) findViewById(R.id.tv_stu_area);
        Tv_class = (TextView) findViewById(R.id.tv_stu_class);

        btn_refuse = (Button) findViewById(R.id.btn_refuse);
        btn_refuse.setOnClickListener(this);
        btn_agree = (Button) findViewById(R.id.btn_agree);
        btn_agree.setOnClickListener(this);
    }

    public void GetDetialInfo(String xsid){
        if(HttpUtil.NetState)//脱网模式
        {
            return;
        }
        if(Utils.token != null){
            try{
                Map<String,String> valueMap = new HashMap<>();
                valueMap.put("xsid",xsid);
                valueMap.put("token",Utils.token);
                HttpUtil.load(StudentInfoForApplyActivity.this,"http://usweb.wangjiayin.cn/us-web-app/xsyh/getXsyhByXsid", valueMap, new HttpUtil.OnResponseListner() {
                    @Override
                    public void onSucess(String response) {
                        Log.e(Utils.TAG,"response yes:"+response);
                        RecoginzeJson(response);
                    }

                    @Override
                    public void onError(String error) {
                        mHandler.sendEmptyMessage(12);
                    }
                });
            }catch(Exception e){
                Log.e(Utils.TAG,"exception0:"+e);
                mHandler.sendEmptyMessage(11);
            }

        }
    }

    private void RecoginzeJson(String jsongString){
        try{
            JSONTokener jsonParser = new JSONTokener(jsongString);
            JSONObject object = (JSONObject) jsonParser.nextValue();
            if(object.getInt("resultCode") == 0)
            {
                String  jsArray = object.getString("data");
                RecoginzeJson2(jsArray);
            }else {

            }
        }catch(Exception e){

        }
    }


    private String RecoginzeJson2(String JSON){
        try {
            JSONTokener jsonParser = new JSONTokener(JSON);
            JSONObject person = (JSONObject) jsonParser.nextValue();
            xsName = person.getString("uname");
            if(person.getInt("sex") == 1)
                xsGender = "男";
            else
                xsGender = "女";

            xssg = person.getInt("sg");
            xstz = person.getInt("tz");

            xsArea = person.getString("sfname")+" "+person.getString("csname")+" "+person.getString("qyname");
            xsSchool = person.getString("xyname");
            xsClass = person.getString("bjname");

            mHandler.sendEmptyMessage(15);
            return "";
            //String tokent = person.getString("token");
        } catch (JSONException ex) {
            Log.e("falter","JSONException:"+ex);
            // 异常处理代码
            return "";
        }
    }
private void ActionAgree(){
    if(HttpUtil.NetState)//脱网模式
    {
        return;
    }
        try{
            Map<String ,String> value = new HashMap<>();
            value.put("xsid",xsid);
            value.put("token",Utils.token);
            value.put("bjid",Utils.bjID);
            value.put("shr", Utils.lsID);
            Log.e(Utils.TAG,xsid+"  Utils.bjid:"+Utils.bjID+"  Utils.lsID:"+Utils.lsID);
            HttpUtil.load(StudentInfoForApplyActivity.this,"http://usweb.wangjiayin.cn/us-web-app/class/agreeJrClass", value, new HttpUtil.OnResponseListner() {
                @Override
                public void onSucess(String response) {
                    try{
                        Log.e(Utils.TAG,"ActionAgree:::::::::::::::::"+response);
                        JSONTokener jsonParser = new JSONTokener(response);
                        JSONObject object = (JSONObject) jsonParser.nextValue();
                        if(object.getInt("resultCode") == 0)
                        {
                            mHandler.sendEmptyMessage(111);
                            Intent intent = new Intent("com.qixiang.bleskip.requestfresh");
                            sendBroadcast(intent);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("resul",110);
                        StudentInfoForApplyActivity.this.setResult(RESULT_OK,intent);
                        StudentInfoForApplyActivity.this.finish();
                    }catch(Exception e){
                        Log.e(Utils.TAG,"exception1:"+e);
                        mHandler.sendEmptyMessage(11);
                    }

                }

                @Override
                public void onError(String error) {

                }
            });
        }catch(Exception e){
            Log.e(Utils.TAG,"exception2:"+e);
                mHandler.sendEmptyMessage(11);
        }
}
    private void ActionRefuse(){
        if(HttpUtil.NetState)//脱网模式
        {
            return;
        }
        try{
            Map<String ,String> value = new HashMap<>();
            value.put("xh",xsid);
            value.put("token",Utils.token);
            value.put("bjid",Utils.bjID);
            Log.e(Utils.TAG,xsid+"  Utils.bjid:"+Utils.bjID+"  Utils.lsID:"+Utils.lsID);
            HttpUtil.load(StudentInfoForApplyActivity.this,"http://usweb.wangjiayin.cn/us-web-app/class/agreeJrClass", value, new HttpUtil.OnResponseListner() {
                @Override
                public void onSucess(String response) {
                    try{
                        Log.e(Utils.TAG,"ActionRefuse:::::::::::::::::::::"+response);
                        JSONTokener jsonParser = new JSONTokener(response);
                        JSONObject object = (JSONObject) jsonParser.nextValue();
                        if(object.getInt("resultCode") == 0)
                        {
                            mHandler.sendEmptyMessage(112);
                        }

                        Intent intent2 = new Intent("com.qixiang.bleskip.requestfresh");
                        sendBroadcast(intent2);

                        Intent intent = new Intent();
                        intent.putExtra("resul",119);
                        StudentInfoForApplyActivity.this.setResult(RESULT_OK,intent);
                        StudentInfoForApplyActivity.this.finish();
                    }catch(Exception e){
                        Log.e(Utils.TAG,"exception3:"+e);
                        mHandler.sendEmptyMessage(11);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }catch(Exception e){
            Log.e(Utils.TAG,"exception4:"+e);
            mHandler.sendEmptyMessage(11);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_headpor:

                break;
            case R.id.btn_refuse:
                ActionRefuse();
                break;
            case R.id.btn_agree:
                ActionAgree();
                break;
        }
    }
}
