package com.thoughtworks.orteroid.models;

import java.util.List;

public class Section {

    private String name;
    private Integer id;

    public Section(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public String name() {
        return name;
    }
}
