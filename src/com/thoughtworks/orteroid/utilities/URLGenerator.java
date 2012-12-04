package com.thoughtworks.orteroid.utilities;

public class URLGenerator {
    public String getBoardURL(String input, String id) {
        return "http://www.ideaboardz.com/for/" + input + "/" + id + ".json";
    }

    public String getPointsURL(String input, String id) {
        return "http://www.ideaboardz.com/retros/" + input + "/" + id + "/points"+ ".json";
    }
}
