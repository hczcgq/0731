package com.chen.insurre.bean;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class ProvinceInfo {
    private String id;
    private List<CityInfo> child;
    private String name;

    public ProvinceInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChild(List<CityInfo> child) {
        this.child = child;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public List<CityInfo> getChild() {
        return child;
    }

    public String getName() {
        return name;
    }
}
