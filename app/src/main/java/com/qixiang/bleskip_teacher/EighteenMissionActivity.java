package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Util.Utils;

/**
 * Created by Administrator on 2018/8/2.
 */

public class EighteenMissionActivity extends Activity implements View.OnClickListener{

    private Button btn_mission1,btn_mission2,btn_mission3;
    private Button btn_mission4,btn_mission5,btn_mission6;
    private Button btn_mission7,btn_mission8,btn_mission9;

    private Button btn_mission10,btn_mission11,btn_mission12;
    private Button btn_mission13,btn_mission14,btn_mission15;
    private Button btn_mission16,btn_mission17,btn_mission18;

    private Button btn_confirm;
    private int[] indexarray;

    private int selectedflag;// 0:全选 1:全男 2:全女  -2:自由选择

    private int ModelFlag = 0;// 1:计时 2：计数

    private byte[] missionData = new byte[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eighteenmission);


        Intent intent = getIntent();
        indexarray =intent.getIntArrayExtra("indexarray");
        //String hf= intent.getStringExtra("aha");
        selectedflag = intent.getIntExtra("selectedflag",-2);

        Utils.LogE("selectedflag:55555555555"+selectedflag);
        selectedflag = 0;
        ModelFlag = 0;

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

        btn_confirm = (Button)findViewById(R.id.btn_mission_confirmgo);
        btn_confirm.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
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

                //byte hg = GetDataCount(selectedflag);
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
                    Toast.makeText(EighteenMissionActivity.this,"未开发。。。",Toast.LENGTH_SHORT).show();
                    break;
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
}
