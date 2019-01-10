package com.qixiang.bleskip_teacher.Model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/10.
 */

public class ClassInfo implements Serializable {
    public String className;
    public String ClassCount;

    public JSONObject[] data;
    public String ClassId;
    public boolean isCheck;

    public ClassInfo(String claName, JSONObject[] data, String classId){
        this.className = claName;
        this.ClassCount =getCount(data);
        this.ClassId = classId;
    }
    public String getCount(JSONObject[] data){
        try{
            if(data[0].get("xh").toString().equals("null")){
                return "0";
            }
            else{
                return String.valueOf(data.length);
            }
        }catch (Exception e){
            Log.e("falter","Json Exception....");
        }
        return "0";
    }
    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCount() {
        return ClassCount;
    }

    public void setClassCount(String classCount) {
        ClassCount = classCount;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
