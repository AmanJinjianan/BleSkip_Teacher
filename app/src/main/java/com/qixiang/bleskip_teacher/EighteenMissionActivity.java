package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.MyView.CreateClassDialog;
import com.qixiang.bleskip_teacher.MyView.CreateMissionDialog;
import com.qixiang.bleskip_teacher.Util.HttpUtil;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/2.
 */

public class EighteenMissionActivity extends Activity implements View.OnClickListener,View.OnLongClickListener{

    private Button btn_mission1,btn_mission2,btn_mission3;
    private Button btn_mission4,btn_mission5,btn_mission6;
    private Button btn_mission7,btn_mission8,btn_mission9;

    private Button btn_mission10,btn_mission11,btn_mission12;
    private Button btn_mission13,btn_mission14,btn_mission15;
    private Button btn_mission16,btn_mission17,btn_mission18;

    private Button btn_confirm;

    private List<Button> theSeveralButton;
    private int[] indexarray;

    private int selectedflag;// 0:全选 1:全男 2:全女  -2:自由选择

    private int ModelFlag = 0;// 1:计时 2：计数

    private byte[] missionData = new byte[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighteenmission);


        /*Intent intent = getIntent();
        indexarray =intent.getIntArrayExtra("indexarray");
        //String hf= intent.getStringExtra("aha");
        selectedflag = intent.getIntExtra("selectedflag",-2);

        Utils.LogE("selectedflag:55555555555"+selectedflag);
        selectedflag = 0;
        ModelFlag = 0;*/

