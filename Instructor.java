package com.company;

import java.util.Scanner;

public class Instructor extends User {
    Scanner in = new Scanner(System.in);

    public Instructor(String uName, String uEmail, String uPassword){
        super(uName, uEmail, uPassword);
    }

    public Instructor(Instructor ins){
        userName = ins.userName;
        userEmail = ins.userEmail;
        userPassword = ins.userPassword;
    }

    @Override
    public String toString() {
        return  "\n(Instructor)" +
                "\nUser: #" + getId() +
                "\nuser Name:'" + userName + '\'' +
                "\nuser Email: '" + userEmail + '\'' +
                "\nuser Password: '" + userPassword + '\'' +
                "\n";
    }

    public void wrongUserInput(){
        System.out.println("Your user not valid");
    }

    public void wrongUserInput(String uName){
        System.out.println("Your user name/ password not valid");
    }

    @Override
    public boolean login() {
        System.out.println("Enter user name: ");
        String uName = in.nextLine();
        System.out.println("Enter password: ");
        String uPassword = in.nextLine();
        if(uName.equals(userName) && uPassword.equals(userPassword))
            return true;
        else if(uPassword.equals(userPassword))
            wrongUserInput();
        else
            wrongUserInput(uName);
        return false;
    }

    public void editUser() {
        System.out.println("Edit user name?");
        System.out.println("Yes- Y/y");
        String temp = in.nextLine();
        if (temp.equals("Y") || temp.equals("y")) {
            System.out.println("Set your new user name:");
            temp = in.nextLine();
            System.out.print("Your user name has been changed from: " + this.userName + " ");
            this.setUserName(temp);
            System.out.println("to -> " + temp);
        }
        System.out.println("Edit password?");
        System.out.println("Yes- Y/y");
        temp = in.nextLine();
        if (temp.equals("y") || temp.equals("Y")) {
            System.out.println("Set your new password:");
            temp = in.nextLine();
            System.out.print("Your password has been changed from: " + this.userPassword + " ");
            this.setUserPassword(temp);
            System.out.println("to -> " + temp);
        }
        System.out.println("Edit Email?");
        System.out.println("Yes- Y/y");
        temp = in.nextLine();
        if (temp.equals("y") || temp.equals("Y")) {
            System.out.println("Set your new Email:");
            temp = in.nextLine();
            System.out.print("Your Email has been changed from: " + this.userEmail + " ");
            this.setUserEmail(temp);
            System.out.println("to -> " + temp);
        }
        System.out.println("Your details now:");
        System.out.println(this.toString());
    }
}
