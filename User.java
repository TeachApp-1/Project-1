package com.company;

public abstract class User {

    public static int ID;//reset to -> 0

    protected String userName;
    protected String userEmail;
    protected String userPassword;
    private int id;

    //instructors:
    public User() {
        System.out.println("Please enter user, email and password");
    }

    public User(String uName, String uEmail, String uPassword) {
        id = ID;
        userName = uName;
        userEmail = uEmail;
        userPassword = uPassword;
        ID++;
    }

/*
    public User(User u) {
        id = ID;
        userName = u.userName;
        userEmail = u.userEmail;
        userPassword = u.userPassword;
        ID++;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }


    public static int getID() {
        return ID;
    }
 */

    public int getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    //abstract functions:
    public abstract String toString();

    public abstract boolean login();

    public abstract void wrongUserInput();//for good pass && bad user

    public abstract void wrongUserInput(String uName);//bad user/pass

    public abstract void editUser();
}
