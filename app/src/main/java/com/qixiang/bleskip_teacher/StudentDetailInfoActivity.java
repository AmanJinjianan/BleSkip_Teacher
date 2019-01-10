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
import android.widget.EditText;
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
 * Created by Administrator on 2018/12/5.
 */

public class StudentDetailInfoActivity extends Activity implements View.OnClickListener{

    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    Toast.makeText(StudentDetailInfoActivity.this,"Jsong Exception.",Toast.LENGTH_SHORT).show();
                    break;
                case 12:
                    Toast.makeText(StudentDetailInfoActivity.this,"Http request error..",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(StudentDetailInfoActivity.this,"已通过审核。。.",Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(StudentDetailInfoActivity.this,"已拒绝审核。。.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String xsName = "",xsGender = "";
    private int xssg =0,xstz=0;
    private String xsArea = "",xsSchool = "",xsClass = "";

    private TextView Tv_name,Tv_gender,Tv_height,Tv_weight,Tv_area,Tv_school,Tv_class;
    private EditText Ed_ps;
    private Button btn_delete;

    public String xsid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_thedetailinfo);
        InitView();

        Intent intent = getIntent();
        xsid = intent.getStringExtra("com.qixiang.bleskip.xsid");
        Log.e(Utils.TAG,"intent String xsid:"+xsid);
        GetDetialInfo(xsid);
    }
    private void InitView(){

        Tv_name = (TextView) findViewById(R.id.tv_stu_name22);
        Tv_gender = (TextView) findViewById(R.id.tv_stu_gender22);
        Tv_height = (TextView) findViewById(R.id.tv_stu_height22);
        Tv_weight = (TextView) findViewById(R.id.tv_stu_weight22);
        Tv_school = (TextView) findViewById(R.id.tv_stu_school22);
        Tv_area = (TextView) findViewById(R.id.tv_stu_area22);
        Tv_class = (TextView) findViewById(R.id.tv_stu_class22);

        Ed_ps = (EditText) findViewById(R.id.ed_stu_ps);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
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
                HttpUtil.load(StudentDetailInfoActivity.this,"http://usweb.wangjiayin.cn/us-web-app/xsyh/getXsyhByXsid", valueMap, new HttpUtil.OnResponseListner() {
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
    public void backtolastpage(View v){
        StudentDetailInfoActivity.this.finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                Toast.makeText(StudentDetailInfoActivity.this,"未开发。。",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
