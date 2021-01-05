package com.example.teachapp;

public class TestModel {

    private String testID;
    private String topScore;
    private String time;

    public TestModel(String testID, String topScore, String time) {
        this.testID = testID;
        this.topScore = topScore;
        this.time = time;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public String getTopScore() {
        return topScore;
    }

    public void setTopScore(String topScore) {
        this.topScore = topScore;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
