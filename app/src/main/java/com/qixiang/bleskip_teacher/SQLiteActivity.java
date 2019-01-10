package com.qixiang.bleskip_teacher;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.qixiang.bleskip_teacher.DB.StuDBHelper;
import com.qixiang.bleskip_teacher.Util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/8/23.
 */

public class SQLiteActivity extends Activity{

    //声明各个按钮
    private Button createBtn;
    private Button insertBtn;
    private Button updateBtn;
    private Button queryBtn;
    private Button deleteBtn;
    private Button ModifyBtn;
    SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        creatView();
        setListener();

        simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        //Date date = new Date(System.currentTimeMillis());
        System.out.println("Date获取当前日期时间"+simpleDateFormat.format(new Date(System.currentTimeMillis())));
    }

    //通过findViewById获得Button对象的方法
    private void creatView(){
        createBtn = (Button)findViewById(R.id.button00001);
        updateBtn = (Button)findViewById(R.id.button00002);
        insertBtn = (Button)findViewById(R.id.button00003);
        ModifyBtn = (Button)findViewById(R.id.button00004);
        queryBtn = (Button)findViewById(R.id.button00005);
        deleteBtn = (Button)findViewById(R.id.button00006);
    }

    //为按钮注册监听的方法
    private void setListener(){
        createBtn.setOnClickListener(new CreateListener());
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkoo3333333333333333366666666633388788");
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db110",null,1);
                SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                //cv.put("number_id",110);
            cv.put("tasktype",10);
            cv.put("taskname","taskname1");
            cv.put("tasktime","tasktime1");
            cv.put("taskpeoplecount",001);
            cv.put("taskpeoplecompletecount",005);
                dbWrite.insert("historytask_abstract",null,cv);
                dbHelper.close();
            }
        });
        insertBtn.setOnClickListener(new InsertListener());
        ModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db110",null,1);
                SQLiteDatabase dbQuery = dbHelper.getReadableDatabase();

                Cursor cursor = dbQuery.query("historytask_abstract",new String[]{"tasktime","taskname"},"taskname=?",new String[]{"taskname1"},null,null,null);
                while (cursor.moveToNext()){
                    String se = cursor.getString(cursor.getColumnIndex("tasktime"));
                    Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkoo333333333388788"+se);
                }
                dbQuery.close();
            }
        });
        queryBtn.setOnClickListener(new QueryListener());
        deleteBtn.setOnClickListener(new DeleteListener());
    }

    class CreateListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db111",null,1);
            SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

            System.out.println("query-----llllllllllllllllllllllllll-->sex:");
        }
    }

    //更新数据库的方法
    class UpdateListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
// 数据库版本的更新,由原来的1变为2
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,2);
            SQLiteDatabase db =dbHelper.getReadableDatabase();
        }
    }

    class InsertListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            System.out.println("query-----lllllllllllllllllllll8888888lllll-->sex:");

            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db110",null,1);
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            //cv.put("number_id",110);
//            cv.put("tasktype",10);
//            cv.put("taskname","taskname1");
//            cv.put("tasktime","tasktime1");
//            cv.put("taskpeoplecount",001);
//            cv.put("taskpeoplecompletecount",005);

            cv.put("tasktime",simpleDateFormat.format(new Date(System.currentTimeMillis())));
            cv.put("tasktype",002);
            cv.put("taskname","name1");
            cv.put("taskcontent","content1");
            cv.put("stuid",0001);
            cv.put("stuname","stuname1");

            cv.put("stusex",02);
            cv.put("stusuccesssount",12);
            cv.put("stufailcount",22);
            cv.put("stutime","time11");

            cv.put("stuscore",32);
            cv.put("stustate",0.2);
            cv.put("stustrtimedif","stustrtimedif002");

            dbWrite.insert("historytask_table",null,cv);
            dbHelper.close();
        }
    }

    class QueryListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db110",null,1);
            SQLiteDatabase dbQuery = dbHelper.getReadableDatabase();

            Cursor cursor = dbQuery.query("historytask_table",new String[]{"number_id","tasktime","taskname"},"taskname=?",new String[]{"name1"},null,null,null);
            while (cursor.moveToNext()){
                String se = cursor.getString(cursor.getColumnIndex("tasktime"));

                Utils.LogE("kkkkkkkkkkkkkkkkkkkkkkkkkkkoo3333333333333"+se);
            }
            dbQuery.close();
        }
    }

    //修改数据的方法
    class ModifyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db",null,1);
            //得到一个可写的数据库
            SQLiteDatabase db =dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("sage", "23");
            //where 子句 "?"是占位符号，对应后面的"1",
            String whereClause="id=?";
            String [] whereArgs = {String.valueOf(1)};
            //参数1 是要更新的表名
            //参数2 是一个ContentValeus对象
            //参数3 是where子句
            db.update("stu_table", cv, whereClause, whereArgs);
        }
    }

    //删除数据的方法
    class DeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            StuDBHelper dbHelper = new StuDBHelper(SQLiteActivity.this,"stu_db110",null,1);
//得到一个可写的数据库
            SQLiteDatabase db =dbHelper.getReadableDatabase();
            dbHelper.deleteDatabase(SQLiteActivity.this);
            //String whereClauses = "id=?";
            //String [] whereArgs = {String.valueOf(2)};
//调用delete方法，删除数据
            //db.delete("stu_table", whereClauses, whereArgs);
        }
    }

}
