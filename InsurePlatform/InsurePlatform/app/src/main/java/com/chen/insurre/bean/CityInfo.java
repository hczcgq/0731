package com.chen.insurre.bean;

import java.util.List;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class CityInfo {
    private String id;
    private List<TownInfo> child;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setChild(List<TownInfo> child) {
        this.child = child;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public List<TownInfo> getChild() {
        return child;
    }

    public String getName() {
        return name;
    }
}
