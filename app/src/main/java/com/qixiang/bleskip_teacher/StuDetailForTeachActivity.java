package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForStuTeach;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/1.
 */

public class StuDetailForTeachActivity extends Activity implements MyAdapterForStuTeach.InnerItemOnclickListener,View.OnClickListener{
    private ListView lv;
    ArrayList mData;
    private int theFourIndex;//全选，全不选，全男，全女，四个索引
    int checkNum = 0;//累计选中人数
    private TextView allCount;
    private Button btnConfirm;
    public MyAdapterForStuTeach mfs;

    public Spinner thespinner;
    private int seletedFlag;

    private CompoundButton.OnCheckedChangeListener listener2 = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {}
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuinfoforteach);

        theFourIndex = 0;
        seletedFlag = -1;

        allCount = (TextView) findViewById(R.id.tv_selectedcount);

        btnConfirm = (Button)findViewById(R.id.btn_selectconfirm);
        btnConfirm.setOnClickListener(this);

        thespinner = (Spinner)findViewById(R.id.spinnerall);

        byte[] datas = intToByteArray1(Utils.bjNum);
        InitList();
        ZuWang(datas);

    }
    //int转byte数组 12--->00,00,00,12
    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
//必须把我们要的值弄到最低位去，有人说不移位这样做也可以， result[0] = (byte)(i  & 0xFF000000);
//，这样虽然把第一个字节取出来了，但是若直接转换为byte类型，会超出byte的界限，出现error。再提下数//之间转换的原则（不管两种类型的字节大小是否一样，原则是不改变值，内存内容可能会变，比如int转为//float肯定会变）所以此时的int转为byte会越界，只有int的前三个字节都为0的时候转byte才不会越界。虽//然 result[0] = (byte)(i  & 0xFF000000); 这样不行，但是我们可以这样 result[0] = (byte)((i  & //0xFF000000) >>24);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }
//发送组网命令
    private void ZuWang(byte[] d){
        if(d.length !=4){
            Toast.makeText(StuDetailForTeachActivity.this,"网络号出错",Toast.LENGTH_SHORT).show();
            return;
        }
        byte[] data = {0x06,0x24,d[0],d[1],d[2],d[3],(byte)(Utils.GetMissionCode())};
        //Tools.mBleService.characterWrite1.setValue(Tools.hexToBytes("06241112131410"));
        Tools.mBleService.characterWrite1.setValue(data);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
    }
