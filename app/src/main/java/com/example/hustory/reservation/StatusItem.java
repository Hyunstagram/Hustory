package com.example.hustory.reservation;

public class StatusItem {
    private String id;
    private String StudentStr;
    private String SummaryStr;
    private String DateStr;
    private String WayStr;
    private String PlaceStr;
    private String allowStr;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentStr() {
        return StudentStr;
    }

    public void setStudentStr(String studentStr) {
        StudentStr = studentStr;
    }

    public String getSummaryStr() {
        return SummaryStr;
    }

    public void setSummaryStr(String summaryStr) {
        SummaryStr = summaryStr;
    }

    public String getDateStr() {
        return DateStr;
    }

    public void setDateStr(String dateStr) {
        DateStr = dateStr;
    }

    public String getWayStr() {
        return WayStr;
    }

    public void setWayStr(String wayStr) {
        WayStr = wayStr;
    }

    public String getPlaceStr() {
        return PlaceStr;
    }

    public void setPlaceStr(String placeStr) {
        PlaceStr = placeStr;
    }

    public String getAllowStr() {
        return allowStr;
    }

    public void setAllowStr(String allowStr) {
        this.allowStr = allowStr;
    }

    public StatusItem(String id, String studentStr, String summaryStr, String dateStr, String wayStr, String placeStr, String allowStr) {
        this.id = id;
        StudentStr = studentStr;
        SummaryStr = summaryStr;
        DateStr = dateStr;
        WayStr = wayStr;
        PlaceStr = placeStr;
        this.allowStr = allowStr;
    }
}
