package com.thoughtworks.orteroid.models;

import org.json.JSONObject;

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

    public String description() {
        return description;
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
}
