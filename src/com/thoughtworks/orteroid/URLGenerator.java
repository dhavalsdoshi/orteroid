package com.thoughtworks.orteroid;

public class URLGenerator {
    public String getBoardURL(String input, String id) {
        return "http://www.ideaboardz.com/for/" + input + "/" + id + ".json";
    }
}
