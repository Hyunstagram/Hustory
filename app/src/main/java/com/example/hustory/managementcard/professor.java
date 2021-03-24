package com.example.hustory.managementcard;

public class professor {

    public String name_student;
    public String my_agency;
    public String my_department;
    public String my_spot;


    public professor() {


    }

    public professor(String name_student, String my_agency, String my_department, String my_spot) {
        this.name_student = name_student;
        this.my_agency = my_agency;
        this.my_department = my_department;
        this.my_spot = my_spot;


    }
    public String getname_student() {
        return name_student;
    }
    public void setname_student(String name_student) {
        this.name_student= name_student;
    }

    public String getmy_agency() {
        return my_agency;
    }
    public void setmy_agency(String my_agency) {
        this.my_agency= my_agency;
    }

    public String getmy_department() {
        return my_department;
    }
    public void setmy_department(String my_department) {
        this.my_department= my_department;
    }

    public String getmy_spot() {
        return my_spot;
    }
    public void setmy_spot(String my_spot) {
        this.my_spot= my_spot;
    }
}
