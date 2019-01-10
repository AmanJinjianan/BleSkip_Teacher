package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.BLEService;
import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.Model.StuGameResult;
import com.qixiang.bleskip_teacher.Model.StuTrainGoingInfo;
import com.qixiang.bleskip_teacher.MyView.CountDownDialog;
import com.qixiang.bleskip_teacher.MyView.TwoSelectDialog;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterForTeachGoing;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/8/21.
 */

public class TeachGoingActivity extends Activity implements View.OnClickListener{

    public TimeCount myTimer;
    int missionNumCount = 0,overNumCount = 0;
    private ListView lv ;
    MyAdapterForTeachGoing  mft;
    public TextView tv_timer;
    private int GoingState;//0:未开始  1：进行中  2：已结束
    private Button btn_three;

    private TextView tv_over,tv_return;
    private int[] theIndexArray;
    //'private boolean[] stateFlag = new boolean[10];

    //维护所有同学的在线（心跳）状态  Integer:学号   true：在线，false :不在线
    public Map<Byte,Boolean>  stateArray = new HashMap<Byte,Boolean>();
    //维护所有同学的在线状态  Integer:学号   true：在线，false :不在线
    public Map<Byte,Boolean>  missionReply = new HashMap<Byte,Boolean>();
    //维护所有同学的任务完成状态 Integer:学号   true：完成，false :没
    public Map<Byte,Boolean>  overReply = new HashMap<Byte,Boolean>();
    //确定所有同学的学号和姓名绑定状态
    public Map<Byte,String>  nameMap = new HashMap<Byte,String>();

