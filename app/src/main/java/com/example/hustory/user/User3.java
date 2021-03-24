package com.example.hustory.user;

public class User3 {

    public String background;
    public String character;
    public String ict_experience;
    public String motivation;

    public User3() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User3(String background, String character,String ict_experience,String motivation) {
        this.background = background;
        this.character = character;
        this.ict_experience = ict_experience;
        this.motivation = motivation;
    }

    public String getbackground() {
        return background;
    }

    public void setbackground(String background) {
        this.background = background;
    }

    public String getcharacter() {
        return character;
    }

    public void setcharacter(String character) {
        this.character = character;
    }

    public String getict_experience() {
        return ict_experience;
    }

    public void setict_experience(String ict_experience) {
        this.ict_experience = ict_experience;
    }

    public String getmotivation() {
        return motivation;
    }

    public void setmotivation(String motivation) {
        this.motivation = motivation;
    }

    @Override
    public String toString() {
        return "User{" +
                "userbackground='" + background + '\'' +
                ", character='" + character + '\'' +
                ", ict_experience='" + ict_experience + '\'' +
                ", motivationr='" + motivation + '\'' +
                '}';
    }
}