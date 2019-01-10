package com.qixiang.bleskip_teacher.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/8/9.
 */

public class StuInfo implements Serializable {

    private String xh = "";
    private String bjid = "";
    private String xsid = "";
    private String xsname = "";

    private String shzt = "";//0 申请，1同意
    private String sex = "";//1男，2女
    private String bjname = "";

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getBjid() {
        return bjid;
    }

    public void setBjid(String bjid) {
        this.bjid = bjid;
    }

    public String getXsid() {
        return xsid;
    }

    public void setXsid(String xsid) {
        this.xsid = xsid;
    }

    public String getXsname() {
        return xsname;
    }

    public void setXsname(String xsname) {
        this.xsname = xsname;
    }

    public String getShzt() {
        return shzt;
    }

    public void setShzt(String shzt) {
        this.shzt = shzt;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBjname() {
        return bjname;
    }

    public void setBjname(String bjname) {
        this.bjname = bjname;
    }

}
