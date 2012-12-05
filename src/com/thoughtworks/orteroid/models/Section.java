package com.thoughtworks.orteroid.models;

import java.util.ArrayList;
import java.util.List;

public class Section {

    private String name;
    private Integer id;
    private List<Point> points;

    public Section(String name, Integer id) {
        this.name = name;
        this.id = id;
        points = new ArrayList<Point>();
    }

    public String name() {
        return name;
    }

    public boolean contains(Point point) {
        return points.contains(point);
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public Integer id() {
        return id;
    }

    public List<Point> points() {
        return points;
    }
}
