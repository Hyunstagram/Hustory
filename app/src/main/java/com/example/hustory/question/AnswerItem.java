package com.example.hustory.question;

public class AnswerItem {
    private String A_Num;
    private String a_content;
    private String a_date;
    private String a_time;
    private String a_writer;
    private String id;
    private String a_diffTime;

    public AnswerItem(String a_Num, String a_content, String a_date, String a_time, String a_writer, String id, String a_diffTime) {
        A_Num = a_Num;
        this.a_content = a_content;
        this.a_date = a_date;
        this.a_time = a_time;
        this.a_writer = a_writer;
        this.id = id;
        this.a_diffTime = a_diffTime;
    }

    public String getA_Num() {
        return A_Num;
    }

    public void setA_Num(String a_Num) {
        A_Num = a_Num;
    }

    public String getA_content() {
        return a_content;
    }

    public void setA_content(String a_content) {
        this.a_content = a_content;
    }

    public String getA_date() {
        return a_date;
    }

    public void setA_date(String a_date) {
        this.a_date = a_date;
    }

    public String getA_time() {
        return a_time;
    }

    public void setA_time(String a_time) {
        this.a_time = a_time;
    }

    public String getA_writer() {
        return a_writer;
    }

    public void setA_writer(String a_writer) {
        this.a_writer = a_writer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getA_diffTime() {
        return a_diffTime;
    }

    public void setA_diffTime(String a_diffTime) {
        this.a_diffTime = a_diffTime;
    }
}
