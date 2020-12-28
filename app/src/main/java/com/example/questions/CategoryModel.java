package com.example.questions;

public class CategoryModel {
    private String docID;
    private String name;
    private int numOfTests;

    public CategoryModel(String docID, String name, int numOfTests) {
        this.docID = docID;
        this.name = name;
        this.numOfTests = numOfTests;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfTests() {
        return numOfTests;
    }

    public void setNumOfTests(int numOfTests) {
        this.numOfTests = numOfTests;
    }
}
