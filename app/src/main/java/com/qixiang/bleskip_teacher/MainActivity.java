package com.qixiang.bleskip_teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qixiang.bleskip_teacher.BLE.Tools;
import com.qixiang.bleskip_teacher.DB.StuDBHelper;
import com.qixiang.bleskip_teacher.Model.StuTrainGoingInfo;
import com.qixiang.bleskip_teacher.Util.HttpUtil;
import com.qixiang.bleskip_teacher.Util.Utils;
import com.qixiang.bleskip_teacher.Model.ClassInfo;
import com.qixiang.bleskip_teacher.MyView.CreateClassDialog;
import com.qixiang.bleskip_teacher.Fragment.ClassFragment;
import com.qixiang.bleskip_teacher.Fragment.MyFragment;
import com.qixiang.bleskip_teacher.Fragment.TeachFragment;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapter;
import com.qixiang.bleskip_teacher.ViewAdapter.MyAdapterMission;
import com.qixiang.bleskip_teacher.ViewAdapter.ViewPagerFragmentAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MyAdapterMission.InnerItemOnclickListener,MyAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener,View.OnClickListener {
    @Override
    public void itemClick(View v) {

        Log.e(TAG,"itemClickkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
    }

    public static Context theContext;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.e(TAG,"11111111122");
    }
    public List<Map<String,String>> mData,mData2;
    private static final String TAG = "MainActivity.TAG";
    TextView titleTextView;
    public LinearLayout firstLinearLayout;
    public LinearLayout secondLinearlayout;
    public LinearLayout threeLinearLayout;
    ViewPager mViewpager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    FragmentManager mFragmentManager;

    List<ClassInfo> classList = new ArrayList();//班级数据集合

   // public ListView lv;
    String[] titleName = new String[]{"班级","教学","个人"};
    List<Fragment> mFragmentList = new ArrayList<Fragment>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);

        Utils.LogE("....................onCreate................");
        //Intent intent = new Intent(MainActivity.this,ColumnChartActivity.class);
        //startActivity(intent);

        //ArrayList<Test2> data2 = new ArrayList<Test2>();
        //data2 = (ArrayList<Test2>) getIntent().getSerializableExtra("test");

        theContext = this;
        initFragmentList();
        mViewPagerFragmentAdapter =   new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        initView();
        initViewPager();


    }



    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btn_dialog_cancle){
                createClassDialog.dismiss();
            }else if(v.getId() == R.id.btn_dialog_confirm){
                classNameString = "";classCountString = "";
                classNameString = createClassDialog.className.getText().toString().trim();
                classCountString = createClassDialog.classCount.getText().toString().trim();
                if( classNameString != "" &&  classCountString!=""){
                    //进行班级创建
                    Log.e(TAG,"hahhah :"+classNameString +"  "+classCountString+"   "+classCreateYears);

                    CreateClass(classCreateYears,classNameString);
                    createClassDialog.dismiss();
                }
            }
        }
    };

    //UpLoadInfoForCreateClass
    Map<String,String> map2=new HashMap<>();
    private void CreateClass(String njid,String bjName){

        if(HttpUtil.NetState)//脱网模式
        {
            return;
        }
        map2.put("lsid","9d55621261fd41c0823b40cc823efcf6");
        map2.put("token",Utils.token);
        map2.put("xyid",Utils.xyID);

        map2.put("njid",njid);
        map2.put("cjr","9d55621261fd41c0823b40cc823efcf6");
        map2.put("clname",njid+"年"+bjName);
        //map2.put("clname",bjName);
        Utils.LogE("size ==========================000:"+Utils.lsID+"  "+Utils.token+"  "+Utils.xyID+"  "+njid+"  "+Utils.lsID+"  "+bjName);


        HttpUtil.load(MainActivity.this,"http://usweb.wangjiayin.cn/us-web-app/class/createClass", map2, new HttpUtil.OnResponseListner() {
            @Override
            public void onSucess(String response) {
                Utils.LogE("size =========================== 1000:"+response);
                RequestClassInfo();
            }

            @Override
            public void onError(String error) {

            }
        });

    }
    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            classCreateYears ="2016";
            String[] classyear = getResources().getStringArray(R.array.years);
            classCreateYears  = classyear[i];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            classCreateYears ="2016";
        }
    };
    public CreateClassDialog createClassDialog;
    public String classNameString,classCountString;
    private String classCreateYears ="2016";
    public void showEditDialog(View view) {
        createClassDialog = new CreateClassDialog(this,R.style.AppTheme,onClickListener,onItemSelectedListener);
        createClassDialog.show();
        //createClassDialog.dismiss();
    }
    String remainTimeString = "";
    int allIndex =0;
    Map<Integer,String> indexAndTimeMap = new HashMap();

    int heIndex =0;
    Map<Integer,String> indexAndTwoPeopleCountMap = new HashMap();

