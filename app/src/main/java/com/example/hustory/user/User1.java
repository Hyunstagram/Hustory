package com.example.hustory.user;

public class User1 {
    public String my_school;
    public String my_major;
    public String my_company_1;
    public String my_company_2;
    public String my_company_3;






    public User1() {

    }

    public User1(String my_school, String my_major, String my_company_1, String my_company_2, String my_company_3) {

        this.my_school = my_school;
        this.my_major = my_major;
        this.my_company_1= my_company_1;
        this.my_company_2 = my_company_2;
        this.my_company_3 = my_company_3;



    }

    public String getmy_school() {
        return my_school;
    }

    public void setmy_school(String my_school) {
        this.my_school = my_school;
    }

    public String getmy_major() {
        return my_major;
    }

    public void setmy_major(String my_major) {
        this.my_major = my_major;
    }

    public String getmy_company_1() {
        return my_company_1;
    }

    public void setmy_company_1(String my_company_1) { this.my_company_1= my_company_1;
    }

    public String getmy_company_2() {return my_company_2; }

    public void setmy_company_2(String my_company_2) {
        this.my_company_2 = my_company_2;
    }

    public String getmy_company_3() {
        return my_company_3;
    }

    public void setmy_company_3(String my_company_3) {
        this.my_company_3 = my_company_3;
    }





    @Override
    public String toString() {
        return "User{" +

                "my_school='" + my_school + '\'' +
                "my_major='" + my_major + '\'' +
                "my_company_1='" + my_company_1 + '\''+
                "my_company_2='" + my_company_2 + '\'' +
                "my_company_3='" + my_company_3 + '\'' +

                '}';
    }
}
