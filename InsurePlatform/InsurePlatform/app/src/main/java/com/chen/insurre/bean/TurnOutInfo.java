package com.chen.insurre.bean;

/**
 * Created by chenguoquan on 8/3/15.
 */
public class TurnOutInfo {
    private String inStreet;
    private String inArea;
    private String reason;
    private String sqDate;
    private String rejectReason;
    private String rejectDate;


    public String getInStreet() {
        return inStreet;
    }

    public void setInStreet(String inStreet) {
        this.inStreet = inStreet;
    }

    public String getInArea() {
        return inArea;
    }

    public void setInArea(String inArea) {
        this.inArea = inArea;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSqDate() {
        return sqDate;
    }

    public void setSqDate(String sqDate) {
        this.sqDate = sqDate;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(String rejectDate) {
        this.rejectDate = rejectDate;
    }
}
