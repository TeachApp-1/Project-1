package com.example.redapp;

public class UserModel {

    private String name;
    private String email;
    private String school;
    private String classroom;

    public UserModel(String name, String email, String school, String classroom) {
        this.name = name;
        this.email = email;
        this.school = school;
        this.classroom = classroom;
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

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
