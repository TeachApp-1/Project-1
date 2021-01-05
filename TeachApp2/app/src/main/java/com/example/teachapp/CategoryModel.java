package com.example.teachapp;

public class CategoryModel
{

    private String docID;
    private String name;
    private String noOfTest;


    public CategoryModel(String docID, String name, String noOfTest)
    {
        this.docID = docID;
        this.name = name;
        this.noOfTest = noOfTest;
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

    public String getNoOfTest() {
        return noOfTest;
    }

    public void setNoOfTest(String noOfTest) {
        this.noOfTest = noOfTest;
    }
}
