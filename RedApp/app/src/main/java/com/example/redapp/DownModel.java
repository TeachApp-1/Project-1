package com.example.redapp;

public class DownModel {
    public DownModel(String name, String link) {
        this.name = name;
        this.link = link;
    }

    String name, link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
