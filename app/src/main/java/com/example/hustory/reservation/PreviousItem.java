package com.example.hustory.reservation;

public class PreviousItem {
    private String id;
    private String ProfessorStr;
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

    public String getProfessorStr() {
        return ProfessorStr;
    }

    public void setProfessorStr(String professorStr) {
        ProfessorStr = professorStr;
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

    public PreviousItem(String id, String professorStr, String summaryStr, String dateStr, String wayStr, String placeStr, String allowStr) {
        this.id = id;
        ProfessorStr = professorStr;
        SummaryStr = summaryStr;
        DateStr = dateStr;
        WayStr = wayStr;
        PlaceStr = placeStr;
        this.allowStr = allowStr;
    }
}
