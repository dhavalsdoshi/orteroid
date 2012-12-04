package com.thoughtworks.orteroid.models;

public class Point {


    private int id;
    public String message;
    int section_id;

    public Point(int section_id, int id, String message) {
        this.id = id;
        this.message = message;
        this.section_id = section_id;
    }
}
