package com.company;

public class Main {
    public static void main(String[] args) {
        User[] myArr = new User[4];
        myArr[0] = new Student("Daniel", "daniel@gmail.com", "123456789", "A3");
        myArr[1] = new Instructor("Yossi", "yossiabu@gmail.com", "312219322");
        myArr[2] = new Teacher("Yarden", "yarden@gmail.com", "1234");
        myArr[3] = new Instructor("Miki", "miki@gmail.com", "112233");
        /*for(User user : myArr)// print all the items
            System.out.println(user.toString());*/
        System.out.println(myArr[1].login());
        myArr[1].editUser();
    }
}