private ArrayList<Map<String,String>> GetSqlData(String tableName){

    Utils.SDL_table_Name = "class"+tableName;
    ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();

    dbHelper = new StuDBHelper(MainActivity.this,Utils.SDLName,null,1);
    dbQuery = dbHelper.getReadableDatabase();
    dbWrite = dbHelper.getWritableDatabase();
    dbHelper.CreateTable2(dbWrite,Utils.SDL_table_Name,MainActivity.this);

    Cursor cursor = dbQuery.query(Utils.SDL_table_Name,new String[]{"tasktime","taskname",
            "taskpeoplecount","taskpeoplecompletecount"},null,null,null,null,"number_id desc");
    while (cursor.moveToNext()){
        String time = cursor.getString(cursor.getColumnIndex("tasktime"));
        String name = cursor.getString(cursor.getColumnIndex("taskname"));
        int peoplecount = cursor.getInt(cursor.getColumnIndex("taskpeoplecount"));
        int completedcount = cursor.getInt(cursor.getColumnIndex("taskpeoplecompletecount"));
        if(!time.substring(0,11).equals(remainTimeString)){
            Map<String,String> data2 = new HashMap<String,String>();
            data2.put("flag","0");
            data2.put("tasktime",time);

            indexAndTimeMap.put(allIndex++,time);
            data2.put("taskname","");
            data2.put("taskpeoplecount","");
            data2.put("taskpeoplecompletecount","");
            indexAndTwoPeopleCountMap.put(heIndex++,"0,0");
            result.add(data2);
            remainTimeString = time.substring(0,11);
        }
        Map<String,String> data1 = new HashMap<String,String>();
        data1.put("flag","1");
        data1.put("tasktime",time);
        indexAndTimeMap.put(allIndex++,time);
        data1.put("taskname",name);
        data1.put("taskpeoplecount",String.valueOf(peoplecount));
        data1.put("taskpeoplecompletecount",String.valueOf(completedcount));
        indexAndTwoPeopleCountMap.put(heIndex++,peoplecount+","+completedcount);
        result.add(data1);
    }
    dbQuery.close();

    remainTimeString = "";
    return result;
}
    StuDBHelper dbHelper;
    SQLiteDatabase dbQuery,dbWrite;
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(Utils.TAG,"Mainactvity onResume......");

    }
    private String GetTableName(String s){
        StringBuilder result=new StringBuilder();
        result.append(s.substring(0,4)).append(s.substring(5,7)).append(s.substring(8,10)).append(s.substring(12,14)).append(s.substring(15,17)).append(s.substring(18));
        return "table"+result.toString();
    }

    MyAdapterMission adapter23;
    String ClassName="",bjid="";
