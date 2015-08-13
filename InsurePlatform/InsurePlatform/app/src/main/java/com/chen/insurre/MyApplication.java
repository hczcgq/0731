package com.chen.insurre;

import android.app.Application;

import com.chen.insurre.bean.ItemInfo;
import com.chen.insurre.bean.ProvinceInfo;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/11.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance = null;
    private List<ItemInfo> caijiList;
    private List<ItemInfo> reasonList;
    private List<ProvinceInfo> provsList;
    private List<ItemInfo> stateList;
    private List<ItemInfo> canbaoList;

    public static MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    //采集
    public void setCaijiList(List<ItemInfo> caijiList) {
        this.caijiList = caijiList;
    }

    public List<ItemInfo> getCaijiList() {
        return caijiList;
    }

    //原因
    public void setReasonList(List<ItemInfo> reasonList) {
        this.reasonList = reasonList;
    }

    public List<ItemInfo> getReasonList() {
        return reasonList;
    }

    //状态
    public void setStateList(List<ItemInfo> stateList) {
        this.stateList = stateList;
    }

    public List<ItemInfo> getStateList() {
        return stateList;
    }

    //参保
    public void setCanbaoList(List<ItemInfo> canbaoList) {
        this.canbaoList = canbaoList;
    }

    public List<ItemInfo> getCanbaoList() {
        return canbaoList;
    }

    //省市
    public void setProvsList(List<ProvinceInfo> provsList) {
        this.provsList = provsList;
    }

    public List<ProvinceInfo> getProvsList() {
        return provsList;
    }


}