public void backtomain(View view){
        StuDetailForTeachActivity.this.finish();
}
    private void ChangeSwich(int index){

        switch (index){
            //全不选
            case 0:
                for(int i = 0;i<mfs.getIsSelected().size();i++){
                    mfs.getIsSelected().put(i,false);
                }
                //HashMap<Integer,Boolean> op = mfs.getIsSelected();
                checkNum = 0;
                break;
            //全选
            case 1:
                for(int i = 0;i<mfs.getIsSelected().size();i++){
                    mfs.getIsSelected().put(i,true);
                }
                seletedFlag = 0;
                checkNum = mfs.getIsSelected().size();
                break;
            //全男
            case 2:
                for(int i = 0;i<mfs.getIsSelected().size();i++){
                    try{
                        if(mfs.mList[i].getInt("sex")==1){
                            mfs.getIsSelected().put(i,true);
                            checkNum++;
                        }
                        else
                            mfs.getIsSelected().put(i,false);
                    }catch(Exception e){
                        Intent intent = new Intent("com.qixiang.jsonexception");
                        sendBroadcast(intent);
                    }
                }
                seletedFlag = 1;
                break;
            //全女
            case 3:
                for(int i = 0;i<mfs.getIsSelected().size();i++){
                    try{
                        if(mfs.mList[i].getInt("sex")==1)
                            mfs.getIsSelected().put(i,false);
                        else{
                            mfs.getIsSelected().put(i,true);
                            checkNum++;
                        }
                    }catch(Exception e){
                        Intent intent = new Intent("com.qixiang.jsonexception");
                        sendBroadcast(intent);
                    }
                }
                seletedFlag = 2;
                break;
        }
    }
    JSONObject[] hj;
    private void InitList(){
        lv = (ListView)findViewById(R.id.lv_stuinfoforteach);

        int index = Utils.ClassIndex2;
        //*******
        hj =Utils.stuInfo[index];

        //hj = new JSONObject[8];
        try{
            if(hj[0].get("xsname").toString().equals("null"))
            {
                Toast.makeText(StuDetailForTeachActivity.this,"这个班还没有人!",Toast.LENGTH_SHORT).show();
            }else {
                /*for(int i = 0;i<hj.length;i++){
                    JSONObject hjy = new JSONObject();
                    hjy.put("xsname",hj[i].get("xsname"));
                    if(i%2 == 0)
                        hjy.put("sex",1);
                    else
                        hjy.put("sex",2);
                    hjy.put("fs",String.valueOf(i));
                    hj[i] = hjy;
                }*/
                thespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Utils.LogE("position:::::::::::"+i);
                        theFourIndex = i;
                        checkNum = 0;
                        ChangeSwich(theFourIndex);

                        mfs.notifyDataSetChanged();
                        allCount.setText("已经选择学生"+String.valueOf(checkNum)+"人");

                        try {
                            //以下三行代码是解决问题所在
                            Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
                            field.setAccessible(true);	//设置mOldSelectedPosition可访问
                            field.setInt(thespinner, AdapterView.INVALID_POSITION); //设置mOldSelectedPosition的值
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }

                });

                //MyAdapterForStuTeach mfs = new MyAdapterForStuTeach(Utils.stuInfo[0],StuDetailForTeachActivity.this,listener2);
                mfs = new MyAdapterForStuTeach(hj,StuDetailForTeachActivity.this,listener2);
                mfs.setOnInnerItemOnClickListener(this);
                lv.setAdapter(mfs);

                // 绑定listView的监听器
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                        MyAdapterForStuTeach.ViewHolder holder = (MyAdapterForStuTeach.ViewHolder) arg1.getTag();
                        // 改变CheckBox的状态
                        holder.yesOrNo.toggle();
                        // 将CheckBox的选中状况记录下来
                        mfs.getIsSelected().put(arg2, holder.yesOrNo.isChecked());
                        seletedFlag = -1;
                        // 调整选定条目
                        if (holder.yesOrNo.isChecked() == true) {
                            checkNum++;
                        } else {
                            checkNum--;
                        }
                        // 用TextView显示
                        //tv_show.setText("已选中"+checkNum+"项");
                        Log.e("falter","check:"+checkNum);

                        allCount.setText("已经选择学生"+String.valueOf(checkNum)+"人");
                    }
                });
            }



        }catch(Exception e){

        }
    }

    @Override
    public void itemClick(View v) {

    }
    public void onClick(View v){
        if(v.getId() == R.id.btn_selectconfirm){

            if(checkNum == 0){
                Toast.makeText(StuDetailForTeachActivity.this,"请选择至少一个人",Toast.LENGTH_SHORT).show();
                return;
            }
            Utils.selectCount = checkNum;

            if(checkNum == mfs.getCount())
                seletedFlag = 0;
            int maIndex = 0;
            int[] selectIndexArray = new int[checkNum];
            for (int i = 0;i<mfs.getIsSelected().size();i++){
                if(mfs.getIsSelected().get(i))
                selectIndexArray[maIndex++] = i;
            }
            ;

//            Intent intent = new Intent(StuDetailForTeachActivity.this,EighteenMissionActivity.class);
//            intent.putExtra("indexarray",selectIndexArray);
//            intent.putExtra("selectedflag",seletedFlag);
//            //intent.putExtra("aha",hj);
//            startActivity(intent);
//            StuDetailForTeachActivity.this.finish();
        }
    }
}