private void InitTeach(){
    ListView lv2 =  (ListView)findViewById(R.id.lv_mission);
    findViewById(R.id.btn_add_mission).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           Intent intent =new Intent(MainActivity.this,StuDetailForTeachActivity.class);
           startActivityForResult(intent,1101);
        }
    });

    TextView tv = (TextView) findViewById(R.id.tv_class_name);

    if(Utils.stuInfo != null){
        try{
            bjid = Utils.stuInfo[Utils.ClassIndex2][0].get("bjid").toString();
            ClassName = Utils.stuInfo[Utils.ClassIndex2][0].get("bjname").toString();
            tv.setText(ClassName);
        }catch (Exception e){}
    }
    Utils.LogE(bjid+"............000............."+ClassName);

    //以班级id作为对应班级的表名
    mData2 = GetSqlData(bjid+"_"+ClassName);

    adapter23 = new MyAdapterMission(mData2,MainActivity.this);
    adapter23.setOnInnerItemOnClickListener(this);
    lv2.setAdapter(adapter23);

    lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(mData2.get(i).get("flag").equals("0"))
                    return;

            Log.e(TAG,"timeeeeeeeeeeee mData2.get(i)"+ mData2.get(i).get("flag"));
            String[] twoPeople = indexAndTwoPeopleCountMap.get(i).split(",");

            Log.e(TAG,"indexxxxxxxxxxxxxxxxxxxxxxxx:"+i+" twoPeople.length:"+indexAndTwoPeopleCountMap.get(i));
            if(twoPeople.length != 2){
                Toast.makeText(MainActivity.this,"长度不对",Toast.LENGTH_SHORT).show();
                return;
            }

            String tableName = GetTableName(indexAndTimeMap.get(i));
            GetDetailInfoFromSQL(tableName,twoPeople[0],twoPeople[1]);
        }
    });



   // load(null);
}
private void GetDetailInfoFromSQL(String tableName,String peoplecount,String overNumCount){

    ArrayList<StuTrainGoingInfo> data2 = new ArrayList<>();
    StuDBHelper dbHelper = new StuDBHelper(MainActivity.this,Utils.SDLName,null,1);
    SQLiteDatabase dbQuery = dbHelper.getReadableDatabase();
        Cursor cursor = dbQuery.query(tableName,new String[]{"stuid","stuname","stusex","stusuccesssount","stufailcount","stuscore","stuInClassIndex"},null,null,null,null,null);
        while (cursor.moveToNext()){
            String stuid = cursor.getString(cursor.getColumnIndex("stuid"));

            String stuname = cursor.getString(cursor.getColumnIndex("stuname"));
            String stusex = cursor.getString(cursor.getColumnIndex("stusex"));
            byte[] stusuccesssount = cursor.getBlob(cursor.getColumnIndex("stusuccesssount"));
            String stufailcount = cursor.getString(cursor.getColumnIndex("stufailcount"));
            String stuscore = cursor.getString(cursor.getColumnIndex("stuscore"));

            int stuInClassIndex = cursor.getInt(cursor.getColumnIndex("stuInClassIndex"));

            StuTrainGoingInfo data = new StuTrainGoingInfo();
            data.name = stuname;
            data.sex = Integer.valueOf(stusex);
            data.xh = Byte.valueOf(stuid);
            data.lostCount = Integer.valueOf(stufailcount);
            data.jumpCount = stusuccesssount;

            data.stuInClassIndex = stuInClassIndex;
            data2.add(data);
            Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkoo333333333333stuname:"+stuid+" "+stuname+" "+stusex+" "+stusuccesssount.length+" "+stufailcount);
        }
        dbQuery.close();

//    Intent it = new Intent(MainActivity.this, GameResultActivity.class);
//    Bundle bundle = new Bundle();
//    bundle.putSerializable("OverInfo", data2);
//    it.putExtras(bundle);
//
//    it.putExtra("srcFlag",2);
//    it.putExtra("PeopleCount",Integer.valueOf(peoplecount));
//    it.putExtra("gotPeopleCount",0);
//    it.putExtra("overPeopleCount",Integer.valueOf(overNumCount));
//
//    startActivity(it);
}
//请求班级列表
private void RequestClassInfo(){

    if(HttpUtil.NetState)//脱网模式
    {
        return;
    }
    Utils.ClassInfo = null;
    Utils.stuInfo= null;

    Map<String,String> map=new HashMap<>();
    map.put("lsid",Utils.lsID);
    map.put("token", Utils.token);
    Log.e("falter","Utils.lsID 55:"+Utils.lsID+" Utils.token:"+Utils.token);
    HttpUtil.load(MainActivity.this,"http://usweb.wangjiayin.cn/us-web-app/class/queryClassByLsid", map, new HttpUtil.OnResponseListner() {
        @Override
        public void onSucess(String response) {
            Log.e("falter","onSuccess:"+response);
            if(RecoginzeJson(response)){
                Log.e(Utils.TAG,"解析大完成。。。。。。。。。。。");
                Utils.writeFileData("data110",response,MainActivity.this);
            }
            mHandler.sendEmptyMessage(100);
        }
        @Override
        public void onError(String error) {}
    });
}
    //设置广播接收器
    private void SetBroadcaseReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qixiang.bleskip.requestfresh");

        intentFilter.addAction("com.qixiang.blesuccess");
        intentFilter.addAction("com.qixiang.jsonexception");
        //intentFilter.addAction("com.qixiang.netend");
        DynamicReceiver kk = new DynamicReceiver() ;
        registerReceiver(kk,intentFilter);
    }
    class DynamicReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(Utils.TAG,"动态注册广播接收到:"+intent.getAction());
            if(intent.getAction().equals("com.qixiang.bleskip.requestfresh")){
                RequestClassInfo();
            }else if(intent.getAction().equals("com.qixiang.blesuccess")){
                mHandler.sendEmptyMessage(1010);

                //onBleConnectedOK();
            }else if(intent.getAction().equals("com.qixiang.jsonexception")){
                if(intent.getStringExtra("value") != null)
                    Utils.LogE("jsonexception:"+intent.getStringExtra("value"));
                ;
                mHandler.sendEmptyMessage(101);
            }
        }
    }
    private void InitClasses(JSONObject[][] stuInfo) {}

    private void onBleConnectedOK(){
            for(ClassInfo cl:classList){
                Log.e(Utils.TAG,"boolearn:"+cl.isCheck());
                if(cl.isCheck())
                {
                    Utils.bjID = cl.getClassId();
                    break;
                }
            }
        if(HttpUtil.NetState)//脱网模式
        {
            return;
        }
        Log.e(Utils.TAG,"boolearn66666666666666666666666666666:"+Utils.lsID+" Utils.bjid:"+Utils.bjID);
            Map<String,String> value = new HashMap<>();
            value.put("lsid",Utils.lsID);
            value.put("token",Utils.token);
            value.put("bjid",Utils.bjID);
            value.put("day","1");
            HttpUtil.load(MainActivity.this,"http://usweb.wangjiayin.cn/us-web-app/taskPlan/findPlanByBjid", value, new HttpUtil.OnResponseListner() {
                @Override
                public void onSucess(String response) {
                        try {
                            JSONTokener jsonParser = new JSONTokener(response);
                            JSONObject person = (JSONObject) jsonParser.nextValue();
                            if (person.getInt("resultCode") == 0){
                                Utils.token  = person.getString("token");
                            }
                        } catch (JSONException ex) {
                            mHandler.sendEmptyMessage(101);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
    }

    private void initViewPager() {

        mViewpager.addOnPageChangeListener(new ViewPagerOnPagerChangedLisenter());
        mViewpager.setAdapter(mViewPagerFragmentAdapter);
        mViewpager.setCurrentItem(0);
        titleTextView.setText(titleName[0]);
        updateBottomLinearLayoutSelect(true,false,false);
    }

    private void initFragmentList() {
        Fragment classF = new ClassFragment();
        Fragment teachF = new TeachFragment();
        Fragment myF = new MyFragment();

        mFragmentList.add(classF);
        mFragmentList.add(teachF);
        mFragmentList.add(myF);
    }
    private void initView() {
        titleTextView = (TextView) findViewById(R.id.ViewTitle);
        mViewpager = (ViewPager) findViewById(R.id.ViewPagerLayout);
        firstLinearLayout = (LinearLayout)findViewById(R.id.firstLinearLayout);
        firstLinearLayout.setOnClickListener(this);
        secondLinearlayout = (LinearLayout)findViewById(R.id.secondLinearLayout);
        secondLinearlayout.setOnClickListener(this);
        threeLinearLayout = (LinearLayout)findViewById(R.id.threeLinearLayout);
        threeLinearLayout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstLinearLayout:
                mViewpager.setCurrentItem(0);
                updateBottomLinearLayoutSelect(true,false,false);
                break;
            case R.id.secondLinearLayout:
                mViewpager.setCurrentItem(1);
                updateBottomLinearLayoutSelect(false,true,false);
                break;
            case R.id.threeLinearLayout:
                mViewpager.setCurrentItem(2);
                updateBottomLinearLayoutSelect(false,false,true);
                break;
            default:
                break;
        }
    }
    private void updateBottomLinearLayoutSelect(boolean f, boolean s, boolean t) {
        firstLinearLayout.setSelected(f);
        secondLinearlayout.setSelected(s);
        threeLinearLayout.setSelected(t);
    }
    private void SwitchColor(int position){
        firstLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        secondLinearlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        threeLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        switch (position){
            case 0:firstLinearLayout.setBackgroundColor(Color.parseColor("#87CEEB"));break;
            case 1:secondLinearlayout.setBackgroundColor(Color.parseColor("#87CEEB"));break;
            case 2:threeLinearLayout.setBackgroundColor(Color.parseColor("#87CEEB"));break;
        }
    }
    class ViewPagerOnPagerChangedLisenter implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            boolean[] state = new boolean[titleName.length];
            state[position] = true;
            titleTextView.setText(titleName[position]);
            SwitchColor(position);
            updateBottomLinearLayoutSelect(state[0],state[1],state[2]);
            Log.e(Utils.TAG,"position:"+position+" Tools.connectedFlag:"+Tools.connectedFlag);
            if(position == 1)
            {

                if(Tools.connectedFlag){
                    InitTeach();
                }else {
                    Intent intent = new Intent(MainActivity.this,BleConnectActivity.class);
                    startActivity(intent);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
    private boolean RecoginzeJson(String JSON){
        try {
            JSONTokener jsonParser = new JSONTokener(JSON);
            // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
            // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
            JSONObject person = (JSONObject) jsonParser.nextValue();
            if (person.getInt("resultCode") == 0){
                Utils.ClassInfo = person.getJSONArray("data");
                int classCount = 0;
                int numCount = 0;int maxCount = 1;

                //int[] countArray = new int[100];
                String classId = "";
                for (int i = 0;i<Utils.ClassInfo.length();i++){
                    JSONObject jb = (JSONObject)Utils.ClassInfo.get(i);
                    if(!jb.getString("bjid").equals(classId)){
//                        if(i != 0){
//                            countArray[classCount-1] = numCount;
//                            numCount = 0;
//                        }
                        if(numCount>=maxCount){
                            maxCount = numCount;
                        }
                        numCount = 1;
                        classCount++;
                        classId = jb.getString("bjid");
                    }else {
                        numCount++;
                    }
                }
                if(numCount>=maxCount){
                    maxCount = numCount;
                }
                Log.e(Utils.TAG,"班级数量是："+classCount +" 人数最多的班级有："+maxCount+"人");
                Utils.stuInfo = new JSONObject[classCount][maxCount];

               // if()
                classId = "";
                int xIndex = -1; int yIndex=0;
                for (int i = 0;i<Utils.ClassInfo.length();i++){
                    JSONObject jb = (JSONObject)Utils.ClassInfo.get(i);
                    if(jb == null) Log.e(Utils.TAG,"jb是null了：222222222222222222222222222");
                    if(!jb.getString("bjid").equals(classId)){
                        //if(jb == null) Log.e(Utils.TAG,"jb是null了：333333333333333333333333"+classId+"   "+);
                        xIndex++;
                        yIndex = 0;
                        jb.put("index",yIndex);
                        Utils.stuInfo[xIndex][yIndex] = jb;
                        classId = jb.getString("bjid");
                    }else {
                        yIndex++;
                        jb.put("index",yIndex);
                        Utils.stuInfo[xIndex][yIndex] = jb;
                    }
                }
            }
            return  true;
        } catch (Exception e) {
            mHandler.sendEmptyMessage(101);
            Log.e("falter","JSONException:"+e);
            // 异常处理代码
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.LogE("onActivityResult:resultCode:"+resultCode+"  requestCode:"+requestCode);
        if(requestCode == 1101){
            mData2.clear();
            List<Map<String,String>>  hh = GetSqlData(bjid+"_"+ClassName);
            for(int i =0;i< hh.size();i++){
                mData2.add(hh.get(i));
            }
            Utils.LogE("requestCode =========================== 1101"+mData2.size());
            adapter23.notifyDataSetChanged();
        }
    }
}
