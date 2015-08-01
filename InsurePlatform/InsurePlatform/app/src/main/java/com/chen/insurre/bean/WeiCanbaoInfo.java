package com.chen.insurre.bean;

/**
 * Created by chenguoquan on 7/30/15.
 */
public class WeiCanbaoInfo {

    private String prov;//当前常住地所在省代码
    private String city;//所在市代码
    private String town;//所在区县代码
//    private String addr; //常住地地址
    private String status;//当前状态： 1  学龄前 2  在校（园）学生 3 单位就业 4   灵活就业 5 无业 6    务农 7 参军 8  服刑 9 老年无待遇
    private String reason;//目前未参保原因代码： 1 无意愿参保 2 无缴费能力 3 政策不了解 4 单位未参保 5 学校（园）未参保 6 不符合参保条件
    private String orgName;//工作或学校单位名称
    private String orgProv;//单位（学校）所在省代码
    private String orgCity;//单位（学校）所在市代码
    private String orgTown;//单位（学校）所在区代码
    private String orgAddr;//单位（学校）地址
    private String canbao;//经宣传后确认参保：Y是N否

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgProv() {
        return orgProv;
    }

    public void setOrgProv(String orgProv) {
        this.orgProv = orgProv;
    }

    public String getOrgCity() {
        return orgCity;
    }

    public void setOrgCity(String orgCity) {
        this.orgCity = orgCity;
    }

    public String getOrgTown() {
        return orgTown;
    }

    public void setOrgTown(String orgTown) {
        this.orgTown = orgTown;
    }

    public String getOrgAddr() {
        return orgAddr;
    }

    public void setOrgAddr(String orgAddr) {
        this.orgAddr = orgAddr;
    }

    public String getCanbao() {
        return canbao;
    }

    public void setCanbao(String canbao) {
        this.canbao = canbao;
    }
}
