package com.thoughtworks.orteroid.models;

import java.util.List;

public class Board {

    private String name;
    private Integer id;
    private String description;
    private List<Section> sections;

    public Board(String name, Integer id, String description, List<Section> sections) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.sections = sections;
    }

    public String name() {
        return name;
    }

    public List<Section> sections() {
        return sections;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (!id.equals(board.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void update(List<Point> points) {
        for (Point point : points)
            for (Section section : sections)
                if (point.sectionId() == section.id() && !section.contains(point))
                    section.addPoint(point);
    }

    public List<Point> pointsOfSection(String selectedSection) {
        for (Section section : sections) {
            if((section.name()).equals(selectedSection))
                return  section.points();
        }
        return null;
    }
}
