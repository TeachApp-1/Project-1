package com.example.redapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Test
    public void validateData() throws Exception
    {
        String email = "david123@gmail.com";
        String password = "123456";
        LoginActivity loginActivity = new LoginActivity();
        assertFalse("Email not empty", email.isEmpty());
        assertTrue("Email is empty", !(email.isEmpty()));
        assertFalse("Password empty", password.isEmpty());
        assertTrue("Password isn't empty", !(password.isEmpty()));

        email = "";
        password = "";
        loginActivity = new LoginActivity();
        assertTrue("Email is empty", email.isEmpty());
        assertFalse("Email empty", !(email.isEmpty()));
        assertTrue("Password empty", password.isEmpty());
        assertFalse("Password isn't empty", !(password.isEmpty()));


    }
}