    private Handler theHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 101)
             mft.notifyDataSetChanged();

            if(timeTrue)
                tv_timer.setText(timeCount++ +"秒");

        }
    };
    private boolean timeTrue;
    private int timeCount;
    int sixCount = 0;
    Timer Tasktimer2 = new Timer();
    TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            sixCount++;
            if(sixCount%6 == 0)
                RefreshBackground();

            theHandler.sendEmptyMessage(101);

            if(sixCount%6 == 0){
                for(byte key:stateArray.keySet()){
                    stateArray.put(key,false);
                }
            }

        }
    };
    //检查有没有心跳
    private void RefreshBackground() {
        //Utils.LogE("stateArray.length:::::::长度:::::::"+stateArray.size());
        Iterator iter = stateArray.keySet().iterator();
        while (iter.hasNext()) {
            Object xh = iter.next();
            Utils.LogE("stateArray.get(key) == false:::::::有人:::::::"+stateArray.get(xh));
            if(stateArray.get(xh) == false){
                //Utils.LogE("有人是false::::::::::::::学号"+xh);
                try{
                    int hg = XhAndIndexMap.get(xh);
                    Log.e("falter","jsonexception,,,,,,,,,,,,,,:"+xh+"  :"+hg);
                    hj[hg].put("onlinebool",false);
                }catch(Exception e){
                    Log.e("falter","jsonexception555,,,,,,,,,,,,,,:"+e.toString());
                    Intent intent = new Intent("com.qixiang.jsonexception");
                    intent.putExtra("value","111111111111111111");
                    sendBroadcast(intent);
                }
            }else {
                try{
                    int hg = XhAndIndexMap.get(xh);
                    hj[hg].put("onlinebool",true);
                }catch(Exception e){
                    Intent intent = new Intent("com.qixiang.jsonexception");
                    intent.putExtra("value","222222222222222222");
                    sendBroadcast(intent);
                }
            }
        }
    }
    //记录学号和此同学的总条数，以便刷新界面
    //private Map<String ,Integer> jumpCount = new HashMap<>();

    //记录学号和所在listview中的索引号
    private Map<Byte ,Integer> XhAndIndexMap = new HashMap<>();
    //记录学号和此同学的过程数据集
    Map<String,LinkedList> dataMap;

    int modelFlag;// 1:计时 2：计数
    int selectedflag;// 0:全选 1:全男 2:全女  -2:自由选择

    int PeopleCount = 0;

    byte[] misData;//一个两byte的数组，先低位，后高位，{0x70,0x17};代表60S计时赛
    byte[] peopleXHList;
    String[] peopleNameList;
    int[] peopleSexList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachgoing);

        Intent intent = getIntent();
        theIndexArray = intent.getIntArrayExtra("indexarray");
        modelFlag = intent.getIntExtra("modelFlag",-1);
        selectedflag= intent.getIntExtra("selectedflag",-2);

        misData= intent.getByteArrayExtra("misData");

        if(modelFlag==1){
            //long millisTime = switchTime(misData);
            myTimer = new TimeCount(switchTime(misData)*10, 1000);//构造CountDownTimer对象
        }

        //暂时保留代码(定义要测试的学号序列)
        //peopleXHList = new byte[]{0x02};


        int index = Utils.ClassIndex2;
        peopleXHList = new byte[theIndexArray.length];
        peopleNameList= new String[theIndexArray.length];
        peopleSexList= new int[theIndexArray.length];
        try{
            for(int i=0;i<theIndexArray.length;i++){
                peopleXHList[i] = Byte.valueOf(Utils.stuInfo[index][theIndexArray[i]].get("xsnum").toString());
                peopleNameList[i] =Utils.stuInfo[index][theIndexArray[i]].get("xsname").toString();
                peopleSexList[i] =Integer.valueOf(Utils.stuInfo[index][theIndexArray[i]].get("sex").toString());
            }
        }catch (Exception e){}

        //modelFlag = 0x01;
        selectedflag = 4;

        GoingState = 0;

        for(int i=0;i<peopleXHList.length;i++){
            stateArray.put(peopleXHList[i],false);
        }

        PeopleCount = stateArray.size();
        //misData = new byte[]{0x70,0x17};

        dataMap = new HashMap<>();
        //以人数确定循环次数,暂时一个人，学号作为key，list保存每一次来的数据包
        for (int i= 0;i<PeopleCount;i++){
            //List<StuModel> firstOne+"1" = new LinkedList<StuModel>();
            try{
                dataMap.put(String.valueOf((int)peopleXHList[i]&0xff), new LinkedList<StuTrainGoingInfo>());
            }catch(Exception e){
            }
        }

        Initdata_stateArray();
        //Tasktimer.schedule(task,6000,6000);
        Tasktimer2.schedule(task2,1000,1000);

        timeCount = 0;
        setBroadcastReveiver();
        Utils.LogE("lenght:"+stateArray.size());
        InitView();
        InitListview();

        for(int i=0;i<PeopleCount;i++){
            try{
                //XhAndIndexMap.put((byte)hj[i].getInt("xh"),i);
                XhAndIndexMap.put(peopleXHList[i],i);
            }catch(Exception e){
                Intent intent2 = new Intent("com.qixiang.jsonexception");
                intent.putExtra("value","3333333333333333333333333333333333");
                sendBroadcast(intent2);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SendMissionList(modelFlag,0xFF,theIndexArray);
            }
        },500);
    }

    private long switchTime(byte[] misData) {
        //long result;

        int dd = (Integer.valueOf(misData[1])&0xff)*256+(Integer.valueOf(misData[0])&0xff);
        return dd;
    }

    //任务列表开始和任务列表结束
    private byte[] GetBytes(boolean flag,byte missionCode,byte model,byte data1,byte data2){
        byte[] data=new byte[8];
        data[0] = 0x07;
        data[1] = 0x21;
        data[2] = missionCode;
        data[3] = model;
        data[4] = data1;//1770 -- >6000*10ms
        data[5] = data2;
        if(flag)
            data[6] = (byte)0xFC;//死数据 列表开始
        else
            data[6] = (byte)0xFB;//死数据 列表结束
        data[7] = missionCode;

        return data;
    }
    //给选中的每个人发送任务指令
    private void SendMissionList(int modelFlag,int selectedflag,int[] theIndexArray){

        byte missionCode = (byte)(Utils.GetMissionCode());
        if(selectedflag == 0 || selectedflag == 1||selectedflag == 2){//全选 全男 全女
            byte[] data=new byte[8];
            data[0] = 0x07;
            data[1] = 0x21;
            data[2] = missionCode;
            data[3] = (byte)(modelFlag);
            data[4] = misData[0];//1770 -- >6000*10ms
            data[5] = misData[1];
            data[6] = (byte)selectedflag;//死数据
            data[7] = missionCode;
            Tools.mBleService.characterWrite1.setValue(data);
            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
        }else {
            byte[] listStart = GetBytes(true,missionCode,(byte)modelFlag,misData[0],misData[1]);
            AddCommandList(listStart);

            PacketPerson(missionCode);

            byte[] listEnd = GetBytes(false,missionCode,(byte)modelFlag,misData[0],misData[1]);
            AddCommandList(listEnd);
            //Tools.mBleService.characterWrite1.setValue(data);
            //Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
        }
    }

    private List<byte[]> theListData = new ArrayList<>();
    private boolean theListDataFlag = true;
    public void CheckCommandList(){
        if(theListData.size() ==0) return;

        theListData.remove(0);
        theListDataFlag = true;
        if(theListData.size() !=0){
            Tools.mBleService.characterWrite1.setValue(theListData.get(0));
            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
            theListDataFlag = false;
        }
    }
    public void AddCommandList(byte[] data){
        theListData.add(data);
        if(theListDataFlag){
            theListDataFlag = false;
            Tools.mBleService.characterWrite1.setValue(theListData.get(0));
            Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
        }
    }

    private void InitView(){
        lv = (ListView) findViewById(R.id.lv_teachgoing);
        //lv.setOnItemClickListener(onItemClickListener);

        tv_return =  (TextView) findViewById(R.id.tv_going_return);
        tv_over = (TextView)findViewById(R.id.tv_going_over);
        tv_over.setText("完成人数"+0+"/"+PeopleCount);
        tv_timer = (TextView)findViewById(R.id.tv_timer);
        btn_three = (Button) findViewById(R.id.countdown3);
        btn_three.setOnClickListener(this);
    }
    JSONObject[] hj;
    private void InitListview(){

        hj = new JSONObject[theIndexArray.length];
        try{
            for(int i = 0;i<PeopleCount;i++){
                //[i] = Utils.stuInfo[Utils.ClassIndex][theIndexArray[i]];
                JSONObject hjy = new JSONObject();
                hjy.put("xh",peopleXHList[i]);
                hjy.put("xsname",peopleNameList[i]);
                nameMap.put(peopleXHList[i],peopleNameList[i]);
                hjy.put("sex",peopleSexList[i]);
                hjy.put("fs",String.valueOf(i));
                hjy.put("gotcount",00);

                hjy.put("onlinebool",false);
                hjy.put("missionbool",false);

                hj[i] = hjy;
            }

        }catch(Exception e){}

        mft = new MyAdapterForTeachGoing(hj,TeachGoingActivity.this);
        lv.setAdapter(mft);
    }
    //用于打包下发任务给跳绳
    private void PacketPerson(byte missionCode){
       /* int cyc = (PeopleCount-1)/13+1;
        for(int i=0;i<cyc;i++){
            byte[] data=new byte[PeopleCount%13+7];//[8]--->一个人
            //data = new byte[theIndexArray.length+7];
            data[0] = (byte)(data.length -1);
            data[1] = 0x21;
            data[2] = missionCode;
            data[3] = (byte)(modelFlag);
            data[4] = misData[0];
            data[5] = misData[1];
            data[6] = (byte)0x14;//死数据 跳绳号（换算成十六进制）
            data[7] = (byte)0x15;//死数据 跳绳号（换算成十六进制）
            data[8] = missionCode;
            AddCommandList(data);
        }*/


        //List<byte[]> ggg = new ArrayList<>();
        int x,y,n,z=0;
        byte[] buff = peopleXHList;
        n = PeopleCount;

        for(y=0;z<5;)
        {
            byte[] buf = new byte[20];
            buf[0]=1;
            buf[1]=0x21;
            buf[2]=missionCode;
            buf[3] = (byte)(modelFlag);
            buf[4] = misData[0];
            buf[5] = misData[1];

            for(x=0;(x<13)&&(z<5);)
            {
                buf[6+x]=buff[13*y+x];
                x++;
                if((13*y+x)>=n)
                {
                    buf[6+x]=missionCode;
                    buf[0] = (byte)(x+6);
                    //ggg.add(Switch(buf,x));
                    AddCommandList(Switch(buf,x));
                    z=6;
                }
            }
            if((x==13)&&(z<5))
            {
                buf[6+x]=missionCode;
                buf[0] = 19;
                AddCommandList(buf);
                //ggg.add(buf);
            }
            y++;
            if((13*y)>=n)z=6;
        }
    }
    private byte[] Switch(byte[] data,int x){
        byte[] retData = new byte[x+7];
        for (int i=0;i<retData.length;i++){
            retData[i] = data[i];
        }
        return retData;
    }


    BluetoothReceiver bluetoothReceiver;
    //设置广播接收器
    private void setBroadcastReveiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_STATE_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_STATE_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_WRITE_DESCRIPTOR_OVER);
        intentFilter.addAction(BLEService.ACTION_CHARACTER_CHANGE);
        intentFilter.addAction(BLEService.ACTION_WRITE_DATA_OVER);

        bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Utils.LogE("........onDestroy............Going");
        Tasktimer2.cancel();
        unregisterReceiver(bluetoothReceiver);
    }

    public boolean  regisBool(byte xh){
        Iterator iter = stateArray.keySet().iterator();
        while (iter.hasNext()) {
            Object xu = iter.next();
            if(xh == (byte)xu)
                return false;
        }
        return true;
    }
    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(BLEService.ACTION_CHARACTER_CHANGE.equals(action)){

                //tmp_byte = characteristic.getValue();
                byte[] tmp_byte = intent.getByteArrayExtra("value");
                if(regisBool(tmp_byte[2]))
                    return;
                Log.e(Utils.TAG,"有数据。。。。。。。。。。。。。。22"+tmp_byte[1]+"  xuehao:"+tmp_byte[2]);

                FourTypeDataSwitch(tmp_byte);
            }else if(BLEService.ACTION_WRITE_DATA_OVER.equals(action)){
                CheckCommandList();
            }else if(BLEService.ACTION_STATE_CONNECTED.equals(action)){
            }else if(BLEService.ACTION_STATE_DISCONNECTED.equals(action)){
            }else if (BLEService.ACTION_WRITE_DESCRIPTOR_OVER.equals(action)) {
            }
        }
    }

    //四种接收到的数据分类
    private void FourTypeDataSwitch(byte[] dataaray){
        switch (dataaray[1]){
            case 0x34:
                Trainning(dataaray);
                break;
            case 0x31:FlagSet(dataaray[2]);
                        break;
            case 0x32:MissionReturn(dataaray);break;
            case 0x33:
                GameOverReply(dataaray);
                Toast.makeText(TeachGoingActivity.this,String.valueOf(dataaray[2])+"号完成",Toast.LENGTH_SHORT).show();
                break;

        }
    }
    //处理所有同学的任务过程中数据
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
        if(chaArray == null)
            Utils.LogE("nullllllllllllllllllllllll");
        else
            Utils.LogE("ooooooooooooooooooooooooooo"+chaArray[0]);
        dataMap.get(String.valueOf(dataaray[2])).add(new StuTrainGoingInfo(nameMap.get(dataaray[2]),dataaray[2],1,(byte)(dataaray[0] - 8),
                new byte[]{dataaray[5],dataaray[6]},new byte[]{dataaray[7],dataaray[8]},chaArray));
        //jumpCount.put(String.valueOf(dataaray[2]),dataaray[5]+dataaray[6]*256);

        try{
            int ff = (Integer.valueOf(dataaray[6])&0xff)*256+(Integer.valueOf(dataaray[5])&0xff);
            Utils.LogE("Integer.valueOf.........................."+ff);
            hj[XhAndIndexMap.get(dataaray[2])].put("gotcount",ff);
        }catch(Exception e){
            Utils.LogE("json exception666.........................."+e.toString());
        }
        //((TextView)((LinearLayout)lv.getChildAt(XhAndIndexMap.get(dataaray[2]))).getChildAt(2)).setText(String.valueOf(dataaray[5]+dataaray[6]*256));
        //((LinearLayout)lv.getChildAt().getCh
    }
    //处理每个同学的任务结束回调
    private void GameOverReply(byte[] dataaray){
        if(overReply.get(dataaray[2]) == false){
            overReply.put(dataaray[2],true);
            overNumCount++;
            tv_over.setText("完成人数"+overNumCount+"/"+overReply.size());
            if(overNumCount == overReply.size()){
                Toast.makeText(TeachGoingActivity.this,"所有人都结束了",Toast.LENGTH_SHORT).show();
                NextAct();
            }
        }
        //List ff = dataMap.get(String.valueOf(dataaray[2]));
    }
    private void NextAct(){
        ArrayList<StuTrainGoingInfo> test2s = new ArrayList<StuTrainGoingInfo>();

         Set<Map.Entry<String,LinkedList>> setme = dataMap.entrySet();
       for(Iterator<Map.Entry<String,LinkedList>> it = setme.iterator();it.hasNext();){
           LinkedList df =it.next().getValue();
           StuTrainGoingInfo data = (StuTrainGoingInfo)(df.get(df.size()-1));
           data.lostCount = CalculateLoseCount(df);
           //data.lostCount = 9;
           test2s.add(data);
       }
        /*for(int i=0;i<PeopleCount;i++){
            LinkedList df = dataMap.
            StuTrainGoingInfo data = (StuTrainGoingInfo)(df.get(df.size()-1));
            //data.lostCount = CalculateLoseCount(dataMap.get(i));
            data.lostCount = 9;
            test2s.add(data);
        }*/
        Intent it = new Intent(TeachGoingActivity.this, GameResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OverInfo", test2s);
        it.putExtras(bundle);

        it.putExtra("srcFlag",1);
        it.putExtra("PeopleCount",PeopleCount);
        it.putExtra("gotPeopleCount",missionNumCount);
        it.putExtra("overPeopleCount",overNumCount);

        startActivity(it);
        finish();
    }
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
                    midTime = midTime+(Integer.valueOf(stf.chaArray[p])&0xff) ;
                }
                //if(stf.thisPacketCount==1)
                    startTime = midTime;
            }
        }
        int gggk =455;
        return lostCount;
    }
    //处理每个同学的任务接收回应
    private void MissionReturn(byte[] dataaray){

        try{
            int  jj = XhAndIndexMap.get(dataaray[2]);
            Utils.LogE("jjjjjjjjjjjjjjj...................."+jj);
            hj[jj].put("missionbool",true);
        }catch(Exception e){
            Utils.LogE("json exception..........................");
        }

        if(missionReply.get(dataaray[2]) == false){
            missionReply.put(dataaray[2],true);
            missionNumCount++;
            tv_return.setText("接收人数"+missionNumCount+"/"+missionReply.size());
        }
            ;
//        for(int i=0;i<hj.length;i++){
//            try{
//                if(String.valueOf(xh).equals(hj[i].getString("xh")))
//                    lv.getChildAt(i).setBackgroundColor(Color.GREEN);
//            }catch(Exception e){
//                Intent intent = new Intent("com.qixiang.jsonexception");
//                sendBroadcast(intent);
//            }
//        }
    }
    //初始化所有同学为不在线状态（六秒到时，刷新之后调用）
    private void Initdata_stateArray(){
        for(byte key:stateArray.keySet()){
            stateArray.put(key,false);
            missionReply.put(key,false);
            overReply.put(key,false);
        }
    }
