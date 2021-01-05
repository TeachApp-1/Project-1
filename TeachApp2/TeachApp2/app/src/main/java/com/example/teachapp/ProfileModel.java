package com.example.teachapp;

public class ProfileModel {

    private String name;
    private String email;
    private String school;
    private String grade;

    public ProfileModel(String name, String email, String school, String grade) {
        this.name = name;
        this.email = email;
        this.school = school;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}




