package com.example.hustory.user;

public class m_User {
    public String name;
    public String email;
    public String certificate;
    public String experience;
    public String carrer;
    public String birth;
    public String education;

    public m_User() {

    }

    public m_User(String name, String birth, String email, String education, String certificate, String experience, String carrer) {
        this.name = name;
        this.email = email;
        this.certificate= certificate;
        this.experience = experience;
        this.carrer = carrer;
        this.birth = birth;
        this.education = education;

    }

    public String getname() {
        return name;
    }

    public void setname(String name2) {
        this.name = name2;
    }

    public String getbirth() {
        return birth;
    }

    public void setbirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String geteducation() {
        return education;
    }

    public void seteducation(String education) {
        this.education = education;
    }

    public String getcertificate() {
        return certificate;
    }

    public void setcertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getexperience() {
        return experience;
    }

    public void setexperience(String experience) {
        this.experience = experience;
    }

    public String carrer() {
        return carrer;
    }

    public void setcarrer(String carrer) {
        this.carrer = carrer;
    }
}

