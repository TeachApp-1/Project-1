package com.example.redapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionTest {

    @Test
    public void setQuestion() {
        Question question = new Question("3+2", "3","4","5","6",3);
        String input = "3+2";
        String output;
        output = question.getQuestion();
        assertEquals(output,input);
        input = "4 - 3";
        assertNotEquals(output,input);
        input = "3";
        output = question.getOptionA();
        assertEquals(output,input);
        input = "7";
        assertNotEquals(output,input);
        int answer = 3;
        int outputInteger = question.getCorrectAns();
        assertEquals(outputInteger,answer);
        answer = 7;
        assertNotEquals(outputInteger,answer);
    }
}