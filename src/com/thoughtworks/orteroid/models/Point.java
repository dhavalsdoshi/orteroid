package com.thoughtworks.orteroid.models;

public class Point {


    private int id;
    private String message;
    private int section_id;

    public Point(int section_id, int id, String message) {
        this.id = id;
        this.message = message;
        this.section_id = section_id;
    }

    public String message() {
        return message;
    }

    public Integer sectionId() {
        return section_id;
    }
}
