package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.DB.StuDBHelper;
import com.qixiang.bleskip_teacher.Model.StuGameResult;
import com.qixiang.bleskip_teacher.Model.StuTrainGoingInfo;
import com.qixiang.bleskip_teacher.MyView.TwoSelectDialog;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForGameResult;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForStuTeach;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForTeachGoing;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/8/29.
 */

public class GameResultActivity extends Activity implements View.OnClickListener,MyAdapterForGameResult.InnerItemOnclickListener{

    private SimpleDateFormat simpleDateFormat;
    private TextView tv1,tv2,numCount;
    private Button btn_over,btn_comfirm;
    private ListView lv;

    private ArrayList<StuTrainGoingInfo> data2;
    private int listIndex;
    private int seletedFlag;
    int checkNum = 0;//累计选中人数

    public MyAdapterForGameResult mft;
    //记录选中的所有人的index
    private int[] selectedIndexArray;

    private TwoSelectDialog twoSelectDialog;

    int[] stuInClassIndex;

    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            listIndex = i;
            twoSelectDialog = new TwoSelectDialog(GameResultActivity.this,4);
            twoSelectDialog.show();
            twoSelectDialog.btn_up.setOnClickListener(GameResultActivity.this);
            twoSelectDialog.btn_down.setOnClickListener(GameResultActivity.this);
            return false;
        }
    };
    private String GetTableName(String s){
        StringBuilder result=new StringBuilder();
        result.append(s.substring(0,4)).append(s.substring(5,7)).append(s.substring(8,10)).append(s.substring(12,14)).append(s.substring(15,17)).append(s.substring(18));
        return "table"+result.toString();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        data2 = (ArrayList<StuTrainGoingInfo>) getIntent().getSerializableExtra("OverInfo");

        // srcFlag==2 ：从历史任务中跳转过来的   srcFlag==1 ：正常流程过来的
        int srcFlag = getIntent().getIntExtra("srcFlag",-2);

        //总的人数
        int PeopleCount = getIntent().getIntExtra("PeopleCount",-2);
        //接收到任务的人数
        int gotPeopleCount = getIntent().getIntExtra("gotPeopleCount",0);
        //完成任务的人数
        int overPeopleCount= getIntent().getIntExtra("overPeopleCount",-2);

        setContentView(R.layout.activity_gameresult);
        InitView(PeopleCount,gotPeopleCount,overPeopleCount);
        InitListview(data2,srcFlag);

        if(srcFlag==1){

            stuInClassIndex = getIntent().getIntArrayExtra("stuInClassIndex");
            simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
            String timeSting = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            SaveDataToSQL(timeSting,Utils.ModelFlag,Utils.taskName,PeopleCount,overPeopleCount);
            SaveDetailDataDataToSQL(GetTableName(timeSting),data2);
        }
    }
    public void backtomain(View view){
        Intent intent  = new Intent();
        GameResultActivity.this.setResult(1111,intent);
        //startActivity(intent);
        GameResultActivity.this.finish();
    }
    private void SaveDataToSQL(String timeString,int tasktype,String taskname,int taskpeoplecount,int taskpeoplecompletecount){
        StuDBHelper dbHelper = new StuDBHelper(GameResultActivity.this,Utils.SDLName,null,1);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("number_id",110);
        cv.put("tasktype",tasktype);
        cv.put("taskname",taskname);
        cv.put("tasktime",timeString);
        cv.put("taskpeoplecount",taskpeoplecount);
        cv.put("taskpeoplecompletecount",taskpeoplecompletecount);

        if(Utils.SDL_table_Name.equals("")) {
            Toast.makeText(GameResultActivity.this,"table_name_exception",Toast.LENGTH_SHORT).show();
            return;
        }else {
            Toast.makeText(GameResultActivity.this,"table_name "+Utils.SDL_table_Name,Toast.LENGTH_SHORT).show();
        }

        dbWrite.insert(Utils.SDL_table_Name,null,cv);
        dbHelper.close();
    }
    private void SaveDetailDataDataToSQL(String timeString,ArrayList<StuTrainGoingInfo> data){
        StuDBHelper dbHelper = new StuDBHelper(GameResultActivity.this,Utils.SDLName,null,1);

        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        dbHelper.CreateTable(dbWrite,timeString);

        int[] gotCountList= new int[data.size()];

            for(int i = 0;i<gotCountList.length;i++) {
                ContentValues cv = new ContentValues();
                cv.put("tasktime", "");
                cv.put("tasktype", 1);
                cv.put("taskname", "");
                cv.put("taskcontent", "content1");
                cv.put("stuid", data.get(i).xh);
                cv.put("stuname", data.get(i).name);
                cv.put("stusex", data.get(i).sex);
                cv.put("stusuccesssount", data.get(i).jumpCount);
                cv.put("stufailcount", data.get(i).lostCount);
                cv.put("stutime", "time11");
                cv.put("stuscore", 18);
                cv.put("stustate", 0);
                cv.put("stustrtimedif", "stustrtimedif000");

                cv.put("stuInClassIndex",stuInClassIndex[i]);
                dbHelper.InsertData(dbWrite, timeString, cv);

//                JSONObject hjy = new JSONObject();
//                hjy.put("xh",110);
//                hjy.put("xsname","李已"+i);
//                hjy.put("sex",data.get(i).getSex());
//                byte[] ds = data.get(i).getJumpCount();
//
//                int ff = 0;
//                if(ds.length == 2){
//                    ff  = (Integer.valueOf(ds[1])&0xff)*256+(Integer.valueOf(ds[0])&0xff);
//                }else {
//                    Toast.makeText(GameResultActivity.this,"长度不太对",Toast.LENGTH_SHORT).show();
//                }
//                hjy.put("gotcount",ff);
//                hjy.put("losecount",data.get(i).lostCount);
//                hjy.put("fs",String.valueOf(i));
//
//                gotCountList[i] = ff;
//
//                hjy.put("onlinebool",false);
//                hjy.put("missionbool",false);
//                hj[i] = hjy;
            }


        //dbWrite.insert("historytask_detail",null,cv);
        dbHelper.close();
    }
    JSONObject[] hj;
    private void InitListview(ArrayList<StuTrainGoingInfo> data,int srcflag){

        int[] gotCountList= new int[data.size()];
        hj = new JSONObject[data.size()+1];
        try{
            for(int i = 0;i<hj.length-1;i++){

                //[i] = Utils.stuInfo[Utils.ClassIndex][theIndexArray[i]];
                JSONObject hjy = new JSONObject();
                hjy.put("xh",data.get(i).xh);
                hjy.put("xsname",data.get(i).name);

                hjy.put("sex",data.get(i).sex);

                byte[] ds = data.get(i).jumpCount;

                int ff = 0;
                if(ds.length == 2){
                    if(srcflag ==1)
                        ff  = (Integer.valueOf(ds[1])&0xff)*256+(Integer.valueOf(ds[0])&0xff);//正常训练结束的
                    else
                        ff  = (Integer.valueOf(ds[0])&0xff)*256+(Integer.valueOf(ds[1])&0xff);
                }else {
                    Toast.makeText(GameResultActivity.this,"长度不太对",Toast.LENGTH_SHORT).show();
                }
                data2.get(i).jumpCount =Utils.intToButeArray(ff-data.get(i).lostCount);

                if(srcflag ==1)
                    hjy.put("gotcount",ff-data.get(i).lostCount);//正常训练结束的
                else
                    hjy.put("gotcount",ff);

                hjy.put("losecount",data.get(i).lostCount);
                hjy.put("fs","随便");

                hjy.put("stuInClassIndex",data.get(i).stuInClassIndex);
                //hjy.put("stuIndex",)
                gotCountList[i] = ff;

                hjy.put("onlinebool",false);
                hjy.put("missionbool",false);

                hj[i] = hjy;
            }
        }catch(Exception e){}
        try{
                JSONObject hjy = new JSONObject();
                //String[] months = new String[] { "Jan", "Feb", "Mar","Apr", "5", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", };
                String[] months = new String[] { "0-120", "121-140", "141-160","161-180", "181-200", "201->"};
                StringBuilder sb = new StringBuilder();
                for (int i=0;i<months.length;i++){
                    sb.append(months[i]);
                    sb.append(',');
                }

            int[] datas = GetSixDuan(gotCountList);

            //由于把最下部的“柱状图”列进listview，所以把listview的最后一个进行无用伪装，如下
                hjy.put("xsname","李已"+88);
                hjy.put("XArray",sb.toString());
                hjy.put("YArray",datas);
                hjy.put("sex", 3);
                hjy.put("gotcount", 0);
                hjy.put("losecount", 0);
                hjy.put("fs", 00);

                hjy.put("stuInClassIndex",-1);
                hj[hj.length-1] = hjy;

        }catch(Exception e){}
        mft = new MyAdapterForGameResult(hj,GameResultActivity.this);
        mft.setOnInnerItemOnClickListener(this);

        lv.setAdapter(mft);
        // 绑定listView的监听器
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


                //JSONObject jb = (JSONObject)arg0.getItemAtPosition(arg2);

                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                MyAdapterForGameResult.ViewHolder holder = (MyAdapterForGameResult.ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.checkBox.toggle();
                // 将CheckBox的选中状况记录下来
                mft.getIsSelected().put(arg2, holder.checkBox.isChecked());
                seletedFlag = -1;
                // 调整选定条目
                if (holder.checkBox.isChecked() == true) {
                    checkNum++;
                } else {
                    checkNum--;
                }
                // 用TextView显示
                //tv_show.setText("已选中"+checkNum+"项");
                Log.e("falter","check:"+checkNum);

                numCount.setText("已经选择学生"+String.valueOf(checkNum)+"人");
            }
        });
    }

    private int[] GetSixDuan(int[] data){
        int[] result = new int[6];
        for(int i=0;i<data.length;i++){
           if(data[i]<121){
               result[0]++;
           }else if(data[i]<141){
               result[1]++;
           }else if(data[i]<161){
               result[2]++;
           }else if(data[i]<181){
               result[3]++;
           }else if(data[i]<201){
               result[4]++;
           }else{
               result[5]++;
           }
        }
        return result;
    }
    private void InitView(int peopleCount,int gotmissionCount,int overmissionCount){

        btn_comfirm = (Button) findViewById(R.id.btn_gr_selectconfirm);
        btn_comfirm.setOnClickListener(this);
        numCount = (TextView)findViewById(R.id.tv_gr_selectedcount);

        tv1 = (TextView)findViewById(R.id.tv_gr_1);
        tv1.setText("接收人数"+gotmissionCount+"/"+peopleCount);
        tv2 = (TextView)findViewById(R.id.tv_gr_2);
        tv2.setText("完成人数"+overmissionCount+"/"+peopleCount);

        btn_over = (Button)findViewById(R.id.btn_gr_1);
        btn_over.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.lv_gr1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.LogE("stateArray.get(key) == false:::::::有人:::::::");
            }
        });
        lv.setOnItemLongClickListener(onItemLongClickListener);
    }
    private void SwitchBack(boolean flag,int index){

        Utils.LogE("index:::::::::::::::::"+index);
        if(flag){
            for(int i=0;i<lv.getCount();i++){

                if(i<=index){
                    checkNum++;
                    mft.setIsSelected(i,true);
                }
                else
                     mft.setIsSelected(i,false);
            }
        }else {
            for(int i=0;i<lv.getCount();i++){
                if(i<index)
                    mft.setIsSelected(i,false);
                else{
                    checkNum++;
                    mft.setIsSelected(i,true);
                }
            }
        }
        numCount.setText("已经选择学生"+String.valueOf(checkNum)+"人");
        mft.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_upselect:
                checkNum = 0;
                twoSelectDialog.dismiss();
                SwitchBack(true,listIndex);
                //Toast.makeText(TeachGoingActivity.this,"hhjgjafjljlg",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_downselect:
                checkNum = 0;
                twoSelectDialog.dismiss();
                SwitchBack(false,listIndex);
                break;
            case R.id.btn_gr_selectconfirm:

                if(checkNum == 0)
                {
                    Toast.makeText(GameResultActivity.this,"请选择至少一个人开始。。。",Toast.LENGTH_SHORT).show();
                    return;
                }
//                if(checkNum == mft.getCount())
//                    seletedFlag = 0;
                int maIndex = 0;
                int[] selectIndexArray = new int[checkNum];
                for (int i = 0;i<mft.getIsSelected().size();i++){
                    if(mft.getIsSelected().get(i))
                        selectIndexArray[maIndex++] = i;
                }

                int[] theIndexArray = new int[checkNum];
                for(int p=0;p<checkNum;p++){

                       try{
                               theIndexArray[p] =  hj[selectIndexArray[p]].getInt("stuInClassIndex");
                    }catch (Exception e){

                    }
                       // Toast.makeText(GameResultActivity.this,"请选择至少一个人开始。。。"+ff,Toast.LENGTH_SHORT).show();
                }

                seletedFlag = -1;
                Intent intent = new Intent();
                intent.putExtra("indexarray",theIndexArray);
                intent.putExtra("selectedflag",seletedFlag);
                intent.setClass(GameResultActivity.this,EighteenMissionActivity.class);
                startActivity(intent);
                GameResultActivity.this.finish();
                break;

        }
    }

    @Override
    public void itemClick(View v) {

    }
}
