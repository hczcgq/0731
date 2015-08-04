package com.chen.insurre.bean;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class ParamInfo {

    private List<ItemInfo> caiji;
    private List<ItemInfo> reason;
    private List<ProvinceInfo> provs;
    private List<ItemInfo> state;
    private List<ItemInfo> canbao;

    public List<ItemInfo> getCaiji() {
        return caiji;
    }

    public void setCaiji(List<ItemInfo> caiji) {
        this.caiji = caiji;
    }

    public List<ItemInfo> getReason() {
        return reason;
    }

    public void setReason(List<ItemInfo> reason) {
        this.reason = reason;
    }

    public List<ProvinceInfo> getProvs() {
        return provs;
    }

    public void setProvs(List<ProvinceInfo> provs) {
        this.provs = provs;
    }

    public List<ItemInfo> getState() {
        return state;
    }

    public void setState(List<ItemInfo> state) {
        this.state = state;
    }

    public List<ItemInfo> getCanbao() {
        return canbao;
    }

    public void setCanbao(List<ItemInfo> canbao) {
        this.canbao = canbao;
    }
}