//    //初始化 任务回应和比赛结束回调
//    private void InitReturnAndOverReply(){
//        for(byte key:stateArray.keySet()){
//            missionReply.put(key,false);
//            overReply.put(key,false);
//        }
//    }
    private boolean sixSecondFlag;
    private void FlagSet(byte xh){
        stateArray.put(xh,true);
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.countdown3:
                CountDownCommand();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //timer.setBase(SystemClock.elapsedRealtime());//计时器清零
                        if(modelFlag==1){
                            myTimer.start();
                        }else if(modelFlag==2){
                            timeTrue = true;
                        }
                    }
                },3000);

                CountDownDialog cb = new CountDownDialog(TeachGoingActivity.this,3);
                cb.show();
                break;
        }

    }


    //发送倒计时
    private void CountDownCommand(){
        byte[] data = {0x03,0x22,0x03,(byte)(Utils.GetMissionCode())};
        //Tools.mBleService.characterWrite1.setValue(Tools.hexToBytes("06241112131410"));
        Tools.mBleService.characterWrite1.setValue(data);
        Tools.mBleService.mBluetoothGatt.writeCharacteristic(Tools.mBleService.characterWrite1);
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            tv_timer.setText("重新验证");
            //checking.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            //checking.setClickable(false);
            tv_timer.setText(millisUntilFinished /1000+"秒");
        }
    }
}
