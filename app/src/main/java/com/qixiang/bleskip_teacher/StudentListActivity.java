package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.MyView.MyClickLister;
import com.qixiang.bleskip_teacher.MyView.MyTableRow;
import com.qixiang.bleskip_teacher.Util.HttpUtil;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForStuInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/26.
 */

public class StudentListActivity extends Activity {

    ListView lv;
    private Handler mHandler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    Toast.makeText(StudentListActivity.this,"Jsong Exception.",Toast.LENGTH_SHORT).show();
                    break;
                case 13:

                    Toast.makeText(StudentListActivity.this,"五角星未开发。。。.",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private MyClickLister nameClick = new MyClickLister(1) {
        @Override
        public void onClick(View v) {
            int og = (int)v.getTag();
            if(Utils.token != null){
                try{
                    String xsid = Utils.stuInfo[Utils.ClassIndex][og].getString("xsid");
                    Utils.bjID = Utils.stuInfo[Utils.ClassIndex][og].getString("bjid");
                    Intent intent = new Intent();
                    intent.setAction("com.qixiang.bleskip.detailinfo");
                    intent.putExtra("com.qixiang.bleskip.xsid",xsid);
                    startActivityForResult(intent,1010);

                }catch(Exception e){
                    Log.e(Utils.TAG,"exception0:"+e);
                    mHandler.sendEmptyMessage(11);
                }
            }
/*            Intent intent = new Intent(StudentListActivity.this,StudentDetailInfoActivity.class);
            startActivity(intent);*/
            mHandler.sendEmptyMessage(13);
        }
    };
    private View.OnClickListener agreeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int og = (int)v.getTag();
            if(Utils.token != null){
                try{
                    String xsid = Utils.stuInfo[Utils.ClassIndex][og].getString("xsid");
                    Utils.bjID = Utils.stuInfo[Utils.ClassIndex][og].getString("bjid");
                    Intent intent = new Intent();
                    intent.setAction("com.qixiang.bleskip.android_intent");
                    intent.putExtra("com.qixiang.bleskip.xsid",xsid);
                    startActivityForResult(intent,10);

                }catch(Exception e){
                    Log.e(Utils.TAG,"exception0:"+e);
                    mHandler.sendEmptyMessage(11);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return;
        Log.e(Utils.TAG,"requestCode:"+requestCode);
        Log.e(Utils.TAG,"resultCode:"+resultCode);
        Log.e(Utils.TAG,"intentdata:"+data.getIntExtra("resul",0));
    }
    public void backtostulist(View view){
        StudentListActivity.this.finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_studentinfo);
        lv=  (ListView) findViewById(R.id.lv_studetail);

        try{
            //lv.setOnItemClickListener();
            //Utils.stuInfo[0][]
            MyAdapterForStuInfo masi = new MyAdapterForStuInfo(Utils.stuInfo[Utils.ClassIndex],StudentListActivity.this,nameClick,agreeClick);
            // masi.setOnInnerItemOnClickListener(StudentListActivity.this);
            lv.setAdapter(masi);
        }catch (Exception e){
            Log.e(Utils.TAG,"exception get"+e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private int getFs(JSONObject obj){
        try{
            //int result = obj.getInt("fs");
            return obj.getInt("fs");
        }catch (Exception e){
            return 0;
        }
    }
}
