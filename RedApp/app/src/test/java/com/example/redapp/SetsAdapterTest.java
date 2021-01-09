package com.example.redapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class SetsAdapterTest {

    @Test
    public void getCount() {
        SetsAdapter setsAdapter = new SetsAdapter(3);
        int input = 3;
        int output = setsAdapter.getCount();
        assertEquals(input,output);
        input = 4;
        assertNotEquals(input,output);
    }
}