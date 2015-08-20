package com.chen.insurre.bean;

/**
 * Created by hm-soft on 2015/8/3.
 */
public class TownInfo {
    private String id;
    private String name;

    public TownInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