        theSeveralButton = new ArrayList<Button>();
        InitView();
    }
    //返回上一页事件
    public void backtomainpage(View v){
        EighteenMissionActivity.this.finish();
    }
    //派发任务事件
    public void paifamission(View v){
        //Intent intent = new Intent(EighteenMissionActivity.this,);
        //startActivity(intent);
    }
    private void InitView(){
        btn_mission1 = (Button)findViewById(R.id.btn_mission_1);
        btn_mission1.setOnClickListener(this);
        btn_mission1.setText("一分钟");

        btn_mission2 = (Button)findViewById(R.id.btn_mission_2);
        btn_mission2.setOnClickListener(this);
        btn_mission2.setText("30秒");

        btn_mission3 = (Button)findViewById(R.id.btn_mission_3);
        btn_mission4 = (Button)findViewById(R.id.btn_mission_4);
        btn_mission5 = (Button)findViewById(R.id.btn_mission_5);
        btn_mission6 = (Button)findViewById(R.id.btn_mission_6);
        btn_mission7 = (Button)findViewById(R.id.btn_mission_7);
        btn_mission8 = (Button)findViewById(R.id.btn_mission_8);
        btn_mission9 = (Button)findViewById(R.id.btn_mission_9);
        theSeveralButton.add(btn_mission1);

        btn_mission10 = (Button)findViewById(R.id.btn_mission_10);
        btn_mission10.setText("180个");
        btn_mission10.setOnClickListener(this);

        btn_mission11 = (Button)findViewById(R.id.btn_mission_11);
        btn_mission11.setText("100个");
        btn_mission11.setOnClickListener(this);

        btn_mission12 = (Button)findViewById(R.id.btn_mission_12);
        btn_mission13 = (Button)findViewById(R.id.btn_mission_13);
        btn_mission14 = (Button)findViewById(R.id.btn_mission_14);
        btn_mission15 = (Button)findViewById(R.id.btn_mission_15);
        btn_mission16 = (Button)findViewById(R.id.btn_mission_16);
        btn_mission17 = (Button)findViewById(R.id.btn_mission_17);
        btn_mission18 = (Button)findViewById(R.id.btn_mission_18);

        theSeveralButton.add(btn_mission1);
        theSeveralButton.add(btn_mission2);
        theSeveralButton.add(btn_mission3);
        theSeveralButton.add(btn_mission4);
        theSeveralButton.add(btn_mission5);
        theSeveralButton.add(btn_mission6);
        theSeveralButton.add(btn_mission7);
        theSeveralButton.add(btn_mission8);
        theSeveralButton.add(btn_mission9);

        theSeveralButton.add(btn_mission10);
        theSeveralButton.add(btn_mission11);
        theSeveralButton.add(btn_mission12);
        theSeveralButton.add(btn_mission13);
        theSeveralButton.add(btn_mission14);
        theSeveralButton.add(btn_mission15);
        theSeveralButton.add(btn_mission16);
        theSeveralButton.add(btn_mission17);
        theSeveralButton.add(btn_mission18);

        for(Button the:theSeveralButton){
            the.setOnClickListener(this);
            the.setOnLongClickListener(this);

            //the.getTag().get
            //Utils.LogE("size =========================== 889:"+the.get());
        }

        btn_confirm = (Button)findViewById(R.id.btn_mission_confirmgo);
        btn_confirm.setOnClickListener(this);
    }
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_missiondialog_cancle){
                createMisionDialog.dismiss();
            }else if(v.getId() == R.id.btn_missiondialog_confirm){
                /*classNameString = "";
                classNameString = createMisionDialog.className.getText().toString().trim();
                if( classNameString != "" &&  classCountString!=""){
                    //进行班级创建
                   // Log.e(TAG,"hahhah :"+classNameString +"  "+classCountString+"   "+classCreateYears);

                    CreateMission(classCreateYears,classNameString);
                    createMisionDialog.dismiss();
                }*/
            }
        }
    };
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            theType ="计时";
            String[] missType = getResources().getStringArray(R.array.missiontype);
            theType  = missType[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            theType ="计时";
        }
    };
    public CreateMissionDialog createMisionDialog;
    public String classNameString,classCountString;
    private String theType ="计时";
    public void showEditDialog(View view) {
        createMisionDialog = new CreateMissionDialog(this,R.style.AppTheme,onClickListener,onItemSelectedListener);
        createMisionDialog.show();
    }
      //Tools.mBleService.characterWrite1.setValue(hexToBytes(string));
      //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);

    //分别上个页面是 “全选”，“全男”，“全女”
    private byte GetDataCount(int selectedflag){
        switch (selectedflag){
            case 0:
                return 0;
            case 1:
                return -1;
            case 2:
                return -2;
        }
        return -3;
    }

    void CreateMission(){}
    @Override
    public void onClick(View v) {

        Utils.LogE("selectedflag:55555555555");
        switch (v.getId()){
            case R.id.btn_mission_1:
                ModelFlag = 1;
                Utils.ModelFlag = ModelFlag;
                Utils.taskName = "一分钟";

                missionData[0] =0x70;
                missionData[1] = 0x17;
                //byte hg = GetDataCount(selectedflag);
                break;
            case R.id.btn_mission_2:
                ModelFlag = 1;
                Utils.ModelFlag = ModelFlag;
                Utils.taskName = "30秒";

                missionData[0] =(byte)0xB8;
                missionData[1] = 0x0B;
                break;
            case R.id.btn_mission_10://180个
                ModelFlag = 2;
                Utils.ModelFlag = ModelFlag;
                Utils.taskName = "180个";

                missionData[0] =(byte)0xB4;
                missionData[1] = 0x00;
                break;
            case R.id.btn_mission_11://100个
                ModelFlag = 2;
                Utils.ModelFlag = ModelFlag;
                Utils.taskName = "100个";

                missionData[0] =(byte)0x64;
                missionData[1] = 0x00;
                break;
            case R.id.btn_mission_confirmgo:
                ReadyGO(ModelFlag,selectedflag,indexarray);
                break;
           default:
               ButtonSwitch(v.getId());
                   // Toast.makeText(EighteenMissionActivity.this,"未开发。。。",Toast.LENGTH_SHORT).show();
                    break;
        }
    }
    
    int buttonFlag=0;
    private void ButtonSwitch(int ViewId){
        showEditDialog(null);
        switch (ViewId){
            case R.id.btn_mission_3: buttonFlag=3; break;
            case R.id.btn_mission_4: buttonFlag=4; break;
            case R.id.btn_mission_5: buttonFlag=5; break;
            case R.id.btn_mission_6: buttonFlag=6; break;
            case R.id.btn_mission_7: buttonFlag=7; break;
            case R.id.btn_mission_8: buttonFlag=8; break;
            case R.id.btn_mission_9: buttonFlag=9; break;

            case R.id.btn_mission_12: buttonFlag=12; break;
            case R.id.btn_mission_13: buttonFlag=13; break;
            case R.id.btn_mission_14: buttonFlag=14; break;
            case R.id.btn_mission_15: buttonFlag=15; break;
            case R.id.btn_mission_16: buttonFlag=16; break;
            case R.id.btn_mission_17: buttonFlag=17; break;
            case R.id.btn_mission_18: buttonFlag=18; break;
        }
    }

    private void ReadyGO(int modelFlag,int selectedflag, int[] indexarray) {

        if(modelFlag == 0){
            Toast.makeText(EighteenMissionActivity.this,"请选择一种训练模式",Toast.LENGTH_SHORT).show();
            return;
        }
        if(indexarray.length<=13){}else {}

        Intent intent = new Intent(EighteenMissionActivity.this,TeachGoingActivity.class);
        intent.putExtra("indexarray",indexarray);
        intent.putExtra("modelFlag",modelFlag);
        intent.putExtra("selectedflag",selectedflag);
        intent.putExtra("misData",missionData);

        startActivity(intent);
        finish();
    }

    Map<String,String> map2=new HashMap<>();
    private void CreateClass(String njid,String bjName){

        if(HttpUtil.NetState)//脱网模式
        {
            return;
        }
        map2.put("taskname","9d55621261fd41c0823b40cc823efcf6");
        map2.put("token",Utils.token);
        map2.put("sl",Utils.xyID);

        map2.put("lsid",njid);
        map2.put("tasktype","9d55621261fd41c0823b40cc823efcf6");

        HttpUtil.load(EighteenMissionActivity.this,"http://usweb.wangjiayin.cn/us-web-app/taskPlan/createTaskTemp", map2, new HttpUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Utils.LogE("size =========================== 1000:"+response);
            }

            @Override
            public void onError(String error) {}
        });
    }

    public void Save(Integer position,String name, Integer valueOf,String buttonName)
     {
                 //保存文件名字为"shared",保存形式为Context.MODE_PRIVATE即该数据只能被本应用读取
                 SharedPreferences preferences=EighteenMissionActivity.this.getSharedPreferences(buttonName,Context.MODE_PRIVATE);

                 SharedPreferences.Editor editor=preferences.edit();
                 editor.putInt("position",position);//代表界面上十八个位置 依次为1---18
                 editor.putString("taskname", name);//任务名称
                 editor.putInt("lsid", valueOf);//老师ID

                    editor.putString("tasktype", name);//任务类型
                    editor.putInt("sl", valueOf);//数量

                 editor.commit();//提交数据
     }

    @Override
    public boolean onLongClick(View v) {

        Toast.makeText(EighteenMissionActivity.this,"删除操作未开发...",Toast.LENGTH_SHORT).show();
        return true;
    }
}
