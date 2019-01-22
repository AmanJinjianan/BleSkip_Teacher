package com.qixiang.bleskip_teacher.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/5.
 */

public class StuTrainGoingInfo implements Serializable{

    public String name;
    public byte xh;
    public int sex;
    public byte thisPacketCount;//当前包包含的跳数
    public byte[] jumpCount = new byte[2];//底层记录的跳数
    public byte[] timeState = new byte[2];//底层记录的倒计时末尾距离此刻时间差
    public byte[] chaArray ;
    public int lostCount;//记录失败个数

    public int stuInClassIndex;//记录此同学在班级大列表中的位置

    public StuTrainGoingInfo(){}
    public StuTrainGoingInfo(String name,byte xh,int sex,byte thisPacketCount,byte[] jumpCount,byte[] timeState,byte[] chaArray){

        this.name = name;
        this.xh = xh;
        lostCount = 0;
        this.sex = sex;
        this.thisPacketCount =thisPacketCount;
        this.jumpCount = jumpCount;
        this.timeState = timeState;
        this.chaArray = chaArray;

        //this.stuInClassIndex = stuInClassIndex;
    }

    public int getClassIndex() {
        return stuInClassIndex;
    }

    public void setClassIndex(int classIndex) {
        this.stuInClassIndex = stuInClassIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public byte getThisPacketCount() {
        return thisPacketCount;
    }

    public void setThisPacketCount(byte thisPacketCount) {
        this.thisPacketCount = thisPacketCount;
    }

    public byte[] getJumpCount() {
        return jumpCount;
    }

    public void setJumpCount(byte[] jumpCount) {
        this.jumpCount = jumpCount;
    }

    public byte[] getTimeState() {
        return timeState;
    }

    public void setTimeState(byte[] timeState) {
        this.timeState = timeState;
    }

}
