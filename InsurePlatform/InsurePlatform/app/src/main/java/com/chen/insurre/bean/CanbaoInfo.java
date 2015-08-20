package com.chen.insurre.bean;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class CanbaoInfo {
    private String jhname;//监护人姓名
    private String jhcardno;//监护人身份证号
    private String jhtelephone;//监护人手机
    private String cbstatus;//采集状态代码： 1 已采集 2 人户分离未采集 3   长期外出未采集 4 情况不明未采集
    private String cbstate;//当前参保状态代码： 1 从未参保 2 参保后断保 3 在外地参保（或享受社保待遇）
    private String telephone;//手机
    private String comment; //备注

    public String getJhname() {
        return jhname;
    }

    public void setJhname(String jhname) {
        this.jhname = jhname;
    }

    public String getJhcardno() {
        return jhcardno;
    }

    public void setJhcardno(String jhcardno) {
        this.jhcardno = jhcardno;
    }

    public String getJhtelephone() {
        return jhtelephone;
    }

    public void setJhtelephone(String jhtelephone) {
        this.jhtelephone = jhtelephone;
    }

    public String getCbstatus() {
        return cbstatus;
    }

    public void setCbstatus(String cbstatus) {
        this.cbstatus = cbstatus;
    }

    public String getCbstate() {
        return cbstate;
    }

    public void setCbstate(String cbstate) {
        this.cbstate = cbstate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
