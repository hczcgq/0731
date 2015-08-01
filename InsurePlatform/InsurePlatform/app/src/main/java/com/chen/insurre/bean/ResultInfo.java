package com.chen.insurre.bean;

import java.io.Serializable;

/**
 * Created by chenguoquan on 7/29/15.
 */
public class ResultInfo<T> implements Serializable{

    private static final long serialVersionUID = -8102798162712714637L;

    private String result;
    private String description;
    private String version;
    private T bean;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }
}
