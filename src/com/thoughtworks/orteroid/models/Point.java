package com.thoughtworks.orteroid.models;

public class Point {


    private int id;
    private String message;
    private int sectionId;

    public Point(int sectionId, int id, String message) {
        this.id = id;
        this.message = message;
        this.sectionId = sectionId;
    }



    public String message() {
        return message;
    }

    public Integer sectionId() {
        return sectionId;
    }

}
