package com.chen.insurre.bean;

import java.io.Serializable;

/**
 * Created by chenguoquan on 7/29/15.
 */
public class LoginInfo implements Serializable {

    private String id;
    private String passwd;
    private String ip;
    private String key;
    private String name;
    private String regkey;
    private String archno;
    private String msg;
    private String state;
    private ParamInfo params;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegkey() {
        return regkey;
    }

    public void setRegkey(String regkey) {
        this.regkey = regkey;
    }

    public String getArchno() {
        return archno;
    }

    public void setArchno(String archno) {
        this.archno = archno;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ParamInfo getParams() {
        return params;
    }

    public void setParams(ParamInfo params) {
        this.params = params;
    }
}
