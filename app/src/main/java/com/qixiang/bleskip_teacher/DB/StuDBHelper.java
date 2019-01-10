package com.qixiang.bleskip_teacher.DB;

/**
 * Created by Administrator on 2018/8/23.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qixiang.bleskip_teacher.Util.Utils;

public class StuDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "TestSQLite";
    public static final int VERSION = 1;
    private String name;

    //必须要有构造函数
    public StuDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.name = name;
    }

    // 当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
        Utils.LogE("create Database------------->1210");
        /*String sql = "create table "+Utils.SDL_table_Name+" (number_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tasktime varchar(20),tasktype int,taskname varchar(10),taskpeoplecount int,taskpeoplecompletecount int)";*/

        String sql = "create table if not exists historytask_abstract(number_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tasktime varchar(20),tasktype int,taskname varchar(10),taskpeoplecount int,taskpeoplecompletecount int)";


        String sql2 = "create table historytask_detail(number_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tasktime varchar(20),tasktype int, taskname varchar(10),taskcontent varchar(10),stuid int,stuname varchar(10)," +
                "stusex int,stusuccesssount int,stufailcount int,stutime varchar(10),stuscore int,stustate REAL,stustrtimedif varchar(10))";
       /* String sql="CREATE TABLE IF NOT EXISTS historyTask(" +
                "number INTEGER PRIMARY KEY autoincrement," +
                "taskTime TEXT,taskType INTEGER," +
                " taskName TEXT,taskContent TEXT,taskPeopleCount INTEGER,taskPeopleCompleteCount INTEGER)";*/
//输出创建数据库的日志信息

//execSQL函数用于执行SQL语句
        db.execSQL(sql);
        //db.execSQL(sql2);
    }

    public void CreateTable2(SQLiteDatabase db, String table_name,Context context) {

        String sql = "create table if not exists " + table_name + " (number_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tasktime varchar(20),tasktype int,taskname varchar(10),taskpeoplecount int,taskpeoplecompletecount int)";
        db.execSQL(sql);
    }

    //每次插入结果详情时，都依照当时时刻作为表名
    public void CreateTable(SQLiteDatabase db, String table_name) {
        String sql2 = "create table IF NOT EXISTS " + table_name + " (number_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tasktime varchar(20),tasktype int, taskname varchar(10),taskcontent varchar(10),stuid int,stuname varchar(10)," +
                "stusex int,stusuccesssount int,stufailcount int,stutime varchar(10),stuscore int,stustate REAL,stustrtimedif varchar(10))";
        db.execSQL(sql2);
    }

    //每次插入结果详情时，都依照当时时刻作为表名
    public void InsertData(SQLiteDatabase db, String table_name, ContentValues cv) {
        db.insert(table_name, null, cv);
    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }

    /**
     * 删除数据库
     *
     * @param context
     * @return
     */
    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(name);
    }

    /**
     * 判断某张表是否存在
     *
     * @param
     * @return
     */
    public boolean tabbleIsExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            return result;
        }
    }
}