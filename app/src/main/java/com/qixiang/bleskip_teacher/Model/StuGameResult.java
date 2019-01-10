package com.qixiang.bleskip_teacher.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/8.
 */

public class StuGameResult implements Serializable {
    public int Xh;
    public String name;
    public int sex;
    public int gotCount;
    public int lostCount;
    public int Mark;

    public StuGameResult(int xh, String name, int sex, int gotCount, int lostCount, int mark) {
        Xh = xh;
        this.name = name;
        this.sex = sex;
        this.gotCount = gotCount;
        this.lostCount = lostCount;
        Mark = mark;
    }



}
